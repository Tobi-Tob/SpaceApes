package actions;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.action.Action;
import eea.engine.component.Component;
import entities.Planet;

public class DisplayPlanetInfoAction implements Action {

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
		Planet planet = (Planet) event.getOwnerEntity();
		java.lang.System.out.println(planet.getID());
		java.lang.System.out.println("Mass: " + planet.getMass() + " Radius: " + planet.getRadius());
		//test
	}

}
