package spaceapes;

import java.util.List;

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

public class AimlineAction implements Action {
	
	private StateBasedEntityManager entityManager;
	private List<float[]> planetData;

	public AimlineAction(StateBasedEntityManager entityManager, List<float[]> planetData) {
		this.entityManager = entityManager;
		this.planetData = planetData;
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
		// TODO Auto-generated method stub
		GameplayState gs = (GameplayState) sb.getCurrentState();
		
		removeAimeLine(gs);
		
		Player activePlayer = gs.getActivePlayer();
		if (activePlayer.isInteractionAllowed()) {
			Ape ape = activePlayer.getApe();
			Vector2f position = ape.getCoordinates();
			float startDirection = ape.getAngleOfView_global();
			float startVelocity = ape.getThrowStrength();
			Vector2f velocity = Utils.toCartesianCoordinates(startVelocity, startDirection);

			//TODO Variablen!!
			int flightTime = 1000;
			int updateFrequency = 3;
			boolean draw = true;
			int numberOfDots = 5;
			
			int iterations = (int) flightTime / updateFrequency;

			// Hilfsprojektil wird erzeugt
			Projectile projectile = new Projectile("Help_Projectile", position, velocity, planetData);
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
					} catch (SlickException e) {
						System.err.println("Cannot find image for dot");
					}
					entityManager.addEntity(gs.getID(), dot);
				}
			}
			entityManager.removeEntity(gs.getID(), projectile);
		}
	}
	
	/**
	 * Entfernt alle Hilfslinien Punkte
	 */
	public void removeAimeLine(GameplayState gs) {
		for (int i = 0; i < 100; i++) {
			Entity dot = entityManager.getEntity(gs.getID(), "dot");
			if (dot == null) {
				break;
			}
			entityManager.removeEntity(gs.getID(), dot);
		}
	}
	
}
