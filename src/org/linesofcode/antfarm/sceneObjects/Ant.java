package org.linesofcode.antfarm.sceneObjects;

import controlP5.Slider;
import org.linesofcode.antfarm.AntFarm;
import org.linesofcode.antfarm.behavior.SeekBehavior;
import org.linesofcode.antfarm.behavior.SteeringBehavior;
import org.linesofcode.antfarm.behavior.WanderingBehavior;
import processing.core.PVector;

import java.awt.Color;

public class Ant implements SceneObject {

	// TODO read from config
	public static Slider SIZE;
    public static Slider MAX_IDLE_TIME;
    public static float VIEW_DISTANCE = 30f;
    public static float FIELD_OF_VIEW = 120f;
    public static float MIN_TIME_TO_LIVE = 120f;
    public static float MAX_TIME_TO_LIVE = 180f;
    public static Slider MOVEMENT_RATE;
    public static float TURN_RATE = AntFarm.radians(45f);
    public static Slider MIN_WANDERING_TIME;
    public static Slider MAX_WANDERING_TIME;

    private final AntFarm antFarm;
    private final Hive hive;
    private PVector position;
    private PVector viewDirection = new PVector(0f, 0f);
    private boolean visible = false;
    private int color;
    private float rotation = 0f;
    private float rotationDelta;
    
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
        setupSliders();
        this.hive = hive;
        color = hive.getColor();
        timeToLive = antFarm.random(25f, 40f);
        speedMultiplier = antFarm.random(0.75f, 1.25f);
        enterHive();
        rotationDelta = AntFarm.radians(180f);
    }

    private void setupSliders() {
        if (SIZE != null) {
            return;
        }
        SIZE = antFarm.addSlider("Ant size", 2, 20, 2);
        MAX_IDLE_TIME = antFarm.addSlider("Ant max idle time", 1, 100,  5);
        MOVEMENT_RATE = antFarm.addSlider("Movement rate", 1,  300,  30);
        MIN_WANDERING_TIME = antFarm.addSlider("Min wandering time", 0, 80,  60);
        MAX_WANDERING_TIME = antFarm.addSlider("Max wandering time", 80,  360,  90);
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
        	if(idleTime >= MAX_IDLE_TIME.value()) {
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
        
        behavior.update(delta);
        turn((float)Math.toRadians(behavior.getRotationDelta()));
        computeViewDirection();
        move(delta);
    }

    private void computeViewDirection() {
		viewDirection.x = AntFarm.sin(rotation);
		viewDirection.y = -AntFarm.cos(rotation);
		viewDirection.normalize();
	}

    private void move(float delta) {
    	PVector velocity = PVector.mult(viewDirection, MOVEMENT_RATE.value() * delta);
    	velocity.mult(speedMultiplier);
    	position.add(velocity);
    }
    
	private void turn(float delta) {
		rotation += delta;
	}
    
	public void draw() {
		
    	if(!visible) {
    		return;
    	}

        bounds = new BoundingBox(position, rotation, new PVector(-SIZE.value(),SIZE.value()), new PVector(0,-SIZE.value()), new PVector(SIZE.value(),SIZE.value()));
        if (antFarm.collides(this)) {
            return;
        }
    	
    	antFarm.translate(position.x, position.y);
        antFarm.rotate(rotation);
        
        if(antFarm.isDrawViewDirectionEnabled()) {
        	antFarm.stroke(Color.RED.getRGB());
        	antFarm.line(0, 0, 0, -4f * SIZE.value());
    	}
        
        antFarm.stroke(color);
        antFarm.fill(color);
        
        antFarm.beginShape();
        antFarm.vertex(-SIZE.value(), SIZE.value());
        antFarm.vertex(0, -SIZE.value());
        antFarm.vertex(SIZE.value(), SIZE.value());
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
        bounds = new BoundingBox(position, SIZE.value(), SIZE.value());
    	visible = true;
    	PVector distance = PVector.sub(position, hive.getCenter());
    	float dot = hive.getCenter().x * distance.x + hive.getCenter().y * distance.y;
    	float magHive = (float)Math.sqrt(hive.getCenter().x * hive.getCenter().x + hive.getCenter().y * hive.getCenter().y);
    	float magPosition = (float)Math.sqrt(distance.x * distance.x + distance.y * distance.y);
    	rotation = (float)Math.acos(dot / (magHive * magPosition));
    	wanderingTime = 0f;
    	maxWanderingTime = antFarm.random(MIN_WANDERING_TIME.value(), MAX_WANDERING_TIME.value());
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
}
