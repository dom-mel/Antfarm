package org.linesofcode.antfarm;

import controlP5.Slider;
import org.linesofcode.antfarm.sceneObjects.*;
import processing.core.PApplet;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

import processing.core.PVector;

public class AntFarm extends PApplet {

	private static final long serialVersionUID = -8658351784308310939L;
	
	private Set<SceneObject> sceneObjects = new HashSet<SceneObject>();

    private Overlay overlay;

    private int antCounter;
    private int hiveCounter;
    private int foodCounter;

    private Slider speed;

    @Override
    public void setup() {
        size(600, 400);
        sceneObjects.add(new Hive(this, Color.BLUE.getRGB()));
        overlay = new Overlay(this);

        antCounter = 0;
        hiveCounter = 0;
        foodCounter = 0;
        speed = addSlider("speed", 0, 10, 2);
    }

    @Override
    public void draw() {
        background(Color.LIGHT_GRAY.getRGB());

        update(1 / frameRate);
        for (SceneObject object : sceneObjects) {
            object.draw();
        }
        overlay.draw();
    }

    private void update(float delta) {
        for (SceneObject object : sceneObjects) {
            object.update(delta);
        }
        overlay.update(delta);
        println(speed.value());
    }

    public void spawnAnt(Hive hive) {
        PVector position = hive.getCenter();
        float dx = ((random(-1, 1) < 0)?-1:1 ) * random(3, 10);
        float dy = ((random(-1, 1) < 0)?-1:1 ) * random(3, 10);
        position.add(dx, dy, 0);

        PVector viewDirection = PVector.add(hive.getCenter(), PVector.mult(position, -1));
        sceneObjects.add(new Ant(this, hive, position, viewDirection));
        antCounter++;
    }

    public void removeAnt(Ant ant) {
        sceneObjects.remove(ant);
        antCounter--;
    }

    public void addFood(Food food) {
        sceneObjects.add(food);
        foodCounter++;
    }

    public void removeFood(Food food) {
        sceneObjects.remove(food);
        foodCounter--;
    }

    public void removeHive(Hive hive) {
        sceneObjects.remove(hive);
        hiveCounter--;
        // TODO let ants die
    }

    public Slider addSlider(String name, float min, float max, float defaultValue) {
        return overlay.addSlider(name, min, max, defaultValue);
    }
}
