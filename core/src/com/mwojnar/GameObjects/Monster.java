package com.mwojnar.GameObjects;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.mwojnar.Assets.AssetLoader;
import com.mwojnar.GameWorld.GMTKJamWorld;
import com.playgon.GameEngine.Entity;
import com.playgon.GameEngine.Mask;
import com.playgon.GameEngine.TouchEvent;
import com.playgon.GameWorld.GameRenderer;
import com.playgon.GameWorld.GameWorld;

public class Monster extends Entity {
	
	private boolean scoreMode = false, highScoreMode = false;
	private int timerBonusCountup = 0, timeElapsedSinceFinish = 0;
	private long privateTimer = 0;
	private List<Integer> highScores = null;
	
	public Monster(GameWorld myWorld) {
		
		super(myWorld);
		setSprite(AssetLoader.spriteMonster);
		setPivot(getSprite().getWidth() / 2.0f, getSprite().getHeight() / 2.0f);
		setMask(new Mask(this, new Vector2(getSprite().getWidth() / 2.0f, getSprite().getHeight() / 2.0f), 22.0f));
		setForceUpdate(true);
		setDepth(100);
		
	}
	
	@Override
	public void update(float delta, List<TouchEvent> touchEventList, List<Character> charactersTyped, List<Integer> keysFirstDown, List<Integer> keysFirstUp, List<Integer> keysDown) {
		
		super.update(delta, touchEventList, charactersTyped, keysFirstDown, keysFirstUp, keysDown);
		
		privateTimer++;
		float moveSpeed = 10.0f;
		if (!((GMTKJamWorld)getWorld()).getSubmarine().isDead())
			moveSpeed = 3.0f + ((GMTKJamWorld)getWorld()).getFramesSinceLevelCreation() / (60.0f * 30.0f);
		if (moveSpeed > 10.0f)
			moveSpeed = 10.0f;
		if (((GMTKJamWorld)getWorld()).isStarted()) {
			
			if (getPos(false).y + getSprite().getHeight() > getWorld().getCamPos(false).y)
				movePos(0.0f, -moveSpeed);
			else {
				
				setPos(getWorld().getCamPos(false).x, getWorld().getCamPos(false).y - getSprite().getHeight() - 50.0f, false);
				scoreMode = true;
				timeElapsedSinceFinish++;
				
			}
			
		}
		if (timeElapsedSinceFinish > 180) {
			
			timerBonusCountup += 500;
			if (timerBonusCountup > ((GMTKJamWorld)getWorld()).getTimerBonus() * 10) {
				
				timerBonusCountup = ((GMTKJamWorld)getWorld()).getTimerBonus() * 10;
				
				if (!highScoreMode) {
					
					int score = ((GMTKJamWorld)getWorld()).getRawScore() + ((GMTKJamWorld)getWorld()).getTimerBonus() * 10;
					Preferences preferences = Gdx.app.getPreferences("GMTKJam Prefs");
					highScores = new ArrayList<Integer>();
					for (int i = 0; i < 5; i++) {
						
						highScores.add(preferences.getInteger("score" + i, 0));
						
					}
					for (int i = 0; i < 5; i++) {
						
						if (highScores.get(i) < score) {
							
							highScores.remove(4);
							highScores.add(i, score);
							break;
							
						}
						
					}
					for (int i = 0; i < 5; i++) {
						
						preferences.putInteger("score" + i, highScores.get(i));
						
					}
					preferences.flush();
					highScoreMode = true;
					
				}
				
			}
			
		}
		if (keysFirstUp.contains(com.badlogic.gdx.Input.Keys.SPACE) && highScoreMode)
			((GMTKJamWorld)getWorld()).startMenu();
	}
	
	@Override
	public void draw(GameRenderer renderer) {
		
		super.draw(renderer);
		AssetLoader.spriteMonsterHead.draw(getPos(true).x - AssetLoader.spriteMonsterHead.getWidth() / 2.0f, getPos(false).y, (int)(privateTimer % (6 * 7)) / 6, getScale(), getScale(), getRotation(), 0.0f, 0.0f, renderer);
		
		float x1 = getPos(false).x;
		float y1 = getPos(false).y + getSprite().getHeight();
		float x2 = x1 + getSprite().getWidth();
		float y2 = y1;
		float x3 = x1;
		float y3 = y1 + 800;
		float x4 = x2;
		float y4 = y3;
		
		AssetLoader.spriteBlack.drawMonocolorTriangle(x1, y1, x2, y2, x3, y3, 1.0f, renderer);
		AssetLoader.spriteBlack.drawMonocolorTriangle(x4, y4, x2, y2, x3, y3, 1.0f, renderer);
		
		if (scoreMode && !highScoreMode) {
			
			if (timeElapsedSinceFinish > 60) {
				
				AssetLoader.titleFont.draw(renderer.getBatcher(), "SCORE", getWorld().getCamPos(false).x + getWorld().getGameDimensions().x / 2.0f, getWorld().getCamPos(false).y + 75.0f, 0, Align.center, false);
				AssetLoader.titleFont.draw(renderer.getBatcher(), Integer.toString(((GMTKJamWorld)getWorld()).getRawScore() + timerBonusCountup), getWorld().getCamPos(false).x + getWorld().getGameDimensions().x / 2.0f, getWorld().getCamPos(false).y + 150.0f, 0, Align.center, false);
				
			}
			if (timeElapsedSinceFinish > 120) {
				
				AssetLoader.titleFont.draw(renderer.getBatcher(), "TIME BONUS", getWorld().getCamPos(false).x + getWorld().getGameDimensions().x / 2.0f, getWorld().getCamPos(false).y + 225.0f, 0, Align.center, false);
				AssetLoader.titleFont.draw(renderer.getBatcher(), Integer.toString(((GMTKJamWorld)getWorld()).getTimerBonus() * 10 - timerBonusCountup), getWorld().getCamPos(false).x + getWorld().getGameDimensions().x / 2.0f, getWorld().getCamPos(false).y + 300.0f, 0, Align.center, false);
				
			}
			
		} else if (highScoreMode) {
			
			AssetLoader.titleFont.draw(renderer.getBatcher(), "HIGH SCORE", getWorld().getCamPos(false).x + getWorld().getGameDimensions().x / 2.0f, getWorld().getCamPos(false).y + 75.0f, 0, Align.center, false);
			for (int i = 0; i < 5; i++)
				AssetLoader.titleFont.draw(renderer.getBatcher(), Integer.toString(i + 1) + ") " + highScores.get(i), getWorld().getCamPos(false).x + getWorld().getGameDimensions().x / 2.0f, getWorld().getCamPos(false).y + 150.0f + 75 * i, 0, Align.center, false);
			AssetLoader.spriteSpacebar.drawAbsolute(getWorld().getGameDimensions().x / 2.0f - AssetLoader.spriteSpacebar.getWidth() / 2.0f, 525.0f, (int)(((GMTKJamWorld)getWorld()).getFramesSinceLevelCreation() % 24) / 12, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, renderer);
			
		}
		
	}
	
	public void teleport() {
		
		if (getPos(false).y > getWorld().getCamPos(false).y + getWorld().getGameDimensions().y)
			setPos(getPos(false).x, getWorld().getCamPos(false).y + getWorld().getGameDimensions().y + 300, false);
		
	}
	
}