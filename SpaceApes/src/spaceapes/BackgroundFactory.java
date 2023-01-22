package spaceapes;

import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.event.ANDEvent;
import eea.engine.event.Event;
import eea.engine.event.basicevents.KeyPressedEvent;
import eea.engine.event.basicevents.MouseClickedEvent;
import eea.engine.interfaces.IEntityFactory;

public class BackgroundFactory implements IEntityFactory {
	
	@Override
	public Entity createEntity() {
		
		Entity background = new Entity("background");
		
		// Startposition des Hintergrunds (Mitte des Fensters)
		background.setPosition(Utils.toPixelCoordinates(0, 0));
		try {
			Image image = new Image("/assets/space1.jpg");
			background.addComponent(new ImageRenderComponent(new Image("/assets/space1.jpg")));
			background.setScale((float) Launch.HEIGHT / image.getHeight());
		} catch (SlickException e) {
			System.err.println("Cannot find image for background");
		}
		
		//MR Warum funktioniert die Scheisse nicht!!???
		Event mouseAndShiftPressed = new ANDEvent(new KeyPressedEvent(Input.KEY_LSHIFT), new MouseClickedEvent());
		mouseAndShiftPressed.addAction(new DisplayCoordinatesAction());
		background.addComponent(mouseAndShiftPressed);
		
		return background;
	}
	
}
