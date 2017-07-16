package com.mwojnar.GameObjects;

import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.mwojnar.Assets.AssetLoader;
import com.mwojnar.GameWorld.GMTKJamWorld;
import com.playgon.GameEngine.Entity;
import com.playgon.GameEngine.Mask;
import com.playgon.GameEngine.TouchEvent;
import com.playgon.GameWorld.GameRenderer;
import com.playgon.GameWorld.GameWorld;

public class Monster extends Entity {
	
	public Monster(GameWorld myWorld) {
		
		super(myWorld);
		setSprite(AssetLoader.spriteMonster);
		setPivot(getSprite().getWidth() / 2.0f, getSprite().getHeight() / 2.0f);
		setMask(new Mask(this, new Vector2(getSprite().getWidth() / 2.0f, getSprite().getHeight() / 2.0f), 22.0f));
		setForceUpdate(true);
		setDepth(100);
		
	}
	
	@Override
	public void update(float delta, List<TouchEvent> touchEventList, List<Character> charactersTyped, List<Integer> keysFirstDown, List<Integer> keysFirstUp, List<Integer> keysDown) {
		
		super.update(delta, touchEventList, charactersTyped, keysFirstDown, keysFirstUp, keysDown);
		
		float moveSpeed = 3.0f + ((GMTKJamWorld)getWorld()).getFramesSinceLevelCreation() / (60.0f * 30.0f);
		if (moveSpeed > 6.0f)
			moveSpeed = 6.0f;
		if (((GMTKJamWorld)getWorld()).isStarted() && getPos(false).y + getSprite().getHeight() > getWorld().getCamPos(false).y)
			movePos(0.0f, -moveSpeed);
		
	}
	
	@Override
	public void draw(GameRenderer renderer) {
		
		super.draw(renderer);
		
		float x1 = getPos(false).x;
		float y1 = getPos(false).y + getSprite().getHeight();
		float x2 = x1 + getSprite().getWidth();
		float y2 = y1;
		float x3 = x1;
		float y3 = y1 + 800;
		float x4 = x2;
		float y4 = y3;
		
		AssetLoader.spriteBlack.drawMonocolorTriangle(x1, y1, x2, y2, x3, y3, 1.0f, renderer);
		AssetLoader.spriteBlack.drawMonocolorTriangle(x4, y4, x2, y2, x3, y3, 1.0f, renderer);
		
	}
	
	public void teleport() {
		
		if (getPos(false).y > getWorld().getCamPos(false).y + getWorld().getGameDimensions().y)
			setPos(getPos(false).x, getWorld().getCamPos(false).y + getWorld().getGameDimensions().y + 300, false);
		
	}
	
}