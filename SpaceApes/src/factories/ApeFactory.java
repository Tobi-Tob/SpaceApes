package factories;

import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import actions.DisplayApeInfoAction;
import actions.MoveOnPlanetAction;
import actions.ShootAction;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.ANDEvent;
import eea.engine.event.Event;
import eea.engine.event.basicevents.KeyDownEvent;
import eea.engine.event.basicevents.KeyPressedEvent;
import eea.engine.event.basicevents.MouseClickedEvent;
import eea.engine.event.basicevents.MouseEnteredEvent;
import entities.Ape;
import entities.Planet;
import factories.ProjectileFactory.MovementType;
import map.Map;
import spaceapes.Constants;
import spaceapes.SpaceApes;
import utils.Utils;

public abstract class ApeFactory {

	public static Ape createApe(String name, Planet homePlanet, float angleOnPlanet, int apeImageIndex, boolean isActive,
			boolean isInteractionAllowed) {

		final float scalingFactor = Constants.APE_DESIRED_SIZE / Utils.pixelLengthToWorldLength(Constants.APE_PIXEL_HEIGHT);
		final float distancePlanetCenter = homePlanet.getRadius()
				+ Utils.pixelLengthToWorldLength(Constants.APE_PIXEL_FEET_TO_CENTER * scalingFactor);
		if (distancePlanetCenter < 0.1f) {
			throw new RuntimeException("Radius ist zu nah an null");
		}
		
		Ape ape = new Ape(name);

		ape.setPlanet(homePlanet);
		homePlanet.setApe(ape);
		ape.setDistanceToPlanetCenter(distancePlanetCenter);
		ape.setAngleOnPlanet(angleOnPlanet);
		ape.setAngleOfView(0);
		ape.setHealth(Constants.APE_MAX_HEALTH);
		ape.setEnergy(Constants.APE_MAX_ENERGY);
		ape.setMovementSpeed(Constants.APE_MOVMENT_SPEED);
		ape.setThrowStrength(5f);
		ape.setActive(isActive);
		ape.setInteractionAllowed(isInteractionAllowed);
		ape.setPosition(Utils.toPixelCoordinates(ape.getWorldCoordinates()));
		ape.setScale(scalingFactor);
		ape.setRotation(angleOnPlanet + 90f);

		if (SpaceApes.renderImages) {
			try {
				ape.addComponent(new ImageRenderComponent(new Image("img/apes/ape" + apeImageIndex + ".png")));
			} catch (SlickException | RuntimeException e) {
				try {
					ape.addComponent(new ImageRenderComponent(new Image("img/apes/ape1.png")));
				} catch (SlickException e1) {
					System.err.println("Problem with image for ape");
				}
			}
		}
		
		// Zeige Informationen zum Ape, wenn auf ihn geklickt wird (nur wenn der Spieler
		// am Zug ist!)
		Event clickOnApeEvent = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		clickOnApeEvent.addAction(new DisplayApeInfoAction());
		ape.addComponent(clickOnApeEvent);

		// Bewege den Affen mit der rechten Pfeiltaste nach rechts (nur wenn der Spieler
		// am Zug ist!)
		Event rightKeyPressed = new KeyDownEvent(Input.KEY_RIGHT);
		rightKeyPressed.addAction(new MoveOnPlanetAction(1.0f, ape));
		ape.addComponent(rightKeyPressed);

		// Bewege den Affen mit der linken Pfeiltaste nach links... (nur wenn der
		// Spieler am Zug ist!)
		// und erzeuge eine Ziellinie
		Event leftKeyPressed = new KeyDownEvent(Input.KEY_LEFT);
		leftKeyPressed.addAction(new MoveOnPlanetAction(-1.0f, ape));
		ape.addComponent(leftKeyPressed);

		// Scheisse mit der Leertaste (nur wenn der Spieler am Zug ist!)
		Event spaceKeyPressed = new KeyPressedEvent(Input.KEY_SPACE);
		spaceKeyPressed.addAction(new ShootAction(MovementType.EXPLICIT_EULER));
		ape.addComponent(spaceKeyPressed);
		
		// Zuweisung zur Map und zum EntityManager
		Map.getInstance().addApe(ape);
		StateBasedEntityManager.getInstance().addEntity(SpaceApes.GAMEPLAY_STATE, ape);
		
		return ape;
	}

}
