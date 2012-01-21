package org.linesofcode.antfarm.sceneObjects.ant;

public class ReturningHomeState implements AntState {

	private Ant ant;
	float trailTime;
	
	public ReturningHomeState(Ant ant) {
		this.ant = ant;
	}
	
	@Override
	public void update(float delta) {
		if(ant.isNearHive()) {
    		ant.enterHive();
    		return;
    	}
    	if(ant.isCarryingFood()) {
    		trailTime -= delta;
    		if(trailTime <= 0) {
    			ant.putTrail();
    			trailTime = Ant.TRAIL_INTERVAL;
    		}
    	}
	}

	@Override
	public void draw() {
		ant.drawAnt();
		if(ant.isCarryingFood()) {
			ant.drawCarriedFood();
		}
	}
}
