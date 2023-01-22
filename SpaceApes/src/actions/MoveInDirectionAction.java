package actions;

import org.newdawn.slick.geom.Vector2f;
import eea.engine.action.Action;
import eea.engine.action.basicactions.Movement;

// wird nicht benoetigt
public class MoveInDirectionAction extends Movement implements Action {

	public MoveInDirectionAction(float currentSpeed) {
		super(currentSpeed);
	}

	@Override
	public Vector2f getNextPosition(Vector2f pos, float speed, float rotation, int delta) {
		float s = speed * delta; // s = v*t
		double directionInRad = Math.toRadians(rotation);
		float dx = s * (float) Math.cos(directionInRad);
		float dy = s * (float) Math.sin(directionInRad);
		return new Vector2f(pos.x + dx, pos.y + dy);
	};
}
