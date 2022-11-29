package spaceapes;

import org.newdawn.slick.geom.Vector2f;
import eea.engine.entity.Entity;

public class Planet extends Entity {

	private final float radius;
	private final float mass;
	private final Vector2f coordinates; // In Welt-Koordinaten
	private int amountApes = 0;

	private final float scalingFactor = 0.5f; // So justieren, dass ein Planet bei (0,0) und Radius 6 genau den oberen und
												// unteren Rand des Bildes beruehrt

	/**
	 * Konstruktor initialisiert Planet mit zufaelligen Eigenschaften: Groesse,
	 * Masse, Ausrichtung
	 * 
	 * @param entityID String
	 * @param x in Welt-Koordinaten
	 * @param y in Welt-Koordinaten
	 */
	public Planet(String entityID, float x, float y) {
		super(entityID);
		radius = Utils.randomFloat(0.75f, 1.5f); // Float zwischen 0.7 und 1.5
		mass = radius * Utils.randomFloat(0.8f, 1.2f) * 6; // Float zwischen 3,6 und 10,8
		coordinates = new Vector2f(x, y); // Gespeichert als Welt-Koordinaten

		setPosition(Utils.toPixelCoordinates(coordinates));
		setScale(radius * scalingFactor);
		setRotation(Utils.randomFloat(-30, 30));
	}

	/**
	 * Bestimmt Abstand vom Mittelpunkt des Planeten zur Kreisbahn auf der sich alle
	 * Entitaeten bewegen. Interner Faktor a muss manuell angepasst werden bei Nutzung
	 * anderer Bildern
	 * 
	 * @return float in Welt-Koordinaten
	 * @throws RuntimeException wenn Radius zu klein
	 */
	public float distanceToEntityPosition() throws RuntimeException {
		float a = 0.2f; // Faktor a Abhaengig von Groesse und Skalierung des Affenbilds
		float dist = getRadius() + a;
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

	public boolean hasApes() {
		if (amountApes > 0) {
			return true;
		} else {
			return false;
		}
	}

	public void addApeToPlanet() {
		amountApes++;
	}

}
