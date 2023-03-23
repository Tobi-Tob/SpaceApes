package factories;

import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import actions.DisplayApeInfoAction;
import actions.MoveOnPlanetAction;
import actions.ShootAction;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.event.ANDEvent;
import eea.engine.event.Event;
import eea.engine.event.basicevents.KeyDownEvent;
import eea.engine.event.basicevents.KeyPressedEvent;
import eea.engine.event.basicevents.MouseClickedEvent;
import eea.engine.event.basicevents.MouseEnteredEvent;
import eea.engine.interfaces.IEntityFactory;
import entities.Ape;
import entities.Planet;
import factories.ProjectileFactory.MovementType;
import spaceapes.Constants;
import spaceapes.SpaceApes;
import utils.Utils;

public class ApeFactory implements IEntityFactory {

	private final String name;
	private final Planet homePlanet;
	private final int health;
	private final int energy;
	private final int apeImageIndex;
	private final boolean isActive;
	private final boolean isInteractionAllowed;
	private final float angleOnPlanet;
	private final float angleOfView;
	private final float throwStrength;
	private final float movementSpeed;
	private final MovementType projectileMovementType;

	public final float scalingFactor = Constants.APE_DESIRED_SIZE / Utils.pixelLengthToWorldLength(Constants.APE_PIXEL_HEIGHT);
	private final float distancePlanetCenter;

	public ApeFactory(String name, Planet homePlanet, int health, int energy, int apeImageIndex, boolean isActive,
			boolean isInteractionAllowed, float movementSpeed, float angleOnPlanet, float angleOfView,
			float throwStrength, MovementType projectileMovementType) {
		this.name = name;
		this.homePlanet = homePlanet;
		this.health = health;
		this.energy = energy;
		this.apeImageIndex = apeImageIndex;
		this.isActive = isActive;
		this.isInteractionAllowed = isInteractionAllowed;
		this.movementSpeed = movementSpeed;
		this.angleOnPlanet = angleOnPlanet;
		this.angleOfView = angleOfView;
		this.throwStrength = throwStrength;
		this.distancePlanetCenter = homePlanet.getRadius()
				+ Utils.pixelLengthToWorldLength(Constants.APE_PIXEL_FEET_TO_CENTER * scalingFactor);
		if (distancePlanetCenter < 0.1f) {
			throw new RuntimeException("Radius ist zu nah an null");
		}
		this.projectileMovementType = projectileMovementType;
	}

	@Override
	public Ape createEntity() {

		Ape ape = new Ape(name);

		ape.setPlanet(homePlanet);
		homePlanet.setApe(ape);
		ape.setDistanceToPlanetCenter(distancePlanetCenter);
		ape.setAngleOnPlanet(angleOnPlanet);
		ape.setAngleOfView(angleOfView);
		ape.setHealth(health);
		ape.setEnergy(energy);
		ape.setMovementSpeed(movementSpeed);
		ape.setThrowStrength(throwStrength);
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
		} else {
			//System.out.println("noRenderImages: assign ape image.");
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
		spaceKeyPressed.addAction(new ShootAction(projectileMovementType));
		ape.addComponent(spaceKeyPressed);

		return ape;
	}

}
