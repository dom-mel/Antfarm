package org.linesofcode.antfarm.sceneObjects;

import controlP5.ControlEvent;
import controlP5.ControlListener;
import controlP5.ControlP5;
import controlP5.Slider;
import controlP5.Tab;
import org.linesofcode.antfarm.AntFarm;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Dominik Eckelmann
 */
public class Overlay {

    private static final float OVERLAY_KEY_BLOCK = 0.2f;
    private static final char OVERLAY_KEY = 'o';

    private final ControlP5 controlP5;
    private final AntFarm antFarm;

    private final Set<Slider> sliders;
    private final Set<Tab> tabs;
    private float block;

    private boolean visible;

    public Overlay(final AntFarm antFarm) {
        this.antFarm = antFarm;
        controlP5 = new ControlP5(antFarm);
        sliders = new HashSet<Slider> ();
        tabs = new HashSet<Tab>();
        visible = false;
        block = 0;
        setupStatic();
        hideSlider();
    }

    private void setupStatic() {
        // controlP5.addSlider(name, min, max, defaultValue, x, y, width, heigth)
        Tab defaultTab = controlP5.tab("default");
        defaultTab.setLabel("General");

        final Slider antSize = controlP5.addSlider("Ant size", 0, 10, 1, 10, 40, 100, 20);
        antSize.moveTo(defaultTab);
        antSize.addListener(new ControlListener() {
            @Override
            public void controlEvent(final ControlEvent controlEvent) {
                Ant.SIZE = antSize.value();
            }
        });
        sliders.add(antSize);

        final Slider timeLaps = controlP5.addSlider("Timelaps", 0.25f, 8, AntFarm.timeLapse, 10, 80, 100, 20);
        timeLaps.moveTo(defaultTab);
        timeLaps.addListener(new ControlListener() {
            @Override
            public void controlEvent(final ControlEvent controlEvent) {
                AntFarm.timeLapse = timeLaps.value();
            }
        });
        sliders.add(timeLaps);
    }


    public void draw() {
        if (!visible) {
            return;
        }

        antFarm.fill(0, 0, 0, 88);
        antFarm.stroke(0, 0, 0, 88);
        antFarm.strokeWeight(0);
        antFarm.rect(0, 0, antFarm.width, antFarm.height);

    }

    public void update(final float delta) {
        block += delta;
        if (block > OVERLAY_KEY_BLOCK && antFarm.keyPressed) {
            if (antFarm.key == OVERLAY_KEY) {
                visible = !visible;
                if (visible) {
                    showSlider();
                } else {
                    hideSlider();
                }
            }
            block = 0;
        }
    }

    private void showSlider() {
        for (final Slider slider : sliders) {
            slider.show();
        }
        for (final Tab tab : tabs) {
            tab.show();
        }
    }

    private void hideSlider() {
        for (final Slider slider : sliders) {
            slider.hide();
        }
        for (final Tab tab : tabs) {
            tab.hide();
        }
    }
}
