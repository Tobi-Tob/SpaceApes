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
import factories.ProjectileFactory.ProjectileType;
import map.Map;
import spaceapes.GameplayState;
import utils.Utils;

public class ShootAction implements Action {

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {

		Map map = Map.getInstance();
		Ape activeApe = map.getActiveApe();

		if (activeApe.isInteractionAllowed()) {
			GameplayState gs = (GameplayState) sb.getCurrentState();
			StateBasedEntityManager entityManager = gs.getEntityManager();

			// Abfragen der ausgewaehlten Waffe
			ProjectileType selectedType = map.getControlPanel().getSelectedProjectile().getType();

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
			Vector2f positionOfProjectileLaunch = new Vector2f(positionOfApe)
					.add(Utils.toCartesianCoordinates(activeApe.getRadiusInWorldUnits(), activeApe.getAngleOnPlanet()));

			// Projektil wird erzeugt
			Projectile projectile = (Projectile) new ProjectileFactory("Projectile", positionOfProjectileLaunch, velocity,
					true, true, selectedType).createEntity();

			entityManager.addEntity(gs.getID(), projectile);
		}
	}
}