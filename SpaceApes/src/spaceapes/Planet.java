package spaceapes;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;

public class Planet extends Entity {

	private float radius;
	private int mass;
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
		radius = Utils.randomFloat(0.75f, 1.5f); // Float zwischen 0.75 und 1.5
		mass = (int) (radius * Utils.randomFloat(0.91f, 1.1f) * 65); // Integer zwischen 44 und 107 (Im Mittel 73)
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
		if (dist > 0.1f) {
			return dist;
		} else
			throw new RuntimeException("Radius ist zu klein");
	}

	public float getRadius() {
		return radius;
	}

	public int getMass() {
		return mass;
	}

	public void changeToBlackHole() {
		radius = Utils.randomFloat(0.4f, 0.5f);
		mass = (int) (radius * 250);
		try {
			this.addComponent(new ImageRenderComponent(new Image("/assets/blackhole1.png")));
		} catch (SlickException e) {
			System.err.println("Cannot find image for black hole");
		}
		float scalingFactorBlackhole = 1.3f;
		this.setScale(radius * scalingFactorBlackhole);
		setRotation(0);
	}

	public void changeToAntiPlanet() {
		mass = (int) (-0.3f * mass);
		try {
			this.addComponent(new ImageRenderComponent(new Image("/assets/planet_anti1.png")));
		} catch (SlickException e) {
			System.err.println("Cannot find image for planet");
		}
		float ScalingFactorPlanetAnti1 = 0.325f;
		this.setScale(radius * ScalingFactorPlanetAnti1);
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
