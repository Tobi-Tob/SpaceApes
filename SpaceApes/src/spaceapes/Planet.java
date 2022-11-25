package spaceapes;

import org.newdawn.slick.geom.Vector2f;
import eea.engine.entity.Entity;

public class Planet extends Entity {

	private final int size;
	private final float mass;
	private final Vector2f coordinates; // In Welt-Koordinaten
	private int amountApes = 0;

	/**
	 * Konstruktor initialisiert Planet mit zufaelligen Eigenschaften: Groesse,
	 * Masse, Ausrichtung
	 * 
	 * @param entityID
	 * @param x_y      in Welt-Koordinaten
	 */
	public Planet(String entityID, float x, float y) {
		super(entityID);
		size = (int) Utils.randomFloat(3, 10); // Integer zwischen 3 und 9
		mass = Math.round(size * Utils.randomFloat(8, 12)) / 10f; // Float zwischen 2,4 und 10,8
		coordinates = new Vector2f(x, y); // Gespeichert als Welt-Koordinaten

		setPosition(Utils.toPixelCoordinates(coordinates));
		setScale(size / 10f);
		setRotation(Utils.randomFloat(-30, 30));
	}

	public int size() {
		return size;
	}

	public float mass() {
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
		}
		else {
			return false;
		}
	}

	public void addApeToPlanet() {
		amountApes ++;
	}

}
