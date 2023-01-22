package entities;

import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;
import eea.engine.component.render.AnimationRenderComponent;
import eea.engine.entity.Entity;
import utils.Utils;

public class AnimatedEntity extends Entity {

	private Vector2f coordinates; // In Welt-Koordinaten
	private Image[] images; // Alle weiteren Bilder sollten moeglichst die gleiche Groesse haben, wie das
							// erste (Breite und Hoehe des ersten Bilds bestimmen Render-Position)

	public AnimatedEntity(String entityID, Vector2f pos) {
		super(entityID);
		coordinates = pos;
	}

	public void setImages(Image[] frames) {
		images = frames;
		setPosition(calcPixelPosition());
	}

	public Vector2f calcPixelPosition() {
		if (images[0] == null) {
			throw new RuntimeException("Image Array is empty");
		}
		Vector2f vec = new Vector2f(images[0].getWidth() / 2, images[0].getHeight() / 2);
		return Utils.toPixelCoordinates(coordinates).sub(vec);
	}

	public void scaleAndRotateAnimation(float scaleFactor, float newAngle) {
		if (images[0] == null) {
			throw new RuntimeException("Image Array is empty");
		}
		for (int i = 0; i < images.length; i++) {
			images[i] = images[i].getScaledCopy(scaleFactor);
			images[i].setRotation(newAngle);
		}
		setPosition(calcPixelPosition());
	}

	public void addAnimation(float speed, boolean loop) {
		if (images[0] == null) {
			throw new RuntimeException("Image Array is empty");
		}
		int widthFirstImage = images[0].getWidth();
		int heightFirstImage = images[0].getHeight();
		this.addComponent(new AnimationRenderComponent(images, speed, widthFirstImage, heightFirstImage, loop));
	}

	public void setCoordinates(Vector2f pos) {
		coordinates = pos;
	}
}
