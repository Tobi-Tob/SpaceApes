package spaceapes;

import org.newdawn.slick.geom.Vector2f;
import eea.engine.entity.Entity;

public class PlanetEntity extends Entity {

	private int size;
	private float mass;
	private Vector2f position;
	private boolean hasApe = false;

	/**
	 * Konstruktor initialisiert Planet mit zufaelligen Eigenschaften: Groesse,
	 * Masse, Ausrichtung
	 * 
	 * @param entityID
	 * @param x_y      in Welt-Koordinaten
	 */
	public PlanetEntity(String entityID, float x, float y) {
		super(entityID);
		size = (int) Utils.randomFloat(3, 10); // Integer zwischen 3 und 9
		mass = Math.round(size * Utils.randomFloat(8, 12)) / 10f; // Float zwischen 2,4 und 10,8
		position = new Vector2f(x, y); // Gespeichert als Welt-Koordinaten

		setPosition(Utils.toPixelCoordinates(position));
		setScale(size / 10f);
		setRotation(Utils.randomFloat(-30, 30));
	}

	public int size() {
		return size;
	}

	public float mass() {
		return mass;
	}

	public Vector2f position() {
		return position;
	}

	public boolean hasApe() {
		return hasApe;
	}

	public void addApeToPlanet() {
		hasApe = true;
	}

}
