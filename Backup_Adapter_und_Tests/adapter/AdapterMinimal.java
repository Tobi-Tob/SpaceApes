package adapter;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;
import eea.engine.entity.StateBasedEntityManager;
import testUtils.TestAppGameContainer;
import utils.Utils;

//The following are just used for the implementation of the adapter-methodes and have to be removed for the student version
import entities.Projectile;
import factories.ProjectileFactory.MovementType;
import map.Map;
import spaceapes.Constants;
import spaceapes.SpaceApes;

/**
 * This is the test adapter for the minimal stage of completion. You must implement the method stubs and match
 * them to your concrete implementation. Please read all the Javadoc of a method before implementing it.
 * Important: this class should not contain any real game logic, you should rather only match the
 * method stubs to your game.
 * Example: in {@link #isCorrectMap()} you may return the value Launch.isCorrectMap(), if you have a variable
 * Launch and a map has before created via {@link #createMap(...)}. What you mustn't do is to
 * implement the actual logic of the method in this class.
 *
 * If you have implemented the minimal stage of completion, you should be able to implement all method stubs. The public
 * and private JUnit tests for the minimal stage of completion will be run on this test adapter. The other test adapters
 * will inherit from this class, because they need the basic methods (like creating a map), too.
 * 
 * The methods of all test adapters need to function without any kind of user interaction.
 * 
 * Note: All other test adapters will inherit from this class.
 * 
 * @see AdapterExtended1
 * @see AdapterExtended2
 * @see AdapterExtended3
 */
public class AdapterMinimal {
	
	SpaceApes launch; 					// inherits from StateBasedGame
	TestAppGameContainer app;			// special variant of the AppGameContainer, which doesn't build a UI (just for the tests!)
	boolean isMapCorrect;
	
	
	/**
	 * 
	 * Use this constructor to set up everything you need.
	 */
	public AdapterMinimal() {
		super();
		launch = null;
		//syntaxException = true;
		//semanticException = true;
		Map.getInstance().resetToDefault();
		isMapCorrect = false;
	}
	
	/* *************************************************** 
	 * ********* initialize, run, stop the game **********
	 * *************************************************** */
	
	public StateBasedGame getStateBasedGame() {
		return launch;
	}
	
	/**
	 * This method initializes the game in no-GUI mode. That means no
	 * AppGameContainer is started, so no window will be created
	 */
	public void initializeGame() {
		
		// Here you set the library path which depends on the operationg system
		if (System.getProperty("os.name").toLowerCase().contains("windows")) {
			System.setProperty("org.lwjgl.librarypath", System.getProperty("user.dir") + "/native/windows");
		} else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
			System.setProperty("org.lwjgl.librarypath", System.getProperty("user.dir") + "/native/macosx");
		} else {
			System.setProperty("org.lwjgl.librarypath",
					System.getProperty("user.dir") + "/native/" + System.getProperty("os.name").toLowerCase());
		}
		
		// Start game without GUI
    	launch = new SpaceApes(false);

