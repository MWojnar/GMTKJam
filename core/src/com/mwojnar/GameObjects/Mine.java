package com.mwojnar.GameObjects;

import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.mwojnar.Assets.AssetLoader;
import com.mwojnar.GameWorld.GMTKJamWorld;
import com.playgon.GameEngine.Entity;
import com.playgon.GameEngine.Mask;
import com.playgon.GameEngine.TouchEvent;
import com.playgon.GameWorld.GameWorld;

public class Mine extends Enemy {

	private Vector2 startPos = null;
	private float amplitude = 3.0f;
	
	public Mine(GameWorld myWorld) {
		
		super(myWorld);
		setSprite(AssetLoader.spriteMine);
		setPivot(getSprite().getWidth() / 2.0f, getSprite().getHeight() / 2.0f);
		setMask(new Mask(this, new Vector2(getSprite().getWidth() / 2.0f, getSprite().getHeight() / 2.0f), 44.0f));
		setDepth(40);
		
	}
	
	@Override
	public void update(float delta, List<TouchEvent> touchEventList, List<Character> charactersTyped, List<Integer> keysFirstDown, List<Integer> keysFirstUp, List<Integer> keysDown) {
		
		super.update(delta, touchEventList, charactersTyped, keysFirstDown, keysFirstUp, keysDown);
		
		if (startPos == null)
			startPos = getPos(true).cpy();
		
		setPos(getPos(true).x, startPos.y + (float)Math.sin(((GMTKJamWorld)getWorld()).getFramesSinceLevelCreation() / 20.0f) * amplitude, true);

		if (getPos(false).y > getWorld().getCamPos(false).y + getWorld().getGameDimensions().y)
			destroy();
		
	}
	
}