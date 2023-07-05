package events;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.entity.Entity;
import eea.engine.event.Event;
import utils.Constants;
import utils.Utils;

public class LeavingWorldEvent extends Event {
	
	private Entity entity;

	public LeavingWorldEvent(Entity entity) {
		super("LeavingWorldEvent");
		this.entity = entity;
	}

	@Override
	protected boolean performAction(GameContainer gc, StateBasedGame sb, int delta) {
		Vector2f coordinates = Utils.toWorldCoordinates(entity.getPosition());
		if (coordinates.x > Constants.WORLD_WIDTH / 2 || coordinates.x < -Constants.WORLD_WIDTH / 2
				|| coordinates.y > Constants.WORLD_HEIGHT / 2 || coordinates.y < -Constants.WORLD_HEIGHT / 2) {
			return true;
		}
		return false;
	}

}
