package actions;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.action.Action;
import eea.engine.component.Component;
import entities.Ape;
import utils.Policy;

public class PolicyNextMoveAction implements Action {
	
	private Ape ape;
	private Policy policy;

	public PolicyNextMoveAction(Ape ape) {
		this.ape = ape;
		this.policy = ape.getPolicy();
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
		if (ape.isActive() && ape.isInteractionAllowed()) {
			policy.calcNextAction(delta);
		}
	}
}
