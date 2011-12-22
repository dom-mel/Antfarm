package org.linesofcode.antfarm.sceneObjects;

import processing.core.PVector;

import java.util.Collection;

public class BoundingBox {
    private PVector topLeft;
    private float width;
    private float height;

    public BoundingBox(PVector topLeft, float width, float height) {
        this.topLeft = topLeft;
        this.width = width;
        this.height = height;
    }

    public BoundingBox(Collection<PVector> vertices) {
        // TODO: compute bounds
    }

    public void draw() {
        // TODO: draw bounds
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

    public BoundingBox getTransformedBoundingBox(PVector position, float rotation) {
        PVector.add(topLeft, position).rotate(rotation);
        return new BoundingBox(topLeft, 0,0);
    }

}
