package org.linesofcode.antfarm;

import org.linesofcode.antfarm.exception.OutOfBoundsException;
import org.linesofcode.antfarm.exception.PathIsBlockedException;
import org.linesofcode.antfarm.sceneObjects.BoundingBox;
import org.linesofcode.antfarm.sceneObjects.Food;
import org.linesofcode.antfarm.sceneObjects.Hive;
import org.linesofcode.antfarm.sceneObjects.Overlay;
import org.linesofcode.antfarm.sceneObjects.Ping;
import org.linesofcode.antfarm.sceneObjects.SceneObject;
import org.linesofcode.antfarm.sceneObjects.ant.Ant;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("serial")
public class AntFarm extends PApplet {

    private static final int RES_Y = 600;
	private static final int RES_X = 800;
	
	private static final int[] HIVE_COLORS = {
            new Color(60, 60, 235).getRGB(),
            new Color(255, 80, 0).getRGB(),
            new Color(255, 220, 0).getRGB(),
            new Color(148, 56, 161).getRGB(),
    };
    public static final float MIN_STATIC_SPAWN_DISTANCE = 150;
    public static final float BORDER_SPANW_DISTANCE = 10;
	private static boolean PINGS_ENABLED = true;
    public static int FOOD_COUNT = 2;
    public static float TIME_LAPSE = 1f;
    public static float PHEROMONE_SIZE = 5f;
    public static boolean DRAW_VIEW_DIRECTION = false;
	private static boolean DRAW_BG_TEXTURE = false;

    private final Set<SceneObject> staticSceneObjects = new HashSet<SceneObject>(1000);
    private final Set<Ant> ants = new HashSet<Ant>(1000);

    private final Set<SceneObject> removeObjects = new HashSet<SceneObject>();
    private final Set<SceneObject> addObjects = new HashSet<SceneObject>();

    private Overlay overlay;
    private PGraphics pheromones;
    
    private PImage bgTexture;
	
	private int currentFoodCount = 0;

    @Override
    public void setup() {
    	
    	loadAssets();
    	
        size(RES_X, RES_Y);

        for (final int HIVE_COLOR : HIVE_COLORS) {
            staticSceneObjects.add(new Hive(this, HIVE_COLOR));
        }
        for(int i=0; i<FOOD_COUNT; i++) {
        	staticSceneObjects.add(new Food(this));
        	currentFoodCount++;
        }
        overlay = new Overlay(this);

        pheromones = createGraphics(width, height, P2D);
        pheromones.background(color(1,0));
        
        smooth();
    }

    private void loadAssets() {
    	Class<?> c = getClass();
    	String path = c.getResource("/assets/grass2.jpg").getFile();
    	bgTexture = loadImage(path);
	}

	@Override
    public void draw() {
        update(1 / frameRate);
        background(155);
        
        if(DRAW_BG_TEXTURE) {
        	image(bgTexture, 0, 0, 600, 400);
        }

        image(pheromones, 0, 0);

        for (final SceneObject sceneObject: staticSceneObjects) {
            sceneObject.draw();
        }
        for (final Ant ant: ants) {
            ant.draw();
        }
        overlay.draw();
    }

    private void update(float delta) {
    	
        overlay.update(delta);
        
        delta *= TIME_LAPSE;
        fadeOutPheromoneTrails(delta);
        for (final SceneObject sceneObject: staticSceneObjects) {
            if (removeObjects.contains(sceneObject)) {
                continue;
            }
            sceneObject.update(delta);
        }

        for (final Ant ant: ants) {
            if (removeObjects.contains(ant)) {
                continue;
            }
            ant.update(delta);
        }
        addAndRemoveSceneObjects();
    }

    private void addAndRemoveSceneObjects() {
        for (final SceneObject sceneObject: removeObjects) {
            if (sceneObject instanceof Ant) {
                ants.remove(sceneObject);
            } else {
                staticSceneObjects.remove(sceneObject);
            }
        }

        for (final SceneObject sceneObject: addObjects) {
            if (sceneObject instanceof Ant) {
                ants.add((Ant) sceneObject);
            } else {
                staticSceneObjects.add(sceneObject);
            }
        }
        removeObjects.clear();
        addObjects.clear();
    }

    public void spawnAnt(final Hive hive) {
    	addObjects.add(new Ant(this, hive));
    }

    public void removeAnt(final Ant ant) {
        removeObjects.add(ant);
    }

    private void spawnFood() {
    	Food food = new Food(this);
        addObjects.add(food);
        currentFoodCount++;
        ping(food.getPosition().x, food.getPosition().y);
    }

    public void removeFood(final Food food) {
        removeObjects.add(food);
        currentFoodCount--;
        if(currentFoodCount < FOOD_COUNT) {
        	spawnFood();
        }
    }

