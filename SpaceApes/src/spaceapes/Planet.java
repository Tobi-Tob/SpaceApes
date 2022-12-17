package spaceapes;

import org.newdawn.slick.geom.Vector2f;
import eea.engine.entity.Entity;

public class Planet extends Entity {

	private final float radius;
	private final float mass;
	private final Vector2f coordinates; // In Welt-Koordinaten
	private Ape ape;

	/**
	 * Konstruktor initialisiert Planet mit zufaelligen Eigenschaften: Groesse,
	 * Masse, Ausrichtung
	 * 
	 * @param entityID String
	 * @param x        in Welt-Koordinaten
	 * @param y        in Welt-Koordinaten
	 */
	public Planet(String entityID, float x, float y) {
		super(entityID);
		radius = Utils.randomFloat(0.75f, 1.5f); // Float zwischen 0.7 und 1.5
		mass = radius * Utils.randomFloat(0.8f, 1.2f) * 80; // Float zwischen 48 und 144
		coordinates = new Vector2f(x, y); // Gespeichert als Welt-Koordinaten

		setPosition(Utils.toPixelCoordinates(coordinates));
		setRotation(Utils.randomFloat(-30, 30));
	}

	/**
	 * Bestimmt Abstand vom Mittelpunkt des Planeten zur Kreisbahn auf der sich die
	 * Affen bewegen. Ape.apeScalingFactor muss manuell angepasst werden bei Nutzung
	 * anderer Bildern
	 * 
	 * @return float in Welt-Koordinaten
	 * @throws RuntimeException wenn Radius zu klein
	 */
	public float distanceToEntityPosition() throws RuntimeException {
		float dist = getRadius() + ape.apeDistanceFromSurface;
		if (dist > 0.5f) {
			return dist;
		} else
			throw new RuntimeException("Radius ist zu klein");
	}

	public float getRadius() {
		return radius;
	}

	public float getMass() {
		return mass;
	}

	/**
	 * Position in Welt-Koordinaten
	 */
	public Vector2f getCoordinates() {
		return coordinates;
	}

	public void setApe(Ape a) {
		ape = a;
	}

	public Ape getApe() {
		return ape;
	}

}
