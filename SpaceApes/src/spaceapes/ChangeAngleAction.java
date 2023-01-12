package spaceapes;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.action.Action;
import eea.engine.component.Component;

public class ChangeAngleAction implements Action {
	private float angleToChange;

	public ChangeAngleAction(float alpha) {
		angleToChange = alpha;
	}

	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2, Component arg3) {

		GameplayState gps = (GameplayState) arg1.getCurrentState();
		Player activePlayer = gps.activePlayer;
		if (gps.playerInteractionAllowed) {
			activePlayer.getApe().changeAngleOfView(angleToChange);
		}
	}

}
