package org.linesofcode.antfarm.sceneObjects;

import java.awt.Color;

import org.linesofcode.antfarm.AntFarm;
import org.linesofcode.antfarm.ObstacleCollisionException;
import org.linesofcode.antfarm.OutOfBoundsException;
import org.linesofcode.antfarm.behavior.PathFollowingBehavior;
import org.linesofcode.antfarm.behavior.SeekBehavior;
import org.linesofcode.antfarm.behavior.SteeringBehavior;
import org.linesofcode.antfarm.behavior.WanderingBehavior;

import processing.core.PVector;

public class Ant implements SceneObject {

	// TODO read from config
	public static float SIZE = 2f;
    public static float MAX_IDLE_TIME = 5f;
    public static float VIEW_DISTANCE = 30f;
    public static float FIELD_OF_VIEW = 120f;
    public static float MIN_TIME_TO_LIVE = 120f;
    public static float MAX_TIME_TO_LIVE = 180f;
    public static float MOVEMENT_RATE = 30f;
    public static float MIN_WANDERING_TIME = 10f;
    public static float MAX_WANDERING_TIME = 15f;

    private final AntFarm antFarm;
    private final Hive hive;
    private PVector position;
    private PVector viewDirection = new PVector(0f, 0f);
    private boolean visible = false;
    private int color;
    private float rotation = 0f;
    
    private AntState state;
    private boolean carriesFood;
    private float timeToLive;
    private float speedMultiplier;
    private float idleTime;
    private float wanderingTime;
    private float maxWanderingTime;
    SteeringBehavior behavior;
    private BoundingBox bounds;

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
        	wanderingTime += delta;
        	if(wanderingTime >= maxWanderingTime) {
        		returnHome();
        	}
        	break;
        }
        case RETURNING_HOME: {
        	if(isNearHive()) {
        		enterHive();
        		break;
        	}
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
        
        behavior.update(delta);
        turn(behavior.getRotationDelta());
        computeViewDirection();
        // TODO check: path blocked?
        move(delta);
        // TODO dodge if collision
    }

    private boolean isNearHive() {
    	float dx = Math.abs(position.x - hive.getCenter().x);
    	float dy = Math.abs(position.y - hive.getCenter().y);
		return dx <= (Hive.SIZE / 2) && dy <= (Hive.SIZE / 2);
	}

	private void computeViewDirection() {
		viewDirection.x = AntFarm.sin(rotation);
		viewDirection.y = -AntFarm.cos(rotation);
		viewDirection.normalize();
	}

    private void move(float delta) {
    	
    	PVector velocity = PVector.mult(viewDirection, MOVEMENT_RATE * delta);
    	velocity.mult(speedMultiplier);
    	PVector newPosition = PVector.add(position, velocity);
    	
    	try {
			antFarm.moveAnt(this, newPosition);
		} catch (OutOfBoundsException e) {
			// TODO change direction
		} catch (ObstacleCollisionException e) {
			// TODO dodge
		}
    }
    
	private void turn(float delta) {
		rotation += delta;
	}
    
	public void draw() {
		
    	if(!visible) {
    		return;
    	}
    	
    	antFarm.translate(position.x, position.y);
        antFarm.rotate(rotation);
        
        if(antFarm.isDrawViewDirectionEnabled()) {
        	antFarm.stroke(Color.RED.getRGB());
        	antFarm.line(0, 0, 0, -4f * SIZE);
    	}
        
        antFarm.stroke(color);
        antFarm.fill(color);
        
        antFarm.beginShape();
        antFarm.vertex(-SIZE, SIZE);
        antFarm.vertex(0, -SIZE);
        antFarm.vertex(SIZE, SIZE);
        antFarm.endShape();
        
        antFarm.rotate(-rotation);
        antFarm.translate(-position.x, -position.y);
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
        bounds = null;
    	idle();
    }
    
    private void leaveHive() {
    	position = hive.getSpawnPosition();
        bounds = new BoundingBox(position, SIZE, SIZE);
    	visible = true;
    	
    	PVector distance = PVector.sub(position, hive.getCenter());
    	distance.normalize();
    	PVector up = new PVector(0f, 1f);
    	rotation = PVector.angleBetween(distance, up);
    	
    	wanderingTime = 0f;
    	maxWanderingTime = antFarm.random(MIN_WANDERING_TIME, MAX_WANDERING_TIME);
    	wander();
    }

    private void wander() {
    	state = AntState.WANDERING;
    	behavior = new WanderingBehavior();
    }

    private void returnHome() {
    	state = AntState.RETURNING_HOME;
    	behavior = new SeekBehavior(hive.getCenter(), this);
    }

    private void idle() {
    	idleTime = 0;
    	state = AntState.IDLE;
    }

    private void die() {
    	visible = false;
    	antFarm.removeAnt(this);
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

    public Hive getHive() {
        return hive;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return bounds;
    }

	public float getHeading() {
		return rotation;
	}

	public void setHeading(float angle) {
		rotation = angle;
	}

	public void setPosition(PVector newPosition) {
		position = newPosition;
	}
}
