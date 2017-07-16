package com.mwojnar.GameObjects;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.mwojnar.Assets.AssetLoader;
import com.mwojnar.GameWorld.GMTKJamWorld;
import com.playgon.GameEngine.Entity;
import com.playgon.GameEngine.Mask;
import com.playgon.GameEngine.Sprite;
import com.playgon.GameEngine.TouchEvent;
import com.playgon.GameWorld.GameRenderer;
import com.playgon.GameWorld.GameWorld;
import com.playgon.Utils.PlaygonMath;

public class Submarine extends Entity {
	
	private float chargeSpeed, horizontalMaxSpeed, horizontalAcceleration, horizontalDeceleration,
				  chargeMultiplier, superChargeMultiplier, superChargeThreshold, maxCharge,
				  boostDeceleration, verticalDeceleration, airLossRate, airLossFromEnemy, airGainRate,
				  maxAir, minVerticalSpeed, maxVerticalSpeed, chargeCooldownMultiplier,
				  superChargeCooldownAddition, minChargeSpeed, bubbleChargeSpeed, bubbleAddSpeed,
				  superBoostAttackDuration;
	private float charge = 0.0f, air = 0.0f, currentMaxVerticalSpeed = 0.0f;
	private int cooldownLeft = 0, bubbleTweenFrames = 0, startBubbleTweenFrames = 10, superBoostCooldown = 0,
			invincibilityTimer = 0, invincibilityTimerMax = 90, recoilTimer = 0, recoilTimerMax = 30,
			bubbleTimer = 0;
	private boolean superCooldown = false, bubbleMaxSpeed = false, dead = false;
	private Bubble stickBubble = null;
	private ChargeMode chargeMode = ChargeMode.IDLE;
	
	private enum ChargeMode { IDLE, CHARGING, UNCHARGING }
	
