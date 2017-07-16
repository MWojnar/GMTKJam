package com.mwojnar.GameObjects;

import java.util.List;

import com.mwojnar.Assets.AssetLoader;
import com.mwojnar.GameWorld.GMTKJamWorld;
import com.playgon.GameEngine.Entity;
import com.playgon.GameEngine.TouchEvent;
import com.playgon.GameWorld.GameRenderer;
import com.playgon.GameWorld.GameWorld;

public class Title extends Entity {
	
	public Title(GameWorld myWorld) {
		
		super(myWorld);
		setSprite(AssetLoader.spriteTitleScreen);
		
	}
	
	@Override
	public void update(float delta, List<TouchEvent> touchEventList, List<Character> charactersTyped, List<Integer> keysFirstDown, List<Integer> keysFirstUp, List<Integer> keysDown) {
		
		super.update(delta, touchEventList, charactersTyped, keysFirstDown, keysFirstUp, keysDown);
		
		if (keysFirstUp.contains(com.badlogic.gdx.Input.Keys.SPACE))
			((GMTKJamWorld)getWorld()).startGame();
		
	}
	
	@Override
	public void draw(GameRenderer renderer) {
		
		getSprite().drawAbsolute(getWorld().getGameDimensions().x / 2.0f - getSprite().getWidth() / 2.0f, getWorld().getGameDimensions().y / 2.0f - getSprite().getHeight() / 2.0f, 0, getScale(), getScale(), getRotation(), getSprite().getWidth() / 2.0f, getSprite().getHeight() / 2.0f, renderer);
		
	}
	
}