package spaceapes;

import org.newdawn.slick.geom.Vector2f;
import eea.engine.entity.Entity;

public class Ape extends Entity {

	private final int belongsToPlayer; // Zugehoeriger Spieler (0 oder 1)
	private final Planet homePlanet; // Planet auf dem sich der Affe befindet
	private final float distancePlanetCenter; // Abstand des Planetenmittelpunkts zur Kreisbahn auf der sich der Affe
												// bewegt
	private float angleOnPlanet; // Repraesentiert durch Winkel, Drehsinn mathematisch positiv, 0 enspricht dem
									// noerdlichsten Punkt
	private float movmentSpeed; // Faktor fuer die Schrittweite des Affen
	private int health;

	/**
	 * Konstruktor fuegt Planeten einen Affen hinzu, der zufaellig auf der
	 * Oberflaeche platziert wird
	 * 
	 * @param entityID String
	 * @param planet   Zugehoeriger Planet
	 * @param player   Zugehoeriger Spieler (0 Oder 1)
	 */
	public Ape(String entityID, Planet planet, int player) {
		super(entityID);
		belongsToPlayer = player;
		homePlanet = planet;
		distancePlanetCenter = homePlanet.distanceToEntityPosition();
		angleOnPlanet = Utils.randomFloat(0, 360);
		movmentSpeed = 0.08f;
		health = 100;
		planet.addApeToPlanet();

		setPosition(Utils.toPixelCoordinates(this.calcApePosition()));
		setScale(0.1f);
		setRotation(angleOnPlanet);
	}

	/**
	 * Berechnet die Welt-Koordinaten des Affen, abhaengig von seinem Winkel auf dem
	 * Planeten (angleOnPlanet) und der Position des Planeten
	 * 
	 * @return Vector2f in Welt-Koordinaten
	 */
	public Vector2f calcApePosition() {
		double angleInRad = Math.toRadians(angleOnPlanet);
		// Koordinaten des Planeten + relative Koordinaten vom Planeten zum Affen
		float apePos_x = homePlanet.getCoordinates().x + distancePlanetCenter * (float) Math.sin(angleInRad);
		float apePos_y = homePlanet.getCoordinates().y - distancePlanetCenter * (float) Math.cos(angleInRad);
		return new Vector2f(apePos_x, apePos_y);
	}

	/**
	 * Aendert Winkel des Affens nach links oder rechts
	 * 
	 * @param direction fuer Bewegung nach links -1 und fuer Bewegung nach
	 *                  rechts +1
	 */
	public void stepOnPlanet(int direction) {
		if (!(direction == -1 || direction == 1)) {
			throw new RuntimeException("Ungueltige direction");
		}
		angleOnPlanet += direction * movmentSpeed / distancePlanetCenter; // Update des Winkels

		setPosition(Utils.toPixelCoordinates(this.calcApePosition()));
		setRotation(angleOnPlanet);
	}

	public int belongsToPlayer() {
		return belongsToPlayer;
	}

	public Planet getHomePlanet() {
		return homePlanet;
	}

	public float getAngleOnPlanet() {
		return angleOnPlanet;
	}

	public void setAngleOnPlanet(float alpha) {
		angleOnPlanet = alpha;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}
}
