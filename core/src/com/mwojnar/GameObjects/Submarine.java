package com.mwojnar.GameObjects;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.mwojnar.Assets.AssetLoader;
import com.playgon.GameEngine.Entity;
import com.playgon.GameEngine.Mask;
import com.playgon.GameEngine.TouchEvent;
import com.playgon.GameWorld.GameRenderer;
import com.playgon.GameWorld.GameWorld;

public class Submarine extends Entity {
	
	private float chargeSpeed, horizontalMaxSpeed, horizontalAcceleration, horizontalDeceleration,
				  chargeMultiplier, superChargeMultiplier, superChargeThreshold, maxCharge,
				  boostDeceleration, verticalDeceleration, airLossRate, airLossFromEnemy, airGainRate,
				  maxAir, minVerticalSpeed, maxVerticalSpeed, chargeCooldownMultiplier;
	private float charge = 0.0f, air = 0.0f;
	private int cooldownLeft = 0;
	
	public Submarine(GameWorld myWorld) {
		
		super(myWorld);
		setSprite(AssetLoader.spriteSubmarine);
		setMask(new Mask(null, new Vector2(3.0f, 0.0f), new Vector2(26.0f, 32.0f)));
		setPivot(getSprite().getWidth() / 2.0f, getSprite().getHeight() / 2.0f);
		setDepth(50);
		loadCustomAttributes();
		
	}
	
	private void loadCustomAttributes() {
		
		JSONParser jsonParser = new JSONParser();
		try {
			
			JSONObject jsonObject = (JSONObject)jsonParser.parse(new FileReader("SubmarineAttributes.txt"));
			chargeSpeed = Float.parseFloat((String)jsonObject.get("Speed of charging"));
			horizontalMaxSpeed = Float.parseFloat((String)jsonObject.get("Horizontal max speed"));
			horizontalAcceleration = Float.parseFloat((String)jsonObject.get("Horizontal acceleration"));
			horizontalDeceleration = Float.parseFloat((String)jsonObject.get("Horizontal deceleration"));
			chargeMultiplier = Float.parseFloat((String)jsonObject.get("Speed a charge sends you upward"));
			superChargeMultiplier = Float.parseFloat((String)jsonObject.get("Speed added by super charge"));
			superChargeThreshold = Float.parseFloat((String)jsonObject.get("Amount of Charge that equals Super Charge"));
			maxCharge = Float.parseFloat((String)jsonObject.get("Max Charge"));
			boostDeceleration = Float.parseFloat((String)jsonObject.get("Boost Charge declerate"));
			verticalDeceleration = Float.parseFloat((String)jsonObject.get("Normal deceleration"));
			airLossRate = Float.parseFloat((String)jsonObject.get("Air decrease speed"));
			airLossFromEnemy = Float.parseFloat((String)jsonObject.get("Air hit reduction"));
			airGainRate = Float.parseFloat((String)jsonObject.get("Air increase speed in bubble"));
			maxAir = Float.parseFloat((String)jsonObject.get("Max air"));
			minVerticalSpeed = Float.parseFloat((String)jsonObject.get("Minimum vertical speed"));
			maxVerticalSpeed = Float.parseFloat((String)jsonObject.get("Maximum vertical speed"));
			chargeCooldownMultiplier = Float.parseFloat((String)jsonObject.get("Charge cooldown multiplier"));
			
			air = maxAir;
			
		} catch (Exception e) {
			
			chargeSpeed = 0.1f;
			horizontalMaxSpeed = 8.0f;
			horizontalAcceleration = 0.5f;
			horizontalDeceleration = 0.25f;
			chargeMultiplier = 1.0f;
			superChargeMultiplier = 0.5f;
			superChargeThreshold = 18.0f;
			maxCharge = 24.0f;
			boostDeceleration = 3.0f;
			verticalDeceleration = 1.0f;
			airLossRate = 0.005f;
			airLossFromEnemy = 0.1f;
			airGainRate = 0.05f;
			maxAir = 10.0f;
			minVerticalSpeed = 0.1f;
			maxVerticalSpeed = 20.0f;
			chargeCooldownMultiplier = 2.0f;
			
			air = maxAir;

			e.printStackTrace();
			
		}
		
	}

	@Override
	public void update(float delta, List<TouchEvent> touchEventList, List<Character> charactersTyped, List<Integer> keysFirstDown, List<Integer> keysFirstUp, List<Integer> keysDown) {
		
		super.update(delta, touchEventList, charactersTyped, keysFirstDown, keysFirstUp, keysDown);
		
		cooldownLeft--;
		if (cooldownLeft < 0)
			cooldownLeft = 0;
		air -= airLossRate;
		if (air <= 0)
			die();
		boolean boosting = false;
		if (keysDown.contains(com.badlogic.gdx.Input.Keys.SPACE) && cooldownLeft <= 0)
			boosting = true;
		if (boosting) {
			
			charge += chargeSpeed;
			setGridVelocity(getGridVelocity().x, getGridVelocity().y + boostDeceleration);
			
		} else
			setGridVelocity(getGridVelocity().x, getGridVelocity().y + verticalDeceleration);
		if (charge > maxCharge || (charge > 0 && !keysDown.contains(com.badlogic.gdx.Input.Keys.SPACE)))
			burst();
		if (keysDown.contains(com.badlogic.gdx.Input.Keys.LEFT))
			setGridVelocity(getGridVelocity().x - horizontalAcceleration, getGridVelocity().y);
		else if (keysDown.contains(com.badlogic.gdx.Input.Keys.RIGHT))
			setGridVelocity(getGridVelocity().x + horizontalAcceleration, getGridVelocity().y);
		else {
			
			float decelerationMultiple = -Math.signum(getGridVelocity().x);
			float newHorizontalSpeed = getGridVelocity().x + decelerationMultiple * horizontalDeceleration;
			if (-Math.signum(newHorizontalSpeed) != decelerationMultiple)
				newHorizontalSpeed = 0;
			setGridVelocity(newHorizontalSpeed, getGridVelocity().y);
			
		}
		if (Math.abs(getGridVelocity().y) < minVerticalSpeed || getGridVelocity().y > 0.0f)
			setGridVelocity(getGridVelocity().x, -minVerticalSpeed);
		if (Math.abs(getGridVelocity().y) > maxVerticalSpeed)
			setGridVelocity(getGridVelocity().x, -maxVerticalSpeed);
		if (Math.abs(getGridVelocity().x) > horizontalMaxSpeed)
			setGridVelocity(horizontalMaxSpeed * Math.signum(getGridVelocity().x), getGridVelocity().y);
		moveByVelocity();
		
	}
	
	private void die() {
		// TODO Auto-generated method stub
		
	}

	private void burst() {
		
		cooldownLeft = (int)(charge * chargeCooldownMultiplier);
		float multiplier = chargeMultiplier;
		if (charge > superChargeThreshold)
			multiplier += superChargeMultiplier;
		setGridVelocity(getGridVelocity().x, -charge * multiplier);
		charge = 0;
		
	}

	@Override
	public void draw(GameRenderer renderer) {
		
		super.draw(renderer);
		
	}
	
}