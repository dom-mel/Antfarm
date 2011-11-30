package org.linesofcode.antfarm;

import processing.core.PApplet;
import processing.core.PVector;

/**
 * @author Dominik Eckelmann
 */
public class Food {

    private long count;

    private PVector position;

    private final AntFarm antFarm;

    public Food(AntFarm antFarm) {
        this.antFarm = antFarm;
    }

    public void update(float delta) {

    }

    public void draw() {

    }

    public void take() {
        count--;
    }

    public void deplete() {

    }

}
