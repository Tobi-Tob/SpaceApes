package entities;

import java.util.List;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import map.Map;
import utils.Utils;

public class Projectile extends Entity {

	private double x; // x-Koordinate (double fuer erhoehte Genauigkeit)
	private double y; // y-Koordinate
	private double vx; // Geschwindigkeit in x-Richtung
	private double vy; // Geschwindigkeit in y-Richtung
	private float direction; // Winkel Spitze des Projektils gegenueber x-Achse
	private float rotationSpeed; // Rotationsgeschwindigkeit (wird nicht benutzt)
	private final float mass; // verschiede Massen der Geschosse moeglich (wird nicht benutzt)
	public float desiredProjectileSize = 0.3f;

	/**
	 * Konstruktor fuer ein Projektil
	 * 
	 * @param entityID String
	 * @param position Startkoordinaten
	 * @param velocity Startgeschwindigkeit
	 */
	public Projectile(String entityID) {
		super(entityID);
		this.direction = getMovementDirection();
		this.rotationSpeed = 0f;
		this.mass = 1f;

		setPosition(Utils.toPixelCoordinates((float) x, (float) y));
		float projectileSizeInPixel = 400;
		float projectileSizeInWorldUnits = Utils.pixelLengthToWorldLength(projectileSizeInPixel);
		this.setScale(desiredProjectileSize / projectileSizeInWorldUnits);
	}
	
	public void setCoordinatesWorld(Vector2f position) {
		this.x = position.x;
		this.y = position.y;
	}
	
	public void setVelocity(Vector2f velocity) {
		this.vx = velocity.x;
		this.vy = velocity.y;
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
		
		List<Planet> planets = Map.getInstance().getPlanets();
		
		//Prüfe auf Kollision mit einem Planeten
		for (int i = 0; i < planets.size(); i++) {
			float planetX = planets.get(i).getXCoordinateWorld();
			float planetY = planets.get(i).getYCoordinateWorld();
			float planetMass = planets.get(i).getMass();
			float planetRadius = planets.get(i).getRadiusWorld();
			Vector2f distanceVector = new Vector2f(planetX - (float) x, planetY - (float) y);
			// Test auf Kollision mit Planet i (durch Kreisgleichung)
			if (Math.pow(distanceVector.x, 2) + Math.pow(distanceVector.y, 2) < Math.pow(planetRadius, 2)) {
				return false; // Bei Kollision Abbruch der weiteren Berechnung
			}

			ddx.add(distanceVector.scale(G * planetMass * (float) Math.pow(distanceVector.length(), -3)));
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