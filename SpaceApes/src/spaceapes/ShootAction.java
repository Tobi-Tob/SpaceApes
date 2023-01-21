package spaceapes;

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
		List<float[]> planetData = map.getPlanetData();
		
		Ape activeApe = map.getActiveApe();
		if (activeApe.isInteractionAllowed()) {
		//if (gs.userInteractionAllowed) {
			// Waehrend des Flugs des Projektils keine Spielerinteraktion erlaubt
			activeApe.setInteractionAllowed(false);
			//gs.userInteractionAllowed = false;
			
			// Abfragen von initialer Position und Geschwindigkeit
			Vector2f position = activeApe.getCoordinates();
			float startDirection = activeApe.getAngleOfView_global();
			float startVelocity = 5f; // Einheit: Koordinaten/Sekunde
			Vector2f velocity = Utils.toCartesianCoordinates(startVelocity, startDirection);
			
			// Projektil wird erzeugt
			Projectile projectile = new Projectile("Projectile", position, velocity, planetData);
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
			
			/*
			CollisionEvent projectileCollisionEvent = new CollisionEvent();
			projectileCollisionEvent.addAction(new Action() {
					
					@Override
					public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
						
						if (projectileCollisionEvent.getCollidedEntity() instanceof Projectile) {
							// collidedEntity is a Projectile
							
							Projectile projectile = (Projectile) projectileCollisionEvent.getCollidedEntity();
							entityManager.removeEntity(gs.getID(), projectile);
							gs.changeActivePlayerToNextPlayer();
							// Zeige Explosion
							AnimatedEntity explosion = new AnimatedEntity("Explosion", projectile.getCoordinates());
							Image[] images = new Image[4];
							try {
								images[0] = new Image("/assets/explosion/explosion1.png");
								images[1] = new Image("/assets/explosion/explosion2.png");
								images[2] = new Image("/assets/explosion/explosion3.png");
								images[3] = new Image("/assets/explosion/explosion4.png");
		
							} catch (SlickException e) {
								System.err.println("Cannot find image for explosion");
							}
							explosion.setImages(images);
							explosion.scaleAndRotateAnimation(0.3f, Utils.randomFloat(0, 360));
							explosion.addAnimation(0.012f, false);
							entityManager.addEntity(gs.getID(), explosion);
							
						    
						} else {
						    // collidedEntity is not a Projectile -> hier soll nichts gemacht werden
						}
					}
			});
			projectile.addComponent(projectileCollisionEvent);
			*/
			
			entityManager.addEntity(gs.getID(), projectile);
		}
	}
}