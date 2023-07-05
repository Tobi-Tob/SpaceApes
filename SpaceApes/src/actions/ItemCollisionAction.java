package actions;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.action.Action;
import eea.engine.component.Component;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.basicevents.CollisionEvent;
import entities.Ape;
import entities.Item;
import factories.ItemFactory.ItemType;
import spaceapes.Map;

public class ItemCollisionAction implements Action {

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
		Entity collidedEntity = ((CollisionEvent) event).getCollidedEntity();
		if(collidedEntity instanceof Item) {
			Item collidedItem = (Item) collidedEntity;
			Ape activeApe = Map.getInstance().getActiveApe();
			activeApe.increaseItemsCollectedStatistics(); // Updating statistics
			if (collidedItem.getItemType().equals(ItemType.ENERGY_PACK)) {
				activeApe.changeEnergy(collidedItem.getValue());
				
			} else if (collidedItem.getItemType().equals(ItemType.HEALTH_PACK)) {
				activeApe.changeHealth(collidedItem.getValue());
				
			} else { // some type of coin
				activeApe.increaseCoins(collidedItem.getValue());
			}
			StateBasedEntityManager.getInstance().removeEntity(sb.getCurrentState().getID(), collidedEntity);
		}
	}

}
