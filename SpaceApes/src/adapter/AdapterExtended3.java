package adapter;

import java.util.List;

import org.newdawn.slick.Music;
import org.newdawn.slick.geom.Vector2f;

//The following are just used for the implementation of the adapter-methodes and have to be removed for the student version
import entities.Item;
import entities.Planet;
import factories.PlanetFactory.PlanetType;
import factories.ProjectileFactory.MovementType;
import factories.ProjectileFactory.ProjectileType;
import map.Map;
import spaceapes.SpaceApes;
import spaceapes.MainMenuState;

public class AdapterExtended3 extends AdapterExtended2 {
	
	/**
	 * Use this constructor to set up everything you need.
	 */
	public AdapterExtended3() {
		super();
	}
	
	/**
	 * This method creates a new map - additionally to the createMap-method in AdapterExtended2 the boolean if an anti-planet and a black hole should be created is passed
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
	public void createMap(Vector2f coordinatesPlanet1, Vector2f coordinatesPlanet2, float radiusPlanet1, float radiusPlanet2, int massPlanet1, int massPlanet2, boolean createNonPlayerPlanets, int projectileMovementType, float angleOnPlanetApe1, float angleOnPlanetApe2, float gravitation, boolean antiPlanetAndBlackHole) {
		MovementType movementType;
		if (projectileMovementType == 1) {
			movementType = MovementType.EXPLICIT_EULER;
		} else {
			movementType = MovementType.LINEAR;
		}
		Map.getInstance().parse(coordinatesPlanet1, coordinatesPlanet2, radiusPlanet1, radiusPlanet2, massPlanet1, massPlanet2, createNonPlayerPlanets, movementType, angleOnPlanetApe1, angleOnPlanetApe2, antiPlanetAndBlackHole);
		if (Map.getInstance() != null) {
			isMapCorrect = true;
		}
	}
	
	/**
	 * 
	 * @return returns the Music of the main menu state
	 */
	public Music getMainMenuMusic() {
		return ((MainMenuState) getStateBasedGame().getState(SpaceApes.MAINMENU_STATE)).getMusic();
	}
	
	/**
	 * 
	 * @return returns the current Item count
	 */
	public int getItemCount() {
		return Map.getInstance().getItems().size();
	}
	
	/**
	 * 
	 * @return returns the minimal distance between an item and a planet
	 */
	public float getMinimalDistanceItemToPlanet() {
		float minimalDistance = 99f;
		float currentDistance = 99f;
		List<Item> items = Map.getInstance().getItems();
		List<Planet> planets = Map.getInstance().getPlanets();
		for (int i = 0; i < planets.size(); i++) {
			for (int j = 0; j < items.size(); j++) {
				currentDistance = planets.get(i).getCoordinates().distance(items.get(j).getCoordinates());
				if (currentDistance < minimalDistance) {
					minimalDistance = currentDistance;
				}
			}
		}
		return minimalDistance;
	}
	
	/**
	 * This method selects a projectile, which costs more than zero
	 */
	public void selectExpensiveProjectile() {
		Map.getInstance().getControlPanel().nextShopProjectil();
		if (Map.getInstance().getControlPanel().getSelectedProjectile().getType() == ProjectileType.COCONUT) {
			Map.getInstance().getControlPanel().nextShopProjectil();
		}
	}
	
	/**
	 * 
	 * @return returns the price of the currently selected projectile
	 */
	public int getSelectedProjectilePrice() {
		return Map.getInstance().getControlPanel().getSelectedProjectile().getPrice();
	}
	
	/**
	 * This method sets the number of coins for the given player
	 * 
	 * @param indexOfPlayer - index of player
	 * @param coins - new number of coins for the given player
	 */
	public void setApeCoins(int indexOfPlayer, int coins) {
		Map.getInstance().getApes().get(indexOfPlayer).setCoins(coins);
	}
	
	/**
	 * This method returns the number of coins of the player with the given index
	 * 
	 * @param indexOfPlayer - index of player
	 * @return returns the number of coins of the player with the given index
	 */
	public int getApeCoins(int indexOfPlayer) {
		return Map.getInstance().getApes().get(indexOfPlayer).getCoins();
	}
	
	/**
	 * @return returns the number of black holes
	 */
	public int getBlackHoleCount() {
		int blackHoleCount = 0;
		for (Planet planet : Map.getInstance().getPlanets()) {
			if (planet.getPlanetType() == PlanetType.BLACKHOLE) {
				blackHoleCount++;
			}
		}
		return blackHoleCount;
	}
	
	/**
	 * @return returns the mass of the black hole. 0 if no black hole is created
	 */
	public int getBlackHoleMass() {
		int blackHoleMass = 0;
		for (Planet planet : Map.getInstance().getPlanets()) {
			if (planet.getPlanetType() == PlanetType.BLACKHOLE) {
				blackHoleMass = planet.getMass();
			}
		}
		return blackHoleMass;
	}
	
	/**
	 * @return returns the number of anti planets
	 */
	public int getAntiPlanetCount() {
		int antiPlanetCount = 0;
		for (Planet planet : Map.getInstance().getPlanets()) {
			if (planet.getPlanetType() == PlanetType.BLACKHOLE) {
				antiPlanetCount++;
			}
		}
		return antiPlanetCount;
	}
	
	/**
	 * @return returns the mass of the anti planet. 0 if no anti planet is created
	 */
	public int getAntiPlanetMass() {
		int antiPlanetMass = 0;
		for (Planet planet : Map.getInstance().getPlanets()) {
			if (planet.getPlanetType() == PlanetType.ANTI) {
				antiPlanetMass = planet.getMass();
			}
		}
		return antiPlanetMass;
	}

}
