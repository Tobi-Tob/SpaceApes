package spaceapes;

import org.newdawn.slick.geom.Vector2f;
import eea.engine.action.Action;
import eea.engine.action.basicactions.Movement;

// wird nicht benoetigt
public class StepRightOnPlanetAction extends Movement implements Action{
	public Planet planet;


	public StepRightOnPlanetAction(float currentSpeed, Planet p) {
		super(currentSpeed);
		this.planet = p;
	}


	@Override
	public Vector2f getNextPosition(Vector2f arg0, float arg1, float arg2, int arg3) {
		return null;
	};
}
