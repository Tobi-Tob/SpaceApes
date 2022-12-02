package spaceapes;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;

public class Projectile extends Entity {

	private Vector2f coordinates; // In Welt-Koordinaten
	private float direction; // Winkel in Welt-Koordinaten
	private float velocity; // Geschwindigkeit
	private final float mass; // verschiede Massen der Geschosse moeglich
	private final float projectileScalingFactor = 0.4f;

	/**
	 * Konstruktor fuer ein Projektil
	 * 
	 * @param entityID String
	 * @param x        in Welt-Koordinaten
	 * @param y        in Welt-Koordinaten
	 * @param alpha    Winkel in Welt-Koordinaten
	 */
	public Projectile(String entityID, Vector2f position, float rotation, float speed) {
		super(entityID);
		coordinates = new Vector2f(position); // Gespeichert als Welt-Koordinaten
		direction = rotation;
		velocity = speed;
		mass = 1f;

		setPosition(Utils.toPixelCoordinates(coordinates));
		setScale(projectileScalingFactor);
		setRotation(direction);
		try {
			addComponent(new ImageRenderComponent(new Image("/assets/coconut.png")));
		} catch (SlickException e) {
			System.err.println("Cannot find file assets/coconut.png");
			e.printStackTrace();
		}
	}

	public Vector2f getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(float x, float y) {
		coordinates = new Vector2f(x, y);
	}

	public float getDirection() {
		return direction;
	}

	public void setDirection(float dir) {
		direction = dir;
	}

	public float getVelocity() {
		return velocity;
	}

	public void setVelocity(float velocity) {
		this.velocity = velocity;
	}

}