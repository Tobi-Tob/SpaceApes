package events;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.Event;
import map.Map;
import spaceapes.Constants;
import utils.Utils;

public class LessThan2ApesLeftEvent extends Event {
	
	public LessThan2ApesLeftEvent() {
		super("LessThan2ApesLeftEvent");
	}

	@Override
	protected boolean performAction(GameContainer gc, StateBasedGame sb, int delta) {
		if (Map.getInstance().getApes().size() < 2) {
			return true;
		}
		return false;
	}

}
