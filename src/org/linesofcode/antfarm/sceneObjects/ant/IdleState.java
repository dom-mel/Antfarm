package org.linesofcode.antfarm.sceneObjects.ant;


public class IdleState implements AntState {

	private Ant ant;
	private float idleTime = 0f;
	
	public IdleState(Ant ant) {
		this.ant = ant;
	}
	
	@Override
	public void update(float delta) {
		idleTime += delta;
    	if(idleTime >= Ant.MAX_IDLE_TIME) {
    		ant.leaveHive();
    	}
	}

	@Override
	public void draw() {
		// ant is not visible when idle. Hence this method will not be called 
	}
}
