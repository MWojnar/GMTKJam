package com.mwojnar.GameObjects;

import com.badlogic.gdx.math.Vector2;
import com.mwojnar.Assets.AssetLoader;
import com.playgon.GameEngine.Entity;
import com.playgon.GameEngine.Mask;
import com.playgon.GameWorld.GameWorld;

public class Bubble extends Entity {
	
	public Bubble(GameWorld myWorld) {
		
		super(myWorld);
		setSprite(AssetLoader.spriteBubble);
		setMask(new Mask(this, new Vector2(getSprite().getWidth() / 2.0f, getSprite().getHeight() / 2.0f), getSprite().getWidth() / 2.0f));
		setPivot(getSprite().getWidth() / 2.0f, getSprite().getHeight() / 2.0f);
		setDepth(60);
		setAnimationSpeed(15.0f);
		
	}
	
}