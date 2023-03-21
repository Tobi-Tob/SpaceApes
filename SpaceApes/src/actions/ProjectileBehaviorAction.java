package actions;

import java.util.HashMap;
import java.util.Map.Entry;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.action.Action;
import eea.engine.component.Component;
import eea.engine.entity.StateBasedEntityManager;
import entities.AnimatedEntity;
import entities.Ape;
import entities.DamageDisplay;
import entities.Projectile;
import factories.ProjectileFactory.MovementType;
import map.Map;
import spaceapes.Constants;
import spaceapes.Launch;
import utils.Utils;

public class ProjectileBehaviorAction implements Action {

	private Projectile projectile;
	private MovementType movementType; // which method calculate the flight trajectory should be used?
								// 0: linearMovement | 1: explicitEuler

	public ProjectileBehaviorAction(Projectile projectile, MovementType movementType) {
		this.projectile = projectile;
		this.movementType = movementType;
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {

		boolean collision = false;
		if (movementType==MovementType.LINEAR && projectile.linearMovementStep(delta) || movementType==MovementType.EXPLICIT_EULER && projectile.explizitEulerStep(delta)) {
			// linearMovementStep und explizitEulerStep geben jeweils true zurück bei Kollision mit Affen/Planeten
			collision = true;
		}
		
		if (collision) {
			StateBasedEntityManager entityManager = StateBasedEntityManager.getInstance();
			Map map = Map.getInstance();
			// Im Kollisionsfall:
			entityManager.removeEntity(Launch.GAMEPLAY_STATE, projectile);
			HashMap<Integer, Ape> damageApeTable = new HashMap<Integer, Ape>();

			for (Ape ape : map.getApes()) {
				float distanceApeToExplosion = ape.getWorldCoordinates().distance(projectile.getCoordinates());
				float distanceApeHitboxToExplosion = distanceApeToExplosion - ape.getRadiusInWorldUnits(); // bei direktem
																											// Treffer = 0
				int maxDamage = projectile.getMaxDamage();
				float damageRadius = projectile.getDamageRadius();
				// System.out.println(ape.getID() + " distanceApeHitbox: " +
				// distanceApeHitboxToExplosion);
				if (distanceApeHitboxToExplosion <= damageRadius) { // Test ob die Explosion nah genug
																	// am Affen ist
					int damage = Math.round(maxDamage * (1 - (distanceApeHitboxToExplosion / damageRadius))); // lineare
																												// Interpolation
					if (distanceApeHitboxToExplosion < 0.1f) {
						damage = maxDamage; // Stellt sicher, dass bei einem direkten Treffer maximaler Schaden verursacht
											// wird
					}
					ape.changeHealth(-damage); // damage muss negativ uebergeben werden, da in der Methode addiert wird
					System.out.println("Health of " + ape.getID() + " is " + ape.getHealth() + ". Damage was " + damage);
					damageApeTable.put(damage, ape);
				}
			}

			map.changeTurn();
			
			if (Launch.renderImages) {
				// Erzeuge DamageDisplays zur Schadens Visualisierung
				for (Entry<Integer, Ape> entry : damageApeTable.entrySet()) { 
				    Integer damage = entry.getKey();
				    Ape damagedApe = entry.getValue();
				    DamageDisplay display = new DamageDisplay(damagedApe, damage, 1500);
					entityManager.addEntity(Launch.GAMEPLAY_STATE, display);
				}
			}

			// Zeige Explosion
			AnimatedEntity explosion = new AnimatedEntity(Constants.EXPLOSION_ID, projectile.getCoordinates());
			if (Launch.renderImages) {
				Image[] images = new Image[4];
				try {
					images[0] = new Image("img/explosions/explosion1.png");
					images[1] = new Image("img/explosions/explosion2.png");
					images[2] = new Image("img/explosions/explosion3.png");
					images[3] = new Image("img/explosions/explosion4.png");
	
				} catch (SlickException e) {
					System.err.println("Problem with image for explosion");
				}
				explosion.setImages(images);
				explosion.scaleAndRotateAnimation(0.6f * projectile.getDamageRadius(), Utils.randomFloat(0, 360));
				explosion.addAnimation(0.012f, false); // TODO Scaling Faktor abhaenging von Bildschrimgroesse
			} else {
				System.out.println("noRenderImages: assign explosion animation images.");
			}
			entityManager.addEntity(Launch.GAMEPLAY_STATE, explosion); // TODO Explosions Entitaeten muessen wieder
																		// entfernt werden

		}
		if (Math.abs(projectile.getCoordinates().x) > 10 || Math.abs(projectile.getCoordinates().y) > 8) {
			// Zu weit ausserhalb des Bildes
			StateBasedEntityManager entityManager = StateBasedEntityManager.getInstance();
			Map map = Map.getInstance();
			entityManager.removeEntity(Launch.GAMEPLAY_STATE, projectile);
			map.changeTurn();
		}
	}

}
