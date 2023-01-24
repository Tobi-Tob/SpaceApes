package events;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.event.Event;
import spaceapes.Launch;

public class MouseDownEvent extends Event {

	// WIRD MOMENTAN NICHT BENUTZT!!!

	public MouseDownEvent() {
		super("MouseDownEvent");
	}

	@Override
	protected boolean performAction(GameContainer gc, StateBasedGame sb, int delta) {
		if (new Input(Launch.HEIGHT).isMouseButtonDown(0)) {
			return true;
		}
		return false;
	}

}
