package org.linesofcode.antfarm;

import processing.core.PVector;

public class Ant {

    private final static float SIZE = 2;

    private Behavior currentBehavior;
    private boolean carriesFood;
    private int age;

    private PVector position;
    private PVector viewDirection;

    private final AntFarm antFarm;
    private final Hive hive;

    private int speedMultiplier = 1;

    public Ant(AntFarm antFarm, Hive hive) {
        this.antFarm = antFarm;
        this.hive = hive;
        calcSpawnPosition();
        currentBehavior = new DummyBehavior();
    }

    private void calcSpawnPosition() {
        position = hive.getCenter();
        float dx = ((antFarm.random(-1, 1) < 0)?-1:1 ) * antFarm.random(3, 10);
        float dy = ((antFarm.random(-1, 1) < 0)?-1:1 ) * antFarm.random(3, 10);
        position.add(dx, dy, 0);

        viewDirection = PVector.add(hive.getCenter(), PVector.mult(position, -1));
    }

    public void update(float delta) {
        currentBehavior.update(delta, this);
    }

    public void draw() {
        if (position == null) return;
        antFarm.stroke(hive.getColor());
        antFarm.fill(hive.getColor());
        antFarm.rect(position.x, position.y, SIZE, SIZE);
    }

    private void pickupFood(Food food) {

    }

    private void deliverFood(Hive hive) {

    }

    private void move(PVector velocity) {

    }

    private void turn(float degrees) {

    }

    private void wander() {

    }

    private void returnFood() {

    }

    private void giveUp() {

    }

    private void idle() {

    }

    private void followPheromoneTrail() {

    }

    private void die() {

    }

    public AntFarm getAntFarm() {
        return antFarm;
    }

    public PVector getPosition() {
        return position;
    }

    public int getSpeedMultiplier() {
        return speedMultiplier;
    }

}
