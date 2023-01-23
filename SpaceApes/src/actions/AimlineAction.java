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
import factories.ProjectileFactory;
import factories.ProjectileFactory.ProjectileType;
import map.Map;
import utils.Utils;

public class AimlineAction implements Action {

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {

		StateBasedEntityManager entityManager = StateBasedEntityManager.getInstance();
		Map map = Map.getInstance(); // nicht benutzt

		// Alte Ziellinie entfernen
		removeAimeLine(entityManager, sb);

		Ape ape = (Ape) event.getOwnerEntity();

		if (ape.isInteractionAllowed()) {
			float startDirection = ape.getGlobalAngleOfView();
			float startVelocity = ape.getThrowStrength();
			Vector2f velocity = Utils.toCartesianCoordinates(startVelocity, startDirection);
			Vector2f positionOfApe = ape.getWorldCoordinates();
			// Das Projektil wird leicht ausserhalb des Apes gestartet, damit nicht sofort
			// eine Kollision eintritt...
			Vector2f positionOfProjectileLaunch = new Vector2f(positionOfApe)
					.add(Utils.toCartesianCoordinates(ape.getRadiusInWorldUnits(), ape.getAngleOnPlanet()));

			// TODO Variablen!!
			int flightTime = 500; // in ms
			int updateFrequency = 3; // in ms
			boolean draw = true;
			int numberOfDots = 5;
			boolean visible = false;
			ProjectileType type = ProjectileType.COCONUT;

			int iterations = (int) flightTime / updateFrequency;

			// Hilfsprojektil wird erzeugt
			Projectile dummyProjectile = (Projectile) new ProjectileFactory("DummyProjectile", positionOfProjectileLaunch,
					velocity, visible, type).createEntity();
			dummyProjectile.setVisible(false);
			for (int i = 1; i < iterations; i++) {
				if (dummyProjectile.explizitEulerStep(updateFrequency)) {
					// Wenn Kollision mit einem Objekt
					break;
				}
				if (draw && i % (60 / updateFrequency) == 0) { // In bestimmten Abstaenden werden Punkte der Hilfslinie
																// gesetzt
					Entity dot = new Entity("dot"); // Entitaet fuer einen Punkt der Linie
					dot.setPosition(Utils.toPixelCoordinates(dummyProjectile.getCoordinates()));
					dot.setScale(1 - (i * 0.8f / iterations));
					try {
						dot.addComponent(new ImageRenderComponent(new Image("/assets/dot.png")));
						// System.out.println("add dot");
					} catch (SlickException e) {
						System.err.println("Cannot find image for dot");
					}
					entityManager.addEntity(sb.getCurrentStateID(), dot);
				}
			}
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
			entityManager.removeEntity(sb.getCurrentStateID(), dot);
		}
	}

}
