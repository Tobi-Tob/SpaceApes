package entities;

import java.awt.Font;
import java.text.DecimalFormat;
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
import events.MouseDownEvent;
import factories.ProjectileFactory;
import factories.ProjectileFactory.ProjectileType;
import map.Map;
import spaceapes.Constants;
import spaceapes.Launch;
import utils.Utils;

public class ControlPanel extends Entity {

	public enum Location {
		TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT, FREE
	};

	private Location location;
	private Vector2f coordinates; // Pixel Position
	public List<Entity> listOfCorrespondingEntities;
	public List<Projectile> listOfShopProjectiles;
	float panelScaleFactor;
	TrueTypeFont font;
	DecimalFormat powerFormatter;
	DecimalFormat angleFormatter;
	ImageRenderComponent imageRenderComponent = null;

	public ControlPanel(Location location) {
		super(Constants.CONTROL_PANEL);
		this.location = location;
		listOfCorrespondingEntities = new ArrayList<>();
		listOfShopProjectiles = new ArrayList<>();
	}

	public void initControlPanel() {
		coordinates = calcPosition();
		this.setPosition(coordinates);
		float controlpanelWidthInPixel = 1483;
		float desiredControlpanelWidth = 0.3f; // im Verhaeltnis zur Fenster Breite
		panelScaleFactor = desiredControlpanelWidth * Launch.WIDTH / controlpanelWidthInPixel;
		this.setScale(panelScaleFactor);

		// Font
		int fontSize = Math.round(panelScaleFactor * 90);
		font = new TrueTypeFont(new Font("Times New Roman", Font.BOLD, fontSize), true);
		powerFormatter = new DecimalFormat("0.00");
		angleFormatter = new DecimalFormat("00.0");

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

		try {
			imageRenderComponent = new ImageRenderComponent(new Image("img/assets/panel.png"));
			this.addComponent(new ImageRenderComponent(new Image("img/assets/panel.png")));
			arrow_Weapons.addComponent(new ImageRenderComponent(new Image("img/assets/arrow_right.png")));
			arrow_Power_Right.addComponent(new ImageRenderComponent(new Image("img/assets/arrow_right.png")));
			arrow_Power_Left.addComponent(new ImageRenderComponent(new Image("img/assets/arrow_left.png")));
			arrow_Angle_Right.addComponent(new ImageRenderComponent(new Image("img/assets/arrow_right.png")));
			arrow_Angle_Left.addComponent(new ImageRenderComponent(new Image("img/assets/arrow_left.png")));
		} catch (SlickException e) {
			System.err.println("Problem with controlpanel images");
		}
		this.addComponent(imageRenderComponent);

		StateBasedEntityManager entityManager = StateBasedEntityManager.getInstance();
		int stateID = Launch.GAMEPLAY_STATE; // MR kann man evtl schoener loesen...
		entityManager.addEntity(stateID, this); // muss zuerst hinzugefuegt werden, sonst ist das Panel ueber den
												// Pfeilen...
		entityManager.addEntity(stateID, arrow_Weapons);
		entityManager.addEntity(stateID, arrow_Power_Right);
		entityManager.addEntity(stateID, arrow_Power_Left);
		entityManager.addEntity(stateID, arrow_Angle_Right);
		entityManager.addEntity(stateID, arrow_Angle_Left);

		// ShopProjectiles
		this.initShopProjectiles(List.of(ProjectileType.COCONUT, ProjectileType.SPIKEBALL, ProjectileType.BOMB,
				ProjectileType.SHARD, ProjectileType.CRYSTAL, ProjectileType.TURTLE));

		// Erstellen der Knopfdruck-Events und die zugehoerige Actions

		ANDEvent change_Weapon_Event = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		Action change_Weapon_Action = new ChangeWeaponAction(arrow_Weapons);
		change_Weapon_Event.addAction(change_Weapon_Action);
		arrow_Weapons.addComponent(change_Weapon_Event);

		ANDEvent increase_Angle_Fast_Event = new ANDEvent(new MouseEnteredEvent(), new MouseDownEvent());
		Action increase_Angle_Fast_Action = new ChangeAngleAction(0.03f * Launch.UPDATE_INTERVAL, arrow_Angle_Right);
		increase_Angle_Fast_Event.addAction(increase_Angle_Fast_Action);
		arrow_Angle_Right.addComponent(increase_Angle_Fast_Event);
		ANDEvent increase_Angle_Slow_Event = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		Action increase_Angle_Slow_Action = new ChangeAngleAction(0.1f, arrow_Angle_Right);
		increase_Angle_Slow_Event.addAction(increase_Angle_Slow_Action);
		arrow_Angle_Right.addComponent(increase_Angle_Slow_Event);

		ANDEvent decrease_Angle_Fast_Event = new ANDEvent(new MouseEnteredEvent(), new MouseDownEvent());
		Action decrease_Angle_Fast_Action = new ChangeAngleAction(-0.03f * Launch.UPDATE_INTERVAL, arrow_Angle_Left);
		decrease_Angle_Fast_Event.addAction(decrease_Angle_Fast_Action);
		arrow_Angle_Left.addComponent(decrease_Angle_Fast_Event);
		ANDEvent decrease_Angle_Slow_Event = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		Action decrease_Angle_Slow_Action = new ChangeAngleAction(-0.1f, arrow_Angle_Left);
		decrease_Angle_Slow_Event.addAction(decrease_Angle_Slow_Action);
		arrow_Angle_Left.addComponent(decrease_Angle_Slow_Event);

		ANDEvent increase_Power_Fast_Event = new ANDEvent(new MouseEnteredEvent(), new MouseDownEvent());
		Action increase_Power_Action = new ChangePowerAction(0.002f * Launch.UPDATE_INTERVAL, arrow_Power_Right);
		increase_Power_Fast_Event.addAction(increase_Power_Action);
		arrow_Power_Right.addComponent(increase_Power_Fast_Event);
		ANDEvent increase_Power_Slow_Event = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		Action increase_Power_Slow_Action = new ChangePowerAction(0.01f, arrow_Power_Right);
		increase_Power_Slow_Event.addAction(increase_Power_Slow_Action);
		arrow_Power_Right.addComponent(increase_Power_Slow_Event);

		ANDEvent decrease_Power_Fast_Event = new ANDEvent(new MouseEnteredEvent(), new MouseDownEvent());
		Action decrease_Power_Action = new ChangePowerAction(-0.002f * Launch.UPDATE_INTERVAL, arrow_Power_Left);
		decrease_Power_Fast_Event.addAction(decrease_Power_Action);
		arrow_Power_Left.addComponent(decrease_Power_Fast_Event);
		ANDEvent decrease_Power_Slow_Event = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		Action decrease_Power_Slow_Action = new ChangePowerAction(-0.01f, arrow_Power_Left);
		decrease_Power_Slow_Event.addAction(decrease_Power_Slow_Action);
		arrow_Power_Left.addComponent(decrease_Power_Slow_Event);

		setPanelAndComponentsVisible(true);
	}

