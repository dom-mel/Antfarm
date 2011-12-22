package org.linesofcode.antfarm.sceneObjects;

import org.linesofcode.antfarm.AntFarm;
import processing.core.PVector;

import java.awt.Color;

public class Food implements SceneObject {
	
	private static final int MAX_COUNT = 1000;
	private static final int SIZE = 10; 

    private int count;
    
    private PVector position;
    private BoundingBox bounds;

    private final AntFarm antFarm;

	private final int outlineColor = Color.BLACK.getRGB();
	private final int color = Color.GREEN.getRGB();

    public Food(final AntFarm antFarm) {
        this.antFarm = antFarm;
        count = (int) antFarm.random(MAX_COUNT * 0.90f, MAX_COUNT);
        do{
            position = new PVector(antFarm.random(SIZE / 2, antFarm.width - SIZE / 2),
                    antFarm.random(SIZE / 2, antFarm.height - SIZE / 2));
            bounds = new BoundingBox(position, SIZE, SIZE);
        } while (antFarm.collides(this));
    }

    public void draw() {
    	antFarm.stroke(outlineColor);
        antFarm.fill(color);
        // make the size of the food source shrink while it depletes
        final float relativeSize = ((float) count  / MAX_COUNT) * SIZE;
        antFarm.rect(position.x, position.y, relativeSize, relativeSize);
        bounds = new BoundingBox(position, relativeSize, relativeSize);
    }

    @Override
    public void update(final float delta) {
    }

    @Override
    public BoundingBox getBoundingBox() {
        return bounds;
    }

    public void pickUp() {
        count--;
        if (count == 0) {
            deplete();
        }
    }

    public void deplete() {
    	antFarm.removeFood(this);
    }
}
