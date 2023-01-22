package factories;

import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import actions.AimlineAction;
import actions.DisplayApeInfoAction;
import actions.MoveOnPlanetAction;
import actions.ShootAction;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.event.ANDEvent;
import eea.engine.event.Event;
import eea.engine.event.basicevents.KeyDownEvent;
import eea.engine.event.basicevents.LoopEvent;
import eea.engine.event.basicevents.MouseClickedEvent;
import eea.engine.event.basicevents.MouseEnteredEvent;
import eea.engine.interfaces.IEntityFactory;
import entities.Ape;
import entities.Planet;
import utils.Utils;

public class ApeFactory implements IEntityFactory {

	private final String name;
	private final Planet homePlanet;
	private final int health;
	private final int energy;
	private final int apeImage;
	private final boolean isActive;
	private final boolean isInteractionAllowed;
	private final float angleOnPlanet; 
	private final float angleOfView; 
	private final float throwStrength; 
	private final float movementSpeed; 
	private final float distancePlanetCenter; 
		
	public final float apePixelHeight = 300;
	public final float pixelfromFeetToCenter = 130;
	public final float desiredApeSizeInWorldUnits = 0.6f;
	public final float scalingFactor = desiredApeSizeInWorldUnits / Utils.pixelLengthToWorldLength(apePixelHeight);
	
	public ApeFactory(String name, Planet homePlanet, int health, int energy,
			int apeImage, boolean isActive, boolean isInteractionAllowed, float movementSpeed,
			float angleOnPlanet, float angleOfView, float throwStrength, float distancePlanetCenter) {
		this.name = name;
		this.homePlanet = homePlanet;
		this.health = health;
		this.energy = energy;
		this.apeImage = apeImage;
		this.isActive = isActive;
		this.isInteractionAllowed = isInteractionAllowed;
		this.movementSpeed = movementSpeed;
		this.angleOnPlanet = angleOnPlanet;
		this.angleOfView = angleOfView;
		this.throwStrength = throwStrength;
		this.distancePlanetCenter = distancePlanetCenter;
	}
	
	@Override
	public Entity createEntity() {
		
		Ape ape = new Ape(name);
		
		ape.setPlanet(homePlanet);
		homePlanet.setApe(ape);
		ape.setHealth(health);
		ape.setEnergy(energy);
		ape.setActive(isActive);
		ape.setInteractionAllowed(isInteractionAllowed);
		ape.setMovementSpeed(movementSpeed);
		ape.setAngleOnPlanet(angleOnPlanet);
		ape.setAngleOfView(angleOfView);
		ape.setThrowStrength(throwStrength);
		ape.setDistanceToPlanetCenter(distancePlanetCenter);
		ape.setPosition(Utils.toPixelCoordinates(ape.getCoordinates()));
		ape.setScale(scalingFactor);
		ape.setRotation(angleOnPlanet + 90f);
		
		try {
			ape.addComponent(new ImageRenderComponent(new Image("/assets/ape" + apeImage + ".png")));
		} catch (SlickException | RuntimeException e) {
			try {
				ape.addComponent(new ImageRenderComponent(new Image("/assets/ape1.png")));
			} catch (SlickException e1) {
				System.err.println("Cannot find image for ape");
			}
		}
		
		// Zeige Informationen zum Ape, wenn auf ihn geklickt wird (nur wenn der Spieler am Zug ist!)
		Event clickOnApeEvent = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		clickOnApeEvent.addAction(new DisplayApeInfoAction());
		ape.addComponent(clickOnApeEvent);
		
		// Bewege den Affen mit der rechten Pfeiltaste nach rechts (nur wenn der Spieler am Zug ist!)
		Event rightKeyPressed = new KeyDownEvent(Input.KEY_RIGHT);
		rightKeyPressed.addAction(new MoveOnPlanetAction(1.0f));
		ape.addComponent(rightKeyPressed);
		
		// Bewege den Affen mit der linken Pfeiltaste nach links... (nur wenn der Spieler am Zug ist!)
		// und erzeuge eine Ziellinie
		Event leftKeyPressed = new KeyDownEvent(Input.KEY_LEFT);
		leftKeyPressed.addAction(new MoveOnPlanetAction(-1.0f)); //MR: float durch bool ersetzen!
		ape.addComponent(leftKeyPressed);
		
		// Konfigiriere je nach Interaktion die Ziellinie des Affen (nur wenn der Spieler am Zug ist!)
		LoopEvent aimLoop = new LoopEvent();
		aimLoop.addAction(new AimlineAction());
		ape.addComponent(aimLoop);
		
		// Schieße mit der Leertaste (nur wenn der Spieler am Zug ist!)
		Event spaceKeyPressed = new KeyDownEvent(Input.KEY_SPACE);
		spaceKeyPressed.addAction(new ShootAction());
		ape.addComponent(spaceKeyPressed);
				
		return ape;
	}

}