	public Submarine(GameWorld myWorld) {
		
		super(myWorld);
		setSprite(AssetLoader.spriteSubmarine);
		setMask(new Mask(this, new Vector2(3.0f, 0.0f), new Vector2(26.0f, 32.0f)));
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
			boostDeceleration = Float.parseFloat((String)jsonObject.get("Boost Charge decelerate"));
			verticalDeceleration = Float.parseFloat((String)jsonObject.get("Normal deceleration"));
			airLossRate = Float.parseFloat((String)jsonObject.get("Air decrease speed"));
			airLossFromEnemy = Float.parseFloat((String)jsonObject.get("Air hit reduction"));
			airGainRate = Float.parseFloat((String)jsonObject.get("Air increase speed in bubble"));
			maxAir = Float.parseFloat((String)jsonObject.get("Max air"));
			minVerticalSpeed = Float.parseFloat((String)jsonObject.get("Minimum vertical speed"));
			maxVerticalSpeed = Float.parseFloat((String)jsonObject.get("Maximum vertical speed"));
			chargeCooldownMultiplier = Float.parseFloat((String)jsonObject.get("Charge cooldown multiplier"));
			superChargeCooldownAddition = Float.parseFloat((String)jsonObject.get("Super Charge cooldown add"));
			minChargeSpeed = Float.parseFloat((String)jsonObject.get("Minimum speed a charge sends you upward"));
			bubbleChargeSpeed = Float.parseFloat((String)jsonObject.get("Charge Speed in bubble"));
			bubbleAddSpeed = Float.parseFloat((String)jsonObject.get("Bubble add speed"));
			superBoostAttackDuration = Float.parseFloat((String)jsonObject.get("Super Boost Attack duration"));
			
			air = maxAir;
			currentMaxVerticalSpeed = maxVerticalSpeed;
			
		} catch (Exception e) {
			
			chargeSpeed = 0.1f;
			horizontalMaxSpeed = 4.0f;
			horizontalAcceleration = 1.0f;
			horizontalDeceleration = 0.5f;
			chargeMultiplier = 1.0f;
			superChargeMultiplier = 0.5f;
			superChargeThreshold = 5.0f;
			maxCharge = 7.0f;
			boostDeceleration = 0.06f;
			verticalDeceleration = 0.04f;
			airLossRate = 0.01f;
			airLossFromEnemy = 0.1f;
			airGainRate = 0.04f;
			maxAir = 10.0f;
			minVerticalSpeed = 0.1f;
			maxVerticalSpeed = 10.0f;
			chargeCooldownMultiplier = 10.0f;
			superChargeCooldownAddition = 120.0f;
			minChargeSpeed = 2.5f;
			bubbleChargeSpeed = 0.175f;
			bubbleAddSpeed = 2.0f;
			superBoostAttackDuration = 150.0f;
			
			air = maxAir;
			currentMaxVerticalSpeed = maxVerticalSpeed;

			e.printStackTrace();
			
		}
		
	}

	@Override
	public void update(float delta, List<TouchEvent> touchEventList, List<Character> charactersTyped, List<Integer> keysFirstDown, List<Integer> keysFirstUp, List<Integer> keysDown) {
		
		super.update(delta, touchEventList, charactersTyped, keysFirstDown, keysFirstUp, keysDown);
		
		if (!dead) {
			
			cooldownLeft--;
			if (cooldownLeft < 0)
				cooldownLeft = 0;
			superBoostCooldown--;
			if (superBoostCooldown < 0)
				superBoostCooldown = 0;
			bubbleTweenFrames--;
			if (bubbleTweenFrames < 0)
				bubbleTweenFrames = 0;
			invincibilityTimer--;
			if (invincibilityTimer < 0)
				invincibilityTimer = 0;
			bubbleTimer--;
			if (bubbleTimer < 0)
				bubbleTimer = 0;
			recoilTimer--;
			if (recoilTimer < 0)
				recoilTimer = 0;
			if (stickBubble == null)
				air -= airLossRate;
			else
				air += airGainRate;
			if (air <= 0) {
				
				die();
				air = 0;
				
			}
			if (air > maxAir)
				air = maxAir;
			boolean boosting = false;
			if (keysDown.contains(com.badlogic.gdx.Input.Keys.SPACE) && cooldownLeft <= 0)
				boosting = true;
			if (boosting) {
				
				if (stickBubble == null)
					charge += chargeSpeed;
				else
					charge += bubbleChargeSpeed;
				if (recoilTimer <= 0)
					setGridVelocity(getGridVelocity().x, getGridVelocity().y + boostDeceleration);
				chargeMode = ChargeMode.CHARGING;
				
			} else if (recoilTimer <= 0)
				setGridVelocity(getGridVelocity().x, getGridVelocity().y + verticalDeceleration);
			if (charge > maxCharge || (charge > 0 && !keysDown.contains(com.badlogic.gdx.Input.Keys.SPACE)))
				burst();
			if (cooldownLeft > 0)
				chargeMode = ChargeMode.UNCHARGING;
			else if (!boosting)
				chargeMode = ChargeMode.IDLE;
			if (recoilTimer <= 0) {
				
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
				
			}
			if (bubbleMaxSpeed && Math.abs(getGridVelocity().y) <= maxVerticalSpeed) {
				
				currentMaxVerticalSpeed = maxVerticalSpeed;
				bubbleMaxSpeed = false;
				
			}
			if ((Math.abs(getGridVelocity().y) < minVerticalSpeed || getGridVelocity().y > 0.0f) && recoilTimer <= 0)
				setGridVelocity(getGridVelocity().x, -minVerticalSpeed);
			if (Math.abs(getGridVelocity().y) > currentMaxVerticalSpeed)
				setGridVelocity(getGridVelocity().x, -currentMaxVerticalSpeed);
			if (Math.abs(getGridVelocity().x) > horizontalMaxSpeed)
				setGridVelocity(horizontalMaxSpeed * Math.signum(getGridVelocity().x), getGridVelocity().y);
			moveByVelocity();
			if (getPos(false).x < 40.0f)
				setPos(40.0f, getPos(false).y, false);
			else if (getPos(false).x + getSprite().getWidth() > getWorld().getGameDimensions().x - 40.0f)
				setPos(getWorld().getGameDimensions().x - 40.0f - getSprite().getWidth(), getPos(false).y, false);
			if (stickBubble != null) {
				
				setGridVelocity(getGridVelocity().x, getGridVelocity().y + boostDeceleration);
				if (bubbleTweenFrames <= 0)
					setPos(stickBubble.getPos(true), true);
				else
					tweenBubble();
				
				if (stickBubble.getSprite() == AssetLoader.spriteBubblePop)
					stickBubble = null;
				
			}
			handleCollisions();
			handleChargeFrames();
			if (bubbleTimer > 0)
				tryCreateBubble();
			if (getPos(true).y >= ((GMTKJamWorld)getWorld()).getMonster().getPos(true).y)
				die();
			
		}
		
	}
	
	private void tryCreateBubble() {
		
		if (((GMTKJamWorld)getWorld()).getRandom().nextInt(2) == 0)
			createBubble();
		
	}

	private void createBubble() {
		
		ParticleBubble bubble = new ParticleBubble(getWorld());
		bubble.setPos(getPos(false).x + getSprite().getWidth() * ((GMTKJamWorld)getWorld()).getRandom().nextFloat(), getPos(false).y + getSprite().getHeight(), true);
		getWorld().createEntity(bubble);
		
	}

	private void tweenBubble() {
		
		float distanceToCenterOfBubble = PlaygonMath.distance(getPos(true), stickBubble.getPos(true));
		float directionToCenterOfBubble = PlaygonMath.direction(getPos(true), stickBubble.getPos(true));
		float moveDistance = distanceToCenterOfBubble / bubbleTweenFrames;
		movePos(PlaygonMath.getGridVector(moveDistance, directionToCenterOfBubble));
		
	}

	private void handleCollisions() {
		
		for (Entity entity : getWorld().getEntityList()) {
			
			if (collisionsWith(entity).size() > 0) {
				
				if (entity instanceof Bubble) {
					
					if (entity.getSprite() != AssetLoader.spriteBubblePop && stickBubble == null) {
						
						stickBubble = (Bubble)entity;
						bubbleTweenFrames = startBubbleTweenFrames;
						superBoostCooldown = 0;
						bubbleTimer = 0;
						
					}
					
				} else if (entity instanceof Enemy) {
					
					if (invincibilityTimer <= 0) {
						
						if (superBoostCooldown > 0)
							((Enemy)entity).burst();
						else {
							
							recoil(entity.getPos(true));
							bubbleTimer = 0;
							if (!(entity instanceof CrumblyWall)) {
								
								invincibilityTimer = invincibilityTimerMax;
								air -= airLossFromEnemy;
								charge = 0;
								
							}
							((Enemy)entity).bump();
							
						}
						
					}
					
				}
				
			}
			
		}
		
	}

	private void handleChargeFrames() {
		
		switch (chargeMode) {
		
		case IDLE: setSprite(AssetLoader.spriteSubmarine); break;
		case CHARGING: setSprite(AssetLoader.spriteSubmarineCharging);
			 if (charge < superChargeThreshold)
				 setFrame((int)(charge * 10 / superChargeThreshold));
			 else
				 setFrame((int)(10 + (charge - superChargeThreshold) * 10 / (maxCharge - superChargeThreshold)));
			 break;
		case UNCHARGING: setSprite(AssetLoader.spriteSubmarineCharging);
			 float chargeEquivalent = (cooldownLeft / (maxCharge * chargeCooldownMultiplier)) * maxCharge;
			 if (superCooldown)
				 chargeEquivalent = (cooldownLeft / (maxCharge * chargeCooldownMultiplier + superChargeCooldownAddition)) * maxCharge;
			 if (chargeEquivalent < superChargeThreshold)
				 setFrame((int)(chargeEquivalent * 10 / superChargeThreshold));
			 else
				 setFrame((int)(10 + (chargeEquivalent - superChargeThreshold) * 10 / (maxCharge - superChargeThreshold)));
			 break;
		
		}
		
	}

	private void die() {
		
		dead = true;
		setGridVelocity(0.0f, 0.0f);
		air = 0.0f;
		charge = 0.0f;
		setFrame(0);
		setSprite(AssetLoader.spriteSubmarine);
		
	}

	private void burst() {
		
		((GMTKJamWorld)getWorld()).setStarted(true);
		superCooldown = false;
		cooldownLeft = (int)(charge * chargeCooldownMultiplier);
		float multiplier = chargeMultiplier;
		bubbleTimer = (int)(superBoostAttackDuration * charge / maxCharge);
		if (charge >= superChargeThreshold) {
			
			multiplier += superChargeMultiplier;
			cooldownLeft += superChargeCooldownAddition;
			superCooldown = true;
			superBoostCooldown = (int)superBoostAttackDuration;
			bubbleTimer = (int)superBoostAttackDuration;
			
		}
		float boostSpeed = -charge * multiplier - minChargeSpeed;
		if (stickBubble != null) {
			
			stickBubble.pop();
			currentMaxVerticalSpeed = maxVerticalSpeed + bubbleAddSpeed;
			bubbleMaxSpeed = true;
			boostSpeed -= bubbleAddSpeed;
			
		}
		stickBubble = null;
		setGridVelocity(getGridVelocity().x, boostSpeed);
		charge = 0;
		setSprite(AssetLoader.spriteSubmarine);
		
	}

	@Override
	public void draw(GameRenderer renderer) {
		
		if (invincibilityTimer <= 0 || invincibilityTimer % 3 == 0) {
			
			if (superBoostCooldown > 0) {
				
				float alpha = superBoostCooldown / 30.0f;
				if (alpha > 1.0f)
					alpha = 1.0f;
				AssetLoader.spriteSubmarineAttackFire.draw(getPos(true).x - AssetLoader.spriteSubmarineAttackFire.getWidth() / 2.0f, getPos(true).y - AssetLoader.spriteSubmarineAttackFire.getHeight() / 2.0f, (10000 - superBoostCooldown) % 3, getScale(), getScale(), PlaygonMath.toRadians(getGridVelocity().x * 40.0f), AssetLoader.spriteSubmarineAttackFire.getWidth() / 2.0f, AssetLoader.spriteSubmarineAttackFire.getHeight() / 2.0f, alpha, renderer);
				
			}
			
			getSprite().draw(getPos(false).x, getPos(false).y, getFrame(), getScale(), getScale(), PlaygonMath.toRadians(getGridVelocity().x * 40.0f), getSprite().getWidth() / 2.0f, getSprite().getHeight() / 2.0f, renderer);
			
		}
		
		/*AssetLoader.spriteChargeMeter.drawAbsolute(0.0f, 0.0f, 0, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, renderer);
		Vector2 whiteRectangleDimensions = new Vector2(AssetLoader.spriteChargeMeter.getWidth(), AssetLoader.spriteChargeMeter.getHeight());
		if (charge > superChargeThreshold) {
			
			whiteRectangleDimensions = new Vector2(AssetLoader.spriteChargeMeter.getWidth() * (superChargeThreshold / maxCharge), AssetLoader.spriteChargeMeter.getHeight());
			Vector2 yellowRectangleDimensions = new Vector2(AssetLoader.spriteChargeMeter.getWidth() * ((charge - superChargeThreshold) / maxCharge), AssetLoader.spriteChargeMeter.getHeight());
			Vector2 yellowPosition = new Vector2(getWorld().getCamPos(false).x + AssetLoader.spriteChargeMeter.getWidth() * (superChargeThreshold / maxCharge), getWorld().getCamPos(false).y);
			drawRectangle(renderer, yellowPosition, yellowRectangleDimensions, AssetLoader.spriteRed);
			
		} else 
			whiteRectangleDimensions = new Vector2(AssetLoader.spriteChargeMeter.getWidth() * (charge / maxCharge), AssetLoader.spriteChargeMeter.getHeight());
		drawRectangle(renderer, getWorld().getCamPos(false), whiteRectangleDimensions, AssetLoader.spriteYellow);*/
		
	}
	
	private void drawDistanceHud(GameRenderer renderer) {
		
		float distance = PlaygonMath.distance(getPos(true), ((GMTKJamWorld)getWorld()).getMonster().getPos(true));
		float x1 = getWorld().getGameDimensions().x - 2.0f - AssetLoader.spriteDistanceHud.getWidth() / 2.0f;
		float y1 = getWorld().getGameDimensions().y - 2.0f - AssetLoader.spriteDistanceHud.getHeight() / 2.0f;
		float x2 = x1;
		float y2 = y1 - distance / 10.0f;
		
		AssetLoader.spriteWhite.drawMonocolorLine(getWorld().getCamPos(false).x + x1, getWorld().getCamPos(false).y + y1, getWorld().getCamPos(false).x + x2, getWorld().getCamPos(false).y + y2, 2.0f, 1.0f, renderer);
		AssetLoader.spriteDistanceHud.drawAbsolute(x1 - AssetLoader.spriteDistanceHud.getWidth() / 2.0f, y1 - AssetLoader.spriteDistanceHud.getHeight() / 2.0f, 0, 1.0f, 1.0f, 0.0f, AssetLoader.spriteDistanceHud.getWidth() / 2.0f, AssetLoader.spriteDistanceHud.getHeight() / 2.0f, renderer);
		AssetLoader.spriteDistanceHud.drawAbsolute(x2 - AssetLoader.spriteDistanceHud.getWidth() / 2.0f, y2 - AssetLoader.spriteDistanceHud.getHeight() / 2.0f, 1, 1.0f, 1.0f, 0.0f, AssetLoader.spriteDistanceHud.getWidth() / 2.0f, AssetLoader.spriteDistanceHud.getHeight() / 2.0f, renderer);
		
	}
	
	private void drawRectangle(GameRenderer renderer, Vector2 position, Vector2 dimensions, Sprite sprite) {
		
		float x1 = position.x;
		float y1 = position.y;
		float x2 = position.x + dimensions.x;
		float y2 = position.y;
		float x3 = position.x;
		float y3 = position.y + dimensions.y;
		float x4 = position.x + dimensions.x;
		float y4 = position.y + dimensions.y;
		sprite.drawMonocolorTriangle(x1, y1, x2, y2, x3, y3, 1.0f, renderer);
		sprite.drawMonocolorTriangle(x2, y2, x4, y4, x3, y3, 1.0f, renderer);
		
	}
	
	private void recoil(Vector2 recoilPoint) {
		
		recoilTimer = recoilTimerMax;
		setRadialVelocity(0.5f, PlaygonMath.direction(recoilPoint, getPos(true)));
		
	}
	
	public float getAir() {
		
		return air;
		
	}

	public float getMaxAir() {
		
		return maxAir;
		
	}
	
}