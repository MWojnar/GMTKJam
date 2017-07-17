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
import com.mwojnar.GameObjects.Hud;
import com.mwojnar.GameObjects.Mine;
import com.mwojnar.GameObjects.Monster;
import com.mwojnar.GameObjects.Submarine;
import com.mwojnar.GameObjects.Title;
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
import com.playgon.Utils.PlaygonMath;

public class GMTKJamWorld extends GameWorld {
	
	public enum Mode { MENU, GAME, HIGHSCORE }
	
	private Mode mode = Mode.GAME;
	private LoadingThread loadingThread = null;
	private boolean showFPS = true, paused = false, started = false;
	private float nextSpawnPos = 0.0f;
	private long framesSinceLevelCreation = 0;
	private int lastChoice = -1, timerBonus = 0, rawScore = 0;
	private Monster monster = null;
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
		
		startMenu();
		
		addBackgrounds();
		AssetLoader.musicHandler.startMusic(AssetLoader.mainMusic);
		
		//setGMTKJamView();
		/*if (loadMenus) {
			
			startMenu();
			
		}*/
		
	}
	
	public void startGame() {
		
		AssetLoader.debugFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		nextSpawnPos = 0.0f;
		clearWorld();
		setRawScore(0);
		setTimerBonus(0);
		setCamPos(new Vector2(getGameDimensions().x / 2.0f, getGameDimensions().y / 2.0f));
		mode = Mode.GAME;
		framesSinceLevelCreation = 0;
		submarine = new Submarine(this);
		submarine.setPos(200.0f, 320.0f, true);
		createEntity(submarine);
		Bubble bubble = new Bubble(this);
		bubble.setPos(200.0f, 320.0f, true);
		bubble.setGridVelocity(0.0f, 0.0f);
		createEntity(bubble);
		monster = new Monster(this);
		monster.setPos(0.0f, 480.0f, false);
		createEntity(monster);
		createEntity(new Hud(this));
		started = false;
		
	}
	
	private void addBackgrounds() {

		mainBackground = new Background(AssetLoader.background);
		mainBackground.setTilingY(true);
		mainBackground.setParallax(new Vector2(1.0f, 0.05f));
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
		
	}
	
	public void startMenu() {
		

		AssetLoader.debugFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		clearWorld();
		setCamPos(new Vector2(getGameDimensions().x / 2.0f, getGameDimensions().y / 2.0f));
		mode = Mode.MENU;
		Title title = new Title(this);
		createEntity(title);
		
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
				
				Vector2 targetCamPos = new Vector2(getCamPos(true).x, submarine.getPos(false).y - 200.0f - submarine.getGridVelocity().y * 2.0f);
				if (!started)
					targetCamPos = new Vector2(getCamPos(true).x, submarine.getPos(true).y);
				Vector2 nextCamPos = PlaygonMath.getGridVector(15.0f, PlaygonMath.direction(getCamPos(true), targetCamPos)).add(getCamPos(true));
				if (PlaygonMath.distance(getCamPos(true), targetCamPos) <= 15)
					nextCamPos = targetCamPos;
				setCamPos(nextCamPos);
				if (getCamPos(false).y < nextSpawnPos) {
					
					spawnObstacles();
					nextSpawnPos -= getGameDimensions().y + 16 * 6;
					
				}
				
			} else if (mode == Mode.MENU) {
				
				setCamPos(new Vector2(getCamPos(true).x, getCamPos(true).y - 15));
				
			}
			
		} else {
			
			trueLoadLevel(levelToLoad);
			levelToLoad = null;
			
		}
		
	}
	
	private void spawnObstacles() {
		
		int choice = rand.nextInt(9);
		while(choice == lastChoice)
			choice = rand.nextInt(9);
		
		switch (choice) {
		
		case 0:spawnObstacle(new Bubble(this), 1, 5);
		spawnObstacle(new Mine(this), 7, 3);
		spawnObstacle(new EnemyA(this), 5, 8);
		spawnObstacle(new Mine(this), 2, 11);
		spawnObstacle(new CrumblyWall(this), 5, 5);
		spawnObstacle(new CrumblyWall(this), 6, 5);
		spawnObstacle(new CrumblyWall(this), 7, 5);
		spawnObstacle(new CrumblyWall(this), 8, 5);
		spawnObstacle(new CrumblyWall(this), 9, 5);
		spawnObstacle(new CrumblyWall(this), 0, 13);
		spawnObstacle(new CrumblyWall(this), 1, 13);
		spawnObstacle(new CrumblyWall(this), 2, 13);
		spawnObstacle(new CrumblyWall(this), 3, 13);
		spawnObstacle(new CrumblyWall(this), 4, 13);
		break;
		case 1:spawnObstacle(new CrumblyWall(this), 0, 9);
		spawnObstacle(new CrumblyWall(this), 1, 9);
		spawnObstacle(new CrumblyWall(this), 2, 9);
		spawnObstacle(new CrumblyWall(this), 3, 9);
		spawnObstacle(new CrumblyWall(this), 4, 9);
		spawnObstacle(new CrumblyWall(this), 5, 9);
		spawnObstacle(new CrumblyWall(this), 6, 9);
		spawnObstacle(new CrumblyWall(this), 7, 9);
		spawnObstacle(new CrumblyWall(this), 8, 9);
		spawnObstacle(new CrumblyWall(this), 9, 9);
		spawnObstacle(new CrumblyWall(this), 0, 10);
		spawnObstacle(new CrumblyWall(this), 1, 10);
		spawnObstacle(new CrumblyWall(this), 2, 10);
		spawnObstacle(new CrumblyWall(this), 3, 10);
		spawnObstacle(new CrumblyWall(this), 4, 10);
		spawnObstacle(new CrumblyWall(this), 5, 10);
		spawnObstacle(new CrumblyWall(this), 6, 10);
		spawnObstacle(new CrumblyWall(this), 7, 10);
		spawnObstacle(new CrumblyWall(this), 8, 10);
		spawnObstacle(new CrumblyWall(this), 9, 10);
		spawnObstacle(new CrumblyWall(this), 0, 11);
		spawnObstacle(new CrumblyWall(this), 1, 11);
		spawnObstacle(new CrumblyWall(this), 2, 11);
		spawnObstacle(new CrumblyWall(this), 3, 11);
		spawnObstacle(new CrumblyWall(this), 4, 11);
		spawnObstacle(new CrumblyWall(this), 5, 11);
		spawnObstacle(new CrumblyWall(this), 6, 11);
		spawnObstacle(new CrumblyWall(this), 7, 11);
		spawnObstacle(new CrumblyWall(this), 8, 11);
		spawnObstacle(new CrumblyWall(this), 9, 11);
		spawnObstacle(new EnemyA(this), 4, 2);
		spawnObstacle(new Bubble(this), 3, 5);
		spawnObstacle(new Bubble(this), 6, 5);
		spawnObstacle(new Mine(this), 1, 8);
		spawnObstacle(new Mine(this), 8, 8);
		break;
		case 2:spawnObstacle(new Mine(this), 6, 2);
		spawnObstacle(new Mine(this), 5, 5);
		spawnObstacle(new Mine(this), 4, 8);
		spawnObstacle(new Mine(this), 3, 11);
		spawnObstacle(new Bubble(this), 7, 9);
		spawnObstacle(new Bubble(this), 2, 4);
		break;
		case 3:spawnObstacle(new EnemyA(this), 4, 0);
		spawnObstacle(new EnemyA(this), 5, 4);
		spawnObstacle(new EnemyA(this), 4, 8);
		spawnObstacle(new EnemyA(this), 5, 12);
		spawnObstacle(new Mine(this), 1, 1);
		spawnObstacle(new Mine(this), 8, 10);
		spawnObstacle(new Bubble(this), 1, 10);
		spawnObstacle(new Bubble(this), 8, 1);
		break;
		case 4:spawnObstacle(new CrumblyWall(this), 4, 4);
		spawnObstacle(new CrumblyWall(this), 4, 5);
		spawnObstacle(new CrumblyWall(this), 4, 6);
		spawnObstacle(new CrumblyWall(this), 4, 7);
		spawnObstacle(new CrumblyWall(this), 4, 8);
		spawnObstacle(new CrumblyWall(this), 4, 9);
		spawnObstacle(new CrumblyWall(this), 4, 10);
		spawnObstacle(new CrumblyWall(this), 4, 11);
		spawnObstacle(new CrumblyWall(this), 4, 12);
		spawnObstacle(new CrumblyWall(this), 5, 4);
		spawnObstacle(new CrumblyWall(this), 5, 5);
		spawnObstacle(new CrumblyWall(this), 5, 6);
		spawnObstacle(new CrumblyWall(this), 5, 7);
		spawnObstacle(new CrumblyWall(this), 5, 8);
		spawnObstacle(new CrumblyWall(this), 5, 9);
		spawnObstacle(new CrumblyWall(this), 5, 10);
		spawnObstacle(new CrumblyWall(this), 5, 11);
		spawnObstacle(new CrumblyWall(this), 5, 12);
		spawnObstacle(new Mine(this), 1, 1);
		spawnObstacle(new Mine(this), 3, 5);
		spawnObstacle(new Mine(this), 0, 9);
		spawnObstacle(new Mine(this), 2, 13);
		spawnObstacle(new Bubble(this), 9, 2);
		spawnObstacle(new Bubble(this), 6, 7);
		spawnObstacle(new Bubble(this), 8, 12);
		break;
		case 5:spawnObstacle(new CrumblyWall(this), 9, 1);
		spawnObstacle(new CrumblyWall(this), 8, 2);
		spawnObstacle(new CrumblyWall(this), 7, 3);
		spawnObstacle(new CrumblyWall(this), 6, 4);
		spawnObstacle(new CrumblyWall(this), 5, 5);
		spawnObstacle(new CrumblyWall(this), 4, 6);
		spawnObstacle(new CrumblyWall(this), 4, 7);
		spawnObstacle(new CrumblyWall(this), 5, 8);
		spawnObstacle(new CrumblyWall(this), 6, 9);
		spawnObstacle(new CrumblyWall(this), 7, 10);
		spawnObstacle(new CrumblyWall(this), 8, 11);
		spawnObstacle(new CrumblyWall(this), 9, 12);
		spawnObstacle(new EnemyA(this), 2, 2);
		spawnObstacle(new EnemyA(this), 2, 11);
		spawnObstacle(new Bubble(this), 6, 1);
		break;
		case 6:spawnObstacle(new CrumblyWall(this), 0, 4);
		spawnObstacle(new CrumblyWall(this), 0, 5);
		spawnObstacle(new CrumblyWall(this), 1, 6);
		spawnObstacle(new CrumblyWall(this), 1, 7);
		spawnObstacle(new CrumblyWall(this), 2, 8);
		spawnObstacle(new CrumblyWall(this), 2, 9);
		spawnObstacle(new CrumblyWall(this), 3, 10);
		spawnObstacle(new CrumblyWall(this), 3, 11);
		spawnObstacle(new CrumblyWall(this), 9, 4);
		spawnObstacle(new CrumblyWall(this), 9, 5);
		spawnObstacle(new CrumblyWall(this), 8, 6);
		spawnObstacle(new CrumblyWall(this), 8, 7);
		spawnObstacle(new CrumblyWall(this), 7, 8);
		spawnObstacle(new CrumblyWall(this), 7, 9);
		spawnObstacle(new CrumblyWall(this), 6, 10);
		spawnObstacle(new CrumblyWall(this), 6, 11);
		spawnObstacle(new Bubble(this), 1, 2);
		spawnObstacle(new Bubble(this), 8, 2);
		spawnObstacle(new EnemyA(this), 4, 4);
		spawnObstacle(new EnemyA(this), 5, 6);
		spawnObstacle(new EnemyA(this), 4, 8);
		spawnObstacle(new Mine(this), 0, 10);
		spawnObstacle(new Mine(this), 9, 10);
		break;
		case 7:spawnObstacle(new CrumblyWall(this), 2, 5);
		spawnObstacle(new CrumblyWall(this), 3, 5);
		spawnObstacle(new CrumblyWall(this), 6, 5);
		spawnObstacle(new CrumblyWall(this), 7, 5);
		spawnObstacle(new CrumblyWall(this), 0, 10);
		spawnObstacle(new CrumblyWall(this), 1, 10);
		spawnObstacle(new CrumblyWall(this), 4, 10);
		spawnObstacle(new CrumblyWall(this), 5, 10);
		spawnObstacle(new CrumblyWall(this), 8, 10);
		spawnObstacle(new CrumblyWall(this), 9, 10);
		spawnObstacle(new Mine(this), 2, 2);
		spawnObstacle(new Mine(this), 7, 2);
		spawnObstacle(new Mine(this), 0, 5);
		spawnObstacle(new Mine(this), 9, 5);
		spawnObstacle(new Mine(this), 2, 8);
		spawnObstacle(new Mine(this), 7, 8);
		spawnObstacle(new EnemyA(this), 5, 12);
		spawnObstacle(new Bubble(this), 4, 1);
		spawnObstacle(new Bubble(this), 5, 7);
		break;
		case 8:spawnObstacle(new CrumblyWall(this), 5, 2);
		spawnObstacle(new CrumblyWall(this), 6, 2);
		spawnObstacle(new CrumblyWall(this), 7, 2);
		spawnObstacle(new CrumblyWall(this), 7, 3);
		spawnObstacle(new CrumblyWall(this), 7, 4);
		spawnObstacle(new CrumblyWall(this), 7, 5);
		spawnObstacle(new CrumblyWall(this), 4, 8);
		spawnObstacle(new CrumblyWall(this), 3, 8);
		spawnObstacle(new CrumblyWall(this), 2, 8);
		spawnObstacle(new CrumblyWall(this), 2, 9);
		spawnObstacle(new CrumblyWall(this), 2, 10);
		spawnObstacle(new CrumblyWall(this), 2, 11);
		spawnObstacle(new Mine(this), 9, 2);
		spawnObstacle(new Mine(this), 6, 3);
		spawnObstacle(new Mine(this), 0, 8);
		spawnObstacle(new Mine(this), 3, 9);
		spawnObstacle(new EnemyA(this), 2, 2);
		spawnObstacle(new EnemyA(this), 7, 8);
		spawnObstacle(new Bubble(this), 4, 0);
		break;
		
		}
		
		lastChoice = choice;
		
		if (rand.nextBoolean()) {
			
			choice = rand.nextInt(4);
			
			switch (choice) {
			
			case 0:spawnObstacle(new Bubble(this), rand.nextInt(10), rand.nextInt(4) - 5); break;
			case 1:spawnObstacle(new CrumblyWall(this), rand.nextInt(10), rand.nextInt(4) - 5); break;
			case 2:spawnObstacle(new EnemyA(this), rand.nextInt(10), rand.nextInt(4) - 5); break;
			case 3:spawnObstacle(new Mine(this), rand.nextInt(10), rand.nextInt(4) - 5); break;
			
			}
			
		}
		
	}

	private void spawnObstacle(Entity entity, int x, int y) {
		
		entity.setPos(56.0f + x * 32.0f, getCamPos(false).y - 200.0f - getGameDimensions().y + 16.0f + 32.0f * y, true);
		createEntity(entity);
		
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

	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean started) {
		
		if (!this.started) {
			
			framesSinceLevelCreation = 0;
			AssetLoader.sndGrpMonster.playRandom(AssetLoader.soundVolume);
			
		}
		this.started = started;
		
	}

	public Monster getMonster() {
		return monster;
	}

	public void setMonster(Monster monster) {
		this.monster = monster;
	}
	
	public Submarine getSubmarine() {
		return submarine;
	}
	
	public void setSubmarine(Submarine submarine) {
		this.submarine = submarine;
	}
	
	public void haltTimerBonus() {
		
		setTimerBonus((int)framesSinceLevelCreation);
		
	}

	public int getRawScore() {
		return rawScore;
	}

	public void setRawScore(int rawScore) {
		this.rawScore = rawScore;
	}

	public void addRawScore(int addScore) {
		
		rawScore += addScore;
		
	}

	public int getTimerBonus() {
		return timerBonus;
	}

	public void setTimerBonus(int timerBonus) {
		this.timerBonus = timerBonus;
	}
	
}