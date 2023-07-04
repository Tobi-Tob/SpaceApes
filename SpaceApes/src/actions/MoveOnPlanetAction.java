package actions;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.action.Action;
import eea.engine.component.Component;
import entities.Ape;
import map.Map;
import spaceapes.Constants;
import spaceapes.SpaceApes;

public class MoveOnPlanetAction implements Action {

	private float direction; // Bewegung nach links -1 und Bewegung nach rechts +1
	private Ape ape;

	public MoveOnPlanetAction(float direction, Ape ape) {
		this.direction = direction;
		this.ape = ape;
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
		if (ape.isInteractionAllowed() && ape.hasEnergy()) {
			ape.stepOnPlanet(direction);
			float energyUsed = Constants.ENERGY_USED_PER_STEP * SpaceApes.UPDATE_INTERVAL;
			ape.changeEnergy(-energyUsed);
			if (ape.getEnergy() > 0) { // Update statistics
				ape.increaseEnergyUsedStatistics(energyUsed);
			}
			Map.getInstance().updateAimline();
		}
	}
}
