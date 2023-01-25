package entities;

import org.newdawn.slick.geom.Vector2f;
import eea.engine.entity.Entity;
import map.Map;
import utils.Utils;

public class Ape extends Entity {

	private Planet homePlanet; // Planet auf dem sich der Affe befindet
	private float angleOnPlanet; // Einheit Grad, Drehsinn im Uhrzeigersinn, 0 enspricht Osten
	private float angleOfView; // Blickwinkel zischen -90 grad (links) und 90 grad (rechts) fuer den Schuss
	private Vector2f worldCoordinates; // in Weltkoordinaten
	private float throwStrength; // Abwurfgeschwindigkeit fuer Projektile
	private float movementSpeed; // Faktor fuer die Schrittweite des Affen
	private float distancePlanetCenter; // Abstand des Planetenmittelpunkts zur Kreisbahn auf der sich der Affe bewegt

	public float apePixelHeight = 300;
	public float pixelfromFeetToCenter = 130;
	public float desiredApeSizeInWorldUnits = 0.6f;
	public final float scalingFactor = desiredApeSizeInWorldUnits / Utils.pixelLengthToWorldLength(apePixelHeight);

	private int health = 100;
	private float energy = 50;
	private int coins = 0;

	private boolean isActive;
	private boolean isInteractionAllowed;

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
		this.angleOnPlanet += direction * movementSpeed / distancePlanetCenter; // Update des Winkels
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
		angleOnPlanet = alpha;
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

	public void setThrowStrength(float power) {
		if (power < 0 || power > 10) {
			throw new RuntimeException("Invalide Throw Strength");
		}
		throwStrength = power;
	}

	public void changeThrowStrength(float power) {
		throwStrength = throwStrength + power;
		if (throwStrength < 0) {
			throwStrength = 0;
		}
		if (throwStrength > 10) {
			throwStrength = 10;
		}
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int newHealth) {
		this.health = newHealth;
		if(health < 0) {
			this.health = 0;
			Map.getInstance().apeDied(this);
		}
	}
	
	public void changeHealth(int damage) {
		this.health = health - damage;
		if(health <= 0) {
			this.health = 0;
			Map.getInstance().apeDied(this);
		}
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

	public void setEnergy(float energy) {
		this.energy = energy;
	}

	public float getDistanceToPlanetCenter() {
		return distancePlanetCenter;
	}

	public void setDistanceToPlanetCenter(float distancePlanetCenter) {
		this.distancePlanetCenter = distancePlanetCenter;
	}

	public void setPlanet(Planet planet) {
		this.homePlanet = planet;
	}

	public Planet getPlanet() {
		if (homePlanet == null) {
			System.out.println("homePlanet not set yet!");
		}
		return homePlanet;
	}

	public void setCoins(int coins) {
		this.coins = coins;
	}

	public int getCoins() {
		return coins;
	}

	public float getApePixelHeight() {
		return apePixelHeight;
	}

	public float getRadiusInWorldUnits() {
		return desiredApeSizeInWorldUnits / 2;
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

}
