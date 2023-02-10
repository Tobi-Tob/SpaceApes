package spaceapes;

import java.util.List;
import java.awt.Font;
import java.util.ArrayList;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.entity.StateBasedEntityManager;

/**
 * @author Timo Baehr
 *
 *         Diese Klasse startet das Spiel "Space Apes". Sie enthaelt zwei
 *         State's fuer das Menue und das eigentliche Spiel.
 */
public class Launch extends StateBasedGame {

	// Jeder State wird durch einen Integer-Wert gekennzeichnet
	public static final int MAINMENU_STATE = 0;
	public static final int GAMEPLAY_STATE = 1;
	public static final int HIGHSCORE_STATE = 2;

	public static final List<String> players = new ArrayList<>(List.of("Player1", "Player2", "Player3"));
	// TODO Kompatibilitaet mit 3+ Spielern

	public static int WIDTH = 1200;
	public static int HEIGHT = 900; // Fenstergroesse wird ueberschrieben, wenn USE_FULL_SCREEN = true
	public static boolean USE_FULL_SCREEN = false;

	public static boolean PLAY_MUSIC = false;

	// Sollte das Spiel anfangen zu laggen, vergroessere das Update Intervall
	public static final int UPDATE_INTERVAL = 20; // Updatefrequenz der Gameloop in ms

	public Launch() {
		super("Space Apes"); // Name des Spiels
	}

	public static void main(String[] args) throws SlickException {
		// Setze den library Pfad abhaengig vom Betriebssystem
		if (System.getProperty("os.name").toLowerCase().contains("windows")) {
			System.setProperty("org.lwjgl.librarypath", System.getProperty("user.dir") + "/native/windows");
		} else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
			System.setProperty("org.lwjgl.librarypath", System.getProperty("user.dir") + "/native/macosx");
		} else {
			System.setProperty("org.lwjgl.librarypath",
					System.getProperty("user.dir") + "/native/" + System.getProperty("os.name").toLowerCase());
		}

		// Setze dieses StateBasedGame in einen App Container (oder Fenster)
		AppGameContainer app = new AppGameContainer(new Launch());
		app.setMinimumLogicUpdateInterval(UPDATE_INTERVAL);
		app.setMaximumLogicUpdateInterval(UPDATE_INTERVAL);
		app.setShowFPS(true);

		// Lege die Einstellungen des Fensters fest und starte das Fenster
		// (nicht aber im Vollbildmodus)

		if (USE_FULL_SCREEN) {
			DisplayMode dm = Display.getDesktopDisplayMode();
			WIDTH = dm.getWidth();
			HEIGHT = dm.getHeight();
		}
		app.setDisplayMode(WIDTH, HEIGHT, USE_FULL_SCREEN);

		app.start();
	}

	@Override
	public void initStatesList(GameContainer arg0) throws SlickException {

		// Fuege dem StateBasedGame die States hinzu
		// (der zuerst hinzugefuegte State wird als erster State gestartet)
		addState(new MainMenuState(MAINMENU_STATE));
		addState(new GameplayState(GAMEPLAY_STATE));
		addState(new HighscoreState(HIGHSCORE_STATE));

		// Fuege dem StateBasedEntityManager die States hinzu
		StateBasedEntityManager.getInstance().addState(MAINMENU_STATE);
		StateBasedEntityManager.getInstance().addState(GAMEPLAY_STATE);
		StateBasedEntityManager.getInstance().addState(HIGHSCORE_STATE);
	}
}