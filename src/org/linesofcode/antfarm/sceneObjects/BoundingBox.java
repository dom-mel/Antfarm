package org.linesofcode.antfarm.sceneObjects;

import org.linesofcode.antfarm.AntFarm;
import processing.core.PVector;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collection;

public class BoundingBox {
    private PVector topLeft;
    private float width;
    private float height;

    public BoundingBox(final PVector topLeft, final float width, final float height) {
        this.topLeft = topLeft;
        this.width = width;
        this.height = height;
    }

    public BoundingBox(final PVector translation, final float rotation, final PVector... vertices) {
        this(translation, rotation, Arrays.asList(vertices));
    }

    public BoundingBox(final PVector translation, final float rotation, final Collection<PVector> vertices) {
        float minX = Float.MAX_VALUE;
        float maxX = Float.MIN_VALUE;
        float minY = Float.MAX_VALUE;
        float maxY = Float.MIN_VALUE;

        for (final PVector vertex: vertices) {
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

    public void draw(final AntFarm antFarm) {
        antFarm.stroke(Color.RED.getRGB());
        antFarm.fill(antFarm.color(0,0,0,1));
        antFarm.rect(topLeft.x, topLeft.y, width, height);
    }

    public boolean intersects(final BoundingBox other) {
        if (topLeft.y > other.topLeft.y + other.height) {
            return false;
        }
        if (topLeft.y + height < other.topLeft.y) {
            return false;
        }
        if (topLeft.x > other.topLeft.x + other.width) {
            return false;
        }
        if (topLeft.x + width < other.topLeft.x) {
            return false;
        }

        return true;
    }

    public BoundingBox getTransformedBoundingBox(PVector position, float rotation) {
        PVector.add(topLeft, position).rotate(rotation);
        return new BoundingBox(topLeft, 0,0);
    }

    public PVector getTopLeft() {
        return topLeft;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
