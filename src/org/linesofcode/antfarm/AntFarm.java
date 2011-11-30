package org.linesofcode.antfarm;

import processing.core.PApplet;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

import org.linesofcode.antfarm.entities.Ant;
import org.linesofcode.antfarm.entities.Food;
import org.linesofcode.antfarm.entities.Hive;

public class AntFarm extends PApplet {

	private static final long serialVersionUID = -8658351784308310939L;
	
	private Set<Hive> hives;
    private Set<Ant> ants;
    private Set<Food> foods;

    @Override
    public void setup() {
        size(600, 400);

        hives = new HashSet<Hive>();
        ants = new HashSet<Ant>();
        foods = new HashSet<Food>();

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

    public void update(float delta) {
        for (Hive hive : hives) {
            hive.update(delta);
        }

        for (Ant ant : ants) {
            ant.update(delta);
        }

        for (Food food : foods) {
            food.update(delta);
        }
    }

    public void addAnt(Ant ant) {
        ants.add(ant);
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
