package org.linesofcode.antfarm;

import controlP5.Slider;
import org.linesofcode.antfarm.sceneObjects.*;
import processing.core.PApplet;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

import processing.core.PVector;

@SuppressWarnings("serial")
public class AntFarm extends PApplet {

    private Set<SceneObject> sceneObjects = new HashSet<SceneObject>();
    private Set<SceneObject> removeObjects = new HashSet<SceneObject>();
    private Set<SceneObject> addObjects = new HashSet<SceneObject>();

    private Overlay overlay;

    private Slider speed;

	private boolean drawViewDirection = true;

    @Override
    public void setup() {
        size(600, 400);
        sceneObjects.add(new Hive(this, Color.BLUE.getRGB()));
        sceneObjects.add(new Hive(this, Color.BLUE.getRGB()));
        sceneObjects.add(new Hive(this, Color.BLUE.getRGB()));
        overlay = new Overlay(this);

        speed = addSlider("speed", 0, 10, 2);
    }

    @Override
    public void draw() {
        background(Color.LIGHT_GRAY.getRGB());

        update(1 / frameRate);
        for (SceneObject sceneObject: sceneObjects) {
            sceneObject.draw();
        }
        overlay.draw();
    }

    private void update(float delta) {
        removeObjects.clear();
        addObjects.clear();

        for (SceneObject sceneObject: sceneObjects) {
            sceneObject.update(delta);
        }

        sceneObjects.removeAll(removeObjects);
        sceneObjects.addAll(addObjects);

        overlay.update(delta);
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
        sceneObjects.remove(hive);
        // TODO let ants die
    }

    public Slider addSlider(String name, float min, float max, float defaultValue) {
        return overlay.addSlider(name, min, max, defaultValue);
    }

	public boolean isDrawViewDirection() {
		return drawViewDirection;
	}
}
