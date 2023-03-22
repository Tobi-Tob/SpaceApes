package adapter;

import entities.Projectile;
import factories.ProjectileFactory.MovementType;
import map.Map;

public class AdapterExtended1 extends AdapterMinimal {
	
	/**
	 * Use this constructor to set up everything you need.
	 */
	public AdapterExtended1() {
		super();
	}
	
	/* *************************************************** 
	 * ******************* Shooting **********************
	 * *************************************************** */
	
	/**
	 * 
	 * @param indexOfPlayer - index of player
	 * @param angle - new shooting angle
	 * @throws IllegalArgumentException - passes the exception when an invalid angle is given
	 */
	public void setShootingAngleOfApe(int indexOfPlayer, float angle) throws IllegalArgumentException {
		try {
			Map.getInstance().getApes().get(indexOfPlayer).setAngleOfView(angle);
		} catch (Exception e) {
			throw new IllegalArgumentException("Invalid shooting angle: " + angle);
		}
	}
	
	/**
	 * 
	 * @param indexOfPlayer - index of player
	 * @param power - new shooting power
	 * @throws IllegalArgumentException - passes the exception when an invalid angle is given
	 */
	public void setShootingPowerOfApe(int indexOfPlayer, float power) throws IllegalArgumentException {
		try {
			Map.getInstance().getApes().get(indexOfPlayer).setThrowStrength(power);
		} catch (Exception e) {
			throw new IllegalArgumentException("Invalid shooting power: " + power);
		}
	}
	
	/**
	 * This method does one step of explicit euler movement for the given projectile. The width of the step depends on the given time delta.
	 * 
	 * @param projectile - the projectile which is used for the explicit euler movement step
	 * @param timeDelta - time used for the explicit euler movement step
	 * @return returns true if a projectile collided with a planet or ape
	 */
	public boolean doExplicitEulerStep(Projectile projectile, int timeDelta) {
		return projectile.explizitEulerStep(timeDelta);
	}

}
