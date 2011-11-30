package org.linesofcode.antfarm;

import processing.core.PApplet;

import java.awt.*;
import java.util.Set;

public class AntFarm extends PApplet {

    private Set<Hive> hives;
    private Set<Ant> ants;
    private Set<Food> foods;

    @Override
    public void setup() {
        size(600, 400);
        background(Color.GRAY.getRGB());
    }

    @Override
    public void draw() {
        update(1 / frameRate);
    }

    public void update(float delta) {

    }

    public void addAnt(Ant ant) {

    }

    public void removeAnt(Ant ant) {

    }

    public void addFood(Food food) {

    }

    public void removeFood(Food food) {

    }

    public void removeHive(Hive hive) {

    }
}
