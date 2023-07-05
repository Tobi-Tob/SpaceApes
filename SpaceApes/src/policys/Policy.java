package policys;

import entities.Ape;

public abstract class Policy {

	public enum PolicyAction {
		MoveRight, MoveLeft, AngleUp, AngleDown, PowerUp, PowerDown, Shoot
	};

	private String name;
	private Ape ape;
	private PolicyAction currentAction = null;

	public Policy(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Ape getApe() {
		return ape;
	}
	
	public void setApe(Ape ape) {
		this.ape = ape;
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
	 * Method is called at the beginning of each turn. Implement optional logic
	 * here.
	 */
	public abstract void initTurn();

	/**
	 * Method is called repeatedly in the update function of the active ape.
	 * Implement logic of policy here and update private attribute currentAction
	 * with setCurrentAction().
	 * 
	 * @param delta time in ms since last call of update()
	 */
	public abstract void calcNextAction(int delta);

}
