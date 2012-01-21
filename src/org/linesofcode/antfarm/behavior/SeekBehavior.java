package org.linesofcode.antfarm.behavior;

import org.linesofcode.antfarm.sceneObjects.ant.Ant;

import processing.core.PVector;

public class SeekBehavior implements SteeringBehavior {

	private static float UPDATES_PER_SECOND = 1f;
	
	private PVector target;
	private Ant ant;
	private float rotationDelta;
	private float timeSinceLastUpdate = 0f;
	
	public SeekBehavior(PVector target, Ant ant) {
		this.target = target;
		this.ant = ant;
		computeRotationDelta();
	}
	
	@Override
	public void update(float delta) {
		
		if(timeSinceLastUpdate < 1/UPDATES_PER_SECOND) {
			timeSinceLastUpdate += delta;
			return;
		}
		
		computeRotationDelta();
		timeSinceLastUpdate = 0f;
	}
	
	private void computeRotationDelta() {
		PVector a = ant.getViewDirection();
		PVector b = PVector.sub(target, ant.getPosition());
		rotationDelta = PVector.angleBetween(a, b);
	}
	
	@Override
	public float getRotationDelta() {
		float temp = rotationDelta;
		rotationDelta = 0f;
		return temp;
	}
}
