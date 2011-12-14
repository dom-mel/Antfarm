package org.linesofcode.antfarm.behavior;

import org.linesofcode.antfarm.sceneObjects.PheromoneTrail;

import processing.core.PVector;

public class PathFollowingBehavior implements SteeringBehavior {

	private PheromoneTrail trail;

	public PathFollowingBehavior(PheromoneTrail trail) {
		this.trail = trail;
	}

	@Override
	public PVector getDirection() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getSpeedModifier() {
		// TODO Auto-generated method stub
		return 1f;
	}

}