	public void setPanelAndComponentsVisible(boolean isVisible) {
		setVisible(isVisible);
		for (Entity e : listOfCorrespondingEntities) {
			e.setVisible(isVisible);
		}
		if (isVisible) {
			makeFirstShopProjectileVisible();
		} else {
			for (Projectile p : listOfShopProjectiles) {
				p.setVisible(false);
			}
		}
	}

	private Vector2f relativPosOnPanelToPixelPos(float x, float y) {
		return new Vector2f(x, y).scale(panelScaleFactor).add(coordinates);
	}

	public void setFont(TrueTypeFont font) {
		this.font = font;
	}

	public List<Entity> getListOfCorrespondingEntities() {
		return listOfCorrespondingEntities;
	}

	public void addToListOfCorrespondingEntities(Entity entity) {
		listOfCorrespondingEntities.add(entity);
	}

	private void initShopProjectiles(List<ProjectileType> types) {
		StateBasedEntityManager entityManager = StateBasedEntityManager.getInstance();
		for (ProjectileType type : types) {
			Projectile shopProjectile = (Projectile) new ProjectileFactory("ShopProjectile", new Vector2f(),
					new Vector2f(), true, false, type).createEntity();
			shopProjectile.setPosition(relativPosOnPanelToPixelPos(380, -230));
			shopProjectile.setVisible(false);

			switch (type) {

			default: // entspricht Case COCONUT
				shopProjectile.setScale(shopProjectile.getScale() * 1.2f);
				shopProjectile.setRotation(45);
				break;

			case SPIKEBALL:
				shopProjectile.setScale(shopProjectile.getScale() * 1.3f);
				shopProjectile.setRotation(0);
				break;

			case BOMB:
				shopProjectile.setScale(shopProjectile.getScale() * 1.2f);
				shopProjectile.setRotation(225);
				break;

			case SHARD:
				shopProjectile.setScale(shopProjectile.getScale() * 1.5f);
				shopProjectile.setRotation(65);
				break;

			case CRYSTAL:
				shopProjectile.setScale(shopProjectile.getScale() * 1.1f);
				shopProjectile.setRotation(45);
				break;

			case TURTLE:
				shopProjectile.setScale(shopProjectile.getScale() * 1.3f);
				shopProjectile.setRotation(0);
				break;
			}

			listOfShopProjectiles.add(shopProjectile);
			entityManager.addEntity(Launch.GAMEPLAY_STATE, shopProjectile);
		}
		makeFirstShopProjectileVisible();
	}

	private void makeFirstShopProjectileVisible() {
		if (!listOfShopProjectiles.isEmpty()) {
			listOfShopProjectiles.get(0).setVisible(true);
		}

	}

