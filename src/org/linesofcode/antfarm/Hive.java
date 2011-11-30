package org.linesofcode.antfarm;

import processing.core.PApplet;
import processing.core.PVector;

public class Hive {

    public final static float SIZE = 10;

    private long foodCount;
    private float spawnSpeed = 5;

    private PVector position;
    private int color;
    private float lastSpawn;

    private final AntFarm antFarm;

    public Hive(AntFarm antFarm, int color) {
        this.antFarm = antFarm;
        this.color = color;
        position = new PVector();
        position.x = antFarm.random(0, antFarm.width);
        position.y = antFarm.random(0, antFarm.height);
        lastSpawn = Float.MAX_VALUE;
    }

    public void update(float delta) {
        lastSpawn += delta;
        if (lastSpawn > spawnSpeed) {
            spawnAnt();
            lastSpawn = 0;
        }

    }

    public void draw() {
        antFarm.stroke(0);
        antFarm.fill(color);
        antFarm.rect(position.x, position.y, SIZE, SIZE);
    }

    public void spawnAnt() {
        antFarm.addAnt(new Ant(antFarm, this));
    }

    public int getColor() {
        return color;
    }

    public PVector getCenter() {
        return new PVector(position.x + SIZE / 2, position.y + SIZE /2);
    }
}
