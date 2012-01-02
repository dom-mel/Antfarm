package org.linesofcode.antfarm.sceneObjects;

import processing.core.PVector;

import java.util.Arrays;
import java.util.Collection;

public class BoundingBox {
    private Collection<PVector> vertices;

    private PVector topLeft;
    private float width;
    private float height;


    public BoundingBox(final PVector translation, final float rotation, final PVector... vertices) {
        this(translation, rotation, Arrays.asList(vertices));
    }

    public BoundingBox(final PVector translation, final float rotation, final Collection<PVector> vertices) {
        this.vertices = vertices;
        float minX = Float.MAX_VALUE;
        float maxX = Float.MIN_VALUE;
        float minY = Float.MAX_VALUE;
        float maxY = Float.MIN_VALUE;

        for (final PVector vertex : vertices) {
            vertex.rotate(rotation);
            vertex.add(translation);
            if (vertex.x < minX) {
                minX = vertex.x;
            }
            if (vertex.x > maxX) {
                maxX = vertex.x;
            }

            if (vertex.y < minY) {
                minY = vertex.y;
            }
            if (vertex.y > maxY) {
                maxY = vertex.y;
            }
        }
        topLeft = new PVector(minX, minY);
        width = maxX - minX;
        height = maxY - minY;
    }

    public BoundingBox getTransformedBoundingBox(final PVector translation, final float rotation) {
        return new BoundingBox(translation, rotation, vertices);
    }

    public boolean intersects(final BoundingBox other) {
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
