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

	public Hud(GameWorld myWorld) {
		
		super(myWorld);
		setForceUpdate(true);
		setDepth(10000);
		
	}
	
	@Override
	public void draw(GameRenderer renderer) {
		
		Submarine sub = ((GMTKJamWorld)getWorld()).getSubmarine();
		int frame = (int)(59.0f - (59.0f * sub.getAir() / sub.getMaxAir()));
		if (frame > 58)
			frame = 58;
		if (frame < 0)
			frame = 0;
		if ((sub.getAir() / sub.getMaxAir() <= 0.25f && ((GMTKJamWorld)getWorld()).getFramesSinceLevelCreation() % 20 > 9) || sub.getAir() <= 0.0f)
			AssetLoader.spriteAirMeter.drawAbsolute(2.0f, getWorld().getGameDimensions().y - AssetLoader.spriteAirMeter.getHeight() - 2.0f, frame, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, Color.RED, renderer);
		else
			AssetLoader.spriteAirMeter.drawAbsolute(2.0f, getWorld().getGameDimensions().y - AssetLoader.spriteAirMeter.getHeight() - 2.0f, frame, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, renderer);
		
		drawDistanceHud(renderer);
		
	}
	
	private void drawDistanceHud(GameRenderer renderer) {
		
		Submarine sub = ((GMTKJamWorld)getWorld()).getSubmarine();
		if (sub.getPos(true).y < ((GMTKJamWorld)getWorld()).getMonster().getPos(true).y) {
			
			float distance = PlaygonMath.distance(sub.getPos(true), ((GMTKJamWorld)getWorld()).getMonster().getPos(true));
			float x1 = getWorld().getGameDimensions().x - 2.0f - AssetLoader.spriteDistanceHud.getWidth() / 2.0f;
			float y1 = getWorld().getGameDimensions().y - 2.0f - AssetLoader.spriteDistanceHud.getHeight() / 2.0f;
			float x2 = x1;
			float y2 = y1 - distance / 10.0f;
			
			AssetLoader.spriteWhite.drawMonocolorLine(getWorld().getCamPos(false).x + x1, getWorld().getCamPos(false).y + y1, getWorld().getCamPos(false).x + x2, getWorld().getCamPos(false).y + y2, 2.0f, 1.0f, renderer);
			AssetLoader.spriteDistanceHud.drawAbsolute(x1 - AssetLoader.spriteDistanceHud.getWidth() / 2.0f, y1 - AssetLoader.spriteDistanceHud.getHeight() / 2.0f, 1, 1.0f, 1.0f, 0.0f, AssetLoader.spriteDistanceHud.getWidth() / 2.0f, AssetLoader.spriteDistanceHud.getHeight() / 2.0f, renderer);
			AssetLoader.spriteDistanceHud.drawAbsolute(x2 - AssetLoader.spriteDistanceHud.getWidth() / 2.0f, y2 - AssetLoader.spriteDistanceHud.getHeight() / 2.0f, 0, 1.0f, 1.0f, 0.0f, AssetLoader.spriteDistanceHud.getWidth() / 2.0f, AssetLoader.spriteDistanceHud.getHeight() / 2.0f, renderer);
			
		} else {
			
			float x = getWorld().getGameDimensions().x - 2.0f - AssetLoader.spriteDistanceHud.getWidth() / 2.0f;
			float y = getWorld().getGameDimensions().y - 2.0f - AssetLoader.spriteDistanceHud.getHeight() / 2.0f;
			AssetLoader.spriteDistanceHud.drawAbsolute(x - AssetLoader.spriteDistanceHud.getWidth() / 2.0f, y - AssetLoader.spriteDistanceHud.getHeight() / 2.0f, 1, 1.0f, 1.0f, 0.0f, AssetLoader.spriteDistanceHud.getWidth() / 2.0f, AssetLoader.spriteDistanceHud.getHeight() / 2.0f, renderer);
			
		}
		
	}
	
}