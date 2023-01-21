package spaceapes;

import org.newdawn.slick.geom.Vector2f;

import java.util.Random;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.interfaces.IEntityFactory;
import eea.engine.event.basicevents.*;
import eea.engine.event.*;

public class PlanetFactory implements IEntityFactory {

	private final String name;
	private final float radius;
	private final int mass;
	private final Vector2f coordinates; // In Welt-Koordinaten
	private final boolean ringImagePossible;
	
	public PlanetFactory(String name, float radius, int mass, Vector2f coordinates) {
		this.name = name;
		this.radius = radius;
		this.mass = mass;
		this.coordinates = coordinates;
		if(name.equals("Planet1")) {
			this.ringImagePossible = false;
		} else {
			this.ringImagePossible = true;
		}
	}
	
	@Override
	public Entity createEntity() {
		
		Planet planet = new Planet(name);
		
		planet.setPassable(false);
		planet.setPosition(Utils.toPixelCoordinates(coordinates));
		planet.setRotation(Utils.randomFloat(-30, 30));
		planet.setRadius(radius);
		planet.setMass(mass);
		
		try {
			addRandomImageToPlanet(planet, ringImagePossible);
		} catch (SlickException e) {
			System.err.println("Cannot find image for planet");
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
