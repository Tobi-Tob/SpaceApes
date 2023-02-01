package factories;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import eea.engine.action.basicactions.RotateRightAction;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.basicevents.LoopEvent;
import eea.engine.interfaces.IEntityFactory;
import entities.Coin;
import factories.PlanetFactory.PlanetType;
import map.Map;
import spaceapes.Launch;
import utils.Utils;

public class ItemFactory implements IEntityFactory {

	public enum ItemType {
		COPPER_COIN, GOLD_COIN, DIAMANT_COIN, HEALTH_PACK, ENERGY_PACK
	};

	private final String name;
	private final ItemType type;
	private final Vector2f coordinates;

	public ItemFactory(String name, ItemType type, Vector2f coordinates) {
		this.name = name;
		this.type = type;
		this.coordinates = coordinates;
	}

	@Override
	public Entity createEntity() {

		StateBasedEntityManager entityManager = StateBasedEntityManager.getInstance();
		int stateID = Launch.GAMEPLAY_STATE;

		LoopEvent itemLoop = new LoopEvent();
		itemLoop.addAction(new RotateRightAction(0.03f));

		try {
			switch (type) {

			case COPPER_COIN:
				Coin copperCoin = new Coin(name);

				float itemWidthInPixel = 100;
				float desiredItemWidth = 0.03f; // im Verhaeltnis zur Fenster Breite
				float itemScaleFactor = desiredItemWidth * Launch.WIDTH / itemWidthInPixel;

				copperCoin.setScale(itemScaleFactor);
				copperCoin.setPosition(Utils.toPixelCoordinates(coordinates));
				copperCoin.setValue(1);

				copperCoin.addComponent(new ImageRenderComponent(new Image("img/items/coin1.png")));
				copperCoin.addComponent(itemLoop);
				entityManager.addEntity(stateID, copperCoin);

				return copperCoin;

			case GOLD_COIN:
				Coin goldCoin = new Coin(name);

				itemWidthInPixel = 100;
				desiredItemWidth = 0.03f; // im Verhaeltnis zur Fenster Breite
				itemScaleFactor = desiredItemWidth * Launch.WIDTH / itemWidthInPixel;

				goldCoin.setScale(itemScaleFactor);
				goldCoin.setPosition(Utils.toPixelCoordinates(coordinates));
				goldCoin.setValue(1);

				goldCoin.addComponent(new ImageRenderComponent(new Image("img/items/coin1.png")));
				goldCoin.addComponent(itemLoop);
				entityManager.addEntity(stateID, goldCoin);

				return goldCoin;

			default: // ansonsten erzeugt er immer einen DIAMANT_COIN
				Coin diamondCoin = new Coin(name);

				itemWidthInPixel = 100;
				desiredItemWidth = 0.03f; // im Verhaeltnis zur Fenster Breite
				itemScaleFactor = desiredItemWidth * Launch.WIDTH / itemWidthInPixel;

				diamondCoin.setScale(itemScaleFactor);
				diamondCoin.setPosition(Utils.toPixelCoordinates(coordinates));
				diamondCoin.setValue(1);

				diamondCoin.addComponent(new ImageRenderComponent(new Image("img/items/coin1.png")));
				diamondCoin.addComponent(itemLoop);
				entityManager.addEntity(stateID, diamondCoin);

				return diamondCoin;

			}
		} catch (SlickException e) {
			System.err.println("Problem with item image");
		}

		return null;
	}

}
