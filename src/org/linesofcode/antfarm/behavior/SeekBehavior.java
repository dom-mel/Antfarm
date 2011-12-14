package org.linesofcode.antfarm.behavior;

import processing.core.PVector;

public class SeekBehavior implements SteeringBehavior {

	private PVector target;

	public SeekBehavior(PVector target) {
		this.target = target;
	}

	@Override
	public PVector getDirection() {
		// TODO steer to target
		return null;
	}

	@Override
	public float getSpeedModifier() {
		// TODO Auto-generated method stub
		return 1f;
	}

}
