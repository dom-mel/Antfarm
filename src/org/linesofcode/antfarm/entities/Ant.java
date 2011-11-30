package org.linesofcode.antfarm.entities;

import org.linesofcode.antfarm.AntFarm;
import org.linesofcode.antfarm.behavior.Behavior;
import org.linesofcode.antfarm.behavior.DummyBehavior;

import processing.core.PVector;

public class Ant {

    private final static float SIZE = 2;

    private Behavior currentBehavior;
    private boolean carriesFood;
    private long timeOfBirth;

    private PVector position;
    private PVector viewDirection;

    private final AntFarm antFarm;
    private final Hive hive;

    private int speedMultiplier = 1;

    public Ant(AntFarm antFarm, Hive hive, long timeOfBirth) {
        this.antFarm = antFarm;
        this.hive = hive;
        calcSpawnPosition();
        currentBehavior = new DummyBehavior(this); // FIXME Debug code
        this.timeOfBirth = timeOfBirth;
    }

    private void calcSpawnPosition() {
        position = hive.getCenter();
        float dx = ((antFarm.random(-1, 1) < 0)?-1:1 ) * antFarm.random(3, 10);
        float dy = ((antFarm.random(-1, 1) < 0)?-1:1 ) * antFarm.random(3, 10);
        position.add(dx, dy, 0);

        viewDirection = PVector.add(hive.getCenter(), PVector.mult(position, -1));
    }

    public void update(float delta) {
        currentBehavior.update(delta);
    }

    public void draw() {
        if (position == null) return;
        antFarm.stroke(hive.getColor());
        antFarm.fill(hive.getColor());
        antFarm.rect(position.x, position.y, SIZE, SIZE);
    }

    public void pickupFood(Food food) {
    	food.pickUp();
    	carriesFood = true;
    }

    public void enterHive() {
    	hive.pickUpAnt();
    	if(carriesFood) {
	    	hive.putFood();
	    	carriesFood = false;
    	}
    	idle();
    }

    public void move(PVector velocity) {
    	// TODO überarbeiten? die ameise hat ja schon eine blickrichtung. eigentlich muss sie nur x einheiten
    	// in blickrichtung laufen. zu kompliziert?
    	position.add(velocity);
    }

    public void turn(float degrees) {
    	// TODO rotate view direction by degrees
    }

    public void wander() {
    	// TODO switch to wander behavior
    }

    public void returnFood() {
    	// TODO switch to return food behavior
    }

    public void giveUp() {
    	// TODO switch to give up behavior
    }

    public void idle() {
    	// TODO switch to idle behavior
    }

    public void followPheromoneTrail() {
    	// TODO switch to follow trail behavior
    }

    public void die() {
    	antFarm.removeAnt(this);
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

	public PVector getViewDirection() {
		return viewDirection;
	}

	public void setViewDirection(PVector viewDirection) {
		this.viewDirection = viewDirection;
	}

	public long getTimeOfBirth() {
		return timeOfBirth;
	}

}
