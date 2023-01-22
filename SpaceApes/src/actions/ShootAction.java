package actions;

import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Image;

import eea.engine.action.Action;
import eea.engine.action.basicactions.MoveDownAction;
import eea.engine.component.Component;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.basicevents.CollisionEvent;
import eea.engine.event.basicevents.LoopEvent;
import entities.Ape;
import entities.Projectile;
import map.Map;
import spaceapes.GameplayState;
import utils.Utils;
import eea.engine.component.render.ImageRenderComponent;

public class ShootAction implements Action {
	
	//private Ape activeApe;
	//private List<float[]> planetData;
	//private StateBasedEntityManager entityManager;

	public ShootAction() {
//		this.planetData = planetData;
//		this.entityManager = entityManager;
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
		GameplayState gs = (GameplayState) sb.getCurrentState();
		StateBasedEntityManager entityManager = gs.getEntityManager();
		Map map = Map.getInstance();
		
		Ape activeApe = map.getActiveApe();
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
			
			// Projektil wird erzeugt
			Projectile projectile = new Projectile("Projectile", position, velocity);
			try {
				projectile.addComponent(new ImageRenderComponent(new Image("/assets/coconut.png")));
			} catch (SlickException e) {
				System.err.println("Cannot find file assets/coconut.png");
				e.printStackTrace();
			}

			// Loop Event
			LoopEvent projectileLoop = new LoopEvent();
			projectileLoop.addAction(new ProjectileMovementAction(projectile, entityManager));
			projectile.addComponent(projectileLoop);	
			
			entityManager.addEntity(gs.getID(), projectile);
		}
	}
}