package com.mwojnar.GameObjects;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
					   boostDeceleration, verticalDecleration, airLossRate, airLossFromEnemy, airGainRate;
	
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
			verticalDecleration = Float.parseFloat((String)jsonObject.get("Normal deceleration"));
			airLossRate = Float.parseFloat((String)jsonObject.get("Air decrease speed"));
			airLossFromEnemy = Float.parseFloat((String)jsonObject.get("Air hit reduction"));
			airGainRate = Float.parseFloat((String)jsonObject.get("Air increase speed in bubble"));
			
		} catch (Exception e) {
			
			chargeSpeed = 0.0f;
			horizontalMaxSpeed = 0.0f;
			horizontalAcceleration = 0.0f;
			horizontalDeceleration = 0.0f;
			chargeMultiplier = 0.0f;
			superChargeMultiplier = 0.0f;
			superChargeThreshold = 0.0f;
			maxCharge = 0.0f;
			boostDeceleration = 0.0f;
			verticalDecleration = 0.0f;
			airLossRate = 0.0f;
			airLossFromEnemy = 0.0f;
			airGainRate = 0.0f;

			e.printStackTrace();
			
		}
		
	}

	@Override
	public void update(float delta, List<TouchEvent> touchEventList, List<Character> charactersTyped, List<Integer> keysFirstDown, List<Integer> keysFirstUp, List<Integer> keysDown) {
		
		
		
	}
	
	@Override
	public void draw(GameRenderer renderer) {
		
		
		
	}
	
}