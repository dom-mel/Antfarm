package org.linesofcode.antfarm.sceneObjects;

import java.awt.Color;

import org.linesofcode.antfarm.AntFarm;
import org.linesofcode.antfarm.behavior.PathFollowingBehavior;
import org.linesofcode.antfarm.behavior.SeekBehavior;
import org.linesofcode.antfarm.behavior.SteeringBehavior;
import org.linesofcode.antfarm.behavior.WanderingBehavior;

import processing.core.PVector;

public class Ant implements SceneObject {

	// TODO read from config
    private static final float SIZE = 2f;
    private static final float MAX_IDLE_TIME = 5f;
    private static final float VIEW_DISTANCE = 30f;
    private static final float FIELD_OF_VIEW = 120f;
    private static final float MIN_TIME_TO_LIVE = 25f;
    private static final float MAX_TIME_TO_LIVE = 40f;
    private static final float MOVEMENT_RATE = 30f;
    private static final float TURN_RATE = 90f;

    private final AntFarm antFarm;
    private final Hive hive;
    private PVector position;
    private PVector viewDirection;
    private boolean visible = false;
    private int color;
    
    private AntState state;
    private boolean carriesFood;
    private float timeToLive;
    private float speedMultiplier;
    private float idleTime;
    SteeringBehavior behavior;

    public Ant(AntFarm antFarm, Hive hive) {
        this.antFarm = antFarm;
        this.hive = hive;
        color = hive.getColor();
        timeToLive = antFarm.random(25f, 40f);
        speedMultiplier = antFarm.random(0.75f, 1.25f);
        enterHive();
    }

    public void update(float delta) {

    	timeToLive -= delta;
    	if(timeToLive <= 0) {
    		die();
    		return;
    	}
    	
        switch(state) {
        case IDLE: {
        	idleTime += delta;
        	if(idleTime >= MAX_IDLE_TIME) {
        		leaveHive();
        	}
        	return;
        }
        case WANDERING: {
        	// TODO check food / trail found
        	break;
        }
        case RETURNING_HOME: {
        	// TODO enter hive, if collision with hive
        	if(carriesFood) {
        		putTrail();
        	}
        	break;
        }
        case FOLLOWING_TRAIL: {
        	// TODO check for trail
        	// TODO if no trail, wander
        	break;
        }
        default: {
        	throw new RuntimeException("Unrecognized ant state");
        }
        }
        
    	//turn(behavior.getSteeringForce(), delta);
        steer(new PVector(1,1), delta);
        move(delta);
    }
    
    private void steer(PVector steeringDirection, float delta) {
    	PVector direction = PVector.add(viewDirection, steeringDirection);
		setViewDirection(direction);
	}

    private void move(float delta) {
    	PVector velocity = PVector.mult(viewDirection, MOVEMENT_RATE * delta);
    	velocity.mult(speedMultiplier);
    	position.add(velocity);
    }
    
	public void draw() {
    	if(!visible) {
    		return;
    	}
    	if(antFarm.isDrawViewDirection()) {
    		drawViewDirection();
    	}
        antFarm.stroke(color);
        antFarm.fill(color);
        antFarm.rect(position.x, position.y, SIZE, SIZE);
    }

	private void drawViewDirection() {
		antFarm.stroke(Color.RED.getRGB());
    	PVector direction = PVector.add(position, PVector.mult(viewDirection, 10f));
        antFarm.line(position.x, position.y, direction.x, direction.y);
	}

	@Deprecated
    private void turn(PVector force, float delta) {
    	float adjustedRate = TURN_RATE * delta;
    	float angle = PVector.angleBetween(viewDirection, force) / adjustedRate;
    	System.out.println(angle);
		viewDirection.rotate(AntFarm.radians(angle));
	}

	private void putTrail() {
		// TODO put a trail
		
	}

    private void pickupFood(Food food) {
    	food.pickUp();
    	carriesFood = true;
    }

    private void enterHive() {
    	hive.pickUpAnt();
    	if(carriesFood) {
	    	hive.putFood();
	    	carriesFood = false;
    	}
    	visible = false;
    	idle();
    }
    
    private void leaveHive() {
    	position = hive.getSpawnPosition();
    	setViewDirection(PVector.add(hive.getCenter(), PVector.mult(position, -1)));
    	visible = true;
    	wander();
    }

    private void wander() {
    	state = AntState.WANDERING;
    	behavior = new WanderingBehavior();
    }

    private void returnHome() {
    	state = AntState.RETURNING_HOME;
    	behavior = new SeekBehavior(hive.getCenter());
    }

    private void idle() {
    	idleTime = 0;
    	state = AntState.IDLE;
    }

    private void followPheromoneTrail(PheromoneTrail trail) {
    	state = AntState.FOLLOWING_TRAIL;
    	behavior = new PathFollowingBehavior(trail);
    }

    private void die() {
    	visible = false;
    	antFarm.removeAnt(this);
    	System.out.println("Argh!!");
    }

    public AntFarm getAntFarm() {
        return antFarm;
    }

    public PVector getPosition() {
        return position;
    }

	public PVector getViewDirection() {
		return viewDirection;
	}

	public void setViewDirection(PVector viewDirection) {
		this.viewDirection = viewDirection;
		this.viewDirection.normalize();
	}
}
