package org.linesofcode.antfarm.behavior;

import processing.core.PVector;

public interface SteeringBehavior {
    public PVector getDirection();
    public float getSpeedModifier();
}
