package actions;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.action.Action;
import eea.engine.component.Component;
import entities.Ape;
import map.Map;
import spaceapes.Launch;

public class MoveOnPlanetAction implements Action {

	private float direction; // Bewegung nach links -1 und Bewegung nach rechts +1
	private Ape ape;

	public MoveOnPlanetAction(float direction, Ape ape) {
		this.direction = direction;
		this.ape = ape;
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) { // TODO laufen ist noch nicht performant
		if (ape.isInteractionAllowed() && ape.hasEnergy()) {
			ape.stepOnPlanet(direction);
			ape.changeEnergy(-0.003f * Launch.UPDATE_INTERVAL);
			Map.getInstance().updateAimline();
		}
	}
}
