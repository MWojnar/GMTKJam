package com.mwojnar.Assets;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFileChooser;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.mwojnar.Game.GMTKJamGame;
import com.playgon.GameEngine.BackgroundTemplate;
import com.playgon.GameEngine.Entity;
import com.playgon.GameEngine.MaskSurface;
import com.playgon.GameEngine.MusicTemplate;
import com.playgon.GameEngine.Sound;
import com.playgon.GameEngine.SoundGroup;
import com.playgon.GameEngine.Sprite;
import com.playgon.Helpers.MusicHandler;
import com.playgon.Utils.Pair;

public class AssetLoader {

	public static boolean loaded = false;
	public static AssetManager assetManager;
	private static TextureAtlas atlas;
	public static Texture wojworksTexture;
	public static TextureRegion submarineTexture, enemyATexture, bubbleTexture, wallTexture, backgroundTexture,
								chargeMeterTexture, submarineChargingTexture, bubblePopTexture, airMeterTexture,
								submarineAttackFireTexture;
	public static TextureRegion whiteTexture, yellowTexture, redTexture;
	public static Sprite spriteSubmarine, spriteEnemyA, spriteBubble, spriteWall, spriteChargeMeter,
						 spriteSubmarineCharging, spriteBubblePop, spriteAirMeter, spriteSubmarineAttackFire;
	public static Sprite spriteWhite, spriteYellow, spriteRed;
	public static BackgroundTemplate background;
	public static MusicTemplate mainMusic;
	public static MusicHandler musicHandler;
	public static List<Class<? extends Entity>> classList = new ArrayList<Class<? extends Entity>>();
	public static List<Pair<String, Sprite>> spriteList = new ArrayList<Pair<String, Sprite>>();
	public static List<Pair<String, BackgroundTemplate>> backgroundList = new ArrayList<Pair<String, BackgroundTemplate>>();
	public static List<Pair<String, MusicTemplate>> musicList = new ArrayList<Pair<String, MusicTemplate>>();
	public static BitmapFont debugFont = new BitmapFont(true), titleFont = new BitmapFont(true);
	public static Color greenTextColor = new Color(52.0f / 255.0f, 217.0f / 255.0f, 34.0f / 255.0f, 1.0f),
			blueTextColor = new Color(77.0f / 255.0f, 207.0f / 255.0f, 228.0f / 255.0f, 1.0f);
	public static float musicVolume = 0.5f, soundVolume = 1.0f;
	
	public static void load() {
		
		dispose();
		
		loaded = false;
		
		assetManager = new AssetManager();
		
		musicHandler = new MusicHandler();
		
		wojworksTexture = new Texture(Gdx.files.internal("data/Images/spr_wojworks.png"));
		
		assetManager.load("data/Images/GMTKJamTextures.pack", TextureAtlas.class);
		
		Preferences preferences = Gdx.app.getPreferences("GMTKJam Prefs");
		musicVolume = preferences.getFloat("musicVolume", 1.0f);
		soundVolume = preferences.getFloat("soundVolume", 0.5f);
		
		loadSoundsManager();
		
		debugFont.setUseIntegerPositions(false);
		titleFont.setUseIntegerPositions(false);
		
	}
	
	public static void postload() {
		
		atlas = assetManager.get("data/Images/GMTKJamTextures.pack", TextureAtlas.class);
		
		loadMusic();
		loadSounds();
		loadTextures();
		loadSprites();
		loadMisc();
		
	}
	
	private static void loadMisc() {
		
		
		
	}
	
	private static void loadSoundsManager() {
		
		
		
	}
	
	private static void loadSounds() {
		
		
		
	}
	
	private static void loadMusic() {
		
		//mainMusic = new MusicTemplate(Gdx.files.internal("data/Music/INSERT_MAIN_MUSIC_HERE"));
		//mainMusic.setLooping(true);
		//musicHandler.addMusic(mainMusic);
		
	}
	
	public static void setMusicVolume(float musicVolume) {
		
		AssetLoader.musicVolume = musicVolume;
		
	}
	
	public static void dispose() {}
	
	private static void loadTextures() {
		
		submarineTexture = atlas.findRegion("spr_sub");
		submarineChargingTexture = atlas.findRegion("spr_sub_charging");
		enemyATexture = atlas.findRegion("spr_enemyA");
		bubbleTexture = atlas.findRegion("spr_bubble");
		chargeMeterTexture = atlas.findRegion("spr_charge_meter");
		wallTexture = atlas.findRegion("ts_walls");
		bubblePopTexture = atlas.findRegion("spr_bubble_pop");
		airMeterTexture = atlas.findRegion("spr_air_meter");
		submarineAttackFireTexture = atlas.findRegion("spr_sub_attack_fire");
		
		backgroundTexture = atlas.findRegion("background");
		
		whiteTexture = atlas.findRegion("white");
		yellowTexture = atlas.findRegion("yellow");
		redTexture = atlas.findRegion("red");
		
	}
	
	private static void loadSprites() {
		
		spriteSubmarine = new Sprite(submarineTexture, 1);
		spriteSubmarineCharging = new Sprite(submarineChargingTexture, 20);
		spriteEnemyA = new Sprite(enemyATexture, 1);
		spriteBubble = new Sprite(bubbleTexture, 2);
		spriteChargeMeter = new Sprite(chargeMeterTexture, 1);
		spriteBubblePop = new Sprite(bubblePopTexture, 3);
		spriteAirMeter = new Sprite(airMeterTexture, 59);
		spriteSubmarineAttackFire = new Sprite(submarineAttackFireTexture, 3);
		
		spriteWall = new Sprite(wallTexture, 1);
		
		background = new BackgroundTemplate(backgroundTexture, 1);
		
		spriteWhite = new Sprite(whiteTexture, 1);
		spriteYellow = new Sprite(yellowTexture, 1);
		spriteRed = new Sprite(redTexture, 1);
		
	}
	
}