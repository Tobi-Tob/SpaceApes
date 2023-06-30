package utils;

import entities.Ape;

public class RuleBasedPolicy extends Policy {

	public RuleBasedPolicy() {
		super("RuleBasedPolicy");
	}

	@Override
	public void calcNextAction(Ape ape, int delta) {
		PolicyAction action = PolicyAction.MoveRight;
		if (ape.getEnergy() < 80) {
			action = PolicyAction.Shoot;
		}
		
		this.setCurrentAction(action);

	}

}
