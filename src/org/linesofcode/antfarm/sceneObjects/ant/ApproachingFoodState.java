package org.linesofcode.antfarm.sceneObjects.ant;

import org.linesofcode.antfarm.sceneObjects.Food;

public class ApproachingFoodState implements AntState {

	private Ant ant;
	private Food food;
	
	public ApproachingFoodState(Ant ant, Food food) {
		this.ant = ant;
		this.food = food;
	}
	
	@Override
	public void update(float delta) {
		if(ant.isFoodClose(food)) {
    		ant.pickupFood(food);
    		ant.returnHome();
    	}
	}

	@Override
	public void draw() {
		ant.drawAnt();
	}
}
