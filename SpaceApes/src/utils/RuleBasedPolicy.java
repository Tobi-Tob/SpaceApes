package utils;

public class RuleBasedPolicy extends Policy {

	public RuleBasedPolicy() {
		super("RuleBasedPolicy");
	}

	@Override
	public void initTurn() {
		// Nothing to do
	}

	@Override
	public void calcNextAction(int delta) {
		PolicyAction action = PolicyAction.MoveRight;
		if (getApe().getEnergy() < 80) {
			action = PolicyAction.Shoot;
		}

		this.setCurrentAction(action);

	}

}
