package utils;

import entities.Ape;

public class RandomPolicy extends Policy {

	public RandomPolicy() {
		super("RuleBasedPolicy");
	}

	@Override
	public void calcNextAction(Ape ape, int delta) {
		float random = Utils.randomFloat(0, 1);
		PolicyAction action = PolicyAction.Shoot;
		
		if (random < 0.2) {
			action = PolicyAction.MoveRight;
		}
		else if (random < 0.4) {
			action = PolicyAction.MoveLeft;
		}
		else if (random < 0.69) {
			action = PolicyAction.AngleDown;
		}
		else if (random < 0.98) {
			action = PolicyAction.AngleUp;
		}
		
		this.setCurrentAction(action);
	}

}
