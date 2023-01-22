package entities;

import org.newdawn.slick.geom.Vector2f;

import eea.engine.entity.Entity;
import utils.Utils;

public class Planet extends Entity {

	private float radius;
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
		float dist = getRadius() + Utils.pixelLengthToWorldLength(ape.pixelfromFeetToCenter * ape.scalingFactor);
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

	public void setMass(int mass) {
		this.mass = mass;
	}
	
	public int getMass() {
		return mass;
	}

	/**
	 * Position in Welt-Koordinaten
	 */
	public Vector2f getPositionWorldCoordinates() {
		return Utils.toWorldCoordinates(getPosition());
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

}
