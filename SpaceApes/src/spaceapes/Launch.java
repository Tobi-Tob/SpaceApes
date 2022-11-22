package spaceapes;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
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
	public static final int WIDTH = 1200; // Pixelbreite des Fensters
	public static final int HEIGHT = 900; // Pixelhoehe des Fensters

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

		// Lege die Einstellungen des Fensters fest und starte das Fenster
		// (nicht aber im Vollbildmodus)
		app.setDisplayMode(WIDTH, HEIGHT, false);
		app.start();
	}

	@Override
	public void initStatesList(GameContainer arg0) throws SlickException {

		// Fuege dem StateBasedGame die States hinzu
		// (der zuerst hinzugefuegte State wird als erster State gestartet)
		addState(new MainMenuState(MAINMENU_STATE));
		addState(new GameplayState(GAMEPLAY_STATE));

		// Fuege dem StateBasedEntityManager die States hinzu
		StateBasedEntityManager.getInstance().addState(MAINMENU_STATE);
		StateBasedEntityManager.getInstance().addState(GAMEPLAY_STATE);

	}
}