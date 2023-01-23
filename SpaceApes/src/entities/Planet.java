package entities;

import org.newdawn.slick.geom.Vector2f;

import eea.engine.entity.Entity;
import utils.Utils;

public class Planet extends Entity {

	private float radius; //Radius in Weltkoordinaten
	private int mass;
	private Ape ape;

	public Planet(String entityID) {
		super(entityID);
		radius = 0;
		mass = 0;
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
		float dist = getRadiusWorld() + Utils.pixelLengthToWorldLength(ape.pixelfromFeetToCenter * ape.scalingFactor);
		if (dist > 0.1f) {
			return dist;
		} else
			throw new RuntimeException("Radius ist zu nah an null");
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}
	
	public float getRadiusWorld() {
		return radius;
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
	public Vector2f getPositionWorld() {
		return Utils.toWorldCoordinates(getPosition());
	}
	
	public float getXCoordinateWorld() {
		return getPositionWorld().x;
	}
	
	public float getYCoordinateWorld() {
		return getPositionWorld().y;
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
	 * Diese Methode prueft, ob die uebergebenen Koordinaten innerhab des Planeten liegen und somit eine Kollision vorliegt
	 * @param x x-Koordinate des zu pruefenden Punktes
	 * @param y y-Koordinate des zu pruefenden Punktes
	 * @return true, wenn eine Kollision vorliegt, ansonsten false
	 */
	public boolean checkCollision(float x, float y) {
		Vector2f distanceVector = new Vector2f(getXCoordinateWorld() - x, getYCoordinateWorld() - y);
		// Test auf Kollision mit Planet i (durch Kreisgleichung)
		if (Math.pow(distanceVector.x, 2) + Math.pow(distanceVector.y, 2) < Math.pow(getRadiusWorld(), 2)) {
			return true;
		}
		return false;
	}

}
