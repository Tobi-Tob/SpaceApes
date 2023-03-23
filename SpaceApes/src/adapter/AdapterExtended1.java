package adapter;

import org.newdawn.slick.geom.Vector2f;

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
	
	/**
	 * This method creates a new map - additionally to the createMap-method in AdapterMinimal the parameter to decide which movement type to use is passed
	 * 
	 * @param coordinatesPlanet1 - coordinates of Planet1. In the "Ausbaustufe" 2 & 3 "null" is passed to indicate a random position as explained in the task
	 * @param coordinatesPlanet2 - coordinates of Planet2. In the "Ausbaustufe" 2 & 3 "null" is passed to indicate a random position as explained in the task
	 * @param radiusPlanet1 - radius of Planet1. In the "Ausbaustufe" 2 & 3 "0" is passed to indicate a random radius as explained in the task
	 * @param radiusPlanet2 - radius of Planet2. In the "Ausbaustufe" 2 & 3 "0" is passed to indicate a random radius as explained in the task
	 * @param massPlanet1 - mass of Planet1. In the "Ausbaustufe" 2 & 3 "0" is passed to indicate a random mass as explained in the task
	 * @param massPlanet2 - mass of Planet1. In the "Ausbaustufe" 2 & 3 "0" is passed to indicate a random mass as explained in the task
	 * @param projectileMovementType - indicates the movement type of the projectiles. 0 means linear movemet, 1 means explicit euler, 2 means explicit euler with atmosphere friction (only CE task)
	 * @param angleOnPlanetApe1 - angle of Ape1 on its planet. In the "Ausbaustufe" 2 & 3 "999" is passed to indicate a random angle
	 * @param angleOnPlanetApe2 - angle of Ape2 on its planet. In the "Ausbaustufe" 2 & 3 "999" is passed to indicate a random angle
	 */
	public void createMap(Vector2f coordinatesPlanet1, Vector2f coordinatesPlanet2, float radiusPlanet1, float radiusPlanet2, int massPlanet1, int massPlanet2, int projectileMovementType, float angleOnPlanetApe1, float angleOnPlanetApe2) {
		MovementType movementType;
		if (projectileMovementType == 1) {
			movementType = MovementType.EXPLICIT_EULER;
		} else if (projectileMovementType == 2) {
			movementType = MovementType.EULER_FRICTION;
		} else {
			movementType = MovementType.LINEAR;
		}
		Map.getInstance().parse(coordinatesPlanet1, coordinatesPlanet2, radiusPlanet1, radiusPlanet2, massPlanet1, massPlanet2, false, movementType, angleOnPlanetApe1, angleOnPlanetApe2, false);
		if (Map.getInstance() != null) {
			isMapCorrect = true;
		}
	}
	
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
