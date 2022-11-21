package de.tud.gdi1.dropofwater.ui;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.action.Action;
import eea.engine.action.basicactions.ChangeStateAction;
import eea.engine.action.basicactions.DestroyEntityAction;
import eea.engine.action.basicactions.MoveDownAction;
import eea.engine.component.Component;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.basicevents.KeyPressedEvent;
import eea.engine.event.basicevents.LeavingScreenEvent;
import eea.engine.event.basicevents.LoopEvent;
import eea.engine.event.basicevents.MouseClickedEvent;

/**
 * @author Timo Bähr
 *
 * Diese Klasse repraesentiert das Spielfenster, indem ein Wassertropfen
 * erscheint und nach unten faellt.
 */
public class GameplayState extends BasicGameState {

	private int stateID; 							// Identifier dieses BasicGameState
	private StateBasedEntityManager entityManager; 	// zugehoeriger entityManager
    
    GameplayState( int sid ) {
       stateID = sid;
       entityManager = StateBasedEntityManager.getInstance();
    }
    
    /**
     * Wird vor dem (erstmaligen) Starten dieses States ausgefuehrt
     */
    @Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
    	
    	// Hintergrund laden
    	Entity background = new Entity("background");	// Entitaet fuer Hintergrund
    	background.setPosition(new Vector2f(400,300));	// Startposition des Hintergrunds
    	background.addComponent(new ImageRenderComponent(new Image("/assets/background.png"))); // Bildkomponente
    	    	
    	// Hintergrund-Entitaet an StateBasedEntityManager uebergeben
    	StateBasedEntityManager.getInstance().addEntity(stateID, background);
    	
    	// Bei Drücken der ESC-Taste zurueck ins Hauptmenue wechseln
    	Entity esc_Listener = new Entity("ESC_Listener");
    	KeyPressedEvent esc_pressed = new KeyPressedEvent(Input.KEY_ESCAPE);
    	esc_pressed.addAction(new ChangeStateAction(Launch.MAINMENU_STATE));
    	esc_Listener.addComponent(esc_pressed);    	
    	entityManager.addEntity(stateID, esc_Listener);
    	
    	// Bei Mausklick soll Wassertropfen erscheinen
    	Entity mouse_Clicked_Listener = new Entity("Mouse_Clicked_Listener");
    	MouseClickedEvent mouse_Clicked = new MouseClickedEvent();
    	
    	mouse_Clicked.addAction(new Action() {
			@Override
			public void update(GameContainer gc, StateBasedGame sb, int delta,
					Component event) {
				// Wassertropfen wird erzeugt
				Entity drop = new Entity("drop of water");
				drop.setPosition(new Vector2f(gc.getInput().getMouseX(),gc.getInput().getMouseY()));
				
				try {
					// Bild laden und zuweisen
					drop.addComponent(new ImageRenderComponent(new Image("assets/drop.png")));
				} catch (SlickException e) {
					System.err.println("Cannot find file assets/drop.png!");
					e.printStackTrace();
				}
				
				// Wassertropfen faellt nach unten
				LoopEvent loop = new LoopEvent();
		    	loop.addAction(new MoveDownAction(0.5f));
		    	drop.addComponent(loop);
		    	
		    	// Wenn der Bildschirm verlassen wird, dann ...
		    	LeavingScreenEvent lse = new LeavingScreenEvent();
		    	
		    	// ... zerstoere den Wassertropfen
		    	lse.addAction(new DestroyEntityAction());
		    	// ... und wechsle ins Hauptmenue
		    	lse.addAction(new ChangeStateAction(Launch.MAINMENU_STATE));
		    	
		    	drop.addComponent(lse);
		    	entityManager.addEntity(stateID, drop);
			}    		
    	});
    	mouse_Clicked_Listener.addComponent(mouse_Clicked);
    	
    	entityManager.addEntity(stateID, mouse_Clicked_Listener);    	
    	
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
	}

	@Override
	public int getID() {
		return stateID;
	}
}
