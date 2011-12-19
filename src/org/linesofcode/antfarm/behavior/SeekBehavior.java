package org.linesofcode.antfarm.behavior;

import org.linesofcode.antfarm.sceneObjects.Ant;

import processing.core.PVector;

public class SeekBehavior implements SteeringBehavior {

	private PVector target;
	private Ant ant;
	
	public SeekBehavior(PVector target, Ant ant) {
		this.target = target;
		this.ant = ant;
	}
	
	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public float getRotationDelta() {
		// TODO Auto-generated method stub
		return 0;
	}

}
