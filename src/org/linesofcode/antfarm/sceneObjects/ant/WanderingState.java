package org.linesofcode.antfarm.sceneObjects.ant;

import org.linesofcode.antfarm.sceneObjects.Food;

public class WanderingState implements AntState {

	private Ant ant;
	private float wanderingTime = 0f;
	private float maxWanderingTime;
	
	public WanderingState(Ant ant) {
		this.ant = ant;
		maxWanderingTime = ant.getAntFarm().random(Ant.MIN_WANDERING_TIME, Ant.MAX_WANDERING_TIME);
	}
	
	@Override
	public void update(float delta) {
		wanderingTime += delta;
    	if(wanderingTime >= maxWanderingTime) {
    		ant.returnHome();
    		return;
    	}
    	Food food = ant.getAntFarm().getFoodInProximity(ant);
    	if(food != null) {
    		ant.approachFood(food);
    	}
	}

	@Override
	public void draw() {
		ant.drawAnt();
	}
}
