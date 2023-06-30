package utils;

import java.util.Random;
import org.newdawn.slick.geom.Vector2f;
import spaceapes.Constants;
import spaceapes.SpaceApes;

public final class Utils {
	
	public static float pixelWidth = SpaceApes.WIDTH;
	public static float pixelHeight = SpaceApes.HEIGHT;
	public static float worldWidth = Constants.WORLD_WIDTH;
 	public static float worldHeight = Constants.WORLD_HEIGHT;

	/**
	 * Wandelt Welt-Koordinaten in Pixel-Koordinaten um
	 * 
	 * @param x in Welt-Koordinaten
	 * @param y in Welt-Koordinaten
	 * @return Vector2f mit Pixel-Koordinaten
	 */
	public static Vector2f toPixelCoordinates(float x, float y) {
		return new Vector2f(x * pixelWidth / worldWidth + pixelWidth / 2, y * pixelWidth / worldWidth + pixelHeight / 2);
	}

	/**
	 * Wandelt Welt-Koordinaten in Pixel-Koordinaten um
	 * 
	 * @param v Vector2f in Welt-Koordinaten
	 * @return Vector2f mit Pixel-Koordinaten
	 */
	public static Vector2f toPixelCoordinates(Vector2f v) {
		return new Vector2f(v.x * pixelWidth / worldWidth + pixelWidth / 2, v.y * pixelWidth / worldWidth + pixelHeight / 2);
	}

	/**
	 * Wandelt Pixel-Koordinaten in Welt-Koordinaten um
	 * 
	 * @param p Vector2f in Pixel-Koordinaten
	 * @return Vector2f mit Welt-Koordinaten
	 */
	public static Vector2f toWorldCoordinates(Vector2f p) {
		return new Vector2f((p.x - pixelWidth * 0.5f) * worldWidth / pixelWidth, (p.y - pixelHeight * 0.5f) * worldWidth / pixelWidth);
	}

	public static float pixelLengthToWorldLength(float p) {
		return p * worldWidth / pixelWidth;
	}

	/**
	 * Gibt einen zufaelligen float-Wert zwischen a und b zur�ck
	 * 
	 * @param a float
	 * @param b float
	 * @return float
	 */
	public static float randomFloat(float a, float b) {
		Random r = new Random();
		return a + r.nextFloat() * (b - a);
	}

	/**
	 * Wandelt Polarkoordinaten [r, phi] in kartesische Koordinaten [x, y] um.
	 * (X-Achse ist nach rechts, Y-Achse nach unten gerichtet, phi laeuft von der
	 * X-Achse aus im Uhrzeigersinn)
	 * 
	 * @param radius      float
	 * @param angleInGrad float
	 * @return Vector2f in kartesische Koordinaten
	 */
	public static Vector2f toCartesianCoordinates(float radius, float angleInGrad) {
		double angleInRad = Math.toRadians(angleInGrad);
		return new Vector2f(radius * (float) Math.cos(angleInRad), radius * (float) Math.sin(angleInRad));
	}

	/**
	 * Gibt Winkel im Intervall (-180, 180] in Grad zurueck.
	 * 
	 * @return Winkel in Grad (0 ist Osten)
	 */
	public static float angleInPolarCoordinates(float x, float y) {
		return (float) Math.toDegrees(Math.atan2(y, x));
	}	
	
}