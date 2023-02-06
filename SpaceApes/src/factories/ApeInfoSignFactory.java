package factories;

import java.awt.Font;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Vector2f;

import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.interfaces.IEntityFactory;
import entities.Ape;
import entities.ApeInfoSign;
import utils.Utils;

public class ApeInfoSignFactory implements IEntityFactory {
	
	private final String entityID;
	private final Vector2f coordinates;
	private final float scale;
	private Ape ape;
	
	public ApeInfoSignFactory(String entityID, Vector2f coordiantes, float scale, Ape ape) {
		this.entityID = entityID;
		this.coordinates = coordiantes;
		this.scale = scale;
		this.ape = ape;
	}

	@Override
	public Entity createEntity() {

		ApeInfoSign planetPanel = new ApeInfoSign(entityID);
		
		planetPanel.setPosition(Utils.toPixelCoordinates(coordinates));
		planetPanel.setRotation(0);
		planetPanel.setScale(scale);
		planetPanel.setApe(ape);
		
		int fontSize = Math.round(scale * 160);
		planetPanel.setFontApe(new TrueTypeFont(new Font("Times New Roman", Font.BOLD, fontSize), true));
		
		fontSize = Math.round(scale * 140);
		planetPanel.setFontStats(new TrueTypeFont(new Font("Times New Roman", Font.PLAIN, fontSize), true));
		
		try {
			ImageRenderComponent imageRenderComponent = new ImageRenderComponent(new Image("img/assets/planet_panel.png"));
			planetPanel.addComponent(imageRenderComponent);
			planetPanel.setImageRenderComponent(imageRenderComponent);
		} catch (SlickException e) {
			System.err.println("Problem with planet panel image");
		}
		
		return planetPanel;
	}

}
