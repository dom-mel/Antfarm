package org.linesofcode.antfarm.entities;

import processing.core.PVector;

public class BoundingBox {
    private PVector topLeft;
    private float width;
    private float height;

    public BoundingBox(PVector topLeft, float width, float height) {
        this.topLeft = topLeft;
        this.width = width;
        this.height = height;
    }

    public boolean intersects(BoundingBox other) {
        if (topLeft.x > other.topLeft.x + other.width) {
            return false;
        }
        if (topLeft.x + width < other.topLeft.x) {
            return false;
        }
        if (topLeft.y > other.topLeft.y + other.height) {
            return false;
        }
        return topLeft.y + height >= other.topLeft.y;
    }

}
