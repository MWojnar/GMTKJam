package com.mwojnar.GameWorld;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mwojnar.Game.GMTKJamGame;
import com.mwojnar.GameObjects.Bubble;
import com.mwojnar.GameObjects.CrumblyWall;
import com.mwojnar.GameObjects.EnemyA;
import com.mwojnar.GameObjects.Mine;
import com.mwojnar.GameObjects.Submarine;
import com.mwojnar.GameWorld.GMTKJamWorld.Mode;
import com.mwojnar.Assets.AssetLoader;
import com.playgon.GameEngine.Background;
import com.playgon.GameEngine.BackgroundTemplate;
import com.playgon.GameEngine.Entity;
import com.playgon.GameEngine.MusicTemplate;
import com.playgon.GameEngine.Sprite;
import com.playgon.GameEngine.TouchEvent;
import com.playgon.GameWorld.GameWorld;
import com.playgon.Utils.LoadingThread;
import com.playgon.Utils.Pair;

public class GMTKJamWorld extends GameWorld {
	
	public enum Mode { MENU, GAME, HIGHSCORE }
	
	private Mode mode = Mode.GAME;
	private LoadingThread loadingThread = null;
	private boolean showFPS = true, paused = false;
	private float nextSpawnPos = 0.0f;
	private long framesSinceLevelCreation = 0;
	private FileHandle levelToLoad = null;
	private Random rand = new Random();
	private Background mainBackground;
	private Submarine submarine;
	
	public GMTKJamWorld() {
		
		super();
		setUsingRegions(false);
		
	}
	
	@Override
	public void initialize() {
		
		setFPS(60);
		initializeLevelEditorLists();
		getRenderer().setUsingIntegerViewPosition(false);
		getRenderer().setClearColor(Color.BLACK);
		Preferences preferences = Gdx.app.getPreferences("GMTKJam Prefs");
		AssetLoader.musicHandler.startMusic(AssetLoader.mainMusic);
		
		boolean loadMenus = true;
		if (GMTKJamGame.args != null) {
			
			for (int i = 0; i < GMTKJamGame.args.length; i++) {
				
				if (GMTKJamGame.args[i].equals("-loadLevel")) {
					
					if (i + 1 < GMTKJamGame.args.length) {
						
						clearWorld();
						loadLevel(Gdx.files.absolute(GMTKJamGame.args[i + 1]));
						loadMenus = false;
						break;
						
					}
					
				}
				
			}
			
		}
		submarine = new Submarine(this);
		submarine.setPos(200.0f, 320.0f, true);
		createEntity(submarine);
		mainBackground = new Background(AssetLoader.background);
		mainBackground.setTilingY(true);
		mainBackground.setParallax(new Vector2(1.0f, 0.0f));
		addBackground(mainBackground);
		Background parallaxWall2 = new Background(AssetLoader.parallaxWall2);
		parallaxWall2.setTilingY(true);
		parallaxWall2.setParallax(new Vector2(1.0f, 0.35f));
		addBackground(parallaxWall2);
		Background parallaxWall1 = new Background(AssetLoader.parallaxWall1);
		parallaxWall1.setTilingY(true);
		parallaxWall1.setParallax(new Vector2(1.0f, 0.6f));
		addBackground(parallaxWall1);
		Background backWall = new Background(AssetLoader.backWall);
		backWall.setTilingY(true);
		addBackground(backWall);
		//setViewEntity(submarine);
		//setGMTKJamView();
		/*if (loadMenus) {
			
			startMenu();
			
		}*/
		
	}
	
	public void startMenu() {
		
		clearWorld();
		mode = Mode.MENU;
		
	}

	public static void initializeLevelEditorLists() {
		
		levelEditorLists.clear();
		levelEditorListClasses.clear();
		addToLevelEditorLists(new ArrayList<Pair<String, ?>>(AssetLoader.spriteList), Sprite.class);
		addToLevelEditorLists(new ArrayList<Pair<String, ?>>(AssetLoader.backgroundList), BackgroundTemplate.class);
		addToLevelEditorLists(new ArrayList<Pair<String, ?>>(AssetLoader.musicList), MusicTemplate.class);
		
	}
	
	@Override
	public void update(float delta) {
		
		if (AssetLoader.loaded) {
			
			if (!paused) {
				
				framesSinceLevelCreation++;
				
			}
			super.update(delta);
			
		}
		
	}
	
	
	@Override
	protected void updateMain(float delta) {
		
		if (getKeysFirstDown().contains(com.badlogic.gdx.Input.Keys.F4)) {
			
			if (Gdx.graphics.isFullscreen()) {
				
				Gdx.graphics.setWindowedMode(1280, 720);
				
			} else {
				
				Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
				
			}
			
		}
		if (paused) {
			
			float scale =  getGameDimensions().y / 240.0f;
			for (TouchEvent touchEvent : getCurrentTouchEventList()) {
				
				Rectangle rect2 = new Rectangle(getGameDimensions().x - 10.0f - 9.0f * 15.0f, getGameDimensions().y - 30.0f, 9.0f * 15.0f, 20.0f);
				if (touchEvent.type == TouchEvent.Type.TOUCH_UP) {
					
					setPaused(false);
					//AssetLoader.soundUIUnpausing.play(AssetLoader.soundVolume);
					return;
					
				}
				
			}
			
		} else if (levelToLoad == null) {
			
			super.updateMain(delta);
			if (mode == Mode.GAME) {
				
				setCamPos(new Vector2(getCamPos(true).x, submarine.getPos(false).y - 200.0f - submarine.getGridVelocity().y * 2.0f));
				if (getCamPos(false).y < nextSpawnPos) {
					
					spawnObstacles();
					nextSpawnPos -= getGameDimensions().y;
					
				}
				
			}
			
		} else {
			
			trueLoadLevel(levelToLoad);
			levelToLoad = null;
			
		}
		
	}
	
