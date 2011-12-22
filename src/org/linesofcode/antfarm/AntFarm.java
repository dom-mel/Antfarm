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
            Color.RED.getRGB(),
            Color.YELLOW.getRGB(),
            Color.PINK.getRGB(),
            Color.MAGENTA.getRGB(),
            new Color(148, 56, 161).getRGB(),
            Color.WHITE.getRGB()
    };
    public static final float MIN_STATIC_SPAWN_DISTANCE = 150;
    private static final boolean SHOW_BOUNDS = true;

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
        overlay = new Overlay(this);
        speed = addSlider("speed", 0, 10, 2);
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
            if (SHOW_BOUNDS) {
                final BoundingBox bounds = ant.getBoundingBox();
                if (bounds != null) {
                    bounds.draw(this);
                }
            }
        }

        overlay.draw();
    }

    private void update(final float delta) {

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

        overlay.update(delta);
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

    public Slider addSlider(final String name, final float min, final float max, final float defaultValue) {
        return overlay.addSlider(name, min, max, defaultValue);
    }

	public boolean isDrawViewDirectionEnabled() {
		return drawViewDirection;
	}

    public Set<Hive> getHives() {
        final Set<Hive> hives = new HashSet<Hive>();
        for (final SceneObject object: staticSceneObjects) {
            if (object instanceof Hive) {
                hives.add((Hive) object);
            }
        }
        return hives;
    }

    public SceneObject getCollidingObject(final SceneObject me) {
        final SceneObject staticIntersect = getCollidingStaticObject(me);
        if (staticIntersect != null) {
            return staticIntersect;
        }

        for (final SceneObject another: ants) {
            if (another.equals(me)) {
                continue;
            }
            final BoundingBox anotherBox = another.getBoundingBox();
            if (anotherBox == null) {
                continue;
            }
            if (anotherBox.intersects(me.getBoundingBox())) {
                return another;
            }
        }
        return null;
    }

    public SceneObject getCollidingStaticObject(final SceneObject me) {
        for (final SceneObject another: staticSceneObjects) {
            if (another.equals(me)) {
                continue;
            }
            if (another.getBoundingBox().intersects(me.getBoundingBox())) {
                return another;
            }
        }
        return null;
    }

    public boolean collides(final Ant me) {
        final BoundingBox bounds = me.getBoundingBox();
        final PVector topLeft = bounds.getTopLeft();
        if (topLeft.x < 0 ||topLeft.y < 0) {
            return true;
        }
        if (topLeft.x + bounds.getWidth() > width || topLeft.y + bounds.getHeight() > height) {
            return true;
        }
        return getCollidingObject(me) != null;
    }

    public boolean collides(final Ant me, final float futureRotation) {
        throw new UnsupportedOperationException();
    }
}
