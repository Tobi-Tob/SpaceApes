package spaceapes;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;

public class ControlPanel extends Entity {

	public ControlPanel(String entityID) {
		super(entityID);
	}

	public void init(Map map) {
		Vector2f bestPosition = findBestPosition(map);
		this.setPosition(bestPosition);
		float controlpanelWidthInPixel = 1500;
		float controlpanelWidthInWorldUnits = Utils.pixelLengthToWorldLength(controlpanelWidthInPixel);
		float desiredControlpanelWidth = 4.5f;
		this.setScale(desiredControlpanelWidth / controlpanelWidthInWorldUnits);
		try {
			this.addComponent(new ImageRenderComponent(new Image("/assets/panel.png")));
		} catch (SlickException e) {
			System.err.println("Problem with Controlpanel");
		}
	}

	/**
	 * Bestimmt unter 4 vorgegebenen Eck-Positionen, diejenige, die den groessten
	 * Qualitaetswert besitzt (Moeglichst keinen Planeten verdeckt)
	 * 
	 * @param planetData Planetenpositionen
	 * @return Vector2f Position, die am besten fuer das Control Panel geeignet ist.
	 */
	private Vector2f findBestPosition(Map map) {
		Vector2f leftUpperCorner = new Vector2f(0.15f * Launch.WIDTH, 0.13f * Launch.HEIGHT);
		Vector2f rightUpperCorner = new Vector2f(0.85f * Launch.WIDTH, 0.13f * Launch.HEIGHT);
		Vector2f leftLowerCorner = new Vector2f(0.15f * Launch.WIDTH, 0.87f * Launch.HEIGHT);
		Vector2f rightLowerCorner = new Vector2f(0.85f * Launch.WIDTH, 0.87f * Launch.HEIGHT);

		HashMap<Vector2f, Float> positionsToDistance = new HashMap<Vector2f, Float>();
		// Die HashMap speichtert als Key die 4 moeglichen Positionen und als Value die
		// Qualitaet der entsprechenden Position
		positionsToDistance.put(leftUpperCorner, getQuality(leftUpperCorner, map));
		positionsToDistance.put(rightUpperCorner, getQuality(rightUpperCorner, map));
		positionsToDistance.put(leftLowerCorner, getQuality(leftLowerCorner, map));
		positionsToDistance.put(rightLowerCorner, getQuality(rightLowerCorner, map));

		// Durchsucht die HashMap nach dem groessten Value und gibt den entsprechenden
		// Key zurueck.
		Vector2f bestPosition = Collections.max(positionsToDistance.entrySet(), HashMap.Entry.comparingByValue()).getKey();
		return bestPosition;
	}

	/**
	 * Berechnet einen Qualitaetswert fuer die uebergebene Pixelposition.
	 * Ausschlaggebend fuer die Qualitaet ist der naeheste Planet. Die Qualitaet ist
	 * hoch, wenn die Position weit entfernt vom naehsten Planeten ist.
	 * 
	 * @param posInPixel Vector2f in Pixel-Koordinaten
	 * @param planetData Planetenpositionen
	 * @return Qualitaetswert, berechnet durch verzerrte Maximum Norm zum naehsten
	 *         Planeten
	 */
	private float getQuality(Vector2f posInPixel, Map map) {
		Vector2f pos = Utils.toWorldCoordinates(posInPixel);
		float quality = Float.POSITIVE_INFINITY;
		// Iteriert ueber alle Planeten und bestimmt den Abstand zu pos
		for (Planet planet : map.listOfPlanets) {
			Vector2f positionPlanet_i = new Vector2f(planet.getCoordinates());
			Vector2f distanceVector = positionPlanet_i.sub(pos);
			// Verzerrte Maximum Norm
			float quality_i = Math.max(Math.abs(distanceVector.x), Math.abs(distanceVector.y * 2.12f));
			// Senke Qualitaet fuer Affenplaneten
			if (planet.hasApe()) {
				quality_i = 0.5f * quality_i;
			}
			if (quality_i < quality) { // Falls neu berechnete Qualitaet kleiner als bisher gespeicherte
				quality = quality_i;
			}
		}
		return quality;
	}

}
