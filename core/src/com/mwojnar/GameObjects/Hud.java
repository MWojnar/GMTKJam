package com.mwojnar.GameObjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.mwojnar.Assets.AssetLoader;
import com.mwojnar.GameWorld.GMTKJamWorld;
import com.playgon.GameEngine.Entity;
import com.playgon.GameEngine.Sprite;
import com.playgon.GameWorld.GameRenderer;
import com.playgon.GameWorld.GameWorld;
import com.playgon.Utils.PlaygonMath;

public class Hud extends Entity {
	
	private int fadeTime = 60;
	private float lastDistance = 0.0f;
	
	public Hud(GameWorld myWorld) {
		
		super(myWorld);
		setForceUpdate(true);
		setDepth(10000);
		
	}
	
	@Override
	public void draw(GameRenderer renderer) {
		
		Submarine sub = ((GMTKJamWorld)getWorld()).getSubmarine();
		float alpha = 1.0f;
		if (sub.isDead()) {
			
			fadeTime--;
			if (fadeTime <= 0)
				fadeTime = 0;
			alpha = fadeTime / 60.0f;
			
		}
		Color redWithAlpha = Color.RED.cpy();
		redWithAlpha.a = alpha;
		int frame = (int)(59.0f - (59.0f * sub.getAir() / sub.getMaxAir()));
		if (frame > 58)
			frame = 58;
		if (frame < 0)
			frame = 0;
		if ((sub.getAir() / sub.getMaxAir() <= 0.25f && ((GMTKJamWorld)getWorld()).getFramesSinceLevelCreation() % 20 > 9) || sub.getAir() <= 0.0f)
			AssetLoader.spriteAirMeter.drawAbsolute(2.0f, getWorld().getGameDimensions().y - AssetLoader.spriteAirMeter.getHeight() - 2.0f, frame, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, redWithAlpha, renderer);
		else
			AssetLoader.spriteAirMeter.drawAbsolute(2.0f, getWorld().getGameDimensions().y - AssetLoader.spriteAirMeter.getHeight() - 2.0f, frame, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, alpha, renderer);
		
		drawScore(renderer, alpha);
		drawDistanceHud(renderer, alpha);
		
	}
	
	private void drawScore(GameRenderer renderer, float alpha) {

		Submarine sub = ((GMTKJamWorld)getWorld()).getSubmarine();
		AssetLoader.debugFont.setColor(1.0f, 1.0f, 1.0f, alpha);
		AssetLoader.debugFont.draw(renderer.getBatcher(), "SCORE", getWorld().getCamPos(false).x + 2.0f, getWorld().getCamPos(false).y + 2.0f);
		AssetLoader.debugFont.draw(renderer.getBatcher(), Integer.toString(((GMTKJamWorld)getWorld()).getRawScore()), getWorld().getCamPos(false).x + 2.0f, getWorld().getCamPos(false).y + 20.0f);
		AssetLoader.debugFont.draw(renderer.getBatcher(), "COMBO", getWorld().getCamPos(false).x + 2.0f, getWorld().getCamPos(false).y + 56.0f);
		AssetLoader.debugFont.draw(renderer.getBatcher(), Integer.toString(sub.getCombo()), getWorld().getCamPos(false).x + 2.0f, getWorld().getCamPos(false).y + 74.0f);
		
	}

	private void drawDistanceHud(GameRenderer renderer, float alpha) {
		
		Submarine sub = ((GMTKJamWorld)getWorld()).getSubmarine();
		if (sub.getPos(true).y < ((GMTKJamWorld)getWorld()).getMonster().getPos(true).y) {
			
			float distance = lastDistance;
			if (!sub.isDead())
				distance = PlaygonMath.distance(sub.getPos(true), ((GMTKJamWorld)getWorld()).getMonster().getPos(true));
			lastDistance = distance;
			if (distance > 6200)
				distance = 6200;
			float x1 = getWorld().getGameDimensions().x - 2.0f - AssetLoader.spriteDistanceHud.getWidth() / 2.0f;
			float y1 = getWorld().getGameDimensions().y - 2.0f - AssetLoader.spriteDistanceHud.getHeight() / 2.0f;
			float x2 = x1;
			float y2 = y1 - distance / 10.0f;
			
			AssetLoader.spriteWhite.drawMonocolorLine(getWorld().getCamPos(false).x + x1, getWorld().getCamPos(false).y + y1, getWorld().getCamPos(false).x + x2, getWorld().getCamPos(false).y + y2, 2.0f, alpha, renderer);
			AssetLoader.spriteDistanceHud.drawAbsolute(x1 - AssetLoader.spriteDistanceHud.getWidth() / 2.0f, y1 - AssetLoader.spriteDistanceHud.getHeight() / 2.0f, 1, 1.0f, 1.0f, 0.0f, AssetLoader.spriteDistanceHud.getWidth() / 2.0f, AssetLoader.spriteDistanceHud.getHeight() / 2.0f, alpha, renderer);
			AssetLoader.spriteDistanceHud.drawAbsolute(x2 - AssetLoader.spriteDistanceHud.getWidth() / 2.0f, y2 - AssetLoader.spriteDistanceHud.getHeight() / 2.0f, 0, 1.0f, 1.0f, 0.0f, AssetLoader.spriteDistanceHud.getWidth() / 2.0f, AssetLoader.spriteDistanceHud.getHeight() / 2.0f, alpha, renderer);
			
		} else {
			
			float x = getWorld().getGameDimensions().x - 2.0f - AssetLoader.spriteDistanceHud.getWidth() / 2.0f;
			float y = getWorld().getGameDimensions().y - 2.0f - AssetLoader.spriteDistanceHud.getHeight() / 2.0f;
			AssetLoader.spriteDistanceHud.drawAbsolute(x - AssetLoader.spriteDistanceHud.getWidth() / 2.0f, y - AssetLoader.spriteDistanceHud.getHeight() / 2.0f, 1, 1.0f, 1.0f, 0.0f, AssetLoader.spriteDistanceHud.getWidth() / 2.0f, AssetLoader.spriteDistanceHud.getHeight() / 2.0f, alpha, renderer);
			
		}
		
	}
	
}