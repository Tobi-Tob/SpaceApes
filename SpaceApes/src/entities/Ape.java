package entities;

import org.newdawn.slick.geom.Vector2f;
import eea.engine.entity.Entity;
import policys.Policy;
import utils.Constants;
import spaceapes.SpaceApes;
import utils.Utils;

public class Ape extends Entity {

	private Planet homePlanet; // Planet auf dem sich der Affe befindet
	private float angleOnPlanet; // Einheit Grad, Drehsinn im Uhrzeigersinn, 0 enspricht Osten
	private float angleOfView; // Blickwinkel zischen -90 grad (links) und 90 grad (rechts) fuer den Schuss
	private Vector2f worldCoordinates; // in Weltkoordinaten
	private float throwStrength; // Abwurfgeschwindigkeit fuer Projektile
	private float movementSpeed; // Faktor fuer die Schrittweite des Affen
	private float distancePlanetCenter; // Abstand des Planetenmittelpunkts zur Kreisbahn auf der sich der Affe bewegt

	public final int apePixelHeight = 300;
	public final int apePixelFeetToCenter = 130;
	private final float scalingFactor = Constants.APE_SIZE / Utils.pixelLengthToWorldLength(apePixelHeight);

	private final int maxHealth = Constants.APE_MAX_HEALTH;
	private final int maxEnergy = Constants.APE_MAX_ENERGY;
	private int health = maxHealth;
	private float energy = maxEnergy;
	private int coins = 0;

	private boolean isActive;
	private boolean isInteractionAllowed;
	
	private Policy policy = null;
	
	/* Game statistics to save */
	private int damageDealt = 0;
	private int damageReceived = 0;
	private float energyUsed = 0;
	private int itemsCollected = 0;
	private int moneySpend = 0;
	

	public Ape(String entityID) {
		super(entityID);
	}

	/**
	 * Berechnet die Welt-Koordinaten des Affen, abhaengig von seinem Winkel auf dem
	 * Planeten (angleOnPlanet) und der Position des Planeten
	 * 
	 * @return Vector2f in Welt-Koordinaten
	 */
	private Vector2f calcWorldCoordinates() {
		if (distancePlanetCenter == 0) {
			throw new RuntimeException("Ape distancePlanetCenter not calculated yet");
		}
		Vector2f relativPos = Utils.toCartesianCoordinates(distancePlanetCenter, angleOnPlanet);
		// Koordinaten des Planeten + relative Koordinaten vom Planeten zum Affen
		return relativPos.add(homePlanet.getCoordinates());
	}
	
	/**
	 * Aendert angleOnPlanet des Affens nach links oder rechts
	 * 
	 * @param direction fuer Bewegung nach links -1 und fuer Bewegung nach rechts +1
	 */
	public void stepOnPlanet(float direction) {
		this.angleOnPlanet += direction * movementSpeed * SpaceApes.UPDATE_INTERVAL / distancePlanetCenter;
		if (angleOnPlanet < 0) {
			this.angleOnPlanet += 360f;
		}
		if (angleOnPlanet >= 360f) {
			this.angleOnPlanet -= 360f;
		}
		this.worldCoordinates = calcWorldCoordinates(); // Update der Koordinaten
		setPosition(Utils.toPixelCoordinates(getWorldCoordinates()));
		setRotation(angleOnPlanet + 90f);
	}

	/**
	 * Position in Welt-Koordinaten
	 */
	public Vector2f getWorldCoordinates() {
		if (worldCoordinates == null) {
			throw new RuntimeException("Ape Coordinates not set yet");
		}
		return new Vector2f(worldCoordinates);
	}

	public float getX() {
		return worldCoordinates.x;
	}

