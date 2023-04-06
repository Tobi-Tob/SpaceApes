package factories;

import org.newdawn.slick.geom.Vector2f;

import actions.DisplayPlanetInfoAction;

import java.util.Random;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import entities.Planet;
import spaceapes.SpaceApes;
import utils.Utils;
import eea.engine.event.basicevents.*;
import eea.engine.event.*;

public abstract class PlanetFactory {

	public enum PlanetType {
		PLAYER, NORMAL, BLACKHOLE, ANTI
	};

	/**
	 * Creates a Planet object with image already assigned to the entityManager.
	 * 
	 * @param type             PlanetType: PLAYER, NORMAL, BLACKHOLE, ANTI
	 * @param name             String as Identifier
	 * @param coordinates      Vector2f in world units
	 * @param radius           float as radius of the planet in world units
	 * @param mass             int as mass of the planet
	 * @param atmosphereRadius Float as Radius of the planets atmosphere. Null if no
	 *                         atmosphere is desired
	 * @return Planet
	 */
	public static Planet createPlanet(PlanetType type, String name, Vector2f coordinates, float radius, int mass, Float atmosphereRadius) {

		Planet planet = new Planet(name);

		planet.setPassable(false);
		planet.setCoordinates(coordinates);
		planet.setPosition(Utils.toPixelCoordinates(coordinates));
		planet.setMass(mass);
		planet.setRadius(radius);
		planet.setRotation(Utils.randomFloat(-30, 30));
		planet.setPlanetType(type);

		StateBasedEntityManager.getInstance().addEntity(SpaceApes.GAMEPLAY_STATE, planet);

		// Images
		if (type == PlanetType.PLAYER) {

			try {
				addRandomImageToPlanet(planet, false);
			} catch (SlickException e) {
				System.err.println("Problem with planet image");
			}

		} else if (type == PlanetType.NORMAL) {

			try {
				addRandomImageToPlanet(planet, true);
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
			}

		} else {
			throw new IllegalArgumentException("Invalid planet type: " + type.toString());
		}

		// Atmosphere
		if (atmosphereRadius != null) {

			Entity atmosphere = new Entity("AtmosphereOf" + name);
			atmosphere.setPassable(true);
			atmosphere.setPosition(Utils.toPixelCoordinates(coordinates));
			atmosphere.setRotation(Utils.randomFloat(0, 360));

			try {
				addRandomImageToAtmosphere(atmosphere, atmosphereRadius);
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
	 * Adds a random image to the planet and scales it individually
	 * 
	 * @param planet       Planet
	 * @param ringsAllowed true, if planet images with rings can be used
	 * @throws SlickException
	 */
	private static void addRandomImageToPlanet(Planet planet, boolean ringsAllowed) throws SlickException {

		if (!SpaceApes.renderImages) {
			return; // do not add any image to planet
		}

		Random r = new Random();
		int imageNumber = r.nextInt(5) + 1; // Integer im Intervall [1, 5]
		if (ringsAllowed) {
			imageNumber = r.nextInt(8) + 1; // Integer im Intervall [1, 8]
		}

		switch (imageNumber) {
		default: // Eqivalent zu case 1
			planet.addComponent(new ImageRenderComponent(new Image("img/planets/planet1.png")));

			float planetRadiusInPixel = 235;
			float planetRadiusInWorldUnits = Utils.pixelLengthToWorldLength(planetRadiusInPixel);
			planet.setScale(planet.getRadius() / planetRadiusInWorldUnits);
			break;
		case 2:
			planet.addComponent(new ImageRenderComponent(new Image("img/planets/planet2.png")));

			planetRadiusInPixel = 230;
			planetRadiusInWorldUnits = Utils.pixelLengthToWorldLength(planetRadiusInPixel);
			planet.setScale(planet.getRadius() / planetRadiusInWorldUnits);
			break;
		case 3:
			planet.addComponent(new ImageRenderComponent(new Image("img/planets/planet3.png")));

			planetRadiusInPixel = 242;
			planetRadiusInWorldUnits = Utils.pixelLengthToWorldLength(planetRadiusInPixel);
			planet.setScale(planet.getRadius() / planetRadiusInWorldUnits);
			break;
		case 4:

			planet.addComponent(new ImageRenderComponent(new Image("img/planets/planet4.png")));

			planetRadiusInPixel = 242;
			planetRadiusInWorldUnits = Utils.pixelLengthToWorldLength(planetRadiusInPixel);
			planet.setScale(planet.getRadius() / planetRadiusInWorldUnits);
			break;
		case 5:

			planet.addComponent(new ImageRenderComponent(new Image("img/planets/planet5.png")));

			planetRadiusInPixel = 222;
			planetRadiusInWorldUnits = Utils.pixelLengthToWorldLength(planetRadiusInPixel);
			planet.setScale(planet.getRadius() / planetRadiusInWorldUnits);
			break;
		case 6:

			planet.addComponent(new ImageRenderComponent(new Image("img/planets/ring_planet1.png")));

			planetRadiusInPixel = 210;
			planetRadiusInWorldUnits = Utils.pixelLengthToWorldLength(planetRadiusInPixel);
			planet.setScale(planet.getRadius() / planetRadiusInWorldUnits);
			break;
		case 7:

			planet.addComponent(new ImageRenderComponent(new Image("img/planets/ring_planet2.png")));

			planetRadiusInPixel = 230;
			planetRadiusInWorldUnits = Utils.pixelLengthToWorldLength(planetRadiusInPixel);
			planet.setScale(planet.getRadius() / planetRadiusInWorldUnits);
			break;
		case 8:

			planet.addComponent(new ImageRenderComponent(new Image("img/planets/ring_planet3.png")));

			planetRadiusInPixel = 245;
			planetRadiusInWorldUnits = Utils.pixelLengthToWorldLength(planetRadiusInPixel);
			planet.setScale(planet.getRadius() / planetRadiusInWorldUnits);
			break;
		}
	}

	/**
	 * Adds a random image to the planets atmosphere and scales it individually
	 * 
	 * @param atmosphere       Entity
	 * @param atmosphereRadius Float as radius
	 * @throws SlickException
	 */
	public static void addRandomImageToAtmosphere(Entity atmosphere, Float atmosphereRadius) throws SlickException {

		if (!SpaceApes.renderImages) {
			return; // do not add any image to atmosphere
		}

		Random r = new Random();
		int imageNumber = r.nextInt(3) + 1; // Integer im Intervall [1, 3]

		switch (imageNumber) {
		default: // Eqivalent zu case 1
			atmosphere.addComponent(new ImageRenderComponent(new Image("img/planets/atmosphere1.png")));

			float atmosphereRadiusInPixel = 400;
			float atmosphereRadiusInWorldUnits = Utils.pixelLengthToWorldLength(atmosphereRadiusInPixel);
			atmosphere.setScale(atmosphereRadius / atmosphereRadiusInWorldUnits);
			break;
		case 2:
			atmosphere.addComponent(new ImageRenderComponent(new Image("img/planets/atmosphere2.png")));

			atmosphereRadiusInPixel = 400;
			atmosphereRadiusInWorldUnits = Utils.pixelLengthToWorldLength(atmosphereRadiusInPixel);
			atmosphere.setScale(atmosphereRadius / atmosphereRadiusInWorldUnits);
			break;
		case 3:
			atmosphere.addComponent(new ImageRenderComponent(new Image("img/planets/atmosphere3.png")));

			atmosphereRadiusInPixel = 400;
			atmosphereRadiusInWorldUnits = Utils.pixelLengthToWorldLength(atmosphereRadiusInPixel);
			atmosphere.setScale(atmosphereRadius / atmosphereRadiusInWorldUnits);
			break;
		}
	}

}
