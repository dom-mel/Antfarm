package org.linesofcode.antfarm;

import controlP5.Slider;
import org.linesofcode.antfarm.sceneObjects.*;
import processing.core.PApplet;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("serial")
public class AntFarm extends PApplet {

    private final Set<SceneObject> staticSceneObjects = new HashSet<SceneObject>(1000);
    private Set<Ant> ants = new HashSet<Ant>(1000);

    private final Set<SceneObject> removeObjects = new HashSet<SceneObject>();
    private final Set<SceneObject> addObjects = new HashSet<SceneObject>();

    private Overlay overlay;

    private Slider speed;

	private boolean drawViewDirection = true;

    @Override
    public void setup() {
        size(600, 400);
        staticSceneObjects.add(new Hive(this, Color.BLUE.getRGB()));
        staticSceneObjects.add(new Hive(this, Color.RED.getRGB()));
        staticSceneObjects.add(new Hive(this, Color.YELLOW.getRGB()));
        staticSceneObjects.add(new Hive(this, Color.GREEN.getRGB()));
        staticSceneObjects.add(new Hive(this, Color.PINK.getRGB()));
        staticSceneObjects.add(new Hive(this, Color.BLACK.getRGB()));
        overlay = new Overlay(this);

        speed = addSlider("speed", 0, 10, 2);
    }

    @Override
    public void draw() {
        background(Color.LIGHT_GRAY.getRGB());

        update(1 / frameRate);
        performChanges();

        for (final SceneObject sceneObject: staticSceneObjects) {
            sceneObject.draw();
        }
        for (final SceneObject sceneObject: ants) {
            sceneObject.draw();
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

        for (final SceneObject sceneObject: ants) {
            if (removeObjects.contains(sceneObject)) {
                continue;
            }
            sceneObject.update(delta);
        }

        overlay.update(delta);
    }

    private void performChanges() {
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

    public void addFood(final Food food) {
        addObjects.add(food);
    }

    public void removeFood(final Food food) {
        removeObjects.add(food);
    }

    public void removeHive(final Hive hive) {
        removeObjects.remove(hive);
        // TODO let ants die
    }

    public Slider addSlider(final String name, final float min, final float max, final float defaultValue) {
        return overlay.addSlider(name, min, max, defaultValue);
    }

	public boolean isDrawViewDirectionEnabled() {
		return drawViewDirection;
	}
}
