package com.mwojnar.GameObjects;

import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.mwojnar.Assets.AssetLoader;
import com.playgon.GameEngine.Entity;
import com.playgon.GameEngine.Mask;
import com.playgon.GameEngine.TouchEvent;
import com.playgon.GameWorld.GameWorld;

public class CrumblyWall extends Entity {
	
	public CrumblyWall(GameWorld myWorld) {
		
		super(myWorld);
		setSprite(AssetLoader.spriteCrumblyWall);
		setPivot(getSprite().getWidth() / 2.0f, getSprite().getHeight() / 2.0f);
		setMask(new Mask(this, getSprite().getWidth(), getSprite().getHeight()));
		setDepth(40);
		
	}
	
	@Override
	public void update(float delta, List<TouchEvent> touchEventList, List<Character> charactersTyped, List<Integer> keysFirstDown, List<Integer> keysFirstUp, List<Integer> keysDown) {
		
		super.update(delta, touchEventList, charactersTyped, keysFirstDown, keysFirstUp, keysDown);
		
		if (getPos(false).y > getWorld().getCamPos(false).y + getWorld().getGameDimensions().y)
			destroy();
		
	}
	
}