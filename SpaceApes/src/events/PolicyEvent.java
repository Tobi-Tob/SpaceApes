package events;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.event.Event;
import policys.Policy;
import policys.Policy.PolicyAction;

/**
 * Event that responds to specific actions of a policy.
 * @author Tobi
 *
 */
public class PolicyEvent extends Event {
	Policy policy;
	PolicyAction actionToListen;

	public PolicyEvent(Policy policy, PolicyAction actionToListen) {
		super("PolicyEvent" + actionToListen.toString());
		this.policy = policy;
		this.actionToListen = actionToListen;
	}

	@Override
	protected boolean performAction(GameContainer gc, StateBasedGame sb, int delta) {
		if (policy.getCurrentAction() == actionToListen) {
			policy.resetAction();
			return true;
		} else {
			return false;
		}
	}
}
