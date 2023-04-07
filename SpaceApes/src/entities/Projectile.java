package entities;

import java.util.List;

import org.newdawn.slick.geom.Vector2f;
import eea.engine.entity.Entity;
import factories.ProjectileFactory.ProjectileType;
import map.Map;
import spaceapes.Constants;
import utils.Utils;

public class Projectile extends Entity {

	private double x; // x-Koordinate (double fuer erhoehte Genauigkeit)
	private double y; // y-Koordinate
	private double vx; // Geschwindigkeit in x-Richtung
	private double vy; // Geschwindigkeit in y-Richtung
	private float direction; // Winkel Spitze des Projektils gegenueber x-Achse
	private float rotationSpeed; // Rotationsgeschwindigkeit (wird nicht benutzt)
	private final float mass; // verschiede Massen der Geschosse moeglich (wird nicht benutzt)
	private ProjectileType type;
	private int price = 0; // default
	private int maxDamage = 10; // default
	private float damageRadius = 0.5f; // default
	public float desiredProjectileSize = 0.5f; // default

	/**
	 * Konstruktor fuer ein Projektil
	 * 
	 * @param entityID String
	 * @param position Startkoordinaten
	 * @param velocity Startgeschwindigkeit
	 */
	public Projectile(String entityID, Vector2f coordinates, Vector2f velocity) {
		super(entityID);
		this.setCoordinates(coordinates);
		this.setVelocity(velocity);
		this.direction = getMovementDirection();
		this.rotationSpeed = 0f;
		this.mass = 1f;

		setPosition(Utils.toPixelCoordinates((float) x, (float) y));
	}

	public void setVelocity(Vector2f velocity) {
		this.vx = velocity.x;
		this.vy = velocity.y;
	}

	public void setCoordinates(Vector2f coordinates) {
		this.x = coordinates.x;
		this.y = coordinates.y;
	}

	public Vector2f getCoordinates() {
		return new Vector2f((float) x, (float) y);
	}

	public void setDesiredProjectileSize(float size) {
		this.desiredProjectileSize = size;
	}

	public float getRadiusInWorldUnits() {
		return desiredProjectileSize / 2;
	}

	public ProjectileType getType() {
		return type;
	}

