package actions;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.action.Action;
import eea.engine.component.Component;
import eea.engine.entity.StateBasedEntityManager;
import entities.Ape;
import entities.ControlPanel;
import entities.Projectile;
import factories.ProjectileFactory;
import map.Map;
import spaceapes.GameplayState;
import spaceapes.Launch;
import utils.Utils;

public class ShootAction implements Action {

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
		GameplayState gs = (GameplayState) sb.getCurrentState();
		StateBasedEntityManager entityManager = gs.getEntityManager();
		Map map = Map.getInstance();

		Ape activeApe = map.getActiveApe();
		if (activeApe.isInteractionAllowed()) {
			// Waehrend des Flugs des Projektils keine Spielerinteraktion erlaubt und das ControlPanel wird zur besseren Sichtbarkeit unsichtbar gemacht
			activeApe.setInteractionAllowed(false);
			((ControlPanel) entityManager.getEntity(gs.getID(), "ControlPanel")).setPanelAndComponentsVisible(false);
			
			// Abfragen von initialer Position und Geschwindigkeit
			float startDirection = activeApe.getGlobalAngleOfView();
			float startVelocity = 5f; // Einheit: Koordinaten/Sekunde
			Vector2f velocity = Utils.toCartesianCoordinates(startVelocity, startDirection);
			Vector2f positionOfApe = activeApe.getCoordinates();
			// Das Projektil wird leicht außerhalb des Apes gestartet, damit nicht sofort eine Kollision eintritt...
			Vector2f position = positionOfApe.add(Utils.toCartesianCoordinates(activeApe.getRadiusWorld() * 1.05f, startDirection));
			boolean visible = true;
			
			// Projektil wird erzeugt
			Projectile projectile = (Projectile) new ProjectileFactory("Projectile", position, velocity, visible).createEntity();
			
			entityManager.addEntity(gs.getID(), projectile);
		}
	}
}