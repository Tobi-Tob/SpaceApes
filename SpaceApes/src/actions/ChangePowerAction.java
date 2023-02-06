package actions;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.component.Component;
import eea.engine.entity.Entity;
import entities.Ape;
import map.Map;

public class ChangePowerAction extends ButtonPressedAction {
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
			//java.lang.System.out.println("ThrowStrength = " + ape.getThrowStrength());
			Map.getInstance().updateAimline();
		}

	}

}
