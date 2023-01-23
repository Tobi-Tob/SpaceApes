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
	
	public float getRadiusWorld() {
		return desiredProjectileSize / 2;
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
		double xNew = x + dt * vx;
		double yNew = y + dt * vy;
		// Geschwindigkeitsupdate:
		// V1 = V0 + dt * ddX
		Vector2f ddx = new Vector2f(0, 0);
		float G = 0.25f; // Gravitationskonstante (frei waehlbar)
		
		List<Planet> planets = Map.getInstance().getPlanets();
		List<Ape> apes = Map.getInstance().getApes();
		
		// Da wir nahezu runde Objekte haben, berechnen wir die Hitbox nicht anhand des png-files, da auch
		// transparente Ecken in die Hitbox einfließen...
		
		// Prüfe auf Kollision mit einem Planeten
		for (Ape ape : apes) {
			if (ape.checkCollision(this)) {
				return false;
			}
		}
		
		// Prüfe auf Kollision mit einem Planeten
		for (Planet planet : planets) {
			Vector2f distanceVector = new Vector2f(planet.getXCoordinateWorld() - (float) x, planet.getYCoordinateWorld() - (float) y);
			if (planet.checkCollision((float) xNew, (float) yNew)) {
				return false;
			}

			// aktualisiere den Beschleinigungsvektor durch die neue Gravitation des Planeten
			ddx.add(distanceVector.scale(G * planet.getMass() * (float) Math.pow(distanceVector.length(), -3)));
		}
		
		this.x = xNew;
		this.y = yNew;
		this.vx = vx + dt * ddx.x;
		this.vy = vy + dt * ddx.y;
		// Aendern der direction in Richtung der Beschleunigung
		this.direction = Utils.angleInPolarCoordinates(ddx.x, ddx.y);
		setRotation(direction + 90f);

		setPosition(Utils.toPixelCoordinates((float) x, (float) y));
		return true; // Keine Kollision
	}

}