package actions;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.action.Action;
import eea.engine.component.Component;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.basicevents.CollisionEvent;
import entities.Item;
import factories.ItemFactory.ItemType;
import map.Map;

public class CollisionAction implements Action {

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
		if(CollisionEvent.class.isInstance(event)){ // Prüfe, ob es sich um eine Kollision handelt -> unnötig??
			Entity collidedEntity = ((CollisionEvent) event).getCollidedEntity();
			if(collidedEntity instanceof Item) {
				Item collidedItem = (Item) collidedEntity;
				Map map = Map.getInstance();
				if (collidedItem.getItemType().equals(ItemType.ENERGY_PACK)) {
					map.getActiveApe().increaseEnergy(collidedItem.getValue());
					System.out.println("Energy of " + map.getActiveApe().getID() + " increased to " + map.getActiveApe().getEnergy());
					
				} else if (collidedItem.getItemType().equals(ItemType.HEALTH_PACK)) {
					map.getActiveApe().increaseHealth(collidedItem.getValue());
					System.out.println("Health of " + map.getActiveApe().getID() + " increased to " + map.getActiveApe().getHealth());
					
				} else { // some type of coin
					map.getActiveApe().increaseCoins(collidedItem.getValue());
					System.out.println("Coins of " + map.getActiveApe().getID() + " increased to " + map.getActiveApe().getCoins());
				}
				StateBasedEntityManager.getInstance().removeEntity(sb.getCurrentState().getID(), collidedEntity);
			}
		}
	}

}
