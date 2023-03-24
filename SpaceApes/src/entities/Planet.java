package entities;

import org.newdawn.slick.geom.Vector2f;

import eea.engine.entity.Entity;
import factories.PlanetFactory.PlanetType;
import spaceapes.Constants;
import utils.Utils;

public class Planet extends Entity {

	private float radius; // Radius in Weltkoordinaten
	private int mass;
	private float atmosphereRadiusFactor = 5f; // atmosphere radius = atmosphereRadiusFactor * radius of planet
	private Vector2f coordinates; // in Weltkoordinaten
	private Ape ape;
	private PlanetType planetType;

	public Planet(String entityID) {
		super(entityID);
	}

	/**
	 * Bestimmt Abstand vom Mittelpunkt des Planeten zur Kreisbahn auf der sich die
	 * Affen bewegen. Ape.apeScalingFactor muss manuell angepasst werden bei Nutzung
	 * anderer Bildern
	 * 
	 * @return float in Welt-Koordinaten
	 * @throws RuntimeException wenn Radius zu klein
	 */
	public float distanceToApePosition() {
		float dist = getRadius() + Utils.pixelLengthToWorldLength(Constants.APE_PIXEL_FEET_TO_CENTER * ape.getScalingFactor());
		if (dist > 0.1f) {
			return dist;
		} else
			throw new RuntimeException("Radius ist zu nah an null");
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public float getRadius() {
		return radius;
	}
	
	public void setAtmosphereRadiusFactor(float factor) {
		this.atmosphereRadiusFactor = factor;
	}
	
	public float getAtmosphereRadius() {
		return radius * atmosphereRadiusFactor;
	}

	public void setMass(int mass) {
		this.mass = mass;
	}

	public int getMass() {
		return mass;
	}

	/**
	 * Position in Welt-Koordinaten
	 */
	public Vector2f getCoordinates() {
		return coordinates;
	}

	public float getX() {
		return coordinates.x;
	}

	public float getY() {
		return coordinates.y;
	}

	public void setCoordinates(Vector2f coordinates) {
		this.coordinates = coordinates;
	}
	
	public void setPlanetType(PlanetType type) {
		planetType = type;
	}

	public PlanetType getPlanetType() {
		return planetType;
	}

	public void setApe(Ape a) {
		ape = a;
	}

	public Ape getApe() {
		return ape;
	}

	public boolean hasApe() {
		if (this.ape == null) {
			return false;
		}
		return true;
	}

	/**
	 * Diese Methode prueft, ob die uebergebene Position innerhalb des Planeten plus
	 * eine frei waehlbare Margin liegt und somit Kollision vorliegt (Test durch
	 * Kreisgleichung mit dem Radius: getRadius() + margin)
	 * 
	 * @param pos    Koordinaten des zu pruefenden Punktes
	 * @param margin manuelle vergoesserung der Kollisionsflaeche
	 * @return true, wenn eine Kollision vorliegt, ansonsten false
	 */
	public boolean checkCollision(Vector2f pos, float margin) {
		Vector2f distanceVector = new Vector2f(getCoordinates()).sub(pos);
		if (Math.pow(distanceVector.x, 2) + Math.pow(distanceVector.y, 2) < Math.pow(getRadius() + margin, 2)) {
			return true;
		}
		return false;
	}

}
