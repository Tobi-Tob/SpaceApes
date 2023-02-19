package adapter;

import java.io.File;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;
import eea.engine.entity.StateBasedEntityManager;
import testUtils.TestAppGameContainer;
import map.Map;
import spaceapes.Launch;

/**
 * This is the test adapter for the minimal stage of completion. You <b>must</b> implement the method stubs and match
 * them to your concrete implementation. Please read all the Javadoc of a method before implementing it. <br>
 * <strong>Important:</strong> this class should not contain any real game logic, you should rather only match the
 * method stubs to your game. <br>
 * Example: in {@link #isCorrectMap()} you may return the value <i>Tanks.isCorrectMap()</i>, if you have a variable
 * <i>Tanks</i> and a map has before been loaded via {@link #loadMapFromFile(File)}. What you mustn't do is to
 * implement the actual logic of the method in this class. <br>
 * <br>
 * If you have implemented the minimal stage of completion, you should be able to implement all method stubs. The public
 * and private JUnit tests for the minimal stage of completion will be run on this test adapter. The other test adapters
 * will inherit from this class, because they need the basic methods (like loading a map), too. <br>
 * <br>
 * The methods of all test adapters need to function without any kind of user interaction.</br>
 * 
 * <i>Note:</i> All other test adapters will inherit from this class.
 * 
 * @see AdapterExtended1
 * @see AdapterExtended2
 * @see AdapterExtended3
 */
//TODO Beschreibung anpassen!!
public class AdapterMinimal {
	
	Launch launch; 						// erbt von StateBasedGame
	TestAppGameContainer app;			// spezielle Variante des AppGameContainer, welche keine UI erzeugt (nur für Tests!)
	//boolean syntaxException;			// gibt es Syntax-Fehler //TODO brauchen wird das?
	//boolean semanticException;		// gibt es Semantik-Fehler //TODO brauchen wird das?
	boolean isMapCorrect;
	
	
	/**
	 * Verwenden Sie diesen Konstruktor zur Initialisierung von allem,
	 * was sie benoetigen
	 * 
	 * Use this constructor to set up everything you need.
	 */
	public AdapterMinimal() {
		super();
		launch = null;
		//syntaxException = true;
		//semanticException = true;
		Map.getInstance().resetToDefault(); //TODO
		isMapCorrect = false;
	}
	
	/* *************************************************** 
	 * ********* initialize, run, stop the game **********
	 * *************************************************** */
	
	public StateBasedGame getStateBasedGame() {
		return launch;
	}
	
	/**
	 * Diese Methode initialisiert das Spiel im Debug-Modus, d.h. es wird
	 * ein AppGameContainer gestartet, der keine Fenster erzeugt und aktualisiert.
	 * 
	 * Sie müssen diese Methode erweitern
	 */
	public void initializeGame() {
		
		// Setze den library Pfad abhaengig vom Betriebssystem
		if (System.getProperty("os.name").toLowerCase().contains("windows")) {
			System.setProperty("org.lwjgl.librarypath", System.getProperty("user.dir") + "/native/windows");
		} else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
			System.setProperty("org.lwjgl.librarypath", System.getProperty("user.dir") + "/native/macosx");
		} else {
			System.setProperty("org.lwjgl.librarypath",
					System.getProperty("user.dir") + "/native/" + System.getProperty("os.name").toLowerCase());
		}
		
//    	if(System.getProperty("os.name").toLowerCase().contains("windows")) {
//			System.setProperty("org.lwjgl.librarypath",System.getProperty("user.dir") + "/lib/lwjgl-2.8.3/native/windows");
//		} 
//		else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
//			System.setProperty("org.lwjgl.librarypath",System.getProperty("user.dir") + "/lib/lwjgl-2.8.3/native/macosx");
//		}
//		else {
//			System.setProperty("org.lwjgl.librarypath",System.getProperty("user.dir") + "/lib/lwjgl-2.8.3/native/" +System.getProperty("os.name").toLowerCase());
//		}
    	// Initialisiere das Spiel Tanks im Debug-Modus (ohne UI-Ausgabe)
    	//tanks = new Tanks(true); -> die haben hier übergeben, ob es ein debug sein soll oder nicht, also ob das Spiel nur über die Komandozeile läuft (so kann man leicht checken, ob z.B. geschossen wurde...)
    	launch = new Launch(false);
		
		// Initialisiere die statische Klasse Map
		try {
			Map.getInstance().resetToDefault();
			app = new TestAppGameContainer(launch);
			app.start(0);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Stoppe das im Hintergrund laufende Spiel
	 */
	public void stopGame() {
		if (app != null) {
			app.exit();
			app.destroy();
		}
		StateBasedEntityManager.getInstance().clearAllStates();
		launch = null;
	}
	
	/**
	 * Run the game for a specified duration.
	 * Laesst das Spiel fuer eine angegebene Zeit laufen
	 * 
	 * @param ms duration of runtime of the game
	 */
	public void runGame(int ms) {
		if (launch != null && app != null) {
			try {
				app.updateGame(ms);
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
	}
	
	/* *************************************************** 
	 * *********************** Map ***********************
	 * *************************************************** */
	
	/**
	 * Erstellt eine Map
	 */
	public void createMap() {
		Vector2f positionPlanet1 = new Vector2f(-4.0f, 0.0f);
		Vector2f positionPlanet2 = new Vector2f(4.0f, 0.0f);
		Map.getInstance().parse(positionPlanet1, positionPlanet2);
		if (Map.getInstance() != null) {
			isMapCorrect = true;
		}
	}
	
	/**
	 * @return Konnte die Map fehlerfrei geladen werden?
	 */
	public boolean isMapCorrect() {
		return isMapCorrect;
	}
	
	/* *************************************************** 
	 * ******************** Planets **********************
	 * *************************************************** */
	
	/**
	 * @return returns the number of planets
	 */
	public int getPlanetCount() {
		return Map.getInstance().getPlanets().size();
	}
	
	/* *************************************************** 
	 * ********************* Apes ************************
	 * *************************************************** */
	
	/**
	 * @return returns the number of apes
	 */
	public int getApeCount() {
		return Map.getInstance().getApes().size();
	}
	
}
