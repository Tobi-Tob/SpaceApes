package actions;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.action.Action;
import eea.engine.component.Component;
import eea.engine.entity.StateBasedEntityManager;
import entities.AnimatedEntity;
import entities.Ape;
import entities.Projectile;
import map.Map;
import spaceapes.Constants;
import spaceapes.GameplayState;
import utils.Utils;

public class ProjectileBehaviorAction implements Action {

	private Projectile projectile;

	public ProjectileBehaviorAction(Projectile projectile) {
		this.projectile = projectile;
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
		GameplayState gs = (GameplayState) sb.getCurrentState();
		StateBasedEntityManager entityManager = gs.getEntityManager();
		Map map = Map.getInstance();

		if (projectile.explizitEulerStep(delta)) { // Berechnet so lange den naechsten Schritt, bis Kollision auftritt und
													// explizitEulerStep true zurueck gibt
			// Im Kollisionsfall:
			entityManager.removeEntity(gs.getID(), projectile);

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
						damage = maxDamage; // Stellt sicher, dass bei einem direkten Treffer immer maximaler Schaden
											// verursacht wird
					}
					ape.changeHealth(-damage); //damage muss negativ uebergeben werden, da in der Methode addiert wird
					System.out.println("Health of " + ape.getID() + " is " + ape.getHealth() + ". Damage was " + damage);
//					if (ape.getHealth() <= 0) {
//						break; // Schleife darf nicht weiter durchlaufen werden wenn ein Affe gestorben ist, da
//								// sich die momentan zu durchlaufende Liste veraendert hat
//					//MR jetzt darf das nicht mehr abgefangen werden!! sonst bekommt nur ein Affe Schaden
//					}
				}
			}

			map.changeTurn();

			// Zeige Explosion
			AnimatedEntity explosion = new AnimatedEntity(Constants.EXPLOSION, projectile.getCoordinates());
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
			explosion.scaleAndRotateAnimation(0.3f, Utils.randomFloat(0, 360));
			explosion.addAnimation(0.012f, false); // TODO Scaling Faktor abhaenging von Bildschrimgroesse
			entityManager.addEntity(gs.getID(), explosion); // TODO Explosions Entitaeten muessen wieder entfernt werden

		}
		if (Math.abs(projectile.getCoordinates().x) > 10 || Math.abs(projectile.getCoordinates().y) > 8) {
			// Zu weit ausserhalb des Bildes
			entityManager.removeEntity(gs.getID(), projectile);
			map.changeTurn();
		}
	}

}
