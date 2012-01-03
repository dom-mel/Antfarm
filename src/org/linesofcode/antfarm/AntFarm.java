package org.linesofcode.antfarm;

import controlP5.Slider;
import org.linesofcode.antfarm.sceneObjects.*;

import processing.core.PApplet;
import processing.core.PVector;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("serial")
public class AntFarm extends PApplet {

    private static final int[] HIVE_COLORS = {
            Color.BLUE.getRGB(),
//            Color.RED.getRGB(),
//            Color.YELLOW.getRGB(),
//            Color.PINK.getRGB(),
//            Color.MAGENTA.getRGB(),
//            new Color(148, 56, 161).getRGB(),
//            Color.WHITE.getRGB()
    };
    public static final float MIN_STATIC_SPAWN_DISTANCE = 150;
    public final static float BORDER_SPANW_DISTANCE = 10;

    private final Set<SceneObject> staticSceneObjects = new HashSet<SceneObject>(1000);
    private final Set<Ant> ants = new HashSet<Ant>(1000);

    private final Set<SceneObject> removeObjects = new HashSet<SceneObject>();
    private final Set<SceneObject> addObjects = new HashSet<SceneObject>();

    private Overlay overlay;

    private Slider speed;

	private boolean drawViewDirection = false;

    @Override
    public void setup() {
        size(600, 400);

        for (final int HIVE_COLOR : HIVE_COLORS) {
            staticSceneObjects.add(new Hive(this, HIVE_COLOR));
        }
        staticSceneObjects.add(new Food(this));
        overlay = new Overlay(this);
    }

    @Override
    public void draw() {
        background(Color.LIGHT_GRAY.getRGB());

        update(1 / frameRate);
        addAndRemoveSceneObjects();

        for (final SceneObject sceneObject: staticSceneObjects) {
            sceneObject.draw();
        }
        for (final Ant ant: ants) {
            ant.draw();
        }
        overlay.draw();
    }

    private void update(final float delta) {
        overlay.update(delta);

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
        addObjects.add(new Food(this));
    }

    public void removeFood(final Food food) {
        removeObjects.add(food);
        spawnFood();
    }

    public void removeHive(final Hive hive) {
        for (final Ant ant: ants) {
            if (ant.getHive() == hive) {
                removeAnt(ant);
            }
        }
        removeObjects.remove(hive);
    }

	public boolean isDrawViewDirectionEnabled() {
		return drawViewDirection;
	}

    public boolean isPathBlocked(final Ant me, final PVector translation) {
        final BoundingBox myBox = me.getBoundingBox();
        if (myBox == null) {
            return false;
        }
        final BoundingBox box = myBox.getTransformedBoundingBox(translation, me.getRotation());
        for (final Ant ant : ants) {
            if (ant == me) {
                continue;
            }

            if (ant.getBoundingBox() == null) {
                continue;
            }

            if (ant.getBoundingBox().intersects(box)) {
                return true;
            }
        }
        return false;
    }

    public void moveAnt(final Ant ant, final PVector newPosition) throws OutOfBoundsException, PathIsBlockedException {
       
    	assertAntInBounds(newPosition);
    	
    	if (isPathBlocked(ant, newPosition)) {
            throw new PathIsBlockedException();
        }
        
        ant.setPosition(newPosition);
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

}