	private void spawnObstacles() {
		
		Bubble bubble = new Bubble(this);
		bubble.setPos(rand.nextFloat() * (getGameDimensions().x - 300.0f) + 150.0f, getCamPos(false).y - 200 - getGameDimensions().y * rand.nextFloat(), true);
		createEntity(bubble);
		
		EnemyA enemy = new EnemyA(this);
		enemy.setPos(rand.nextFloat() * (getGameDimensions().x - 300.0f) + 150.0f, getCamPos(false).y - 200 - getGameDimensions().y * rand.nextFloat(), true);
		createEntity(enemy);
		
		Mine mine = new Mine(this);
		mine.setPos(rand.nextFloat() * (getGameDimensions().x - 200.0f) + 100.0f, getCamPos(false).y - 200 - getGameDimensions().y * rand.nextFloat(), true);
		createEntity(mine);
		
		CrumblyWall wall = new CrumblyWall(this);
		wall.setPos(rand.nextFloat() * (getGameDimensions().x - 200.0f) + 100.0f, getCamPos(false).y - 200 - getGameDimensions().y * rand.nextFloat(), true);
		createEntity(wall);
		
	}

	public boolean isPaused() {
		
		return paused;
		
	}
	
	public void setPaused(boolean paused) {
		
		this.paused = paused;
		if (paused) {
			
			AssetLoader.musicHandler.setVolume(AssetLoader.musicVolume * 0.25f);
			
		} else {
			
			AssetLoader.musicHandler.setVolume(AssetLoader.musicVolume);
			
		}
		
	}
	
	public void loadLevel(FileHandle file) {
		
		levelToLoad = file;
		
	}
	
	public void trueLoadLevel(FileHandle file) {
		
		AssetLoader.musicHandler.unload();
		AssetLoader.musicHandler.stopMusic();
		
//		if (loadingThread == null) {
//			
//			loadingThread = new LoadingThread(this, file, levelEditorListClasses, levelEditorLists);
//			loadingThread.start();
//			
//		}
		
		load(file, levelEditorListClasses, levelEditorLists);
		
		addEntities();
		
		/*BackgroundShape backgroundShape = new BackgroundShape(this, getCamPos(false));
		backgroundShape.setPos(dribbleEntity.getPos(true), false);
		backgroundShape.width = 200.0f;
		backgroundShape.height = 100.0f;
		createEntity(backgroundShape);*/
		
		framesSinceLevelCreation = 0;
		
	}
	
	@Override
	protected void loadFromMultiThread() {
		
		super.loadFromMultiThread();
		
		addEntities();
		
		/*BackgroundShape backgroundShape = new BackgroundShape(this, getCamPos(false));
		backgroundShape.setPos(dribbleEntity.getPos(true), false);
		backgroundShape.width = 200.0f;
		backgroundShape.height = 100.0f;
		createEntity(backgroundShape);*/
		
		framesSinceLevelCreation = 0;
		loadingThread = null;
		
	}
	
	public void clearWorld() {
		
		for (Entity entity : getEntityList()) {
			
			entity.destroy();
			
		}
		resetEntities();
		releaseEntities();
		getEntityList().clear();
		getActiveEntityList().clear();
		
	}

	public boolean isLoading() {
		
		return (loadingThread != null);
		
	}

	public boolean isShowFPS() {
		
		return showFPS;
		
	}

	public void setShowFPS(boolean showFPS) {
		
		this.showFPS = showFPS;
		
	}
	
	@Override
	public void restartLevel() {
		
		super.restartLevel();
		
	}
	
	public long getFramesSinceLevelCreation() {
		
		return framesSinceLevelCreation;
		
	}
	
	public void endLevel() {
		
		clear();
		
	}
	
	public void setGMTKJamView() {
		
		setViewOffset(new Vector2(0.0f, 0.0f));
		setViewSpeed(0.0f);
		setViewYield(new Vector2(0.0f, 0.0f));
		toggleViewPredictPath(true);
		toggleViewAccelerateToPoint(true);
		
	}

	public FileHandle getLevelToLoad() {
		
		return levelToLoad;
		
	}
	
	public Random getRandom() {
		
		return rand;
		
	}
	
}