	public void nextShopProjectil() {
		int indexOfVisibleProjectile = getIndexOfVisibleShopProjectile();
		listOfShopProjectiles.get(indexOfVisibleProjectile).setVisible(false); // Altes Shop Projektil unsichtbar machen
		int indexOfNextProjectile = indexOfVisibleProjectile + 1;
		if (indexOfNextProjectile >= listOfShopProjectiles.size()) { // Wenn letztes Listenelement erreicht
			makeFirstShopProjectileVisible();
		} else {
			listOfShopProjectiles.get(indexOfNextProjectile).setVisible(true);
		}

	}

	private int getIndexOfVisibleShopProjectile() {
		Integer indexOfVisibleProjectile = null;
		for (int i = 0; i < listOfShopProjectiles.size(); i++) {
			if (listOfShopProjectiles.get(i).isVisible()) {
				if (indexOfVisibleProjectile != null) {
					throw new RuntimeException("More than one shop projectile is visible");
				}
				indexOfVisibleProjectile = i;
			}
		}
		if (indexOfVisibleProjectile == null) {
			throw new RuntimeException("No shop projectile is visible");
		}
		return indexOfVisibleProjectile;
	}

	public Projectile getSelectedProjectile() {
		return listOfShopProjectiles.get(getIndexOfVisibleShopProjectile());
	}

	/**
	 * Bestimmt, wenn location = FREE, unter 4 vorgegebenen Eck-Positionen,
	 * diejenige, die den groessten Qualitaetswert besitzt (Moeglichst keinen
	 * Planeten verdeckt) Anderenfalls wird die Position fuer die erzwungene
	 * location zurueckgegeben
	 * 
	 * @return Vector2f Pixel Position
	 */
	private Vector2f calcPosition() {
		Vector2f leftUpperCorner = new Vector2f(0.18f * Launch.WIDTH, 0.14f * Launch.HEIGHT);
		Vector2f rightUpperCorner = new Vector2f(0.82f * Launch.WIDTH, 0.14f * Launch.HEIGHT);
		Vector2f leftLowerCorner = new Vector2f(0.18f * Launch.WIDTH, 0.86f * Launch.HEIGHT);
		Vector2f rightLowerCorner = new Vector2f(0.82f * Launch.WIDTH, 0.86f * Launch.HEIGHT);
		if (location == Location.TOP_LEFT) {
			return leftUpperCorner;
		}
		if (location == Location.TOP_RIGHT) {
			return rightUpperCorner;
		}
		if (location == Location.BOTTOM_LEFT) {
			return leftLowerCorner;
		}
		if (location == Location.BOTTOM_RIGHT) {
			return rightLowerCorner;
		}

		HashMap<Vector2f, Float> positionQualityTable = new HashMap<Vector2f, Float>();
		// Die HashMap speichtert als Key die 4 moeglichen Positionen und als Value die
		// Qualitaet der entsprechenden Position
		positionQualityTable.put(leftUpperCorner, getQuality(leftUpperCorner));
		positionQualityTable.put(rightUpperCorner, getQuality(rightUpperCorner));
		positionQualityTable.put(leftLowerCorner, getQuality(leftLowerCorner));
		positionQualityTable.put(rightLowerCorner, getQuality(rightLowerCorner));

		// Durchsucht die HashMap nach dem groessten Value und gibt den entsprechenden
		// Key zurueck.
		Vector2f bestPosition = Collections.max(positionQualityTable.entrySet(), HashMap.Entry.comparingByValue()).getKey();
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

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		if (this.isVisible() && !(imageRenderComponent == null)) {

			imageRenderComponent.render(container, game, g);

			// Render Text
			Ape activeApe = Map.getInstance().getActiveApe();

			Vector2f textPos_Power = relativPosOnPanelToPixelPos(-470, -30);
			font.drawString(textPos_Power.x, textPos_Power.y, "Power", Color.black);

			Vector2f textPos_Angle = relativPosOnPanelToPixelPos(230, -20);
			font.drawString(textPos_Angle.x, textPos_Angle.y, "Angle", Color.black);

			Vector2f textPos_ActivePlayer = relativPosOnPanelToPixelPos(-600, -290);
			font.drawString(textPos_ActivePlayer.x, textPos_ActivePlayer.y, activeApe.getID(), Color.black);

			Vector2f numberPos_Power = relativPosOnPanelToPixelPos(-450, 150);
			font.drawString(numberPos_Power.x, numberPos_Power.y, powerFormatter.format(activeApe.getThrowStrength()),
					Color.black);

			Vector2f numberPos_Angle = relativPosOnPanelToPixelPos(280, 150);
			font.drawString(numberPos_Angle.x, numberPos_Angle.y, angleFormatter.format(activeApe.getLocalAngleOfView()),
					Color.black);

			Vector2f numberPos_Price = relativPosOnPanelToPixelPos(50, -280);
			int price = getSelectedProjectile().getPrice();
			if (price <= activeApe.getCoins()) {
				font.drawString(numberPos_Price.x, numberPos_Price.y, Integer.toString(price) + "$",
						Color.green.darker(0.5f));
			} else {
				font.drawString(numberPos_Price.x, numberPos_Price.y, Integer.toString(price) + "$",
						Color.red.darker(0.3f));
			}

		}
	}

}
