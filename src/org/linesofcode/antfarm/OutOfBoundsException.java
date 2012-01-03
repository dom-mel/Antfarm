package org.linesofcode.antfarm;

@SuppressWarnings("serial")
public class OutOfBoundsException extends Exception {

	private Direction direction;
	
	public OutOfBoundsException(Direction direction) {
		this.direction = direction;
	}
	
	public Direction getDirection() {
		return direction;
	}

	public static enum Direction {
		Y_AXIS,
		X_AXIS;
	}
}
