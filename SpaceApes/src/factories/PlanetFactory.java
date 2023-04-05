package factories;

import org.newdawn.slick.geom.Vector2f;

import actions.DisplayPlanetInfoAction;

import java.util.Random;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.interfaces.IEntityFactory;
import entities.Planet;
import spaceapes.SpaceApes;
import utils.Utils;
import eea.engine.event.basicevents.*;
import eea.engine.event.*;

public class PlanetFactory implements IEntityFactory {

	public enum PlanetType {
		PLAYER, BLACKHOLE, ANTI, NORMAL
	};

	private final String name;
	private final float radius;
	private final int mass;
	private final Vector2f coordinates; // In Welt-Koordinaten
	private final boolean ringImagePossible;
	private final PlanetType type;
	private boolean hasAtmosphere;

	public PlanetFactory(String name, float radius, int mass, Vector2f coordinates, PlanetType type, boolean hasAtmosphere) {
		this.name = name;
		this.radius = radius;
		this.mass = mass;
		this.coordinates = coordinates;
		this.type = type;
		this.hasAtmosphere = hasAtmosphere;
		if (type == PlanetType.NORMAL) {
			this.ringImagePossible = true;
		} else {
			this.ringImagePossible = false;
		}
	}

	@Override
	public Planet createEntity() {

		Planet planet = new Planet(name);

		planet.setPassable(false);
		planet.setCoordinates(coordinates);
		planet.setPosition(Utils.toPixelCoordinates(coordinates));
		planet.setMass(mass);
		planet.setRadius(radius);
		planet.setRotation(Utils.randomFloat(-30, 30));
		planet.setPlanetType(type);

		if (type == PlanetType.PLAYER) {

			try {
				addRandomImageToPlanet(planet, ringImagePossible);
			} catch (SlickException e) {
				System.err.println("Problem with planet image");
			}

		} else if (type == PlanetType.BLACKHOLE) {

			float blackHoleRadiusInPixel = 60;
			float blackHoleRadiusInWorldUnits = Utils.pixelLengthToWorldLength(blackHoleRadiusInPixel);
			planet.setScale(radius / blackHoleRadiusInWorldUnits);
			planet.setRotation(0);

			if (SpaceApes.renderImages) {
				try {
					planet.addComponent(new ImageRenderComponent(new Image("img/planets/blackhole1.png")));
				} catch (SlickException e) {
					System.err.println("Problem with black hole image");
				}
			} else {
				// System.out.println("noRenderImages: assign blackhole image.");
			}

		} else if (type == PlanetType.ANTI) {

			float planetRadiusInPixel = 230;
			float planetRadiusInWorldUnits = Utils.pixelLengthToWorldLength(planetRadiusInPixel);
			planet.setScale(radius / planetRadiusInWorldUnits);

			if (SpaceApes.renderImages) {
				try {
					planet.addComponent(new ImageRenderComponent(new Image("img/planets/planet_anti1.png")));
				} catch (SlickException e) {
					System.err.println("Problem with planet image");
				}
			} else {
				// System.out.println("noRenderImages: assign antiplanet image.");
			}

		} else if (type == PlanetType.NORMAL) {

			try {
				addRandomImageToPlanet(planet, ringImagePossible);
			} catch (SlickException e) {
				System.err.println("Problem with planet image");
			}

		} else {

			throw new IllegalArgumentException("Invalid planet type: " + type.toString());
		}
		
		// Atmosphere
		if (this.hasAtmosphere) {

			Entity atmosphere = new Entity("AtmosphereOf" + name);
			atmosphere.setPassable(true);
			atmosphere.setPosition(Utils.toPixelCoordinates(coordinates));
			atmosphere.setRotation(Utils.randomFloat(0, 360));

			try {
				addRandomImageToAtmosphere(atmosphere, planet);
			} catch (SlickException e) {
				System.err.println("Problem with atmosphere image");
			}
			
			StateBasedEntityManager.getInstance().addEntity(SpaceApes.GAMEPLAY_STATE, atmosphere);
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
			if (SpaceApes.renderImages) {
				planet.addComponent(new ImageRenderComponent(new Image("img/planets/planet1.png")));
			} else {
				// System.out.println("noRenderImages: assign planet1 image.");
			}
			float planetRadiusInPixel = 235;
			float planetRadiusInWorldUnits = Utils.pixelLengthToWorldLength(planetRadiusInPixel);
			planet.setScale(planet.getRadius() / planetRadiusInWorldUnits);
			break;
		case 2:
			if (SpaceApes.renderImages) {
				planet.addComponent(new ImageRenderComponent(new Image("img/planets/planet2.png")));
			} else {
				// System.out.println("noRenderImages: assign planet2 image.");
			}
			planetRadiusInPixel = 230;
			planetRadiusInWorldUnits = Utils.pixelLengthToWorldLength(planetRadiusInPixel);
			planet.setScale(planet.getRadius() / planetRadiusInWorldUnits);
			break;
		case 3:
			if (SpaceApes.renderImages) {
				planet.addComponent(new ImageRenderComponent(new Image("img/planets/planet3.png")));
			} else {
				// System.out.println("noRenderImages: assign planet3 image.");
			}
			planetRadiusInPixel = 242;
			planetRadiusInWorldUnits = Utils.pixelLengthToWorldLength(planetRadiusInPixel);
			planet.setScale(planet.getRadius() / planetRadiusInWorldUnits);
			break;
		case 4:
			if (SpaceApes.renderImages) {
				planet.addComponent(new ImageRenderComponent(new Image("img/planets/planet4.png")));
			} else {
				// System.out.println("noRenderImages: assign planet4 image.");
			}
			planetRadiusInPixel = 242;
			planetRadiusInWorldUnits = Utils.pixelLengthToWorldLength(planetRadiusInPixel);
			planet.setScale(planet.getRadius() / planetRadiusInWorldUnits);
			break;
		case 5:
			if (SpaceApes.renderImages) {
				planet.addComponent(new ImageRenderComponent(new Image("img/planets/planet5.png")));
			} else {
				// System.out.println("noRenderImages: assign planet5 image.");
			}
			planetRadiusInPixel = 222;
			planetRadiusInWorldUnits = Utils.pixelLengthToWorldLength(planetRadiusInPixel);
			planet.setScale(planet.getRadius() / planetRadiusInWorldUnits);
			break;
		case 6:
			if (SpaceApes.renderImages) {
				planet.addComponent(new ImageRenderComponent(new Image("img/planets/ring_planet1.png")));
			} else {
				// System.out.println("noRenderImages: assign ring planet1 image.");
			}
			planetRadiusInPixel = 210;
			planetRadiusInWorldUnits = Utils.pixelLengthToWorldLength(planetRadiusInPixel);
			planet.setScale(planet.getRadius() / planetRadiusInWorldUnits);
			break;
		case 7:
			if (SpaceApes.renderImages) {
				planet.addComponent(new ImageRenderComponent(new Image("img/planets/ring_planet2.png")));
			} else {
				// System.out.println("noRenderImages: assign ring planet2 image.");
			}
			planetRadiusInPixel = 230;
			planetRadiusInWorldUnits = Utils.pixelLengthToWorldLength(planetRadiusInPixel);
			planet.setScale(planet.getRadius() / planetRadiusInWorldUnits);
			break;
		case 8:
			if (SpaceApes.renderImages) {
				planet.addComponent(new ImageRenderComponent(new Image("img/planets/ring_planet3.png")));
			} else {
				// System.out.println("noRenderImages: assign ring planet3 image.");
			}
			planetRadiusInPixel = 245;
			planetRadiusInWorldUnits = Utils.pixelLengthToWorldLength(planetRadiusInPixel);
			planet.setScale(planet.getRadius() / planetRadiusInWorldUnits);
			break;
		}
	}
	
