package spaceapes;

import java.util.List;

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
	private final float mass; // verschiede Massen der Geschosse moeglich (wird nicht benutzt)
	private final float projectileScalingFactor = 0.07f; // Faktor der das Bild skaliert

	public final List<float[]> planetData; // Alle benoetigten Daten fuer die Bahnberechnung (x, y, Masse, Radius) sind
											// hier gespeichert

	/**
	 * Konstruktor fuer ein Projektil
	 * 
	 * @param entityID String
	 * @param position Startkoordinaten
	 * @param velocity Startgeschwindigkeit
	 */
	public Projectile(String entityID, Vector2f position, Vector2f velocity, List<float[]> planetData) {
		super(entityID);
		this.x = position.x;
		this.y = position.y;
		this.vx = velocity.x;
		this.vy = velocity.y;
		this.direction = getMovementDirection();
		this.rotationSpeed = 0f;
		this.mass = 1f;
		this.planetData = planetData;

		setPosition(Utils.toPixelCoordinates((float) x, (float) y));
		setScale(projectileScalingFactor);
	}

	public Vector2f getCoordinates() {
		return new Vector2f((float) x, (float) y);
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
	public void followVeleocityStep(int timeDelta) {
		double dt = timeDelta * 1e-3d; // dt in Sekunden
		this.x = x + dt * vx; // X1 = X0 + dX, dX = dt * V0
		this.y = y + dt * vy;
		setPosition(Utils.toPixelCoordinates((float) x, (float) y));
	}

	/**
	 * Berechnet die Neue Position des Projektils abhaengig von seiner
	 * Geschwindigkeit, Anziehungskraefte der Planeten und der verstrichenen Zeit in
	 * ms
	 * 
	 * @param timeDelta int in Millisekunden
	 * @return true, falls keine Kollision mit einem Planeten in diesem Schritt
	 *         vorliegt
	 */
	public boolean explizitEulerStep(int timeDelta) {
		double dt = timeDelta * 1e-3d; // dt in Sekunden
		// Positionsupdate:
		// X1 = X0 + dt * V0
		double x_new = x + dt * vx;
		double y_new = y + dt * vy;
		// Geschwindigkeitsupdate:
		// V1 = V0 + dt * ddX
		Vector2f ddx = new Vector2f(0, 0);
		float G = 0.25f; // Gravitationskonstante (frei waehlbar)

		for (int i = 0; i < planetData.size(); i++) {
			float p_x = planetData.get(i)[0];
			float p_y = planetData.get(i)[1];
			float p_mass = planetData.get(i)[2];
			float p_radius = planetData.get(i)[3];
			Vector2f distanceVector = new Vector2f(p_x - (float) x, p_y - (float) y);
			// Test auf Kollision mit Planet i (durch Kreisgleichung)
			if (Math.pow(distanceVector.x, 2) + Math.pow(distanceVector.y, 2) < Math.pow(p_radius, 2)) {
				return false; // Bei Kollision Abbruch der weiteren Berechnung
			}

			ddx.add(distanceVector.scale(G * p_mass * (float) Math.pow(distanceVector.length(), -3)));
		}
		this.x = x_new;
		this.y = y_new;
		this.vx = vx + dt * ddx.x;
		this.vy = vy + dt * ddx.y;
		// Aendern der direction in Richtung der Beschleunigung
		this.direction = Utils.angleInPolarCoordinates(ddx.x, ddx.y);
		setRotation(direction + 90f);

		setPosition(Utils.toPixelCoordinates((float) x, (float) y));
		return true; // Keine Kollision
	}

}