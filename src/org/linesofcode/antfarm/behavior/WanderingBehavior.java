package org.linesofcode.antfarm.behavior;

import java.util.Random;

public class WanderingBehavior implements SteeringBehavior {
	
	public static int UPDATES_PER_SECOND = 25;
	public static float MAX_ANGLE = 10f;
	public static float MIN_ANGLE = 0f;
	
	private float timeSinceLastUpdate = 0f;
	private float rotationDelta = 0f;
	
	private Random random = new Random();
	
	public WanderingBehavior() {
		random.setSeed(System.currentTimeMillis());
	}
	
	@Override
	public float getRotationDelta() {
		return rotationDelta;
	}

	@Override
	public void update(float delta) {
		
		if(timeSinceLastUpdate < 1/UPDATES_PER_SECOND) {
			timeSinceLastUpdate += delta;
			return;
		}
		
		timeSinceLastUpdate = 0;
		setRotationDelta(getRandomNumber(MIN_ANGLE, MAX_ANGLE) * (float)Math.pow(-1.0, random.nextInt(30)));
	}

	private void setRotationDelta(float angleInDegrees) {
		rotationDelta = (float)Math.toRadians(angleInDegrees);
	}

	private float getRandomNumber(float min, float max) {
		return (float)random.nextDouble() * (max - min) + min;
	}
}
