package com.mwojnar.GameObjects;

import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.mwojnar.Assets.AssetLoader;
import com.mwojnar.GameWorld.GMTKJamWorld;
import com.playgon.GameEngine.Entity;
import com.playgon.GameEngine.Mask;
import com.playgon.GameEngine.TouchEvent;
import com.playgon.GameWorld.GameRenderer;
import com.playgon.GameWorld.GameWorld;
import com.playgon.Utils.PlaygonMath;

public class EnemyA extends Enemy {
	
	private boolean isLeft = false;
	private Vector2 startPos = null;
	private float moveSpeed = 1.5f, edgeBuffer = 150.0f, amplitude = 20.0f;
	
	public EnemyA(GameWorld myWorld) {
		
		super(myWorld);
		setSprite(AssetLoader.spriteEnemyA);
		setPivot(getSprite().getWidth() / 2.0f, getSprite().getHeight() / 2.0f);
		setMask(new Mask(this, new Vector2(getSprite().getWidth() / 2.0f, getSprite().getHeight() / 2.0f), 22.0f));
		setDepth(40);
		setAnimationSpeed(10.0f);
		
	}
	
	@Override
	public void update(float delta, List<TouchEvent> touchEventList, List<Character> charactersTyped, List<Integer> keysFirstDown, List<Integer> keysFirstUp, List<Integer> keysDown) {
		
		super.update(delta, touchEventList, charactersTyped, keysFirstDown, keysFirstUp, keysDown);
		
		if (startPos == null) {
			
			startPos = getPos(true).cpy();
			startPos.x = getWorld().getGameDimensions().x / 2.0f;
			if (getPos(true).x < getWorld().getGameDimensions().x / 2.0f)
				isLeft = true;
			
		}
		
		if (getPos(false).y + getSprite().getHeight() > getWorld().getCamPos(false).y) {
					
			if (isLeft)
				movePos(-moveSpeed, 0.0f);
			else
				movePos(moveSpeed, 0.0f);
			
			if (getPos(true).x - startPos.x >= edgeBuffer)
				isLeft = true;
			if (getPos(true).x - startPos.x <= -edgeBuffer)
				isLeft = false;
			
			setPos(getPos(true).x, startPos.y + (float)Math.sin(((GMTKJamWorld)getWorld()).getFramesSinceLevelCreation() / 20.0f) * amplitude, true);
			
			if (getPos(false).y > getWorld().getCamPos(false).y + getWorld().getGameDimensions().y)
				destroy();
			
		}
		
		handleCollisions();
		
	}
	
	private void handleCollisions() {
		
		for (Entity entity : getWorld().getEntityList()) {
			
			if (entity instanceof Bubble) {
				
				if (collisionsWith(entity).size() > 0)
					((Bubble)entity).pop();
				
			} else if (entity instanceof CrumblyWall) {
				
				if (collisionsWith(entity).size() > 0) {
					
					if (entity.getPos(true).x < getPos(true).x)
						isLeft = false;
					else
						isLeft = true;
					
				}
				
			}
			
		}
		
	}
	
	@Override
	public void draw(GameRenderer renderer) {
		
		float rotation = (float)Math.sin(((GMTKJamWorld)getWorld()).getFramesSinceLevelCreation() / 20.0f + 1.0f) * 90.0f / (float)Math.PI;
		getSprite().draw(getPos(false).x, getPos(false).y, getFrame(), isLeft ? 1.0f : -1.0f, 1.0f, isLeft ? -rotation : rotation, getSprite().getWidth() / 2.0f, getSprite().getHeight() / 2.0f, renderer);
		
	}
	
	@Override
	public void burst() {
		
		destroy();
		AssetLoader.sndKillFish.play(AssetLoader.soundVolume);
		
		for (int i = 0; i < 5; i++) {
			
			Vector2 pos = PlaygonMath.getGridVector(getSprite().getWidth() / 12.0f, i * 2.0f * (float)Math.PI / 5.0f + ((GMTKJamWorld)getWorld()).getRandom().nextFloat() * 3.0f - 1.5f).add(getPos(true));
			Vector2 gridVelocity = PlaygonMath.getGridVector(((GMTKJamWorld)getWorld()).getRandom().nextFloat() * 0.5f + 0.5f, i * 2.0f * (float)Math.PI / 5.0f + ((GMTKJamWorld)getWorld()).getRandom().nextFloat() * 0.2f - 0.1f).add(0.0f, -2.5f);
			generateParticle(pos, gridVelocity);
			
		}
		for (int i = 0; i < 10; i++) {
			
			Vector2 pos = PlaygonMath.getGridVector(getSprite().getWidth() / 3.0f, i * 2.0f * (float)Math.PI / 5.0f + ((GMTKJamWorld)getWorld()).getRandom().nextFloat() * 3.0f - 1.5f).add(getPos(true));
			Vector2 gridVelocity = PlaygonMath.getGridVector(((GMTKJamWorld)getWorld()).getRandom().nextFloat() * 0.5f + 0.5f, i * 2.0f * (float)Math.PI / 5.0f + ((GMTKJamWorld)getWorld()).getRandom().nextFloat() * 0.2f - 0.1f).add(0.0f, -2.5f);
			generateParticle(pos, gridVelocity);
			
		}
		
	}
	
	private void generateParticle(Vector2 pos, Vector2 gridVelocity) {
		
		Particle particle = new Particle(getWorld());
		particle.setSprite(AssetLoader.spriteEnemyAParticle);
		particle.setPos(pos, true);
		particle.setForceUpdate(true);
		particle.setShrinking(true);
		particle.setTimed(true);
		particle.setMaxTime(120);
		particle.setRotates(true);
		particle.setRotationSpeed(((GMTKJamWorld)getWorld()).getRandom().nextFloat() * 0.02f - 0.01f);
		particle.setAnimationSpeed(0.0f);
		particle.setFrame(((GMTKJamWorld)getWorld()).getRandom().nextInt(4));
		particle.setGridVelocity(gridVelocity);
		getWorld().createEntity(particle);
		
	}
	
}