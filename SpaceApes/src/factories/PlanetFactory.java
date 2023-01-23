package factories;

import org.newdawn.slick.geom.Vector2f;

import actions.DisplayPlanetInfoAction;

import java.util.Random;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.interfaces.IEntityFactory;
import entities.Planet;
import utils.Utils;
import eea.engine.event.basicevents.*;
import eea.engine.event.*;

public class PlanetFactory implements IEntityFactory {

	public enum PlanetType {PLAYER, BLACKHOLE, ANTI, NORMAL};
	
	private final String name;
	private final float radius;
	private final int mass;
	private final Vector2f coordinates; // In Welt-Koordinaten
	private final boolean ringImagePossible;
	private final PlanetType type;
	
	public PlanetFactory(String name, float radius, int mass, Vector2f coordinates, PlanetType type) {
		this.name = name;
		this.radius = radius;
		this.mass = mass;
		this.coordinates = coordinates;
		this.type = type;
		if(type == PlanetType.NORMAL) {
			this.ringImagePossible = true;
		} else {
			this.ringImagePossible = false;
		}
	}
	
	@Override
	public Entity createEntity() {
		
		Planet planet = new Planet(name);
		
		planet.setPassable(false);
		planet.setCoordinates(coordinates);
		planet.setPosition(Utils.toPixelCoordinates(coordinates));
		planet.setMass(mass);
		planet.setRadius(radius);
		planet.setRotation(Utils.randomFloat(-30, 30));
		
		if (type == PlanetType.PLAYER) {
			
			try {
				addRandomImageToPlanet(planet, ringImagePossible);
			} catch (SlickException e) {
				System.err.println("Cannot find image for planet");
			}
			
			
		} else if (type == PlanetType.BLACKHOLE) {
			
			float blackHoleRadiusInPixel = 60;
			float blackHoleRadiusInWorldUnits = Utils.pixelLengthToWorldLength(blackHoleRadiusInPixel);
			planet.setScale(radius / blackHoleRadiusInWorldUnits);
			planet.setRotation(0);
			
			try {
				planet.addComponent(new ImageRenderComponent(new Image("/assets/blackhole1.png")));
			} catch (SlickException e) {
				System.err.println("Cannot find image for black hole");
			}
			
			
		} else if (type == PlanetType.ANTI) {
			
			float planetRadiusInPixel = 230;
			float planetRadiusInWorldUnits = Utils.pixelLengthToWorldLength(planetRadiusInPixel);
			planet.setScale(radius / planetRadiusInWorldUnits);
			
			try {
				planet.addComponent(new ImageRenderComponent(new Image("/assets/planet_anti1.png")));
			} catch (SlickException e) {
				System.err.println("Cannot find image for planet");
			}
			
			
		} else if (type == PlanetType.NORMAL) {
			
			try {
				addRandomImageToPlanet(planet, ringImagePossible);
			} catch (SlickException e) {
				System.err.println("Cannot find image for planet");
			}
			
			
		} else {
			
			throw new IllegalArgumentException("Invalid planet type: " + type.toString());
		}
		
		
		
		
		// Zeige Planeteninformationen, wenn auf ihn geklickt wird
		Event clickOnPlanetEvent = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		clickOnPlanetEvent.addAction(new DisplayPlanetInfoAction());
		planet.addComponent(clickOnPlanetEvent);
		
		return planet;
	}
	
	/**
	 * Fuegt Planeten ein zufaelliges Bild hinzu und skaliert dieses individuell
	 * 
	 * @param planet
	 * @param ringsAllowed true, wenn Planetenbilder mit Ringen verwedet werden
	 *                     duerfen
	 * @throws SlickException
	 */
	public void addRandomImageToPlanet(Planet planet, boolean ringsAllowed) throws SlickException {
		Random r = new Random();
		int imageNumber = r.nextInt(5) + 1; // Integer im Intervall [1, 5]
		if (ringsAllowed) {
			imageNumber = r.nextInt(8) + 1; // Integer im Intervall [1, 8]
		}

		switch (imageNumber) {
		default: // Eqivalent zu case 1
			planet.addComponent(new ImageRenderComponent(new Image("/assets/planet1.png")));
			float planetRadiusInPixel = 235;
			float planetRadiusInWorldUnits = Utils.pixelLengthToWorldLength(planetRadiusInPixel);
			planet.setScale(planet.getRadius() / planetRadiusInWorldUnits);
			break;
		case 2:
			planet.addComponent(new ImageRenderComponent(new Image("/assets/planet2.png")));
			planetRadiusInPixel = 230;
			planetRadiusInWorldUnits = Utils.pixelLengthToWorldLength(planetRadiusInPixel);
			planet.setScale(planet.getRadius() / planetRadiusInWorldUnits);
			break;
		case 3:
			planet.addComponent(new ImageRenderComponent(new Image("/assets/planet3.png")));
			planetRadiusInPixel = 242;
			planetRadiusInWorldUnits = Utils.pixelLengthToWorldLength(planetRadiusInPixel);
			planet.setScale(planet.getRadius() / planetRadiusInWorldUnits);
			break;
		case 4:
			planet.addComponent(new ImageRenderComponent(new Image("/assets/planet4.png")));
			planetRadiusInPixel = 242;
			planetRadiusInWorldUnits = Utils.pixelLengthToWorldLength(planetRadiusInPixel);
			planet.setScale(planet.getRadius() / planetRadiusInWorldUnits);
			break;
		case 5:
			planet.addComponent(new ImageRenderComponent(new Image("/assets/planet5.png")));
			planetRadiusInPixel = 222;
			planetRadiusInWorldUnits = Utils.pixelLengthToWorldLength(planetRadiusInPixel);
			planet.setScale(planet.getRadius() / planetRadiusInWorldUnits);
			break;
		case 6:
			planet.addComponent(new ImageRenderComponent(new Image("/assets/ring_planet1.png")));
			planetRadiusInPixel = 210;
			planetRadiusInWorldUnits = Utils.pixelLengthToWorldLength(planetRadiusInPixel);
			planet.setScale(planet.getRadius() / planetRadiusInWorldUnits);
			break;
		case 7:
			planet.addComponent(new ImageRenderComponent(new Image("/assets/ring_planet2.png")));
			planetRadiusInPixel = 230;
			planetRadiusInWorldUnits = Utils.pixelLengthToWorldLength(planetRadiusInPixel);
			planet.setScale(planet.getRadius() / planetRadiusInWorldUnits);
			break;
		case 8:
			planet.addComponent(new ImageRenderComponent(new Image("/assets/ring_planet3.png")));
			planetRadiusInPixel = 245;
			planetRadiusInWorldUnits = Utils.pixelLengthToWorldLength(planetRadiusInPixel);
			planet.setScale(planet.getRadius() / planetRadiusInWorldUnits);
			break;
		}
	}

}
