package org.linesofcode.antfarm.behavior;

import org.linesofcode.antfarm.entities.Ant;

import processing.core.PVector;

/**
 * @author Dominik Eckelmann
 */
public class DummyBehavior implements Behavior {

    @Override
    public void update(float delta, Ant ant) {
        PVector position = ant.getPosition();
        position.add(ant.getSpeedMultiplier() * delta, ant.getSpeedMultiplier() * delta, 0);
        if (position.x > ant.getAntFarm().width) {
            position.x = 0;
        }
        if (position.y > ant.getAntFarm().height) {
            position.y = 0;
        }
    }
}
