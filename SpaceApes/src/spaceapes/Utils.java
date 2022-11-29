package spaceapes;

import java.util.Random;

import org.newdawn.slick.geom.Vector2f;

public final class Utils {

	/**
	 * Wandelt Welt-Koordinaten in Pixel-Koordinaten um
	 * 
	 * @param x sichtbarer Bereich zwischen -8 und 8
	 * @param y sichtbarer Bereich zwischen -6 und 6
	 * @return Vector2f mit Pixel-Koordinaten
	 */
	public static Vector2f toPixelCoordinates(float x, float y) {
		// [-8,8]x[-6,6] abgebildet auf [0,1200]x[0,900] z.B
		float w = Launch.WIDTH;
		float h = Launch.HEIGHT;
		return new Vector2f(x * w / 8 + w, y * h / 6 + h);
	}

	/**
	 * Wandelt Welt-Koordinaten in Pixel-Koordinaten um
	 * 
	 * @param v Vector2f mit Welt-Koordinaten
	 * @return Vector2f mit Pixel-Koordinaten
	 */
	public static Vector2f toPixelCoordinates(Vector2f v) {
		float w = Launch.WIDTH;
		float h = Launch.HEIGHT;
		return new Vector2f(v.x * w / 8 + w, v.y * h / 6 + h);
	}

	/**
	 * Wandelt Pixel-Koordinaten in Welt-Koordinaten um
	 * 
	 * @param p Vector2f mit Pixel-Koordinaten
	 * @return Vector2f mit Welt-Koordinaten
	 */
	public static Vector2f toInternCoordinates(Vector2f p) {
		// z.B [0,1200]x[0,900] abgebildet auf [-8,8]x[-6,6]
		float w = Launch.WIDTH;
		float h = Launch.HEIGHT;
		return new Vector2f((p.x - w) * 8 / w, (p.y - h) * 6 / h);
	}

	/**
	 * Gibt einen zufaelligen float-Wert zwischen a und b zurück
	 * 
	 * @param a float
	 * @param b float
	 * @return float
	 */
	public static float randomFloat(float a, float b) {
		Random r = new Random();
		return a + r.nextFloat() * (b - a);
	}
}