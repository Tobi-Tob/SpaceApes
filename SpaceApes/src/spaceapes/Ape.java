package spaceapes;

import org.newdawn.slick.geom.Vector2f;
import eea.engine.entity.Entity;

public class Ape extends Entity {

	private final int belongsToPlayer; // Zugehoeriger Spieler (0 oder 1)
	private final Planet homePlanet; // Planet auf dem sich der Affe befindet
	private float angleOnPlanet; // Repraesentiert durch Winkel, Drehsinn mathematisch positiv, 0 enspricht dem
									// noerdlichsten Punkt
	private int health; // Lebenspunkte des Affen

	public Ape(String entityID, Planet planet, int player) {
		super(entityID);
		belongsToPlayer = player;
		homePlanet = planet;
		angleOnPlanet = Utils.randomFloat(0, 360);
		health = 100;
		planet.addApeToPlanet();

		setPosition(Utils.toPixelCoordinates(this.calcApePosition()));
		setScale(0.1f);
		setRotation(angleOnPlanet);
	}

	/**
	 * Bestimmt Abstand vom Mittelpunkt des Affen zum Mittelpunkt seines Planeten.
	 * Faktoren a und b muessen manuell angepasst werden bei Nutzung anderer Bildern
	 * 
	 * @throws RuntimeException wenn Radius zu klein
	 */
	private float calcRadius() throws RuntimeException {
		float a = 0.2f; // Faktor a Abhaengig von Groesse und Skalierung des Planetenbilds
		float b = 0.2f; // Faktor b Abhaengig von Groesse und Skalierung des Affenbilds
						// Hinweis: Erst a justieren, bis Affenmittelpunkt auf Planetenoberflaeche, dann
						// mit b den Affen etwas von der Oberflaeche anheben
		float r = homePlanet.size() * a + b; // Radius von Planetenmittelpunkt zu Affenmittelpunkt
		if (r > 0.5f) {
			return r;
		} else
			throw new RuntimeException("Radius ist zu klein");
	}

	/**
	 * Berechnet die Welt-Koordinaten des Affen, abhaengig von seinem Winkel auf dem
	 * Planeten (angleOnPlanet) und der Position des Planeten
	 * 
	 * @return Vector2f in Welt-Koordinaten
	 */
	public Vector2f calcApePosition() {
		float r = this.calcRadius();
		double angleInRad = Math.toRadians(angleOnPlanet);
		// Koordinaten des Planeten + relative Koordinaten vom Planeten zum Affen
		float apePos_x = homePlanet.getCoordinates().x + r * (float) Math.sin(angleInRad);
		float apePos_y = homePlanet.getCoordinates().y - r * (float) Math.cos(angleInRad); // Minus, da y-Achse nach unten
		return new Vector2f(apePos_x, apePos_y);
	}

	public void stepRigth() {
		float c = 4; // Faktor c fuer die Schrittweite des Affen
		angleOnPlanet += c / calcRadius(); // Update des Winkels

		setPosition(Utils.toPixelCoordinates(this.calcApePosition()));
		setRotation(angleOnPlanet);
	}

	public void stepLeft() {
		float c = 4; // Faktor c fuer die Schrittweite des Affen
		angleOnPlanet -= c / calcRadius(); // Update des Winkels

		setPosition(Utils.toPixelCoordinates(this.calcApePosition()));
		setRotation(angleOnPlanet);
	}

	public int belongsToPlayer() {
		return belongsToPlayer;
	}

	public Planet homePlanet() {
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
