package com.mwojnar.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.mwojnar.Assets.AssetLoader;
import com.mwojnar.GameWorld.GMTKJamRenderer;
import com.mwojnar.GameWorld.GMTKJamWorld;
import com.playgon.GameWorld.GameRenderer;
import com.playgon.GameWorld.GameWorld;
import com.playgon.Helpers.InputHandler;

public class GameScreen implements Screen {

	private GameWorld world;
	private GameRenderer renderer;
	private float runTime;
	
	public GameScreen() {
		
		AssetLoader.load();
		float screenWidth = Gdx.graphics.getWidth();
		float screenHeight = Gdx.graphics.getHeight();
		float gameHeight = 640;
		float gameWidth = 400;
		Gdx.input.setCatchBackKey(true);
		Gdx.input.setCatchMenuKey(false);
		
		world = new GMTKJamWorld();
		Gdx.input.setInputProcessor(new InputHandler(world, screenWidth / gameWidth, screenHeight / gameHeight));
		renderer = new GMTKJamRenderer(world, (int) gameWidth, (int) gameHeight);
		world.setRenderer(renderer);
		
	}
	
	@Override
	public void render(float delta) {
		runTime += delta;
		world.update(delta);
		renderer.render(delta, runTime);
	}
	
	@Override
	public void resize(int width, int height) {}

	@Override
	public void show() {}

	@Override
	public void hide() {}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void dispose() {
		
		AssetLoader.dispose();
		
	}

}