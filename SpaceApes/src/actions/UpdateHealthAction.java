package actions;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.action.Action;
import eea.engine.component.Component;
import entities.Ape;
import entities.Projectile;
import map.Map;

public class UpdateHealthAction implements Action {

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {

		Map map = Map.getInstance();

		for (Ape ape : map.getApes()) { // Wirkt sehr ineffizent TODO
			// Man braucht wahrscheinlichkeine doppelte for Schleife die dauerhaft testet,
			// sondern nur einen funktionsaufruf wenn explizitEuler eine Kollision
			// detektiert und true zurück gibt
			for (Projectile projectile : map.getLivingProjectiles()) {
				float distanceApeToExplosion = ape.getWorldCoordinates().distance(projectile.getCoordinates());
				float distanceApeHitboxToExplosion = distanceApeToExplosion - ape.getRadiusInWorldUnits();

				if (distanceApeHitboxToExplosion > projectile.getMaxDamageDistance()) {

					continue; // Projektil verursacht keinen Schaden

				} else {

					float damageFactor = 20f;
					int damage = (int) (damageFactor * distanceApeHitboxToExplosion / projectile.getMaxDamageDistance());

					ape.setHealth(ape.getHealth() - damage);
					System.out
							.println("new healt of " + ape.getID() + " is " + ape.getHealth() + ". Damage was " + damage);

				}

			}
		}

		map.setProjectileExploded(false);

	}

}
