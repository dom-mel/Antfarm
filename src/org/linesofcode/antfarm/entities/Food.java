package org.linesofcode.antfarm.entities;

import java.awt.Color;

import org.linesofcode.antfarm.AntFarm;

import processing.core.PVector;

public class Food {
	
	private static final int MAX_COUNT = 1000;
	private static final int SIZE = 10; 

    private int count;
    
    private PVector position;

    private final AntFarm antFarm;

	private int outlineColor = Color.BLACK.getRGB();
	private int color = Color.GREEN.getRGB();

    public Food(AntFarm antFarm) {
        this.antFarm = antFarm;
    }

    public void draw() {
    	antFarm.stroke(outlineColor);
        antFarm.fill(color);
        // make the size of the food source shrink while it depletes
        float relativeSize = ((count * 100) / MAX_COUNT) * SIZE;
        antFarm.rect(position.x, position.y, relativeSize, relativeSize);
    }

    public void pickUp() {
        count--;
    }

    public void deplete() {
    	antFarm.removeFood(this);
    }
}