	public void addRandomImageToAtmosphere(Entity atmosphere, Planet planet) throws SlickException {
		Random r = new Random();
		int imageNumber = r.nextInt(3) + 1; // Integer im Intervall [1, 3]

		switch (imageNumber) {
		default: // Eqivalent zu case 1
			if (SpaceApes.renderImages) {
				atmosphere.addComponent(new ImageRenderComponent(new Image("img/planets/atmosphere1.png")));
			}
			float atmosphereRadiusInPixel = 400;
			float atmosphereRadiusInWorldUnits = Utils.pixelLengthToWorldLength(atmosphereRadiusInPixel);
			atmosphere.setScale(1.5f * planet.getRadius() / atmosphereRadiusInWorldUnits);
			break;
		case 2:
			if (SpaceApes.renderImages) {
				atmosphere.addComponent(new ImageRenderComponent(new Image("img/planets/atmosphere2.png")));
			}
			atmosphereRadiusInPixel = 400;
			atmosphereRadiusInWorldUnits = Utils.pixelLengthToWorldLength(atmosphereRadiusInPixel);
			atmosphere.setScale(1.5f * planet.getRadius() / atmosphereRadiusInWorldUnits);
			break;
		case 3:
			if (SpaceApes.renderImages) {
				atmosphere.addComponent(new ImageRenderComponent(new Image("img/planets/atmosphere3.png")));
			}
			atmosphereRadiusInPixel = 400;
			atmosphereRadiusInWorldUnits = Utils.pixelLengthToWorldLength(atmosphereRadiusInPixel);
			atmosphere.setScale(1.5f * planet.getRadius() / atmosphereRadiusInWorldUnits);
			break;
		}
	}

}
