package factories;

import java.awt.Font;
import java.util.Collections;
import java.util.HashMap;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Vector2f;

import actions.ChangeAngleAction;
import actions.ChangePowerAction;
import actions.ChangeWeaponAction;
import eea.engine.action.Action;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.ANDEvent;
import eea.engine.event.basicevents.MouseClickedEvent;
import eea.engine.event.basicevents.MouseEnteredEvent;
import eea.engine.interfaces.IEntityFactory;
import entities.ControlPanel;
import entities.Planet;
import map.Map;
import spaceapes.Launch;
import utils.Utils;

public class ControlPanelFactory implements IEntityFactory {
	
	// WIRD MOMENTAN NICHT BENUTZT!!!
	
	//private List<Entity> listOfCorrespondingEntities = new ArrayList<>();

	@Override
	public Entity createEntity() {
		
		ControlPanel controlPanel = new ControlPanel("ControlPanel");
		
		Vector2f coordinates = findBestPosition();
		controlPanel.setPosition(coordinates);
		float controlpanelWidthInPixel = 1483;
		float desiredControlpanelWidth = 0.3f; // im Verhaeltnis zur Fenster Breite
		float panelScaleFactor = desiredControlpanelWidth * Launch.WIDTH / controlpanelWidthInPixel;
		controlPanel.setScale(panelScaleFactor);
		
		// Font
		int fontSize = Math.round(panelScaleFactor * 90);
		controlPanel.setFont(new TrueTypeFont(new Font("Times New Roman", Font.BOLD, fontSize), true));
		
		
		// Erstellen der zugehoerigen Entities (Pfeile)
		float desiredArrowScale = 0.8f; // im Verhaeltnis zum Control Panel

		Entity arrow_Weapons = new Entity("ArrowForWeapons");
		arrow_Weapons.setScale(panelScaleFactor * desiredArrowScale);
		arrow_Weapons.setPosition(relativPosOnPanelToPixelPos(600, -230, panelScaleFactor, coordinates));
		controlPanel.addToListOfCorrespondingEntities(arrow_Weapons);

		Entity arrow_Power_Right = new Entity("ArrowForPowerRight");
		arrow_Power_Right.setScale(panelScaleFactor * desiredArrowScale);
		arrow_Power_Right.setPosition(relativPosOnPanelToPixelPos(-120, 200, panelScaleFactor, coordinates));
		controlPanel.addToListOfCorrespondingEntities(arrow_Power_Right);

		Entity arrow_Power_Left = new Entity("ArrowForPowerLeft");
		arrow_Power_Left.setScale(panelScaleFactor * desiredArrowScale);
		arrow_Power_Left.setPosition(relativPosOnPanelToPixelPos(-620, 200, panelScaleFactor, coordinates));
		controlPanel.addToListOfCorrespondingEntities(arrow_Power_Left);

		Entity arrow_Angle_Right = new Entity("ArrowForAngleRight");
		arrow_Angle_Right.setScale(panelScaleFactor * desiredArrowScale);
		arrow_Angle_Right.setPosition(relativPosOnPanelToPixelPos(620, 200, panelScaleFactor, coordinates));
		controlPanel.addToListOfCorrespondingEntities(arrow_Angle_Right);

		Entity arrow_Angle_Left = new Entity("ArrowForAngleLeft");
		arrow_Angle_Left.setScale(panelScaleFactor * desiredArrowScale);
		arrow_Angle_Left.setPosition(relativPosOnPanelToPixelPos(120, 200, panelScaleFactor, coordinates));
		controlPanel.addToListOfCorrespondingEntities(arrow_Angle_Left);

		float desiredProjectilScale = 0.23f;
		Entity shopProjectil_1 = new Entity("ShopProjectil_1");
		shopProjectil_1.setScale(panelScaleFactor * desiredProjectilScale);
		shopProjectil_1.setPosition(relativPosOnPanelToPixelPos(380, -230, panelScaleFactor, coordinates));
		shopProjectil_1.setRotation(45);
		// LoopEvent rotationLoop = new LoopEvent();
		// rotationLoop.addAction(new RotateRightAction(0.03f));
		// shopProjectil_1.addComponent(rotationLoop);
		controlPanel.addToListOfCorrespondingEntities(shopProjectil_1);
		
		// Bilder fuer die Entities festlegen
		try {
			controlPanel.addComponent(new ImageRenderComponent(new Image("img/assets/panel.png")));
			arrow_Weapons.addComponent(new ImageRenderComponent(new Image("img/assets/arrow_right.png")));
			arrow_Power_Right.addComponent(new ImageRenderComponent(new Image("img/assets/arrow_right.png")));
			arrow_Power_Left.addComponent(new ImageRenderComponent(new Image("img/assets/arrow_left.png")));
			arrow_Angle_Right.addComponent(new ImageRenderComponent(new Image("img/assets/arrow_right.png")));
			arrow_Angle_Left.addComponent(new ImageRenderComponent(new Image("img/assets/arrow_left.png")));
			shopProjectil_1.addComponent(new ImageRenderComponent(new Image("img/projectile/coconut.png")));
		} catch (SlickException e) {
			System.err.println("Problem with Controlpanel images");
		}
		//controlPanel.addComponent(imageRenderComponent);
		
		StateBasedEntityManager entityManager = StateBasedEntityManager.getInstance();
		entityManager.addEntity(Launch.GAMEPLAY_STATE, arrow_Weapons);
		entityManager.addEntity(Launch.GAMEPLAY_STATE, arrow_Power_Right);
		entityManager.addEntity(Launch.GAMEPLAY_STATE, arrow_Power_Left);
		entityManager.addEntity(Launch.GAMEPLAY_STATE, arrow_Angle_Right);
		entityManager.addEntity(Launch.GAMEPLAY_STATE, arrow_Angle_Left);
		entityManager.addEntity(Launch.GAMEPLAY_STATE, shopProjectil_1);
		
		
		// Erstellen der Knopfdruck-Events und die zugehoerige Actions

		ANDEvent change_Weapon_Event = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		Action change_Weapon_Action = new ChangeWeaponAction();
		change_Weapon_Event.addAction(change_Weapon_Action);
		arrow_Weapons.addComponent(change_Weapon_Event);

		ANDEvent increase_Angle_Event = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		Action increase_Angle_Action = new ChangeAngleAction(5f);
		increase_Angle_Event.addAction(increase_Angle_Action);
		arrow_Angle_Right.addComponent(increase_Angle_Event);

		ANDEvent decrease_Angle_Event = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		Action decrease_Angle_Action = new ChangeAngleAction(-5f);
		decrease_Angle_Event.addAction(decrease_Angle_Action);
		arrow_Angle_Left.addComponent(decrease_Angle_Event);

		ANDEvent increase_Power_Event = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		Action increase_Power_Action = new ChangePowerAction(1f);
		increase_Power_Event.addAction(increase_Power_Action);
		arrow_Power_Right.addComponent(increase_Power_Event);

		ANDEvent decrease_Power_Event = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		Action decrease_Power_Action = new ChangePowerAction(-1f);
		decrease_Power_Event.addAction(decrease_Power_Action);
		arrow_Power_Left.addComponent(decrease_Power_Event);
		
		
		controlPanel.setPanelAndComponentsVisible(true);
		return controlPanel;
	}
	
