package factories;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import eea.engine.action.basicactions.RotateRightAction;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.basicevents.LoopEvent;
import entities.Item;
import utils.Constants;
import spaceapes.Map;
import spaceapes.SpaceApes;
import utils.Utils;

public abstract class ItemFactory {

	public enum ItemType {
		COPPER_COIN, GOLD_COIN, DIAMOND_COIN, HEALTH_PACK, ENERGY_PACK
	};

	/**
	 * This method is able to create all item types with their images. All items are
	 * already assigned to the EntityManager and the Map.
	 * 
	 * @param type ItemType: COPPER_COIN, GOLD_COIN, DIAMOND_COIN, HEALTH_PACK, ENERGY_PACK
	 * @param coordinates Vector2f in world units
	 * @return Item
	 */
	public static Item createItem(ItemType type, Vector2f coordinates) {

		String iD = null;
		float itemWidthInPixel;
		float desiredItemWidth;
		float itemScaleFactor = 999; // just init values
		int value = 999; // just init values
		ImageRenderComponent imageRenderComponent = null; // just init values
		try {

			switch (type) {

			case ENERGY_PACK:
				iD = Constants.ENERGY_PACK_ID;
				itemWidthInPixel = 100;
				desiredItemWidth = 0.03f; // im Verhaeltnis zur Fenster Breite
				itemScaleFactor = desiredItemWidth * SpaceApes.WIDTH / itemWidthInPixel;
				value = Constants.ENERGY_PACK_VALUE;
				if (SpaceApes.renderImages) {
					imageRenderComponent = new ImageRenderComponent(new Image("img/items/energy.png"));
				}
				break;

			case HEALTH_PACK:
				iD = Constants.HEALTH_PACK_ID;
				itemWidthInPixel = 100;
				desiredItemWidth = 0.03f; // im Verhaeltnis zur Fenster Breite
				itemScaleFactor = desiredItemWidth * SpaceApes.WIDTH / itemWidthInPixel;
				value = Constants.HEALTH_PACK_VALUE;
				if (SpaceApes.renderImages) {
					imageRenderComponent = new ImageRenderComponent(new Image("img/items/health.png"));
				}
				break;

			case COPPER_COIN:
				iD = Constants.COIN_ID;
				itemWidthInPixel = 100;
				desiredItemWidth = 0.03f; // im Verhaeltnis zur Fenster Breite
				itemScaleFactor = desiredItemWidth * SpaceApes.WIDTH / itemWidthInPixel;
				value = Constants.COPPER_COIN_VALUE;
				if (SpaceApes.renderImages) {
					imageRenderComponent = new ImageRenderComponent(new Image("img/items/coin1.png"));
				}
				break;

			case GOLD_COIN:
				iD = Constants.COIN_ID;
				itemWidthInPixel = 100;
				desiredItemWidth = 0.03f; // im Verhaeltnis zur Fenster Breite
				itemScaleFactor = desiredItemWidth * SpaceApes.WIDTH / itemWidthInPixel;
				value = Constants.GOLD_COIN_VALUE;
				if (SpaceApes.renderImages) {
					imageRenderComponent = new ImageRenderComponent(new Image("img/items/coin2.png"));
				}
				break;

			default: // ansonsten erzeugt er immer einen DIAMANT_COIN
				iD = Constants.COIN_ID;
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

		// Erzeuge das Item
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

		// Fuege das Item der Map und dem EntityManager hinzu
		Map.getInstance().addItem(item);
		StateBasedEntityManager.getInstance().addEntity(SpaceApes.GAMEPLAY_STATE, item);

		return item;
	}

}
