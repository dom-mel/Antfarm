package org.linesofcode.antfarm.sceneObjects;

import org.linesofcode.antfarm.AntFarm;
import processing.core.PVector;

import java.awt.Color;
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

        for (final PVector vertex: vertices) {
            final PVector translated = new PVector(vertex.x, vertex.y);
            translated.rotate(rotation);
            translated.add(translation);
            if (translated.x < minX) {
                minX = translated.x;
            }
            if (translated.x > maxX) {
                maxX = translated.x;
            }

            if (translated.y < minY) {
                minY = translated.y;
            }
            if (translated.y > maxY) {
                maxY = translated.y;
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

     public void draw(final AntFarm antFarm) {
        antFarm.stroke(Color.RED.getRGB());
        antFarm.fill(antFarm.color(0,0,0,1));
        antFarm.rect(topLeft.x, topLeft.y, width, height);
    }
}
