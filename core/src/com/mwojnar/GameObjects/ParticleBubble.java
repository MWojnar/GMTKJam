package com.mwojnar.GameObjects;

import com.mwojnar.Assets.AssetLoader;
import com.playgon.GameWorld.GameWorld;

public class ParticleBubble extends Particle {

	public ParticleBubble(GameWorld myWorld) {
		
		super(myWorld);
		setSprite(AssetLoader.spriteParticleBubble);
		setPivot(getSprite().getWidth() / 2.0f, getSprite().getHeight() / 2.0f);
		setDepth(25);
		
	}
	
	@Override
	public void animationEnd() {
		
		destroy();
		
	}

}
