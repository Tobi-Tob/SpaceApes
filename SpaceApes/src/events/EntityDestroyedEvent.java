package events;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.Event;

public class EntityDestroyedEvent extends Event {
	
	//WIRD MOMENTAN NICHT BENUTZT!!!
	
	private String entityID;

	public EntityDestroyedEvent(String entityID) {
		super("ProjectileExploded");
		this.entityID = entityID;
	}

	@Override
	protected boolean performAction(GameContainer gc, StateBasedGame sb,
			int delta) {
		return false;
	}

}
