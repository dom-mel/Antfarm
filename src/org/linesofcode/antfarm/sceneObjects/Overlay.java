package org.linesofcode.antfarm.sceneObjects;

import controlP5.ControlP5;
import controlP5.Slider;
import org.linesofcode.antfarm.AntFarm;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Dominik Eckelmann
 */
public class Overlay implements SceneObject {

    private ControlP5 controlP5;
    private AntFarm antFarm;

    private Map<String, Slider> sliders;
    private int sliderCount;

    private boolean visible;

    public Overlay(AntFarm antFarm) {
        this.antFarm = antFarm;
        controlP5 = new ControlP5(antFarm);
        sliders = new HashMap<String, Slider>();
        sliderCount = 0;
        visible = false;
    }

    public Slider addSlider(String name, float min, float max, float defaultValue) {
        Slider slider = controlP5.addSlider(name, min, max, defaultValue, 10, sliderCount, 100, 10);
        if (!visible) {
            slider.hide();
        }
        sliders.put(name, slider);
        sliderCount += 15;
        return slider;
    }

    @Override
    public void draw() {
        if (visible == false) {
            return;
        }

        antFarm.fill(0, 0, 0, 88);
        antFarm.strokeWeight(0);
        antFarm.rect(0, 0, antFarm.width, antFarm.height);

    }

    @Override
    public void update(float delta) {
        if (antFarm.keyPressed) {
            if (antFarm.key == 's') {
                visible = !visible;
                if (visible) {
                    showSlider();
                } else {
                    hideSlider();
                }
            }
        }
    }

    private void showSlider() {
        for (Slider slider : sliders.values()) {
            slider.show();
        }
    }

    private void hideSlider() {
        for (Slider slider : sliders.values()) {
            slider.hide();
        }
    }
}
