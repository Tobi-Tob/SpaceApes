package adapter;

import org.newdawn.slick.geom.Vector2f;

import factories.ProjectileFactory.MovementType;
import map.Map;

public class AdapterExtended2 extends AdapterExtended1 {
	
	/**
	 * Use this constructor to set up everything you need.
	 */
	public AdapterExtended2() {
		super();
	}
	
	/* *************************************************** 
	 * *********************** Map ***********************
	 * *************************************************** */
	
	/**
	 * Erstellt eine Map - zusätzlicher Parameter ist die Angabe, ob non-player-planets erzeugt werden sollen
	 */
	public void createMap(Vector2f coordinatesPlanet1, Vector2f coordinatesPlanet2, float radiusPlanet1, float radiusPlanet2, int massPlanet1, int massPlanet2, boolean createNonPlayerPlanets, MovementType projectileMovementType, float angleOnPlanetApe1, float angleOnPlanetApe2) {
		Map.getInstance().parse(coordinatesPlanet1, coordinatesPlanet2, radiusPlanet1, radiusPlanet2, massPlanet1, massPlanet2, createNonPlayerPlanets, projectileMovementType, angleOnPlanetApe1, angleOnPlanetApe2);
		if (Map.getInstance() != null) {
			isMapCorrect = true;
		}
	}

}
