package com.mwojnar.GameObjects;

import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.mwojnar.Assets.AssetLoader;
import com.mwojnar.GameWorld.GMTKJamWorld;
import com.playgon.GameEngine.Entity;
import com.playgon.GameEngine.TouchEvent;
import com.playgon.GameWorld.GameRenderer;
import com.playgon.GameWorld.GameWorld;

public class EnemyA extends Entity {
	
	boolean isLeft = false;
	Vector2 startPos = null;
	float moveSpeed = 1.5f, edgeBuffer = 150.0f, amplitude = 20.0f;
	
	public EnemyA(GameWorld myWorld) {
		
		super(myWorld);
		setSprite(AssetLoader.spriteEnemyA);
		setPivot(getSprite().getWidth() / 2.0f, getSprite().getHeight() / 2.0f);
		//setMask();
		setDepth(40);
		isLeft = ((GMTKJamWorld)getWorld()).getRandom().nextBoolean();
		
	}
	
	@Override
	public void update(float delta, List<TouchEvent> touchEventList, List<Character> charactersTyped, List<Integer> keysFirstDown, List<Integer> keysFirstUp, List<Integer> keysDown) {
		
		super.update(delta, touchEventList, charactersTyped, keysFirstDown, keysFirstUp, keysDown);
		
		if (startPos == null) {
			
			startPos = getPos(true).cpy();
			startPos.x = getWorld().getGameDimensions().x / 2.0f;
			
		}
		
		if (isLeft)
			movePos(-moveSpeed, 0.0f);
		else
			movePos(moveSpeed, 0.0f);
		
		if (getPos(true).x - startPos.x >= edgeBuffer)
			isLeft = true;
		if (getPos(true).x - startPos.x <= -edgeBuffer)
			isLeft = false;
		
		setPos(getPos(true).x, startPos.y + (float)Math.sin(((GMTKJamWorld)getWorld()).getFramesSinceLevelCreation() / 20.0f) * amplitude, true);
		
	}
	
	@Override
	public void draw(GameRenderer renderer) {
		
		float rotation = (float)Math.sin(((GMTKJamWorld)getWorld()).getFramesSinceLevelCreation() / 20.0f + 0.75f) * 90.0f / (float)Math.PI;
		getSprite().draw(getPos(false).x, getPos(false).y, getFrame(), isLeft ? 1.0f : -1.0f, 1.0f, isLeft ? -rotation : rotation, getSprite().getWidth() / 2.0f, getSprite().getHeight() / 2.0f, renderer);
		
	}
	
}