package spaceapes;

import java.util.Collections;
import java.util.HashMap;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import eea.engine.action.Action;
import eea.engine.action.basicactions.ChangeStateInitAction;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.ANDEvent;
import eea.engine.event.basicevents.MouseClickedEvent;
import eea.engine.event.basicevents.MouseEnteredEvent;

public class ControlPanel extends Entity {

	private Vector2f coordinates;
	public Player activePlayer;

	public ControlPanel(String entityID) {
		super(entityID);
	}

	public void initControlPanel(Map map, int stateID, StateBasedEntityManager entityManager) {
		coordinates = findBestPosition(map);
		this.setPosition(coordinates);
		float controlpanelWidthInPixel = 1483;
		float desiredControlpanelWidth = 0.3f; // im Verhaeltnis zur Fenster Breite
		float panelScaleFactor = desiredControlpanelWidth * Launch.WIDTH / controlpanelWidthInPixel;
		this.setScale(panelScaleFactor);

		// Arrows
		float desiredArrowWidth = 0.8f; // im Verhaeltnis zum Control Panel

		Entity arrow_Weapons = new Entity("ArrowForWeapons");
		arrow_Weapons.setScale(panelScaleFactor * desiredArrowWidth);
		Vector2f relativPos = new Vector2f(600, -230); // relative Position auf dem Control Panel
		arrow_Weapons.setPosition(relativPos.scale(panelScaleFactor).add(coordinates));

		Entity arrow_Power_Right = new Entity("ArrowForPowerRight");
		arrow_Power_Right.setScale(panelScaleFactor * desiredArrowWidth);
		arrow_Power_Right.setPosition(new Vector2f(-120, 200).scale(panelScaleFactor).add(coordinates));

		Entity arrow_Power_Left = new Entity("ArrowForPowerLeft");
		arrow_Power_Left.setScale(panelScaleFactor * desiredArrowWidth);
		arrow_Power_Left.setPosition(new Vector2f(-620, 200).scale(panelScaleFactor).add(coordinates));

		Entity arrow_Angle_Right = new Entity("ArrowForAngleRight");
		arrow_Angle_Right.setScale(panelScaleFactor * desiredArrowWidth);
		arrow_Angle_Right.setPosition(new Vector2f(620, 200).scale(panelScaleFactor).add(coordinates));

		Entity arrow_Angle_Left = new Entity("ArrowForAngleLeft");
		arrow_Angle_Left.setScale(panelScaleFactor * desiredArrowWidth);
		arrow_Angle_Left.setPosition(new Vector2f(120, 200).scale(panelScaleFactor).add(coordinates));

		try {
			this.addComponent(new ImageRenderComponent(new Image("/assets/panel.png")));
			arrow_Weapons.addComponent(new ImageRenderComponent(new Image("/assets/arrow_right.png")));
			arrow_Power_Right.addComponent(new ImageRenderComponent(new Image("/assets/arrow_right.png")));
			arrow_Power_Left.addComponent(new ImageRenderComponent(new Image("/assets/arrow_left.png")));
			arrow_Angle_Right.addComponent(new ImageRenderComponent(new Image("/assets/arrow_right.png")));
			arrow_Angle_Left.addComponent(new ImageRenderComponent(new Image("/assets/arrow_left.png")));
		} catch (SlickException e) {
			System.err.println("Problem with Controlpanel");
		}
		entityManager.addEntity(stateID, this);
		entityManager.addEntity(stateID, arrow_Weapons);
		entityManager.addEntity(stateID, arrow_Power_Right);
		entityManager.addEntity(stateID, arrow_Power_Left);
		entityManager.addEntity(stateID, arrow_Angle_Right);
		entityManager.addEntity(stateID, arrow_Angle_Left);

		// Erstellen der Knopfdruck-Events und die zugehoerige Actions

		ANDEvent increase_Angle_Event = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		Action increase_Angle_Action = new ChangeAngleAction(1f);
		increase_Angle_Event.addAction(increase_Angle_Action);
		arrow_Angle_Right.addComponent(increase_Angle_Event);

		ANDEvent decrease_Angle_Event = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		Action decrease_Angle_Action = new ChangeAngleAction(-1f);
		decrease_Angle_Event.addAction(decrease_Angle_Action);
		arrow_Angle_Left.addComponent(decrease_Angle_Event);

		ANDEvent increase_Power_Event = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		Action increase_Power_Action = new ChangePowerAction(0.1f);
		increase_Power_Event.addAction(increase_Power_Action);
		arrow_Power_Right.addComponent(increase_Power_Event);

		ANDEvent decrease_Power_Event = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		Action decrease_Power_Action = new ChangePowerAction(-0.1f);
		decrease_Power_Event.addAction(decrease_Power_Action);
		arrow_Power_Left.addComponent(decrease_Power_Event);
	}

	/**
	 * Bestimmt unter 4 vorgegebenen Eck-Positionen, diejenige, die den groessten
	 * Qualitaetswert besitzt (Moeglichst keinen Planeten verdeckt)
	 * 
	 * @param planetData Planetenpositionen
	 * @return Vector2f Position, die am besten fuer das Control Panel geeignet ist.
	 */
	private Vector2f findBestPosition(Map map) {
		Vector2f leftUpperCorner = new Vector2f(0.18f * Launch.WIDTH, 0.14f * Launch.HEIGHT);
		Vector2f rightUpperCorner = new Vector2f(0.82f * Launch.WIDTH, 0.14f * Launch.HEIGHT);
		Vector2f leftLowerCorner = new Vector2f(0.18f * Launch.WIDTH, 0.86f * Launch.HEIGHT);
		Vector2f rightLowerCorner = new Vector2f(0.82f * Launch.WIDTH, 0.86f * Launch.HEIGHT);

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
