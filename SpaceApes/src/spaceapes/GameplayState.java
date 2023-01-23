package spaceapes;

import java.util.Iterator;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.action.basicactions.*;
import eea.engine.component.Component;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.event.Event;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.basicevents.*;
import entities.Ape;
import entities.Coin;
import entities.ControlPanel;
import entities.Projectile;
import factories.ProjectileFactory;
import factories.ProjectileFactory.ProjectileType;
import map.Map;
import utils.Utils;

/**
 * @author Timo Baehr
 *
 *         Diese Klasse repraesentiert das Spielfenster
 */
public class GameplayState extends BasicGameState {

	private int stateID; // Identifier dieses BasicGameState
	private StateBasedEntityManager entityManager; // zugehoeriger entityManager
	private Map map;
	
	GameplayState( int stateID ) {
	       this.stateID = stateID;
	       this.entityManager = StateBasedEntityManager.getInstance();
	       this.map = Map.getInstance();
	}
	
	/**
	 * Wird vor dem (erstmaligen) Starten dieses States ausgefuehrt
	 */
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		
		// Map parsen
		map.parse();
		
		Entity coin = new Coin();
		coin.setPosition(new Vector2f(Launch.WIDTH / 2, Launch.HEIGHT / 2));
		coin.addComponent(new ImageRenderComponent(new Image("assets/items/coin2.png")));
		float itemWidthInPixel = 100;
		float desiredItemWidth = 0.03f; // im Verhaeltnis zur Fenster Breite
		float itemScaleFactor = desiredItemWidth * Launch.WIDTH / itemWidthInPixel;
		coin.setScale(itemScaleFactor);
		LoopEvent itemLoop = new LoopEvent();
		itemLoop.addAction(new RotateRightAction(0.03f));
		coin.addComponent(itemLoop);
		entityManager.addEntity(stateID, coin);
		
		// Die dummyEntity steuert die Wechsel der States
		Entity dummyEntity = new Entity("Dummy");
		
		/* ESC-Taste */
		// Bei Druecken der ESC-Taste zurueck ins Hauptmenue wechseln
		Event escPressed = new KeyPressedEvent(Input.KEY_ESCAPE);
		escPressed.addAction(new ChangeStateAction(Launch.MAINMENU_STATE));
		dummyEntity.addComponent(escPressed);
		
		//Hier kommen alle weiteren Events hinzu...
		
		entityManager.addEntity(stateID, dummyEntity);
		
		// Initialisierung der Aimline
		map.updateAimline();
	}
	
	/**
	 * Wird vor dem Frame ausgefuehrt
	 */
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		// StatedBasedEntityManager soll alle Entities aktualisieren
		entityManager.updateEntities(container, game, delta);
	}

	/**
	 * Wird mit dem Frame ausgefuehrt
	 */
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		// StatedBasedEntityManager soll alle Entities rendern
		entityManager.renderEntities(container, game, g);
		// g.drawString("TEST", 100, 100);
	}

	@Override
	public int getID() {
		return stateID;
	}
	
	public StateBasedEntityManager getEntityManager() {
		return entityManager;
	}
}
