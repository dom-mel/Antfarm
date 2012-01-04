package org.linesofcode.antfarm.sceneObjects;

import org.linesofcode.antfarm.AntFarm;
import processing.core.PVector;

import java.awt.Color;

public class Food implements SceneObject {
	
	private static int MAX_COUNT = 20;
	private static int SIZE = 20;
	public static int outlineColor = Color.BLACK.getRGB();
	public static int color = Color.GREEN.getRGB();

    private int count;
    private PVector position;
    private final AntFarm antFarm;
    private float relativeSize;

    public Food(final AntFarm antFarm) {
        this.antFarm = antFarm;
        count = (int) antFarm.random(MAX_COUNT * 0.1f, MAX_COUNT);
        position = antFarm.calcStaticSpawnPosition(this, SIZE);
    }

    public void draw() {
    	antFarm.stroke(outlineColor);
        antFarm.fill(color);
        // make the size of the food source shrink while it depletes
        relativeSize = ((float) count  / MAX_COUNT) * SIZE;
        antFarm.ellipse(position.x, position.y, relativeSize, relativeSize);
    }

    @Override
    public void update(final float delta) {
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

    public PVector getPosition() {
        return position;
    }
    
    public float getRelativeSize() {
    	return relativeSize;
    }
}
