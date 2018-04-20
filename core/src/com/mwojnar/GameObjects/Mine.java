package com.mwojnar.GameObjects;

import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.mwojnar.Assets.AssetLoader;
import com.mwojnar.GameWorld.GMTKJamWorld;
import com.playgon.GameEngine.Entity;
import com.playgon.GameEngine.Mask;
import com.playgon.GameEngine.TouchEvent;
import com.playgon.GameWorld.GameWorld;
import com.playgon.Utils.PlaygonMath;

public class Mine extends Enemy {

	private Vector2 startPos = null;
	private float amplitude = 3.0f;
	
	public Mine(GameWorld myWorld) {
		
		super(myWorld);
		setSprite(AssetLoader.spriteMine);
		setPivot(getSprite().getWidth() / 2.0f, getSprite().getHeight() / 2.0f);
		setMask(new Mask(this, new Vector2(getSprite().getWidth() / 2.0f, getSprite().getHeight() / 2.0f), 22.0f));
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
	
	@Override
	public void burst() {
		
		destroy();
		AssetLoader.sndKillMine.play(AssetLoader.soundVolume);
		
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
		for (int i = 0; i < 5; i++) {
			
			ParticleBubble explosion = new ParticleBubble(getWorld());
			explosion.setPos(getPos(false).x + ((GMTKJamWorld)getWorld()).getRandom().nextFloat() * getSprite().getWidth(), getPos(false).y + ((GMTKJamWorld)getWorld()).getRandom().nextFloat() * getSprite().getHeight(), true);
			if (((GMTKJamWorld)getWorld()).getRandom().nextBoolean())
				explosion.setSprite(AssetLoader.spriteExplosionRedSmall);
			else
				explosion.setSprite(AssetLoader.spriteExplosionRedLarge);
			explosion.setPivot(explosion.getSprite().getWidth() / 2.0f, explosion.getSprite().getHeight() / 2.0f);
			explosion.setRotation(((GMTKJamWorld)getWorld()).getRandom().nextFloat() * 360.0f);
			explosion.setAnimationSpeed(15.0f);
			getWorld().createEntity(explosion);
			
		}
		
	}
	
	@Override
	public void bump() {
		
		burst();
		
	}
	
	private void generateParticle(Vector2 pos, Vector2 gridVelocity) {
		
		Particle particle = new Particle(getWorld());
		particle.setSprite(AssetLoader.spriteMineParticle);
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