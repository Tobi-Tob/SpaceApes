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
import map.Map;
import spaceapes.GameplayState;
import utils.Utils;

public class ShootAction implements Action {

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
		GameplayState gs = (GameplayState) sb.getCurrentState();
		StateBasedEntityManager entityManager = gs.getEntityManager();
		
		Ape activeApe = Map.getInstance().getActiveApe();
		if (activeApe.isInteractionAllowed()) {
		//if (gs.userInteractionAllowed) {
			// Waehrend des Flugs des Projektils keine Spielerinteraktion erlaubt
			activeApe.setInteractionAllowed(false);
			//gs.userInteractionAllowed = false;
			
			// Abfragen von initialer Position und Geschwindigkeit
			Vector2f position = activeApe.getCoordinates();
			float startDirection = activeApe.getGlobalAngleOfView();
			float startVelocity = 5f; // Einheit: Koordinaten/Sekunde
			Vector2f velocity = Utils.toCartesianCoordinates(startVelocity, startDirection);
			boolean visible = true;
			
			// Projektil wird erzeugt
			Projectile projectile = (Projectile) new ProjectileFactory("Projectile", position, velocity, visible).createEntity();
			
			entityManager.addEntity(gs.getID(), projectile);
		}
	}
}