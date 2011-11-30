package org.linesofcode.antfarm;

import processing.core.PApplet;
import processing.core.PVector;

/**
 * @author Dominik Eckelmann
 */
public class Food {

    private long count;

    private PVector position;

    private final PApplet antFarm;

    public Food(PApplet antFarm) {
        this.antFarm = antFarm;
    }

    public void update(float delta) {

    }

    public void draw(PApplet applet) {

    }

    public void take() {
        count--;
    }

    public void deplete() {

    }

}