	public void setType(ProjectileType type) {
		this.type = type;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public void setMaxDamage(int damage) {
		this.maxDamage = damage;
	}

	public int getMaxDamage() {
		return maxDamage;
	}

	public void setDamageRadius(float radius) {
		this.damageRadius = radius;
	}

	public float getDamageRadius() {
		return damageRadius;
	}

	/*
	 * Gibt Ausrichtung des Projektils als Winkel zurueck
	 */
	public float getMovementDirection() {
		return Utils.angleInPolarCoordinates((float) vx, (float) vy);
	}

	/**
	 * Berechnet die neue Position des Projektils abhaengig von seiner
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
	 * Berechnet die neue Position des Projektils abhaengig von seiner
	 * Geschwindigkeit, Anziehungskraefte der Planeten und der verstrichenen Zeit in
	 * ms
	 * 
	 * @param timeDelta int in Millisekunden
	 * @return true, falls eine Kollision mit einem Planeten/Affen in diesem Schritt
	 *         vorliegt
	 */
	public boolean explizitEulerStep(int timeDelta, boolean useAirFriction) {
		double dt = timeDelta * 1e-3d; // dt in Sekunden
		float G = Constants.GRAVITATION_CONSTANT;
		boolean projectileIsVisible = this.isVisible();
		// Positionsupdate:
		// X1 = X0 + dt * V0
		double xNew = x + dt * vx;
		double yNew = y + dt * vy;
		// Geschwindigkeitsupdate:
		// V1 = V0 + dt * ddX
		Vector2f ddx = new Vector2f(0, 0);

		List<Planet> planets = Map.getInstance().getPlanets();
		List<Ape> apes = Map.getInstance().getApes();

		// Da wir nahezu runde Objekte haben, berechnen wir die Hitbox nicht anhand des
		// png-files, da auch transparente Ecken in die Hitbox einfliessen...
		// Pruefe auf Kollision mit einem Affen
		if (projectileIsVisible) {
			for (Ape ape : apes) {
				if (ape.checkCollision(new Vector2f((float) xNew, (float) yNew), 0)) {
					return true;
				}
			}
		}

		// Pruefe auf Kollision mit einem Planeten
		for (Planet planet : planets) {
			Vector2f distanceVector = new Vector2f(planet.getX() - (float) x, planet.getY() - (float) y);
			// Wenn man xNew und yNew nimmt fuer Berechnung von ddx: implizites Verhalten?
			if (planet.checkCollision(new Vector2f((float) xNew, (float) yNew), 0)) {
				return true;
			}

			// aktualisiere den Beschleinigungsvektor durch die neue Gravitation des
			// Planeten und ggf. durch den Luftwiederstand in der Atmosphaere
			if (!useAirFriction) {
				ddx.add(distanceVector.scale(G * planet.getMass() * (float) Math.pow(distanceVector.length(), -3)));
			} else {
				float airFrictionAcceleration = 0;
				if (planet.hasAtmosphere() && distanceVector.length() < planet.getAtmosphereRadius1()) {
					float airDensity = 1.293f;
					float C = 2.1f;
					float halfSurfaceArea = (float) (2 * Math.PI * Math.pow(getRadiusInWorldUnits(), 2));
					Vector2f airSpeed = new Vector2f(0, 0); // TODO vllt Berechnung vereinfachen
					Vector2f relativeSpeedToAirSpeed = new Vector2f((float) vx - airSpeed.x, (float) vy - airSpeed.y);
					airFrictionAcceleration = (float) (0.5 * airDensity * C * halfSurfaceArea
							* Math.pow(relativeSpeedToAirSpeed.length(), 2));
				}
				ddx.add(distanceVector
						.scale(G * planet.getMass() * (float) Math.pow(distanceVector.length(), -3) + airFrictionAcceleration)); // TODO
																																	// warum
																																	// +
																																	// airFrictionAcceleration
			}
		}
		// ddx enthaelt nun die summierten Beschleunigungsanteile aller Planeten

		this.x = xNew;
		this.y = yNew;
		this.vx = vx + dt * ddx.x;
		this.vy = vy + dt * ddx.y;
		// Aendern der direction in Richtung der Beschleunigung
		if (projectileIsVisible) {
			this.direction = Utils.angleInPolarCoordinates(ddx.x, ddx.y);
			setRotation(direction + 90f);
			setPosition(Utils.toPixelCoordinates((float) x, (float) y));
		}
		return false; // Keine Kollision
	}

	/**
	 * Berechnet die neue Position des Projektils fuer eine linearer Flugbahn
	 * 
	 * @param timeDelta int in Millisekunden
	 * @return true, falls eine Kollision mit einem Planeten/Affen in diesem Schritt
	 *         vorliegt
	 */
	public boolean linearMovementStep(int timeDelta) {
		double dt = timeDelta * 1e-3d; // dt in Sekunden
		boolean projectileIsVisible = this.isVisible();
		// Positionsupdate:
		// X1 = X0 + dt * V0
		double xNew = x + dt * vx;
		double yNew = y + dt * vy;

		List<Planet> planets = Map.getInstance().getPlanets();
		List<Ape> apes = Map.getInstance().getApes();

		// Da wir nahezu runde Objekte haben, berechnen wir die Hitbox nicht anhand des
		// png-files, da auch transparente Ecken in die Hitbox einfliessen...

		// Pruefe auf Kollision mit einem Affen
		if (projectileIsVisible) {
			for (Ape ape : apes) {
				if (ape.checkCollision(new Vector2f((float) xNew, (float) yNew), 0)) {
					return true;
				}
			}
		}

		// Pruefe auf Kollision mit einem Planeten
		for (Planet planet : planets) {
			if (planet.checkCollision(new Vector2f((float) xNew, (float) yNew), 0)) {
				return true;
			}
		}

		this.x = xNew;
		this.y = yNew;
		// this.vx = vx; // no update necessary
		// this.vy = vy; // no update necessary

		setRotation(direction + 90f);
		setPosition(Utils.toPixelCoordinates((float) x, (float) y));
		return false; // Keine Kollision
	}

}