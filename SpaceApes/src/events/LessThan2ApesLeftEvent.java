package events;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;
import eea.engine.event.Event;
import spaceapes.Map;

public class LessThan2ApesLeftEvent extends Event {
	
	private int timeWaited = 0;
	
	public LessThan2ApesLeftEvent() {
		super("LessThan2ApesLeftEvent");
	}

	@Override
	protected boolean performAction(GameContainer gc, StateBasedGame sb, int delta) {
		if (Map.getInstance().getApes().size() < 2) {
			this.timeWaited += delta;
			if (this.timeWaited >= 1500) { // Verzoegerung des Events
				return true;
			}
		}
		return false;
	}

}
