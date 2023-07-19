package actions;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.component.Component;
import eea.engine.entity.Entity;
import entities.Ape;
import spaceapes.Map;

/**
 * Action to change the selected projectile
 * @author Tobi
 *
 */
public class ChangeWeaponAction extends ButtonAnimationAction {
	
	public ChangeWeaponAction(Entity button) {
		super(button);
	}

	@Override
	protected void updateToPerform(GameContainer container, StateBasedGame game, int delta, Component event) {
		Ape ape = Map.getInstance().getActiveApe();
		if (ape.isInteractionAllowed()) {
			Map.getInstance().getControlPanel().nextShopProjectil();
		}
	}
}
