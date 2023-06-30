package utils;

import entities.Ape;

public abstract class Policy {

	public enum PolicyAction {
		MoveRight, MoveLeft, AngleUp, AngleDown, PowerUp, PowerDown, Shoot
	};

	private String name;
	private PolicyAction currentAction = null;

	public Policy(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public PolicyAction getCurrentAction() {
		return currentAction;
	}
	
	public void resetAction() {
		currentAction = null;
	}

	public void setCurrentAction(PolicyAction action) {
		this.currentAction = action;
	}

	/**
	 * Method is called repeatedly in the update function of the active ape.
	 * Implement logic of policy here and update private attribute currentAction
	 * with setCurrentAction().
	 * 
	 * @param delta time in ms since last call of update()
	 */
	public abstract void calcNextAction(Ape ape, int delta);

}
