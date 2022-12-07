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
	private final float projectileScalingFactor = 0.4f; // Faktor der das Bild skaliert

	public final List<float[]> planetData; // Alle benoetigten Daten fuer die Bahnberechnung (x, y, Masse der Planeten)
											// sind hier gespeichert

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
		this.mass = 1f;
		this.planetData = planetData;

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
	 */
	public void explizitEulerStep(int timeDelta) {
		double dt = timeDelta * 1e-3d; // dt in Sekunden
		// Positionsupdate:
		// X1 = X0 + dt * V0
		double x_new = x + dt * vx;
		double y_new = y + dt * vy;
		// Geschwindigkeitsupdate:
		// V1 = V0 + dt * ddX
		Vector2f sum = new Vector2f(0, 0);

		for (int i = 0; i < planetData.size(); i++) {
			float p_x = planetData.get(i)[0];
			float p_y = planetData.get(i)[1];
			float p_mass = planetData.get(i)[2];
			Vector2f distanceVector = new Vector2f(p_x - (float) x, p_y - (float) y);

			sum.add(distanceVector.scale(p_mass * (float) Math.pow(distanceVector.length(), -3)));
		}
		this.x = x_new;
		this.y = y_new;
		float G = 0.2f; // Gravitationskonstante (frei waehlbar)
		this.vx = vx + dt * G * sum.x;
		this.vy = vy + dt * G * sum.y;

		setPosition(Utils.toPixelCoordinates((float) x, (float) y));
	}

}