package adapter;

import org.newdawn.slick.geom.Vector2f;

//The following are just used for the implementation of the adapter-methodes and have to be removed for the student version
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
	 * @param projectileMovementType - indicates the movement type of the projectiles. 0 means linear movemet, 1 means explicit euler
	 * @param angleOnPlanetApe1 - angle of Ape1 on its planet in degrees. In the "Ausbaustufe" 2 & 3 "999" is passed to indicate a random angle
	 * @param angleOnPlanetApe2 - angle of Ape2 on its planet in degrees. In the "Ausbaustufe" 2 & 3 "999" is passed to indicate a random angle
	 * @param gravitation - gravitation constant
	 */
	public void createMap(Vector2f coordinatesPlanet1, Vector2f coordinatesPlanet2, float radiusPlanet1, float radiusPlanet2, int massPlanet1, int massPlanet2, int projectileMovementType, float angleOnPlanetApe1, float angleOnPlanetApe2, float gravitation) {
		MovementType movementType;
		if (projectileMovementType == 1) {
			movementType = MovementType.EXPLICIT_EULER;
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
	 * This method sets the initial velocity for a shot of the ape
	 * 
	 * @param indexOfPlayer - index of player
	 * @param power - new shooting power in worldcoordinates per second
	 * @throws IllegalArgumentException - passes the exception when an invalid angle is given
	 */
	public void setShootingPowerOfApe(int indexOfPlayer, float power) throws IllegalArgumentException {
		try {
			Map.getInstance().getApes().get(indexOfPlayer).setThrowStrength(power);
		} catch (Exception e) {
			throw new IllegalArgumentException("Invalid shooting power: " + power);
		}
	}

}
