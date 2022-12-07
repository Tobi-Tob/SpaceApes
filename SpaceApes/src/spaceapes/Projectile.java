package spaceapes;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;

public class Projectile extends Entity {

	private double x; // x-Koordinate (double fuer erhoehte Genauigkeit)
	private double y; // y-Koordinate
	private double vx; // Geschwindigkeit in x-Richtung
	private double vy; // Geschwindigkeit in y-Richtung
	private float direction; // Winkel Spitze des Projektils gegenueber x-Achse
	private float rotationSpeed; // Rotationsgeschwindigkeit (wird nicht benutzt)
	private final float mass; // verschiede Massen der Geschosse moeglich
	private final float projectileScalingFactor = 0.4f; // Faktor der das Bild skaliert

	/**
	 * Konstruktor fuer ein Projektil
	 * @param entityID String
	 * @param position Startkoordinaten
	 * @param velocity Startgeschwindigkeit
	 */
	public Projectile(String entityID, Vector2f position, Vector2f velocity) {
		super(entityID);
		this.x = position.x;
		this.y = position.y;
		this.vx = velocity.x;
		this.vy = velocity.y;
		this.direction = getMovementDirection();
		mass = 1f;

		setPosition(Utils.toPixelCoordinates((float) x, (float) y));
		setScale(projectileScalingFactor);
		setRotation(direction + 90f);
		try {
			addComponent(new ImageRenderComponent(new Image("/assets/coconut.png")));
		} catch (SlickException e) {
			System.err.println("Cannot find file assets/coconut.png");
			e.printStackTrace();
		}
	}

	/*
	 * Gibt Ausrichtung des Projektils als Winkel zurueck
	 */
	public float getMovementDirection() {
		return Utils.angleInPolarCoordinates((float) vx, (float) vy);
	}

	/**
	 * Berechnet die Neue Position des Projektils abhaengig von seiner
	 * Geschwindigkeit und der verstrichenen Zeit in ms
	 * 
	 * @param timeDelta int in Millisekunden
	 */
	public void moveStepInStraightLine(int timeDelta) {
		double dt = timeDelta * 1e-3d;
		this.x = x + vx * dt; // x1 = x0 + dx, dx = v0 * dt
		this.y = y + vy * dt;
		setPosition(Utils.toPixelCoordinates((float) x, (float) y));
	}

}