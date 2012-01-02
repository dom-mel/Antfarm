package org.linesofcode.antfarm.sceneObjects;

import org.linesofcode.antfarm.AntFarm;

import processing.core.PVector;

public class Hive implements SceneObject {

    public final static float SIZE = 10;
    private static final float SPAWN_NOISE = 0.5f;

    private long foodCount;
    private float spawnSpeed = 5;
    private int ants;

    private PVector position;
    private int color;
    private int outlineColor = 0;

    private float lastSpawn;

    private final AntFarm antFarm;

    public Hive(final AntFarm antFarm, final int color) {
        this.antFarm = antFarm;
        this.color = color;
        position = calcStaticSpawnPosition();
        lastSpawn = Float.MAX_VALUE;
    }

    public void update(final float delta) {
        lastSpawn += delta;
        if (lastSpawn > spawnSpeed) {
            antFarm.spawnAnt(this);
            lastSpawn = - antFarm.random(1, SIZE * SPAWN_NOISE);
        }

    }

    public void draw() {
        antFarm.stroke(outlineColor);
        antFarm.fill(color);
        antFarm.rect(position.x, position.y, SIZE, SIZE);
    }

    public int getColor() {
        return color;
    }

    public PVector getCenter() {
        return new PVector(position.x + SIZE / 2, position.y + SIZE /2);
    }
    
    public void putFood() {
    	foodCount++;
    }
    
    public void pickUpAnt() {
    	ants++;
    }

	public PVector getSpawnPosition() {
		final PVector pos = getCenter();
        final float dx = ((antFarm.random(-1, 1) < 0) ? -1 : 1 ) * antFarm.random(3, 10);
        final float dy = ((antFarm.random(-1, 1) < 0) ? -1 : 1 ) * antFarm.random(3, 10);
        pos.add(dx, dy, 0);
        return pos;
    }

    public PVector calcStaticSpawnPosition() {
        final PVector position = new PVector();
        while (true) {
            position.x = antFarm.random(AntFarm.BORDER_SPANW_DISTANCE, antFarm.width - SIZE - AntFarm.BORDER_SPANW_DISTANCE);
            position.y = antFarm.random(AntFarm.BORDER_SPANW_DISTANCE, antFarm.height - SIZE - AntFarm.BORDER_SPANW_DISTANCE);
            boolean correct = true;
            for (final Hive object: antFarm.getHives()) {
                if (Math.abs(PVector.dist(position, object.position)) < AntFarm.MIN_STATIC_SPAWN_DISTANCE) {
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