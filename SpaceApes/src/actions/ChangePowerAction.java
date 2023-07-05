package actions;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.component.Component;
import eea.engine.entity.Entity;
import entities.Ape;
import spaceapes.Map;

public class ChangePowerAction extends ButtonAnimationAction {
	private float powerToChange;

	public ChangePowerAction(float powerToChange, Entity button) {
		super(button);
		this.powerToChange = powerToChange;
	}

	@Override
	protected void updateToPerform(GameContainer gc, StateBasedGame sb, int delta, Component event) {
		Ape ape = Map.getInstance().getActiveApe();
		if (ape.isInteractionAllowed()) {
			ape.changeThrowStrength(powerToChange);
			Map.getInstance().updateAimline();
		}

	}

}
