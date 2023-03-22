package factories;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import eea.engine.action.basicactions.RotateRightAction;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.basicevents.LoopEvent;
import eea.engine.interfaces.IEntityFactory;
import entities.Item;
import spaceapes.Constants;
import spaceapes.Launch;
import utils.Utils;

public class ItemFactory implements IEntityFactory {

	public enum ItemType {
		COPPER_COIN, GOLD_COIN, DIAMOND_COIN, HEALTH_PACK, ENERGY_PACK
	};

	private String iD;
	private final ItemType type;
	private final Vector2f coordinates;

	public ItemFactory(ItemType type, Vector2f coordinates) {
		this.type = type;
		this.coordinates = coordinates;
	}

	@Override
	public Item createEntity() {
		
		float itemWidthInPixel;
		float desiredItemWidth;
		float itemScaleFactor=999; // just init values
		int value=999; // just init values
		ImageRenderComponent imageRenderComponent=null; // just init values
		try {
			
			switch (type) {
			
			case ENERGY_PACK:
				this.iD = Constants.ENERGY_PACK_ID;
				itemWidthInPixel = 100;
				desiredItemWidth = 0.03f; // im Verhaeltnis zur Fenster Breite
				itemScaleFactor = desiredItemWidth * Launch.WIDTH / itemWidthInPixel;
				value = Constants.ENERGY_PACK_VALUE; 
				if (Launch.renderImages) {
					imageRenderComponent = new ImageRenderComponent(new Image(Constants.ENERGY_IMAGE_PATH));
				} else {
					//System.out.println("noRenderImages: assign energy pack image.");
				}
				break;
				
			case HEALTH_PACK:
				this.iD = Constants.HEALTH_PACK_ID;
				itemWidthInPixel = 100;
				desiredItemWidth = 0.03f; // im Verhaeltnis zur Fenster Breite
				itemScaleFactor = desiredItemWidth * Launch.WIDTH / itemWidthInPixel;
				value = Constants.HEALTH_PACK_VALUE;
				if (Launch.renderImages) {
					imageRenderComponent = new ImageRenderComponent(new Image(Constants.HEALTH_IMAGE_PATH));
				} else {
					//System.out.println("noRenderImages: assign health pack image.");
				}
				break;

			case COPPER_COIN:
				this.iD = Constants.COIN_ID;
				itemWidthInPixel = 100;
				desiredItemWidth = 0.03f; // im Verhaeltnis zur Fenster Breite
				itemScaleFactor = desiredItemWidth * Launch.WIDTH / itemWidthInPixel;
				value = Constants.COPPER_COIN_VALUE;
				if (Launch.renderImages) {
					imageRenderComponent = new ImageRenderComponent(new Image(Constants.COPPER_COIN_IMAGE_PATH));
				} else {
					//System.out.println("noRenderImages: assign copper coin image.");
				}
				break;

			case GOLD_COIN:
				this.iD = Constants.COIN_ID;
				itemWidthInPixel = 100;
				desiredItemWidth = 0.03f; // im Verhaeltnis zur Fenster Breite
				itemScaleFactor = desiredItemWidth * Launch.WIDTH / itemWidthInPixel;
				value = Constants.GOLD_COIN_VALUE;
				if (Launch.renderImages) {
					imageRenderComponent = new ImageRenderComponent(new Image(Constants.GOLD_COIN_IMAGE_PATH));
				} else {
					//System.out.println("noRenderImages: assign gold coin image.");
				}
				break;

			default: // ansonsten erzeugt er immer einen DIAMANT_COIN
				this.iD = Constants.COIN_ID;
				itemWidthInPixel = 100;
				desiredItemWidth = 0.03f; // im Verhaeltnis zur Fenster Breite
				itemScaleFactor = desiredItemWidth * Launch.WIDTH / itemWidthInPixel;
				value = Constants.DIAMOND_COIN_VALUE;
				if (Launch.renderImages) {
					imageRenderComponent = new ImageRenderComponent(new Image(Constants.DIAMOND_COIN_IMAGE_PATH));
				} else {
					//System.out.println("noRenderImages: assign diamond coin image.");
				}
				break;
				
			}
			
		} catch (SlickException e) {
			System.err.println("Problem with item image");
		}
		
		Item item = new Item(iD, type);
		// Setze alle Parameter des Items
		item.setScale(itemScaleFactor);
		item.setPosition(Utils.toPixelCoordinates(coordinates));
		item.setValue(value);
		
		// Events des Items
		LoopEvent itemLoop = new LoopEvent();
		itemLoop.addAction(new RotateRightAction(0.03f));
		item.addComponent(itemLoop);
		
		if (Launch.renderImages) {
			item.addComponent(imageRenderComponent);
		}
		
		// Füge das Item dem EntityManager hinzu
		StateBasedEntityManager.getInstance().addEntity(Launch.GAMEPLAY_STATE, item);

		return item;
	}

}
