package org.linesofcode.antfarm.sceneObjects;

import org.junit.Assert;
import org.junit.Test;
import processing.core.PVector;

/**
 * @author Dominik Eckelmann
 */
public class BoundingBoxTest {

    @Test
    public void testBoundingIntersection() {
        final BoundingBox me = new BoundingBox(new PVector(5,5), 1, 1);

        Assert.assertTrue(me.intersects(me)); // i hit myself

        Assert.assertFalse(me.intersects(new BoundingBox(new PVector(3,5), 1, 1))); // left
        Assert.assertFalse(me.intersects(new BoundingBox(new PVector(7,5), 1, 1))); // right
        Assert.assertFalse(me.intersects(new BoundingBox(new PVector(5,3), 1, 1))); // top
        Assert.assertFalse(me.intersects(new BoundingBox(new PVector(5,7), 1, 1))); // bottom
        Assert.assertFalse(me.intersects(new BoundingBox(new PVector(3,3), 1, 1))); // topleft
        Assert.assertFalse(me.intersects(new BoundingBox(new PVector(7,7), 1, 1))); // bottom right

        // backwards
        Assert.assertFalse(new BoundingBox(new PVector(3, 5), 1, 1).intersects(me)); // left
        Assert.assertFalse(new BoundingBox(new PVector(7, 5), 1, 1).intersects(me)); // right
        Assert.assertFalse(new BoundingBox(new PVector(5, 3), 1, 1).intersects(me)); // top
        Assert.assertFalse(new BoundingBox(new PVector(5, 7), 1, 1).intersects(me)); // bottom
        Assert.assertFalse(new BoundingBox(new PVector(3, 3), 1, 1).intersects(me)); // topleft
        Assert.assertFalse(new BoundingBox(new PVector(7, 7), 1, 1).intersects(me)); // bottom right


        Assert.assertTrue(me.intersects(new BoundingBox(new PVector(3,3), 5,5)));



    }
}
