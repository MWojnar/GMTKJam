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
								explosionYellowLargeTexture, titleScreenTexture, spacebarTexture,
								monsterHeadTexture;
	public static TextureRegion whiteTexture, yellowTexture, redTexture, blackTexture;
	public static Sprite spriteSubmarine, spriteEnemyA, spriteBubble, spriteWall, spriteChargeMeter,
						 spriteSubmarineCharging, spriteBubblePop, spriteAirMeter, spriteSubmarineAttackFire,
						 spriteParticleBubble, spriteMine, spriteCrumblyWall, spriteEnemyAParticle, spriteMineParticle,
						 spriteCrumblyWallParticle, spriteMonster, spriteDistanceHud, spriteExplosionRedSmall,
						 spriteExplosionRedLarge, spriteExplosionYellowSmall, spriteExplosionYellowLarge,
						 spriteTitleScreen, spriteSpacebar, spriteMonsterHead;
	public static Sprite spriteWhite, spriteYellow, spriteRed, spriteBlack;
	public static BackgroundTemplate background, backWall, parallaxWall1, parallaxWall2;
	public static Sound sndAirGain, sndAirLow, sndBubblePop, sndBurst, sndCharging, sndDeath, sndKillFish, sndKillMine,
						sndMonster1, sndMonster2, sndMonster3, sndSuperBurst, sndBreakBlock, sndBump, sndHurt;
	public static SoundGroup sndGrpMonster;
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
		
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("data/Fonts/pixel font-7.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 12;
		parameter.flip = true;
		parameter.color = Color.WHITE;
		debugFont = generator.generateFont(parameter);
		generator.dispose();
		
		generator = new FreeTypeFontGenerator(Gdx.files.internal("data/Fonts/pixel font-7.ttf"));
		parameter = new FreeTypeFontParameter();
		parameter.size = 64;
		parameter.flip = true;
		parameter.color = Color.WHITE;
		titleFont = generator.generateFont(parameter);
		generator.dispose();
		
	}
	
	private static void loadSoundsManager() {
		
		assetManager.load("data/Sounds/snd_airGain.mp3", com.badlogic.gdx.audio.Sound.class);
		assetManager.load("data/Sounds/snd_airLow.mp3", com.badlogic.gdx.audio.Sound.class);
		assetManager.load("data/Sounds/snd_bubble_pop.mp3", com.badlogic.gdx.audio.Sound.class);
		assetManager.load("data/Sounds/snd_burst.mp3", com.badlogic.gdx.audio.Sound.class);
		assetManager.load("data/Sounds/snd_charging.mp3", com.badlogic.gdx.audio.Sound.class);
		assetManager.load("data/Sounds/snd_death.mp3", com.badlogic.gdx.audio.Sound.class);
		assetManager.load("data/Sounds/snd_killFish.mp3", com.badlogic.gdx.audio.Sound.class);
		assetManager.load("data/Sounds/snd_killMine.mp3", com.badlogic.gdx.audio.Sound.class);
		assetManager.load("data/Sounds/snd_monster1.mp3", com.badlogic.gdx.audio.Sound.class);
		assetManager.load("data/Sounds/snd_monster2.mp3", com.badlogic.gdx.audio.Sound.class);
		assetManager.load("data/Sounds/snd_monster3.mp3", com.badlogic.gdx.audio.Sound.class);
		assetManager.load("data/Sounds/snd_superburst.mp3", com.badlogic.gdx.audio.Sound.class);
		assetManager.load("data/Sounds/snd_breakBlock.mp3", com.badlogic.gdx.audio.Sound.class);
		assetManager.load("data/Sounds/snd_bumpBlock.mp3", com.badlogic.gdx.audio.Sound.class);
		assetManager.load("data/Sounds/snd_hurt.mp3", com.badlogic.gdx.audio.Sound.class);
		
	}
	
	private static void loadSounds() {
		
		sndAirGain = GMTKJamGame.createSound(assetManager.get("data/Sounds/snd_airGain.mp3", com.badlogic.gdx.audio.Sound.class));
		sndAirLow = GMTKJamGame.createSound(assetManager.get("data/Sounds/snd_airLow.mp3", com.badlogic.gdx.audio.Sound.class));
		sndBubblePop = GMTKJamGame.createSound(assetManager.get("data/Sounds/snd_bubble_pop.mp3", com.badlogic.gdx.audio.Sound.class));
		sndBurst = GMTKJamGame.createSound(assetManager.get("data/Sounds/snd_burst.mp3", com.badlogic.gdx.audio.Sound.class));
		sndCharging = GMTKJamGame.createSound(assetManager.get("data/Sounds/snd_charging.mp3", com.badlogic.gdx.audio.Sound.class));
		sndDeath = GMTKJamGame.createSound(assetManager.get("data/Sounds/snd_death.mp3", com.badlogic.gdx.audio.Sound.class));
		sndKillFish = GMTKJamGame.createSound(assetManager.get("data/Sounds/snd_killFish.mp3", com.badlogic.gdx.audio.Sound.class));
		sndKillMine = GMTKJamGame.createSound(assetManager.get("data/Sounds/snd_killMine.mp3", com.badlogic.gdx.audio.Sound.class));
		sndMonster1 = GMTKJamGame.createSound(assetManager.get("data/Sounds/snd_monster1.mp3", com.badlogic.gdx.audio.Sound.class));
		sndMonster2 = GMTKJamGame.createSound(assetManager.get("data/Sounds/snd_monster2.mp3", com.badlogic.gdx.audio.Sound.class));
		sndMonster3 = GMTKJamGame.createSound(assetManager.get("data/Sounds/snd_monster3.mp3", com.badlogic.gdx.audio.Sound.class));
		sndSuperBurst = GMTKJamGame.createSound(assetManager.get("data/Sounds/snd_superburst.mp3", com.badlogic.gdx.audio.Sound.class));
		sndBreakBlock = GMTKJamGame.createSound(assetManager.get("data/Sounds/snd_breakBlock.mp3", com.badlogic.gdx.audio.Sound.class));
		sndBump = GMTKJamGame.createSound(assetManager.get("data/Sounds/snd_bumpBlock.mp3", com.badlogic.gdx.audio.Sound.class));
		sndHurt = GMTKJamGame.createSound(assetManager.get("data/Sounds/snd_hurt.mp3", com.badlogic.gdx.audio.Sound.class));
		
		sndGrpMonster = new SoundGroup();
		sndGrpMonster.add(sndMonster1);
		sndGrpMonster.add(sndMonster2);
		sndGrpMonster.add(sndMonster3);
		
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
		spacebarTexture = atlas.findRegion("spr_spacebar");
		monsterHeadTexture = atlas.findRegion("spr_monster-sheet");
		
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
		spriteSpacebar = new Sprite(spacebarTexture, 2);
		spriteMonsterHead = new Sprite(monsterHeadTexture, 7);
		
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