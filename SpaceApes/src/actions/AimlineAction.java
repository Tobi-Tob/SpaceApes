package actions;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.action.Action;
import eea.engine.component.Component;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import entities.Ape;
import entities.Projectile;
import map.Map;
import utils.Utils;

public class AimlineAction implements Action {

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
		
		StateBasedEntityManager entityManager = StateBasedEntityManager.getInstance();
		Map map = Map.getInstance();
		
		// Alte Ziellinie entfernen
		removeAimeLine(entityManager, sb);
		Ape ape = (Ape) event.getOwnerEntity();
		
		if (ape.isInteractionAllowed()) {
			Vector2f position = ape.getCoordinates();
			float startDirection = ape.getGlobalAngleOfView();
			float startVelocity = ape.getThrowStrength();
			Vector2f velocity = Utils.toCartesianCoordinates(startVelocity, startDirection);

			//TODO Variablen!!
			int flightTime = 1000;
			int updateFrequency = 3;
			boolean draw = true;
			int numberOfDots = 5;
			
			int iterations = (int) flightTime / updateFrequency;

			// Hilfsprojektil wird erzeugt
			Projectile projectile = new Projectile("Help_Projectile", position, velocity);
			for (int i = 1; i < iterations; i++) {
				if (projectile.explizitEulerStep(updateFrequency) == false) {
					// Wenn Kollision mit Planet
					break;
				}
				if (draw && i % (100 / updateFrequency) == 0) { // In bestimmten Abstaenden werden Punkte der Hilfslinie
																// gesetzt
					Entity dot = new Entity("dot"); // Entitaet fuer einen Punkt der Linie
					dot.setPosition(Utils.toPixelCoordinates(projectile.getCoordinates()));
					dot.setScale(1 - (i * 0.8f / iterations));
					try {
						dot.addComponent(new ImageRenderComponent(new Image("/assets/dot.png")));
						//System.out.println("add dot");
					} catch (SlickException e) {
						System.err.println("Cannot find image for dot");
					}
					entityManager.addEntity(sb.getCurrentStateID(), dot);
				}
			}
			entityManager.removeEntity(sb.getCurrentStateID(), projectile);
		}
	}
	
	/**
	 * Entfernt alle Hilfslinien Punkte
	 */
	public void removeAimeLine(StateBasedEntityManager entityManager, StateBasedGame sb) {
		for (int i = 0; i < 100; i++) {
			Entity dot = entityManager.getEntity(sb.getCurrentStateID(), "dot");
			if (dot == null) {
				break;
			}
			//System.out.println("remove dot");
			entityManager.removeEntity(sb.getCurrentStateID(), dot);
		}
	}
	
}
