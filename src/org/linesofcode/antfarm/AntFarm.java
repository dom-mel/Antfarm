package org.linesofcode.antfarm;

import controlP5.Slider;
import org.linesofcode.antfarm.sceneObjects.*;
import processing.core.PApplet;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("serial")
public class AntFarm extends PApplet {

    private Set<SceneObject> staticSceneObjects = new HashSet<SceneObject>(1000);
    private Set<Ant> ants = new HashSet<Ant>(1000);

    private Set<SceneObject> removeObjects = new HashSet<SceneObject>();
    private Set<SceneObject> addObjects = new HashSet<SceneObject>();

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

        for (SceneObject sceneObject: staticSceneObjects) {
            sceneObject.draw();
        }
        for (SceneObject sceneObject: ants) {
            sceneObject.draw();
        }
        overlay.draw();
    }

    private void update(float delta) {

        for (SceneObject sceneObject: staticSceneObjects) {
            if (removeObjects.contains(sceneObject)) {
                continue;
            }
            sceneObject.update(delta);
        }

        for (SceneObject sceneObject: ants) {
            if (removeObjects.contains(sceneObject)) {
                continue;
            }
            sceneObject.update(delta);
        }

        overlay.update(delta);
    }

    private void performChanges() {
        for (SceneObject sceneObject: removeObjects) {
            if (sceneObject instanceof Ant) {
                ants.remove(sceneObject);
            } else {
                staticSceneObjects.remove(sceneObject);
            }
        }

        for (SceneObject sceneObject: addObjects) {
            if (sceneObject instanceof Ant) {
                ants.add((Ant) sceneObject);
            } else {
                staticSceneObjects.add(sceneObject);
            }
        }
        removeObjects.clear();
        addObjects.clear();
    }

    public void spawnAnt(Hive hive) {
    	addObjects.add(new Ant(this, hive));
    }

    public void removeAnt(Ant ant) {
        removeObjects.add(ant);
    }

    public void addFood(Food food) {
        addObjects.add(food);
    }

    public void removeFood(Food food) {
        removeObjects.add(food);
    }

    public void removeHive(Hive hive) {
        removeObjects.remove(hive);
        // TODO let ants die
    }

    public Slider addSlider(String name, float min, float max, float defaultValue) {
        return overlay.addSlider(name, min, max, defaultValue);
    }

	public boolean isDrawViewDirectionEnabled() {
		return drawViewDirection;
	}
}
