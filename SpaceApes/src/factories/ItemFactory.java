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
import spaceapes.SpaceApes;
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
				itemScaleFactor = desiredItemWidth * SpaceApes.WIDTH / itemWidthInPixel;
				value = Constants.ENERGY_PACK_VALUE; 
				if (SpaceApes.renderImages) {
					imageRenderComponent = new ImageRenderComponent(new Image("img/items/energy.png"));
				}
				break;
				
			case HEALTH_PACK:
				this.iD = Constants.HEALTH_PACK_ID;
				itemWidthInPixel = 100;
				desiredItemWidth = 0.03f; // im Verhaeltnis zur Fenster Breite
				itemScaleFactor = desiredItemWidth * SpaceApes.WIDTH / itemWidthInPixel;
				value = Constants.HEALTH_PACK_VALUE;
				if (SpaceApes.renderImages) {
					imageRenderComponent = new ImageRenderComponent(new Image("img/items/health.png"));
				}
				break;

			case COPPER_COIN:
				this.iD = Constants.COIN_ID;
				itemWidthInPixel = 100;
				desiredItemWidth = 0.03f; // im Verhaeltnis zur Fenster Breite
				itemScaleFactor = desiredItemWidth * SpaceApes.WIDTH / itemWidthInPixel;
				value = Constants.COPPER_COIN_VALUE;
				if (SpaceApes.renderImages) {
					imageRenderComponent = new ImageRenderComponent(new Image("img/items/coin1.png"));
				}
				break;

			case GOLD_COIN:
				this.iD = Constants.COIN_ID;
				itemWidthInPixel = 100;
				desiredItemWidth = 0.03f; // im Verhaeltnis zur Fenster Breite
				itemScaleFactor = desiredItemWidth * SpaceApes.WIDTH / itemWidthInPixel;
				value = Constants.GOLD_COIN_VALUE;
				if (SpaceApes.renderImages) {
					imageRenderComponent = new ImageRenderComponent(new Image("img/items/coin2.png"));
				}
				break;

			default: // ansonsten erzeugt er immer einen DIAMANT_COIN
				this.iD = Constants.COIN_ID;
				itemWidthInPixel = 100;
				desiredItemWidth = 0.03f; // im Verhaeltnis zur Fenster Breite
				itemScaleFactor = desiredItemWidth * SpaceApes.WIDTH / itemWidthInPixel;
				value = Constants.DIAMOND_COIN_VALUE;
				if (SpaceApes.renderImages) {
					imageRenderComponent = new ImageRenderComponent(new Image("img/items/coin3.png"));
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
		
		if (SpaceApes.renderImages) {
			item.addComponent(imageRenderComponent);
		}
		
		// Fuege das Item dem EntityManager hinzu
		StateBasedEntityManager.getInstance().addEntity(SpaceApes.GAMEPLAY_STATE, item);

		return item;
	}

}