	public float getY() {
		return worldCoordinates.y;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setInteractionAllowed(boolean isInteractionAllowed) {
		this.isInteractionAllowed = isInteractionAllowed;
	}

	public boolean isInteractionAllowed() {
		return isInteractionAllowed;
	}

	public float getAngleOnPlanet() {
		return angleOnPlanet;
	}

	public void setAngleOnPlanet(float alpha) {
		if (alpha < 0) {
			alpha += 360f;
		}
		if (alpha >= 360f) {
			alpha -= 360f;
		}
		this.angleOnPlanet = alpha;
		this.worldCoordinates = calcWorldCoordinates();
	}

	public void setAngleOfView(float beta) {
		if (beta < -90 || beta > 90) {
			throw new RuntimeException("Invalide angle of view");
		}
		angleOfView = beta;
	}

	public void changeAngleOfView(float gamma) {
		angleOfView = angleOfView + gamma;
		if (angleOfView < -90) {
			angleOfView = -90;
		}
		if (angleOfView > 90) {
			angleOfView = 90;
		}
	}

	public float getLocalAngleOfView() {
		return angleOfView;
	}

	public float getGlobalAngleOfView() {
		return angleOnPlanet + angleOfView;
	}

	public float getThrowStrength() {
		return throwStrength;
	}

	/**
	 * throwStrength of ape between 1 and 7
	 */
	public void setThrowStrength(float power) {
		if (power < 1 || power > 7) {
			throw new RuntimeException("Invalide Throw Strength");
		}
		throwStrength = power;
	}

	/**
	 * throwStrength of ape between 1 and 7
	 * 
	 * @param power amount to change
	 */
	public void changeThrowStrength(float power) {
		throwStrength = throwStrength + power;
		if (throwStrength < 1) {
			throwStrength = 1;
		}
		if (throwStrength > 7) {
			throwStrength = 7;
		}
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int value) {
		if (value > maxHealth) {
			health = maxHealth;
		} else if (value < 0) {
			health = 0;
		} else {
			health = value;
		}
	}

	public void changeHealth(int value) {
		if (health + value > maxHealth) {
			health = maxHealth;
		} else if (health + value < 0) {
			health = 0;
		} else {
			health += value;
		}
	}

	public boolean isAlive() {
		return health > 0;
	}

	public boolean hasEnergy() {
		return energy > 0;
	}

	public float getMovementSpeed() {
		return movementSpeed;
	}

	public void setMovementSpeed(float movementSpeed) {
		this.movementSpeed = movementSpeed;
	}

	public float getEnergy() {
		return energy;
	}

	public void setEnergy(float value) {
		if (value > maxEnergy) {
			energy = maxEnergy;
		} else if (value < 0) {
			energy = 0;
		} else {
			energy = value;
		}
	}

	public void changeEnergy(float value) {
		if (energy + value > maxEnergy) {
			energy = maxEnergy;
		} else if (energy + value < 0) {
			energy = 0;
		} else {
			energy += value;
		}
	}

	public float getDistanceToPlanetCenter() {
		return distancePlanetCenter;
	}

	public void setDistanceToPlanetCenter() {
		float distance = homePlanet.getRadius()
				+ Utils.pixelLengthToWorldLength(apePixelFeetToCenter * scalingFactor);
		if (distance < 0.1f) {
			throw new RuntimeException("Distance to planet center is to close to 0");
		}
		this.distancePlanetCenter = distance;
	}

	public void setPlanet(Planet planet) {
		this.homePlanet = planet;
	}

	public Planet getPlanet() {
		if (homePlanet == null) {
			throw new RuntimeException("homePlanet not set yet!");
		}
		return homePlanet;
	}

	public void setCoins(int coins) {
		this.coins = coins;
	}

	public int getCoins() {
		return coins;
	}

	public void reduceCoins(int value) {
		coins -= value;
	}

	public void increaseCoins(int value) {
		coins += value;
	}

	public float getRadiusInWorldUnits() { //necessary for collision check
		return Constants.APE_SIZE / 2;
	}

	/**
	 * Diese Methode prueft, ob die uebergebene Position innerhalb des Affenradius
	 * plus eine frei waehlbare Margin liegt und somit Kollision vorliegt (Test
	 * durch Kreisgleichung mit dem Radius: getRadiusInWorldUnits() + margin)
	 * 
	 * @param pos    Koordinaten des zu pruefenden Punktes
	 * @param margin manuelle vergoesserung der Kollisionsflaeche
	 * @return true, wenn eine Kollision vorliegt, ansonsten false
	 */
	public boolean checkCollision(Vector2f pos, float margin) {
		Vector2f distanceVector = new Vector2f(getWorldCoordinates()).sub(pos);
		if (Math.pow(distanceVector.x, 2) + Math.pow(distanceVector.y, 2) < Math.pow(getRadiusInWorldUnits() + margin,
				2)) {
			return true;
		}
		return false;
	}

	public float getScalingFactor() {
		return scalingFactor;
	}

	public String getDamageDealtStatistics() {
		return Integer.toString(damageDealt);
	}

	public void increaseDamageDealtStatistics(int damage) {
		this.damageDealt += damage;
	}

	public String getDamageReceivedStatistics() {
		return Integer.toString(damageReceived);
	}

	public void increaseDamageReceivedStatistics(int damage) {
		this.damageReceived += damage;
	}

	public String getEnergyUsedStatistics() {
		return Integer.toString((int) Math.ceil(energyUsed));
	}

	public void increaseEnergyUsedStatistics(float energyUsed) {
		this.energyUsed += energyUsed;
	}

	public String getItemsCollectedStatistics() {
		return Integer.toString(itemsCollected);
	}

	public void increaseItemsCollectedStatistics() {
		this.itemsCollected ++;
	}

	public String getMoneySpendStatistics() {
		return Integer.toString(moneySpend);
	}

	public void increaseMoneySpendStatistics(int moneySpend) {
		this.moneySpend += moneySpend;
	}

	public void setPolicy(Policy policy) {
		this.policy = policy;
	}
	
	public Policy getPolicy() {
		return this.policy;
	}
	
	public boolean isAIControlled() {
		if (this.policy == null) {
			return false;
		}
		else {
			return true;
		}
	}

}
