package adapter;

import java.util.List;

import org.newdawn.slick.geom.Vector2f;

//The following are just used for the implementation of the adapter-methodes and have to be removed for the student version
import entities.Planet;
import factories.ProjectileFactory.MovementType;
import map.Map;

public class AdapterExtended2 extends AdapterExtended1 {
	
	/**
	 * Use this constructor to set up everything you need.
	 */
	public AdapterExtended2() {
		super();
	}
	
	/**
	 * This method creates a new map - additionally to the createMap-method in AdapterMinimal the boolean if non-player-planets should be created is passed
	 * 
	 * @param coordinatesPlanet1 - coordinates of Planet1. In the "Ausbaustufe" 2 & 3 "null" is passed to indicate a random position as explained in the task
	 * @param coordinatesPlanet2 - coordinates of Planet2. In the "Ausbaustufe" 2 & 3 "null" is passed to indicate a random position as explained in the task
	 * @param radiusPlanet1 - radius of Planet1. In the "Ausbaustufe" 2 & 3 "0" is passed to indicate a random radius as explained in the task
	 * @param radiusPlanet2 - radius of Planet2. In the "Ausbaustufe" 2 & 3 "0" is passed to indicate a random radius as explained in the task
	 * @param massPlanet1 - mass of Planet1. In the "Ausbaustufe" 2 & 3 "0" is passed to indicate a random mass as explained in the task
	 * @param massPlanet2 - mass of Planet1. In the "Ausbaustufe" 2 & 3 "0" is passed to indicate a random mass as explained in the task
	 * @param createNonPlayerPlanets - indication if non-player-planets should be created
	 * @param projectileMovementType - indicates the movement type of the projectiles. 0 means linear movemet, 1 means explicit euler
	 * @param angleOnPlanetApe1 - angle of Ape1 on its planet in degrees. In the "Ausbaustufe" 2 & 3 "999" is passed to indicate a random angle
	 * @param angleOnPlanetApe2 - angle of Ape2 on its planet in degrees. In the "Ausbaustufe" 2 & 3 "999" is passed to indicate a random angle
	 * @param gravitation - gravitation constant
	 */
	public void createMap(Vector2f coordinatesPlanet1, Vector2f coordinatesPlanet2, float radiusPlanet1, float radiusPlanet2, int massPlanet1, int massPlanet2, boolean createNonPlayerPlanets, int projectileMovementType, float angleOnPlanetApe1, float angleOnPlanetApe2, float gravitation) {
		MovementType movementType;
		if (projectileMovementType == 1) {
			movementType = MovementType.EXPLICIT_EULER;
		} else {
			movementType = MovementType.LINEAR;
		}
		Map.getInstance().parse(coordinatesPlanet1, coordinatesPlanet2, radiusPlanet1, radiusPlanet2, massPlanet1, massPlanet2, createNonPlayerPlanets, movementType, angleOnPlanetApe1, angleOnPlanetApe2, false);
		if (Map.getInstance() != null) {
			isMapCorrect = true;
		}
	}
	
	/**
	 * 
	 * @return returns the minimal distance of two planets
	 */
	public float getMinimalDistancePlanets() {
		float minimalDistance = 99f;
		float currentDistance = 99f;
		List<Planet> planets = Map.getInstance().getPlanets();
		for (int i = 0; i < planets.size(); i++) {
			for (int j = 0; j < planets.size(); j++) {
				if (i==j) {
					j++;
				}
				if (j >= planets.size()) {
					break;
				}
				currentDistance = planets.get(i).getCoordinates().distance(planets.get(j).getCoordinates());
				if (currentDistance < minimalDistance) {
					minimalDistance = currentDistance;
				}
			}
		}
		return minimalDistance;
	}
	
	/**
	 * 
	 * @param indexOfPlayer - index of player
	 * @return returns the energy level of the ape of the player with the given index
	 */
	public float getApeEnergy(int indexOfPlayer) {
		return Map.getInstance().getApes().get(indexOfPlayer).getEnergy();
	}

}
