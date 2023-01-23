package events;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.Event;
import map.Map;

public class ProjectileExplodedEvent extends Event {
	
	
	private String entityID;

	public ProjectileExplodedEvent() {
		super("ProjectileExploded");
	}

	@Override
	protected boolean performAction(GameContainer gc, StateBasedGame sb,
			int delta) {
		return Map.getInstance().hasProjectileExploded();
	}

}
