package spaceapes;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.component.Component;

public class ChangeAngleAction extends ButtonPressedAction {
	private float angleToChange;

	public ChangeAngleAction(float alpha) {
		angleToChange = alpha;
	}

	@Override
	protected void updateToPerform(GameContainer container, StateBasedGame game, int delta, Component event) {
		GameplayState gps = (GameplayState) game.getCurrentState();
		Player activePlayer = gps.activePlayer;
		if (gps.playerInteractionAllowed) {
			activePlayer.getApe().changeAngleOfView(angleToChange);
			java.lang.System.out.println("AngleOfView = " + activePlayer.getApe().getAngleOfView_local());
		}

	}

}
