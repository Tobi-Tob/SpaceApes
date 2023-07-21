package actions;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.action.Action;
import eea.engine.component.Component;
import entities.Ape;
import entities.Projectile;
import factories.ProjectileFactory;
import factories.ProjectileFactory.MovementType;
import factories.ProjectileFactory.ProjectileType;
import spaceapes.Map;
import spaceapes.SpaceApes;
import utils.Constants;
import utils.Resources;
import utils.Utils;

public class ShootAction implements Action {

	private final MovementType movementType;
	private final boolean isAIControlled;

	public ShootAction(MovementType movementType, boolean isAIControlled) {
		super();
		this.movementType = movementType;
		this.isAIControlled = isAIControlled;
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {

		Ape activeApe = Map.getInstance().getActiveApe();
		if (activeApe.isInteractionAllowed() && (activeApe.isAIControlled() == this.isAIControlled)) {

			// Abfragen der ausgewaehlten Waffe
			ProjectileType selectedType = ProjectileType.COCONUT;
			int projectilePrice = 0;

			if (Map.getInstance().getControlPanel().isVisible()) {
				Projectile selectedProjectile = Map.getInstance().getControlPanel().getSelectedProjectile();
				selectedType = selectedProjectile.getType();
				projectilePrice = selectedProjectile.getPrice();
			}
			if (projectilePrice > activeApe.getCoins()) {

				System.out.println("Du bist zu arm fuer dieses Projektil :'(");
				if (SpaceApes.PLAY_SOUNDS) {
					Resources.REFUSED.play(1.5f, 0.3f);
				}

			} else {
				// Throw sound
				if (SpaceApes.PLAY_SOUNDS) {
					Resources.THROW_SOUND.play(1f, 1f);
				}

				activeApe.reduceCoins(projectilePrice);
				activeApe.increaseMoneySpendStatistics(projectilePrice);
				// Waehrend des Flugs des Projektils keine Spielerinteraktion erlaubt und das
				// ControlPanel wird zur besseren Sichtbarkeit unsichtbar gemacht
				activeApe.setInteractionAllowed(false);
				Map.getInstance().getControlPanel().setPanelAndComponentsVisible(false);
				Map.getInstance().removeAimeLine();

				// Abfragen von initialer Position und Geschwindigkeit
				float startDirection = activeApe.getGlobalAngleOfView();
				float startVelocity = activeApe.getThrowStrength(); // Einheit: Koordinaten/Sekunde
				Vector2f velocity = Utils.toCartesianCoordinates(startVelocity, startDirection);
				Vector2f positionOfApe = activeApe.getWorldCoordinates();
				// Das Projektil wird leicht ausserhalb des Apes gestartet, damit nicht sofort
				// eine Kollision eintritt...
				Vector2f positionOfProjectileLaunch = new Vector2f(positionOfApe).add(
						Utils.toCartesianCoordinates(activeApe.getRadiusInWorldUnits(), activeApe.getAngleOnPlanet()));

				// Projektil wird erzeugt
				ProjectileFactory.createProjectile(Constants.PROJECTILE_ID, selectedType, positionOfProjectileLaunch, velocity, true, true,
						movementType);
			}
		}
	}
}