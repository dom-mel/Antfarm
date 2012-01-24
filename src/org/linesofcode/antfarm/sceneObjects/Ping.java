package org.linesofcode.antfarm.sceneObjects;

import java.awt.Color;

import org.linesofcode.antfarm.AntFarm;

public class Ping implements SceneObject {

	public static final float MAX_TIME = 3f;
	public static final float SIZE = 30f;
	public static final int COLOR = new Color(255,0,0).getRGB();
	public static final float FRAME_TIME = 1/25;
	public static final int MAX_FRAMES = 25;
	
	private AntFarm antFarm;
	private float time = 0f;
	private float frameTime = 0f;
	private float frames = 0f;
	private float x;
	private float y;
	private float size = 0f;
	private float alpha = 1f;
	
	public Ping(AntFarm antFarm, float x, float y) {
		this.antFarm = antFarm;
		this.x = x;
		this.y = y;
	}
	
	@Override
	public void draw() {
		antFarm.stroke(new Color(255,255,255, (int)(alpha*255)).getRGB());
		antFarm.noFill();
		antFarm.strokeWeight(2f);
		antFarm.ellipse(x, y, size, size);
	}

	@Override
	public void update(float delta) {
		
		time += delta;
		if(time > MAX_TIME) {
			antFarm.removeSceneObject(this);
			return;
		}
		
		frameTime += delta;
		if(frameTime > FRAME_TIME) {
			size = (frames / MAX_FRAMES) * SIZE;
			alpha = 1 - (frames / MAX_FRAMES);
			frameTime = 0f;
			frames++;
		}
		
		if(frames > MAX_FRAMES) {
			size = 0f;
			frames = 0;
			alpha = 1f;
		}
	}

}
