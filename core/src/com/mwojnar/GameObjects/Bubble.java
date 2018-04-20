package com.mwojnar.GameObjects;

import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.mwojnar.Assets.AssetLoader;
import com.playgon.GameEngine.Entity;
import com.playgon.GameEngine.Mask;
import com.playgon.GameEngine.TouchEvent;
import com.playgon.GameWorld.GameWorld;

public class Bubble extends Entity {
	
	public Bubble(GameWorld myWorld) {
		
		super(myWorld);
		setSprite(AssetLoader.spriteBubble);
		setMask(new Mask(this, new Vector2(getSprite().getWidth() / 2.0f, getSprite().getHeight() / 2.0f), getSprite().getWidth() / 2.0f));
		setPivot(getSprite().getWidth() / 2.0f, getSprite().getHeight() / 2.0f);
		setDepth(60);
		setAnimationSpeed(15.0f);
		setGridVelocity(0.0f, -0.11f);
		
	}
	
	@Override
	public void update(float delta, List<TouchEvent> touchEventList, List<Character> charactersTyped, List<Integer> keysFirstDown, List<Integer> keysFirstUp, List<Integer> keysDown) {
		
		super.update(delta, touchEventList, charactersTyped, keysFirstDown, keysFirstUp, keysDown);
		
		if (getPos(false).y + getSprite().getHeight() > getWorld().getCamPos(false).y)
			moveByVelocity();
		if (getPos(false).y > getWorld().getCamPos(false).y + getWorld().getGameDimensions().y)
			destroy();
		for (Entity entity : getWorld().getEntityList())
			if (!(entity instanceof Bubble || entity instanceof Submarine))
				if (collisionsWith(entity).size() > 0)
					pop();
		
	}
	
	public void pop() {
		
		setSprite(AssetLoader.spriteBubblePop);
		setAnimationSpeed(15.0f);
		
	}
	
	@Override
	public void animationEnd() {
		
		if (getSprite() == AssetLoader.spriteBubblePop)
			destroy();
		
	}
	
}