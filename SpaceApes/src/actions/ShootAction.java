package actions;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.action.Action;
import eea.engine.component.Component;
import eea.engine.entity.StateBasedEntityManager;
import entities.Ape;
import entities.Projectile;
import factories.ProjectileFactory;
import factories.ProjectileFactory.MovementType;
import factories.ProjectileFactory.ProjectileType;
import map.Map;
import spaceapes.Constants;
import spaceapes.SpaceApes;
import utils.Utils;

public class ShootAction implements Action {
	
	private final MovementType movementType;
	
	public ShootAction(MovementType movementType) {
		super();
		this.movementType = movementType;
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {

		Map map = Map.getInstance();
		Ape activeApe = map.getActiveApe();

		if (activeApe.isInteractionAllowed()) {
			
			StateBasedEntityManager entityManager = StateBasedEntityManager.getInstance();

			// Abfragen der ausgewaehlten Waffe
			Projectile selectedProjectile = map.getControlPanel().getSelectedProjectile();
			ProjectileType selectedType = selectedProjectile.getType();
			if (selectedProjectile.getPrice() > activeApe.getCoins()) {
				
				System.out.println("Du bist zu arm fuer dieses Projektil :'(");
				
			} else {
				
				activeApe.reduceCoins(selectedProjectile.getPrice());
				// Waehrend des Flugs des Projektils keine Spielerinteraktion erlaubt und das
				// ControlPanel wird zur besseren Sichtbarkeit unsichtbar gemacht
				activeApe.setInteractionAllowed(false);
				map.getControlPanel().setPanelAndComponentsVisible(false);
				map.removeAimeLine();

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
				Projectile projectile = new ProjectileFactory(Constants.PROJECTILE_ID, positionOfProjectileLaunch,
						velocity, true, true, selectedType, movementType).createEntity();

				entityManager.addEntity(SpaceApes.GAMEPLAY_STATE, projectile);
				
			}
		}
	}
}