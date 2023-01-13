package spaceapes;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.action.Action;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.ANDEvent;
import eea.engine.event.basicevents.MouseClickedEvent;
import eea.engine.event.basicevents.MouseEnteredEvent;

public class ControlPanel extends Entity {

	private Vector2f coordinates;
	public List<Entity> listOfCorrespondingEntities;
	public Player activePlayer;
	float panelScaleFactor;
	TrueTypeFont font;
	ImageRenderComponent imageRenderComponent = null;

	public ControlPanel(String entityID) {
		super(entityID);
		listOfCorrespondingEntities = new ArrayList<>();
	}

	public void initControlPanel(Map map, Player player, int stateID, StateBasedEntityManager entityManager) {
		this.activePlayer = player;
		coordinates = findBestPosition(map);
		this.setPosition(coordinates);
		float controlpanelWidthInPixel = 1483;
		float desiredControlpanelWidth = 0.3f; // im Verhaeltnis zur Fenster Breite
		panelScaleFactor = desiredControlpanelWidth * Launch.WIDTH / controlpanelWidthInPixel;
		this.setScale(panelScaleFactor);

		// Font
		int fontSize = Math.round(panelScaleFactor * 90);
		font = new TrueTypeFont(new Font("Times New Roman", Font.BOLD, fontSize), true);

		// Arrows
		float desiredArrowScale = 0.8f; // im Verhaeltnis zum Control Panel

		Entity arrow_Weapons = new Entity("ArrowForWeapons");
		arrow_Weapons.setScale(panelScaleFactor * desiredArrowScale);
		arrow_Weapons.setPosition(relativPosOnPanelToPixelPos(600, -230));
		listOfCorrespondingEntities.add(arrow_Weapons);

		Entity arrow_Power_Right = new Entity("ArrowForPowerRight");
		arrow_Power_Right.setScale(panelScaleFactor * desiredArrowScale);
		arrow_Power_Right.setPosition(new Vector2f(-120, 200).scale(panelScaleFactor).add(coordinates));
		listOfCorrespondingEntities.add(arrow_Power_Right);

		Entity arrow_Power_Left = new Entity("ArrowForPowerLeft");
		arrow_Power_Left.setScale(panelScaleFactor * desiredArrowScale);
		arrow_Power_Left.setPosition(relativPosOnPanelToPixelPos(-620, 200));
		listOfCorrespondingEntities.add(arrow_Power_Left);

		Entity arrow_Angle_Right = new Entity("ArrowForAngleRight");
		arrow_Angle_Right.setScale(panelScaleFactor * desiredArrowScale);
		arrow_Angle_Right.setPosition(relativPosOnPanelToPixelPos(620, 200));
		listOfCorrespondingEntities.add(arrow_Angle_Right);

		Entity arrow_Angle_Left = new Entity("ArrowForAngleLeft");
		arrow_Angle_Left.setScale(panelScaleFactor * desiredArrowScale);
		arrow_Angle_Left.setPosition(relativPosOnPanelToPixelPos(120, 200));
		listOfCorrespondingEntities.add(arrow_Angle_Left);

		float desiredProjectilScale = 0.23f;
		Entity shopProjectil_1 = new Entity("ShopProjectil_1");
		shopProjectil_1.setScale(panelScaleFactor * desiredProjectilScale);
		shopProjectil_1.setPosition(relativPosOnPanelToPixelPos(380, -230));
		shopProjectil_1.setRotation(45);
		listOfCorrespondingEntities.add(shopProjectil_1);

		try {
			imageRenderComponent = new ImageRenderComponent(new Image("/assets/panel.png"));
			this.addComponent(new ImageRenderComponent(new Image("/assets/panel.png")));
			arrow_Weapons.addComponent(new ImageRenderComponent(new Image("/assets/arrow_right.png")));
			arrow_Power_Right.addComponent(new ImageRenderComponent(new Image("/assets/arrow_right.png")));
			arrow_Power_Left.addComponent(new ImageRenderComponent(new Image("/assets/arrow_left.png")));
			arrow_Angle_Right.addComponent(new ImageRenderComponent(new Image("/assets/arrow_right.png")));
			arrow_Angle_Left.addComponent(new ImageRenderComponent(new Image("/assets/arrow_left.png")));
			shopProjectil_1.addComponent(new ImageRenderComponent(new Image("/assets/coconut.png")));
		} catch (SlickException e) {
			System.err.println("Problem with Controlpanel");
		}
		this.addComponent(imageRenderComponent);

		entityManager.addEntity(stateID, this);
		entityManager.addEntity(stateID, arrow_Weapons);
		entityManager.addEntity(stateID, arrow_Power_Right);
		entityManager.addEntity(stateID, arrow_Power_Left);
		entityManager.addEntity(stateID, arrow_Angle_Right);
		entityManager.addEntity(stateID, arrow_Angle_Left);
		entityManager.addEntity(stateID, shopProjectil_1);

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

		setPanelAndComponentsVisible(true);
	}

	public void setPanelAndComponentsVisible(boolean isVisible) {
		this.setVisible(isVisible);
		for (Entity e : listOfCorrespondingEntities) {
			e.setVisible(isVisible);
		}
	}

	private Vector2f relativPosOnPanelToPixelPos(float x, float y) {
		return new Vector2f(x, y).scale(panelScaleFactor).add(coordinates);
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

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		if (this.isVisible() && !(imageRenderComponent == null)) {

			imageRenderComponent.render(container, game, g);

			// Render Text

			Vector2f textPos_Power = relativPosOnPanelToPixelPos(-470, -30);
			font.drawString(textPos_Power.x, textPos_Power.y, "Power", Color.black);

			Vector2f textPos_Angle = relativPosOnPanelToPixelPos(250, -20);
			font.drawString(textPos_Angle.x, textPos_Angle.y, "Angle", Color.black);

			Vector2f textPos_ActivePlayer = relativPosOnPanelToPixelPos(-600, -290);
			font.drawString(textPos_ActivePlayer.x, textPos_ActivePlayer.y, activePlayer.getName(), Color.black);

			Vector2f numberPos_Power = relativPosOnPanelToPixelPos(-460, 150);
			font.drawString(numberPos_Power.x, numberPos_Power.y, Float.toString(activePlayer.getApe().getThrowStrength()),
					Color.black);

			Vector2f numberPos_Angle = relativPosOnPanelToPixelPos(290, 150);
			font.drawString(numberPos_Angle.x, numberPos_Angle.y,
					Float.toString(activePlayer.getApe().getAngleOfView_local()), Color.black);

			Vector2f numberPos_Coins = relativPosOnPanelToPixelPos(40, -280);
			font.drawString(numberPos_Coins.x, numberPos_Coins.y, Float.toString(activePlayer.getCoins()), Color.black);

		}
	}
}
