package actions;

import java.util.HashMap;
import java.util.Map.Entry;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.action.Action;
import eea.engine.component.Component;
import eea.engine.entity.StateBasedEntityManager;
import entities.AnimatedEntity;
import entities.Ape;
import entities.DamageDisplay;
import entities.Projectile;
import factories.ProjectileFactory.ProjectileStatus;
import utils.Constants;
import utils.Resources;
import spaceapes.Map;
import spaceapes.SpaceApes;
import utils.Utils;

public class ProjectileBehaviorAction implements Action {

	private Projectile projectile;

	public ProjectileBehaviorAction(Projectile projectile) {
		this.projectile = projectile;
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {

		ProjectileStatus status = projectile.explizitEulerStep(delta);

		if (status != ProjectileStatus.flying) { // Kollision
			StateBasedEntityManager entityManager = StateBasedEntityManager.getInstance();
			Map map = Map.getInstance();

			entityManager.removeEntity(SpaceApes.GAMEPLAY_STATE, projectile);
			HashMap<Integer, Ape> damageApeTable = new HashMap<Integer, Ape>(); // Saving damage of this round

			for (Ape ape : map.getApes()) {
				float distanceApeToExplosion = ape.getWorldCoordinates().distance(projectile.getCoordinates());
				float distanceApeHitboxToExplosion = distanceApeToExplosion - ape.getRadiusInWorldUnits(); // bei direktem
																											// Treffer = 0
				int maxDamage = projectile.getMaxDamage();
				float damageRadius = projectile.getDamageRadius();
				if (distanceApeHitboxToExplosion <= damageRadius) { // Test ob die Explosion nah genug
																	// am Affen ist
					int damage = (int) Math.ceil(maxDamage * (1 - (distanceApeHitboxToExplosion / damageRadius))); // lineare
																													// Interpolation
					if (distanceApeHitboxToExplosion < 0.1f) {
						damage = maxDamage; // Stellt sicher, dass bei einem direkten Treffer maximaler Schaden verursacht
											// wird
					}
					ape.changeHealth(-damage); // damage muss negativ uebergeben werden, da in der Methode addiert wird
					damageApeTable.put(damage, ape);
				}
			}

			// Erzeuge DamageDisplays zur Schadens Visualisierung
			if (SpaceApes.renderImages) {
				Ape activeApe = map.getActiveApe();
				for (Entry<Integer, Ape> entry : damageApeTable.entrySet()) {
					Integer damage = entry.getKey();
					Ape damagedApe = entry.getValue();
					DamageDisplay display = new DamageDisplay(damagedApe, damage, 1500);
					entityManager.addEntity(SpaceApes.GAMEPLAY_STATE, display);
					// Updating statistics
					damagedApe.increaseDamageReceivedStatistics(damage);
					if (!damagedApe.equals(activeApe)) {
						activeApe.increaseDamageDealtStatistics(damage);
					}
				}
			}

			map.changeTurn();

			// Zeige Explosion
			if (SpaceApes.renderImages && status != ProjectileStatus.inBlackHole) {
				AnimatedEntity explosion = new AnimatedEntity(Constants.EXPLOSION_ID, projectile.getCoordinates());
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
				explosion.addAnimation(0.012f, false);
				entityManager.addEntity(SpaceApes.GAMEPLAY_STATE, explosion);

				// Explosion Sound
				if (SpaceApes.PLAY_SOUNDS) {
					Resources.EXPLOSION_SMALL_SOUND.play(1f, 1f);
				}
			}
			if (SpaceApes.renderImages && SpaceApes.PLAY_SOUNDS && status == ProjectileStatus.inBlackHole) {
				Resources.HIT_BALCK_HOLE_SOUND.play(1f, 1f);
			}

		}
		if (Math.abs(projectile.getCoordinates().x) > 10 || Math.abs(projectile.getCoordinates().y) > 8) {
			// Zu weit ausserhalb des Bildes
			StateBasedEntityManager entityManager = StateBasedEntityManager.getInstance();
			Map map = Map.getInstance();
			entityManager.removeEntity(SpaceApes.GAMEPLAY_STATE, projectile);
			map.changeTurn();
		}
	}

}
