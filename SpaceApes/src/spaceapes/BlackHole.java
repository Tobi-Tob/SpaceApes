package spaceapes;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import eea.engine.component.render.ImageRenderComponent;

public class BlackHole extends Planet {

	public BlackHole(String entityID, float x, float y) {
		super(entityID, x, y);
		radius = Utils.randomFloat(0.4f, 0.5f);
		mass = (int) (radius * 250);
		try {
			this.addComponent(new ImageRenderComponent(new Image("/assets/blackhole1.png")));
		} catch (SlickException e) {
			System.err.println("Cannot find image for black hole");
		}
		float ScalingFactorblackhole = 1.3f;
		this.setScale(radius * ScalingFactorblackhole);
		setRotation(0);
	}

}
