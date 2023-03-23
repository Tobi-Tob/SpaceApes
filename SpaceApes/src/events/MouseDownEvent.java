package events;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.event.Event;
import spaceapes.SpaceApes;

public class MouseDownEvent extends Event {
	int mouseDownTime;
	int timeUntilActionIsPerformed = 500;

	public MouseDownEvent() {
		super("MouseDownEvent");
		this.mouseDownTime = 0;
	}

	@Override
	protected boolean performAction(GameContainer gc, StateBasedGame sb, int delta) {
		boolean mouseButtonDown = new Input(SpaceApes.HEIGHT).isMouseButtonDown(0);
		if (!mouseButtonDown && mouseDownTime != 0) {
			mouseDownTime = 0; // Taste wurde losgelassen
		}
		if (mouseButtonDown) {
			if (mouseDownTime >= timeUntilActionIsPerformed)  {
				mouseDownTime += delta;
				return true;
			} else {
				mouseDownTime += delta;
			}
		}
		return false;
	}

}
