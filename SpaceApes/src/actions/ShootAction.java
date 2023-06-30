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
import map.Map;
import spaceapes.Constants;
import utils.Utils;

public class ShootAction implements Action {

	private final MovementType movementType;
	private Ape ape;

	public ShootAction(MovementType movementType, Ape ape) {
		super();
		this.movementType = movementType;
		this.ape = ape;
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {

		if (ape.isInteractionAllowed() && ape.isActive()) {
			
			// Abfragen der ausgewaehlten Waffe
			ProjectileType selectedType = ProjectileType.COCONUT;
			int projectilePrice = 0;
			
			if (Map.getInstance().getControlPanel().isVisible()) {
				Projectile selectedProjectile = Map.getInstance().getControlPanel().getSelectedProjectile();
				selectedType = selectedProjectile.getType();
				projectilePrice = selectedProjectile.getPrice();
			}
			if (projectilePrice > ape.getCoins()) {

				System.out.println("Du bist zu arm fuer dieses Projektil :'(");

			} else {

				ape.reduceCoins(projectilePrice);
				ape.increaseMoneySpendStatistics(projectilePrice);
				// Waehrend des Flugs des Projektils keine Spielerinteraktion erlaubt und das
				// ControlPanel wird zur besseren Sichtbarkeit unsichtbar gemacht
				ape.setInteractionAllowed(false);
				Map.getInstance().getControlPanel().setPanelAndComponentsVisible(false);
				Map.getInstance().removeAimeLine();

				// Abfragen von initialer Position und Geschwindigkeit
				float startDirection = ape.getGlobalAngleOfView();
				float startVelocity = ape.getThrowStrength(); // Einheit: Koordinaten/Sekunde
				Vector2f velocity = Utils.toCartesianCoordinates(startVelocity, startDirection);
				Vector2f positionOfApe = ape.getWorldCoordinates();
				// Das Projektil wird leicht ausserhalb des Apes gestartet, damit nicht sofort
				// eine Kollision eintritt...
				Vector2f positionOfProjectileLaunch = new Vector2f(positionOfApe).add(
						Utils.toCartesianCoordinates(ape.getRadiusInWorldUnits(), ape.getAngleOnPlanet()));

				// Projektil wird erzeugt
				ProjectileFactory.createProjectile(Constants.PROJECTILE_ID, selectedType, positionOfProjectileLaunch, velocity, true, true,
						movementType);
			}
		}
	}
}