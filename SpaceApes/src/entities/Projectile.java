package entities;

import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.entity.Entity;
import factories.PlanetFactory.PlanetType;
import factories.ProjectileFactory.ProjectileStatus;
import factories.ProjectileFactory.ProjectileType;
import spaceapes.Map;
import spaceapes.SpaceApes;
import utils.Constants;
import utils.Resources;
import utils.Utils;

public class Projectile extends Entity {

	private double x; // x-Koordinate (double fuer erhoehte Genauigkeit)
	private double y; // y-Koordinate
	private double vx; // Geschwindigkeit in x-Richtung
	private double vy; // Geschwindigkeit in y-Richtung
	private float direction; // Winkel Spitze des Projektils gegenueber x-Achse
	// private float rotationSpeed; // Rotationsgeschwindigkeit (wird nicht benutzt)
	// private final float mass; // verschiede Massen der Geschosse moeglich (wird nicht benutzt)
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
	public ProjectileStatus explizitEulerStep(int timeDelta) {
		double dt = timeDelta * 1e-3d; // dt in Sekunden
		boolean projectileIsVisible = this.isVisible();
		// Positionsupdate:
		// X1 = X0 + dt * V0
		this.x = x + dt * vx;
		this.y = y + dt * vy;

		// ddx soll die summierten Beschleunigungsanteile aller Planeten und
		// ggf. Abbremswirkung durch Luftwiederstand enthalten
		Vector2f ddx = new Vector2f(0, 0);

		if (projectileIsVisible) {
			// Pruefe auf Kollision mit einem Affen
			for (Ape ape : Map.getInstance().getApes()) {
				if (ape.checkCollision(new Vector2f((float) x, (float) y), 0)) {
					return ProjectileStatus.hittingApe;
				}
			}
			// Pruefe auf Kollision mit einem Mond
			for (Entity moon : Map.getInstance().getMoons()) {
				Vector2f distanceVector = Utils.toWorldCoordinates(moon.getPosition()).sub(new Vector2f((float) x, (float) y));
				if (Math.pow(distanceVector.x, 2) + Math.pow(distanceVector.y, 2) < Math.pow(Constants.MOON_RADIUS, 2)) {
					return ProjectileStatus.crashingMoon;
				}
			}
			// Spiele Sound ab, wenn Projektil hohe Geschwindigkeit hat.
			if (SpaceApes.PLAY_SOUNDS && !Resources.WHOOSH_SOUND.playing() && this.vx + this.vy >= 6) {
				Resources.WHOOSH_SOUND.play();
			}
		}

		// Pruefe auf Kollision mit einem Planeten
		boolean isInAmosphere = false;
		for (Planet planet : Map.getInstance().getPlanets()) {
			Vector2f distanceVector = new Vector2f(planet.getX() - (float) x, planet.getY() - (float) y);

			if (planet.checkCollision(new Vector2f((float) x, (float) y), 0)) {
				if (planet.getPlanetType() == PlanetType.BLACKHOLE) {
					return ProjectileStatus.inBlackHole;
				} else {
					return ProjectileStatus.crashingPlanet;
				}
			}
			if (planet.hasAtmosphere()) { // Test ob sich das Projektil in einer Atmosphaere befindet
				if (distanceVector.length() < planet.getAtmosphereRadius1()) {
					isInAmosphere = true;
				}
			}
			// Aktualisiere den Beschleinigungsvektor durch die Gravitation eines Planeten
			ddx.add(distanceVector
					.scale(Constants.GRAVITATION_CONSTANT * planet.getMass() * (float) Math.pow(distanceVector.length(), -3)));
		}
		if (isInAmosphere) { // Luftbremswirkung abziehen
			Vector2f speedVector = new Vector2f((float) vx, (float) vy);
			float factor = (float) (Constants.AIR_RESISTANCE * Math.pow(speedVector.length(), 2));
			ddx.sub(speedVector.scale(factor));
		}
		// Geschwindigkeitsupdate:
		// V1 = V0 + dt * ddX
		this.vx = vx + dt * ddx.x;
		this.vy = vy + dt * ddx.y;
		// Aendern der direction in Richtung der Beschleunigung
		if (projectileIsVisible) {
			this.direction = Utils.angleInPolarCoordinates(ddx.x, ddx.y);
			setRotation(direction + 90f);
			setPosition(Utils.toPixelCoordinates((float) x, (float) y));
		}
		return ProjectileStatus.flying; // Keine Kollision
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
		List<Entity> moons = Map.getInstance().getMoons();

		// Da wir nahezu runde Objekte haben, berechnen wir die Hitbox nicht anhand des
		// png-files, da auch transparente Ecken in die Hitbox einfliessen...
		if (projectileIsVisible) {
			// Pruefe auf Kollision mit einem Affen
			for (Ape ape : apes) {
				if (ape.checkCollision(new Vector2f((float) xNew, (float) yNew), 0)) {
					return true;
				}
			}
			// Pruefe auf Kollision mit einem Mond
			for (Entity moon : moons) {
				Vector2f distanceVector = Utils.toWorldCoordinates(moon.getPosition()).sub(new Vector2f((float) x, (float) y));
				if (Math.pow(distanceVector.x, 2) + Math.pow(distanceVector.y, 2) < Math.pow(Constants.MOON_RADIUS, 2)) {
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