package spaceapes;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.geom.Vector2f;
import java.text.DecimalFormat;

import eea.engine.action.Action;
import eea.engine.component.Component;

public class DisplayCoordinatesAction implements Action {

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
		Vector2f mousePixelPos = new Vector2f(gc.getInput().getMouseX(), gc.getInput().getMouseY());
		Vector2f mouseCoords = Utils.toWorldCoordinates(mousePixelPos);
		int px = (int) mousePixelPos.x;
		int py = (int) mousePixelPos.y;
		DecimalFormat formatter = new DecimalFormat("#.##");
		java.lang.System.out.println("World coords (" + formatter.format(mouseCoords.x) + ", "
				+ formatter.format(mouseCoords.y) + "); Pixel pos (" + px + ", " + py + ")");
	}
	
	//test comment

}