		try {
			Map.getInstance().resetToDefault();
			app = new TestAppGameContainer(launch);
			app.start(0);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method stopps the running game
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
	 * *************** game settings *********************
	 * *************************************************** */
	
	/**
	 * This method returns the value of the main menu state
	 * 
	 * @return returns the value of the main menu state
	 */
	public int getMainMenuStateID() {
		return SpaceApes.MAINMENU_STATE;
	}
	
	/**
	 * This method returns the value of the gameplay state
	 * 
	 * @return returns the value of the gameplay state
	 */
	public int getGameplayStateID() {
		return SpaceApes.GAMEPLAY_STATE;
	}
	
	/**
	 * This method returns fixed update intervall in milli seconds (ms)
	 * Note: If you are not sure what to choose as update rate, we used 20ms in the reference implementation
	 * 
	 * @return returns fixed update intervall
	 */
	public int getUpdateIntervall() {
		return SpaceApes.UPDATE_INTERVAL;
	}
	
	
	/* *************************************************** 
	 * *********************** Map ***********************
	 * *************************************************** */
	
	/**
	 * This method creates a new map. 
	 * 
	 * @param coordinatesPlanet1 - coordinates of Planet1. In the "Ausbaustufe" 2 & 3 "null" is passed to indicate a random position as explained in the task
	 * @param coordinatesPlanet2 - coordinates of Planet2. In the "Ausbaustufe" 2 & 3 "null" is passed to indicate a random position as explained in the task
	 * @param radiusPlanet1 - radius of Planet1. In the "Ausbaustufe" 2 & 3 "0" is passed to indicate a random radius as explained in the task
	 * @param radiusPlanet2 - radius of Planet2. In the "Ausbaustufe" 2 & 3 "0" is passed to indicate a random radius as explained in the task
	 * @param massPlanet1 - mass of Planet1. In the "Ausbaustufe" 2 & 3 "0" is passed to indicate a random mass as explained in the task
	 * @param massPlanet2 - mass of Planet1. In the "Ausbaustufe" 2 & 3 "0" is passed to indicate a random mass as explained in the task
	 * @param angleOnPlanetApe1 - angle of Ape1 on its planet in degrees. In the "Ausbaustufe" 2 & 3 "999" is passed to indicate a random angle
	 * @param angleOnPlanetApe2 - angle of Ape2 on its planet in degrees. In the "Ausbaustufe" 2 & 3 "999" is passed to indicate a random angle
	 */
	public void createMap(Vector2f coordinatesPlanet1, Vector2f coordinatesPlanet2, float radiusPlanet1, float radiusPlanet2, int massPlanet1, int massPlanet2, float angleOnPlanetApe1, float angleOnPlanetApe2) {
		Map.getInstance().init(coordinatesPlanet1, coordinatesPlanet2, radiusPlanet1, radiusPlanet2, massPlanet1, massPlanet2, false, MovementType.LINEAR, angleOnPlanetApe1, angleOnPlanetApe2, false);
		if (Map.getInstance() != null) {
			isMapCorrect = true;
		}
	}
	
	/**
	 * @return true if the map was created correctly
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
	
	/**
	 * 
	 * @param indexOfPlayer - index of player
	 * @return returns the name of a planet of the player with the given index
	 */
	public String getPlanetName(int indexOfPlayer) {
		return Map.getInstance().getPlanets().get(indexOfPlayer).getID();
	}
	
	/**
	 * 
	 * @param indexOfPlayer - index of player
	 * @return returns the coordinates of the planet of the player with the given index
	 */
	public Vector2f getPlanetCoordinates(int indexOfPlayer) {
		return Map.getInstance().getPlanets().get(indexOfPlayer).getCoordinates();
	}
	
	/**
	 * 
	 * @param indexOfPlayer - index of player
	 * @return returns the radius of the planet of the player with the given index
	 */
	public float getPlanetRadius(int indexOfPlayer) {
		return Map.getInstance().getPlanets().get(indexOfPlayer).getRadius();
	}
	
	/**
	 * 
	 * @param indexOfPlayer - index of player
	 * @return returns the mass of the planet of the player with the given index
	 */
	public float getPlanetMass(int indexOfPlayer) {
		return Map.getInstance().getPlanets().get(indexOfPlayer).getMass();
	}
	
	/**
	 * 
	 * @param indexOfPlayer - index of player
	 * @return returns true if an ape is placed on the planet of the player with the given index
	 */
	public boolean hasApe(int indexOfPlayer) {
		return Map.getInstance().getPlanets().get(indexOfPlayer).hasApe();
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
	
	/**
	 * 
	 * @param indexOfPlayer - index of player
	 * @return returns the name of a ape of the player with the given index
	 */
	public String getApeName(int indexOfPlayer) {
		return Map.getInstance().getApes().get(indexOfPlayer).getID();
	}
	
	/**
	 * 
	 * @param indexOfPlayer - index of player
	 * @return returns the coordinates of the ape of the player with the given index
	 */
	public Vector2f getApeCoordinates(int indexOfPlayer) {
		return Map.getInstance().getApes().get(indexOfPlayer).getWorldCoordinates();
	}
	
	/**
	 * Note: it is important to return the distance from the feet of the ape to the planet center, not the coordinates of the ape!
	 * 
	 * @param indexOfPlayer - index of player
	 * @return returns the distance to the planet center of the apes feet of the player with the given index
	 */
	public float getApeDistanceFeetToPlanetCenter(int indexOfPlayer) {
		return Map.getInstance().getApes().get(indexOfPlayer).getDistanceToPlanetCenter() - Utils.pixelLengthToWorldLength(Constants.APE_PIXEL_FEET_TO_CENTER * getApeScalingFactor());
	}
	
	/**
	 * Note: it is important to return the distance from the position of the ape to the planet center, not the coordinates of the ape!
	 * 
	 * @param indexOfPlayer - index of player
	 * @return returns the distance from the position of the ape to the planet center
	 */
	public float getApeDistanceToPlanetCenter(int indexOfPlayer) {
		return Map.getInstance().getApes().get(indexOfPlayer).getDistanceToPlanetCenter();
	}
	
	/**
	 * 
	 * @param indexOfPlayer - index of player
	 * @return returns the angle on the planet of the ape of the player with the given index
	 */
	public float getApeAngleOnPlanet(int indexOfPlayer) {
		return Map.getInstance().getApes().get(indexOfPlayer).getAngleOnPlanet();
	}
	
	/**
	 * This method sets the angle on the planet of the ape of the player with the given index.
	 * So it basicly sets its position on the planet
	 * 
	 * @param indexOfPlayer - index of player
	 */
	public void setApeAngleOnPlanet(int indexOfPlayer, int angleInDegrees) {
		Map.getInstance().getApes().get(indexOfPlayer).setAngleOnPlanet(angleInDegrees);
	}
	
	/**
	 * 
	 * @param indexOfPlayer - index of player
	 * @return returns the radius of the ape of the ape of the player with the given index in world units
	 */
	public float getApeRadiusInWorldUnits(int indexOfPlayer) {
		return Map.getInstance().getApes().get(indexOfPlayer).getRadiusInWorldUnits();
	}
	
	/**
	 * 
	 * @param indexOfPlayer - index of player
	 * @return returns the rotation of the ape of the player with the given index
	 */
	public float getApeRotation(int indexOfPlayer) {
		return Map.getInstance().getApes().get(indexOfPlayer).getRotation();
	}
	
	/**
	 * 
	 * @return returns the scaling factor of the apes 
	 */
	public float getApeScalingFactor() {
		// get(0) da es bei allen Apes gleich ist
		return Map.getInstance().getApes().get(0).getScalingFactor();
	}
	
	/**
	 * 
	 * @param indexOfPlayer - index of player
	 * @return returns global angle of view of the ape of the player with the given index can interact
	 */
	public float getApeGlobalAngleOfView(int indexOfPlayer) {
		return Map.getInstance().getApes().get(indexOfPlayer).getGlobalAngleOfView();
	}
	
	/**
	 * 
	 * @param indexOfPlayer - index of player
	 * @return returns the throw strength set by the player with the given index
	 */
	public float getThrowStrength(int indexOfPlayer) {
		return Map.getInstance().getApes().get(indexOfPlayer).getThrowStrength();
	}
	
	/**
	 * This method returns the movement speed of the ape.
	 * Note: The returned value is a factor that is multiplied with the velocity in coordinates/milliseconds.
	 * If you are not sure what to do, in the test a movement speed of 0.05f is used with an
	 * update rate of 20 milliseconds
	 * 
	 * @return returns the movement speed of the ape. 
	 */
	public float getApeMovementSpeed() {
		return Constants.APE_MOVMENT_SPEED;
	}
	
	/**
	 * 
	 * @param indexOfPlayer - index of player
	 * @return returns the health of the ape of the player with the given index
	 */
	public int getApeHealth(int indexOfPlayer) {
		return Map.getInstance().getApes().get(indexOfPlayer).getHealth();
	}
	
	/**
	 * @return returns the number of living apes
	 */
	public int getNumberOfLivingApes() {
		return Map.getInstance().getApes().size();
	}
	
	/**
	 * @param indexOfPlayer - index of player
	 * @return returns true if the ape of the player with the given index can interact
	 */
	public boolean isApeInteractionAllowed(int indexOfPlayer) {
		return Map.getInstance().getApes().get(indexOfPlayer).isInteractionAllowed();
	}
	
	/**
	 * This method manually changes turn to the other player
	 * (no matter if the projectile is still flying or not)
	 */
	public void changeTurn() {
		Map.getInstance().changeTurn();
	}
	
	/* *************************************************** 
	 * ***************** Projectiles *********************
	 * *************************************************** */
	
	/**
	 * @return returns the world coordinates of the current projectile. If not existing then null
	 */
	public Vector2f getProjectileCoordinates() {
		if (StateBasedEntityManager.getInstance().getEntity(SpaceApes.GAMEPLAY_STATE, Constants.PROJECTILE_ID)==null) {
			System.out.println("No Entity with ID '" + Constants.PROJECTILE_ID + "' in EntityManager! In getProjectile");
			return null;
		} else {
			return ((Projectile) StateBasedEntityManager.getInstance().getEntity(SpaceApes.GAMEPLAY_STATE, Constants.PROJECTILE_ID)).getCoordinates();
		}
	}
	
	
	/* *************************************************** 
	 * ******************** General **********************
	 * *************************************************** */
	
	/**
	 * This Method should emulate the key pressed event.
	 * This enables the testing of player interaction.
	 * 
	 * @param updatetime : time duration till the update-method is called
	 * @param input : e.g. Input.KEY_K, Input.KEY_L
	 */
	public void handleKeyPressed(int updatetime, Integer input) {
		if (launch != null && app != null) {
			app.getTestInput().setKeyPressed(input);
			try {
				app.updateGame(updatetime);
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * This Method should emulate the key down event.
	 * This enables the testing of player interaction.
	 * 
	 * @param updatetime : time duration till the update-method is called
	 * @param input : e.g. Input.KEY_K, Input.KEY_L
	 */
	public void handleKeyDown(int updatetime, Integer input) {
		if (launch != null && app != null) {
			app.getTestInput().setKeyDown(input);
			try {
				app.updateGame(updatetime);
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
	}
	
}
