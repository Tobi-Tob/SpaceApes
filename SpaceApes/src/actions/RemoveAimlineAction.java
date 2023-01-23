package actions;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.action.Action;
import eea.engine.component.Component;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;

public class RemoveAimlineAction implements Action {

	// WIRD MOMENTAN NICHT BENUTZT!!!

	private StateBasedEntityManager entityManager;

	public RemoveAimlineAction(StateBasedEntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
//		GameplayState gs = (GameplayState) sb.getCurrentState();
//		for (int i = 0; i < 100; i++) {
//			Entity dot = entityManager.getEntity(gs.getID(), "dot");
//			if (dot == null) {
//				break;
//			}
//			entityManager.removeEntity(gs.getID(), dot);
//		}
	}

}
