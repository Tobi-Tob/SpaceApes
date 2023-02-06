package actions;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.component.Component;
import eea.engine.entity.Entity;
import entities.Ape;
import map.Map;

public class ChangeAngleAction extends ButtonPressedAction {
	private float angleToChange;

	public ChangeAngleAction(float angleToChange, Entity button) {
		super(button);
		this.angleToChange = angleToChange;
	}

	@Override
	protected void updateToPerform(GameContainer gc, StateBasedGame sb, int delta, Component event) {
		Ape ape = Map.getInstance().getActiveApe();
		if (ape.isInteractionAllowed()) {
			ape.changeAngleOfView(angleToChange);
			//java.lang.System.out.println("AngleOfView = " + ape.getLocalAngleOfView());
			Map.getInstance().updateAimline();
		}

	}

}
