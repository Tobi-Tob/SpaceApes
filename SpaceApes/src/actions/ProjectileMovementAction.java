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
import spaceapes.GameplayState;
import utils.Utils;

public class ProjectileMovementAction implements Action {

	private Projectile projectile;

	public ProjectileMovementAction(Projectile projectile) {
		this.projectile = projectile;
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
		GameplayState gs = (GameplayState) sb.getCurrentState();
		StateBasedEntityManager entityManager = gs.getEntityManager();
		Map map = Map.getInstance();

		if (projectile.explizitEulerStep(delta)) {

			// Wenn Kollision mit einem Objekt, durch das es nicht hindurchfliegen kann
			// (z.B. Planet/Ape/...)
			entityManager.removeEntity(gs.getID(), projectile);

			for (Ape ape : map.getApes()) {
				float distanceApeToExplosion = ape.getWorldCoordinates().distance(projectile.getCoordinates());
				float distanceApeHitboxToExplosion = distanceApeToExplosion - ape.getRadiusInWorldUnits();

				if (distanceApeHitboxToExplosion <= projectile.getMaxDamageDistance()) {

					float damageFactor = 20f; // TODO hardcoded
					int damage = (int) (damageFactor
							* (1 - distanceApeHitboxToExplosion / projectile.getMaxDamageDistance()));

					ape.setHealth(ape.getHealth() - damage);
					System.out.println(
							"new healt of " + ape.getID() + " is " + ape.getHealth() + ". Damage was " + damage);

				}
			}

			map.changeTurn();

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

		}
		if (Math.abs(projectile.getCoordinates().x) > 10 || Math.abs(projectile.getCoordinates().y) > 8) {
			// Zu weit ausserhalb des Bildes
			entityManager.removeEntity(gs.getID(), projectile);
			map.changeTurn();
		}
	}

}
