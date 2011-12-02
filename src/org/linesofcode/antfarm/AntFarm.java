package org.linesofcode.antfarm;

import processing.core.PApplet;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

import org.linesofcode.antfarm.entities.Ant;
import org.linesofcode.antfarm.entities.Food;
import org.linesofcode.antfarm.entities.Hive;
import processing.core.PVector;

public class AntFarm extends PApplet {

	private static final long serialVersionUID = -8658351784308310939L;
	
	private Set<Hive> hives = new HashSet<Hive>();
    private Set<Ant> ants = new HashSet<Ant>();
    private Set<Food> foods = new HashSet<Food>();

    @Override
    public void setup() {
        size(600, 400);
        hives.add(new Hive(this, Color.BLUE.getRGB()));
    }

    @Override
    public void draw() {
        background(Color.LIGHT_GRAY.getRGB());

        update(1 / frameRate);
        for (Hive hive : hives) {
            hive.draw();
        }

        for (Ant ant : ants) {
            ant.draw();
        }

        for (Food food : foods) {
            food.draw();
        }
    }

    private void update(float delta) {
        for (Hive hive : hives) {
            hive.update(delta);
        }
        for (Ant ant : ants) {
            ant.update(delta);
        }
    }

    public void spawnAnt(Hive hive) {
        PVector position = hive.getCenter();
        float dx = ((random(-1, 1) < 0)?-1:1 ) * random(3, 10);
        float dy = ((random(-1, 1) < 0)?-1:1 ) * random(3, 10);
        position.add(dx, dy, 0);

        PVector viewDirection = PVector.add(hive.getCenter(), PVector.mult(position, -1));
        ants.add(new Ant(this, hive, position, viewDirection));
    }

    public void removeAnt(Ant ant) {
        ants.remove(ant);
    }

    public void addFood(Food food) {
        foods.add(food);
    }

    public void removeFood(Food food) {
        foods.remove(food);
    }

    public void removeHive(Hive hive) {
        hives.remove(hive);
    }
}