	/**
	 * Bestimmt unter 4 vorgegebenen Eck-Positionen, diejenige, die den groessten
	 * Qualitaetswert besitzt (Moeglichst keinen Planeten verdeckt)
	 * 
	 * @param planetData Planetenpositionen
	 * @return Vector2f Position, die am besten fuer das Control Panel geeignet ist.
	 */
	private Vector2f findBestPosition() {
		Vector2f leftUpperCorner = new Vector2f(0.18f * Launch.WIDTH, 0.14f * Launch.HEIGHT);
		Vector2f rightUpperCorner = new Vector2f(0.82f * Launch.WIDTH, 0.14f * Launch.HEIGHT);
		Vector2f leftLowerCorner = new Vector2f(0.18f * Launch.WIDTH, 0.86f * Launch.HEIGHT);
		Vector2f rightLowerCorner = new Vector2f(0.82f * Launch.WIDTH, 0.86f * Launch.HEIGHT);

		HashMap<Vector2f, Float> positionsToDistance = new HashMap<Vector2f, Float>();
		// Die HashMap speichtert als Key die 4 moeglichen Positionen und als Value die
		// Qualitaet der entsprechenden Position
		positionsToDistance.put(leftUpperCorner, getQuality(leftUpperCorner));
		positionsToDistance.put(rightUpperCorner, getQuality(rightUpperCorner));
		positionsToDistance.put(leftLowerCorner, getQuality(leftLowerCorner));
		positionsToDistance.put(rightLowerCorner, getQuality(rightLowerCorner));

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
	private float getQuality(Vector2f posInPixel) {
		Map map = Map.getInstance();
		Vector2f pos = Utils.toWorldCoordinates(posInPixel);
		float quality = Float.POSITIVE_INFINITY;
		// Iteriert ueber alle Planeten und bestimmt den Abstand zu pos
		for (Planet planet : map.getPlanets()) {
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
	
	private Vector2f relativPosOnPanelToPixelPos(float x, float y, float panelScaleFactor, Vector2f coordinates) {
		return new Vector2f(x, y).scale(panelScaleFactor).add(coordinates);
	}

}
