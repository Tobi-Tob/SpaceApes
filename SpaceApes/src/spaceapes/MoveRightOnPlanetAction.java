package spaceapes;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.action.Action;
import eea.engine.component.Component;

public class MoveRightOnPlanetAction implements Action {

	private Player activePlayer;
	
	public MoveRightOnPlanetAction(Player activePlayer) {
		this.activePlayer = activePlayer;
	}

	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2, Component arg3) {
		if (activePlayer.isInteractionAllowed()) {
			activePlayer.getApe().stepOnPlanet(1); // 1 = rechts rum
		}
	}

}
