package org.linesofcode.antfarm;

import org.linesofcode.antfarm.sceneObjects.Obstacle;

@SuppressWarnings("serial")
public class ObstacleCollisionException extends Exception {

	private Obstacle obstacle;
	
	public ObstacleCollisionException(Obstacle obstacle) {
		this.obstacle = obstacle;
	}
	
	public Obstacle getObstacle() {
		return obstacle;
	}
}
