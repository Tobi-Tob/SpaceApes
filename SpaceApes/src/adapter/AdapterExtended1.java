package adapter;

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

}
