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
								submarineAttackFireTexture, particleBubbleTexture, mineTexture, crumblyWallTexture,
								backWallTexture, parallaxWall1Texture, parallaxWall2Texture, enemyAParticleTexture,
								mineParticleTexture, crumblyWallParticleTexture, monsterTexture, distanceHudTexture,
								explosionRedSmallTexture, explosionRedLargeTexture, explosionYellowSmallTexture,
								explosionYellowLargeTexture, titleScreenTexture;
	public static TextureRegion whiteTexture, yellowTexture, redTexture, blackTexture;
	public static Sprite spriteSubmarine, spriteEnemyA, spriteBubble, spriteWall, spriteChargeMeter,
						 spriteSubmarineCharging, spriteBubblePop, spriteAirMeter, spriteSubmarineAttackFire,
						 spriteParticleBubble, spriteMine, spriteCrumblyWall, spriteEnemyAParticle, spriteMineParticle,
						 spriteCrumblyWallParticle, spriteMonster, spriteDistanceHud, spriteExplosionRedSmall,
						 spriteExplosionRedLarge, spriteExplosionYellowSmall, spriteExplosionYellowLarge,
						 spriteTitleScreen;
	public static Sprite spriteWhite, spriteYellow, spriteRed, spriteBlack;
	public static BackgroundTemplate background, backWall, parallaxWall1, parallaxWall2;
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
		
		mainMusic = new MusicTemplate(Gdx.files.internal("data/Music/AquaAscent_Theme.mp3"));
		mainMusic.setLooping(true);
		musicHandler.addMusic(mainMusic);
		
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
		particleBubbleTexture = atlas.findRegion("spr_propulsion_bubble");
		mineTexture = atlas.findRegion("spr_mine");
		crumblyWallTexture = atlas.findRegion("spr_breakablewall");
		enemyAParticleTexture = atlas.findRegion("spr_enemyA_particles");
		mineParticleTexture = atlas.findRegion("spr_mine_particles");
		crumblyWallParticleTexture = atlas.findRegion("spr_breakablewall_particles");
		monsterTexture = atlas.findRegion("spr_monster");
		distanceHudTexture = atlas.findRegion("spr_distance_HUD");
		explosionRedSmallTexture = atlas.findRegion("spr_explosion2R");
		explosionRedLargeTexture = atlas.findRegion("spr_explosion1R");
		explosionYellowSmallTexture = atlas.findRegion("spr_explosion2Y");
		explosionYellowLargeTexture = atlas.findRegion("spr_explosion1Y");
		titleScreenTexture = atlas.findRegion("Logo");
		
		backgroundTexture = atlas.findRegion("background");
		backWallTexture = atlas.findRegion("ts_walls");
		parallaxWall1Texture = atlas.findRegion("ts_parallax1");
		parallaxWall2Texture = atlas.findRegion("ts_parallax2");
		
		whiteTexture = atlas.findRegion("white");
		yellowTexture = atlas.findRegion("yellow");
		redTexture = atlas.findRegion("red");
		blackTexture = atlas.findRegion("black");
		
	}
	
	private static void loadSprites() {
		
		spriteSubmarine = new Sprite(submarineTexture, 1);
		spriteSubmarineCharging = new Sprite(submarineChargingTexture, 20);
		spriteEnemyA = new Sprite(enemyATexture, 6);
		spriteBubble = new Sprite(bubbleTexture, 2);
		spriteChargeMeter = new Sprite(chargeMeterTexture, 1);
		spriteBubblePop = new Sprite(bubblePopTexture, 3);
		spriteAirMeter = new Sprite(airMeterTexture, 59);
		spriteSubmarineAttackFire = new Sprite(submarineAttackFireTexture, 3);
		spriteParticleBubble = new Sprite(particleBubbleTexture, 8);
		spriteMine = new Sprite(mineTexture, 12);
		spriteCrumblyWall = new Sprite(crumblyWallTexture, 1);
		spriteEnemyAParticle = new Sprite(enemyAParticleTexture, 4);
		spriteMineParticle = new Sprite(mineParticleTexture, 4);
		spriteCrumblyWallParticle = new Sprite(crumblyWallParticleTexture, 4);
		spriteMonster = new Sprite(monsterTexture, 1);
		spriteDistanceHud = new Sprite(distanceHudTexture, 2);
		spriteExplosionRedSmall = new Sprite(explosionRedSmallTexture, 8);
		spriteExplosionRedLarge = new Sprite(explosionRedLargeTexture, 5);
		spriteExplosionYellowSmall = new Sprite(explosionYellowSmallTexture, 8);
		spriteExplosionYellowLarge = new Sprite(explosionYellowLargeTexture, 5);
		spriteTitleScreen = new Sprite(titleScreenTexture, 1);
		
		spriteWall = new Sprite(wallTexture, 1);
		
		background = new BackgroundTemplate(backgroundTexture, 1);
		backWall = new BackgroundTemplate(backWallTexture, 1);
		parallaxWall1 = new BackgroundTemplate(parallaxWall1Texture, 1);
		parallaxWall2 = new BackgroundTemplate(parallaxWall2Texture, 1);
		
		spriteWhite = new Sprite(whiteTexture, 1);
		spriteYellow = new Sprite(yellowTexture, 1);
		spriteRed = new Sprite(redTexture, 1);
		spriteBlack = new Sprite(blackTexture, 1);
		
	}
	
}