    public void removeHive(final Hive hive) {
        for (final Ant ant: ants) {
            if (ant.getHive() == hive) {
                removeAnt(ant);
            }
        }
        removeObjects.add(hive);
        ping(hive.getCenter().x, hive.getCenter().y);
    }

	public boolean isDrawViewDirectionEnabled() {
		return DRAW_VIEW_DIRECTION;
	}

    public boolean isPathBlocked(final Ant me, final PVector translation) {
        return false;
    }

    public Ant hitsEnemyAnt(final Ant me) {
        final BoundingBox myBox = me.getBoundingBox();
        if (myBox == null) {
            return null;
        }

        final int team = me.getHive().getColor();

        for (final Ant ant : ants) {
            if (ant == me) {
                continue;
            }

            if (ant.getHive().getColor() == team) {
                continue; // not an enemy
            }

            if (ant.getBoundingBox() == null) {
                continue;
            }

            if (ant.getBoundingBox().intersects(myBox)) {
                return ant;
            }
        }
        return null;
    }

    public void moveAnt(final Ant ant, final PVector newPosition) throws OutOfBoundsException, PathIsBlockedException {
       
    	assertAntInBounds(newPosition);

        final Ant enemy = hitsEnemyAnt(ant);
        if (enemy != null) {
            antFight(ant, enemy);
        }

    	if (isPathBlocked(ant, newPosition)) {
            throw new PathIsBlockedException();
        }

        ant.setPosition(newPosition);
    }

    public void antFight(final Ant a, final Ant b) {
        
    	float combatValueA = a.getRelativeStamina() + random(1f);
        float combatValueB = b.getRelativeStamina() + random(1f);
        
        if(combatValueA > combatValueB) {
        	b.die();
        	ping(b.getPosition().x, b.getPosition().y);
        	return;
        }
        
		if(combatValueB > combatValueA) {
        	a.die();
        	ping(a.getPosition().x, a.getPosition().y);
        	return;
		}
    	
		antFight(a, b);
    }

    private void assertAntInBounds(PVector newPosition) throws OutOfBoundsException {
    	if((newPosition.x + Ant.SIZE) > width || (newPosition.x - Ant.SIZE) < 0) {
    		throw new OutOfBoundsException(OutOfBoundsException.Direction.X_AXIS);
    	}
    	if((newPosition.y + Ant.SIZE) > height || (newPosition.y - Ant.SIZE) < 0) {
    		throw new OutOfBoundsException(OutOfBoundsException.Direction.Y_AXIS);
    	}
	}

	public PVector calcStaticSpawnPosition(final SceneObject me, float size) {
        final PVector position = new PVector();
        while (true) {
            position.x = random(BORDER_SPANW_DISTANCE, width - size - BORDER_SPANW_DISTANCE);
            position.y = random(BORDER_SPANW_DISTANCE, height - size - BORDER_SPANW_DISTANCE);
            boolean correct = true;
            for (final SceneObject object: staticSceneObjects) {
                final PVector objectPosition;
                if (object instanceof Hive) {
                    objectPosition = ((Hive) object).getPosition();
                } else if (object instanceof Food) {
                    objectPosition = ((Food) object).getPosition();
                } else {
                    continue;
                }

                if (Math.abs(PVector.dist(position, objectPosition)) < MIN_STATIC_SPAWN_DISTANCE) {
                    correct = false;
                    break;
                }
            }
            if (correct) {
                break;
            }
        }
        return position;
    }

	public Food getFoodInProximity(Ant ant) {
		for(Object o : staticSceneObjects) {
			if(o instanceof Food) {
				Food food = (Food)o;
				double distance = Math.abs(PVector.dist(ant.getPosition(), food.getPosition()));
				distance -= Food.SIZE;
				if(distance < 30.0) {
					return food;
				}
			}
		}
		return null;
	}

    public void putPheromone(final Ant me) {
        pheromones.fill(me.getHive().getColor());
        pheromones.ellipse(me.getPosition().x-(PHEROMONE_SIZE/2), me.getPosition().y-(PHEROMONE_SIZE/2), PHEROMONE_SIZE, PHEROMONE_SIZE);
    }

    public PVector getClosePheromoneTrail(final Ant me) {
        throw new UnsupportedOperationException();
    }

    private void fadeOutPheromoneTrails(final float delta) {
        for (int w = 0; w < width; w++) {
            for (int h = 0; h < height; h++) {
                final int pixel = pheromones.get(w, h);
                if (alpha(pixel) == 0) {
                    continue;
                }
                pheromones.set(w, h,color(red(pixel), green(pixel), blue(pixel), alpha(pixel) - 10 * delta));
            }
        }
    }

	public void removeSceneObject(SceneObject sceneObject) {
		removeObjects.add(sceneObject);
	}

	public void ping(float x, float y) {
		if(PINGS_ENABLED) {
			addObjects.add(new Ping(this, x, y));
		}
	}
}
