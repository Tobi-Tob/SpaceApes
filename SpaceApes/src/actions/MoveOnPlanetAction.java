package actions;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.action.Action;
import eea.engine.component.Component;
import entities.Ape;
import utils.Constants;
import utils.Resources;
import utils.Utils;
import spaceapes.Map;
import spaceapes.SpaceApes;

/**
 * Action to make steps on a planet
 * @author Tobi
 *
 */
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
			// Lauf Sound
			if (SpaceApes.PLAY_SOUNDS && !Resources.STEP_SOUND.playing()) {
				Resources.STEP_SOUND.play(Utils.randomFloat(1.1f, 1.6f), Utils.randomFloat(0.4f, 0.7f));
			}
			Map.getInstance().updateAimline();
		}
	}
}
