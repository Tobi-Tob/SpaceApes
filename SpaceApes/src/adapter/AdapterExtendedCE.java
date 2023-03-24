package adapter;

// The following are just used for the implementation of the adapter-methodes and have to be removed for the student version
import entities.Planet;
import map.Map;

public class AdapterExtendedCE extends AdapterExtended1 {
	
	/**
	 * Use this constructor to set up everything you need.
	 */
	public AdapterExtendedCE() {
		super();
	}
	
	/**
	 * This method sets the factor to calculate the atmosphere radius for every planet
	 * 
	 * @param atmosphereRadiusFactor - if you multiply this be the planet radius you get the atmosphere radius
	 */
	public void setAtmosphereRadiusFactor(float atmosphereRadiusFactor) {
		for (Planet planet : Map.getInstance().getPlanets()) {
			planet.setAtmosphereRadiusFactor(atmosphereRadiusFactor);
		}
	}
	
	/**
	 * This method sets the indicator if air friction should be used when the projectile is within the atmosphere
	 * 
	 * @param useAirFriction - if air friction should be used
	 */
	public void useAirFriction(boolean useAirFriction) {
		Map.getInstance().useAirFriction(useAirFriction);
	}

}
