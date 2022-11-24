package spaceapes;

import org.newdawn.slick.geom.Vector2f;

public final class Utils {

	/**
	 * Wandelt interne Koordinaten in Koordinaten der Pixel um
	 * @param x sichtbarer Bereich zwischen -8 und 8
	 * @param y sichtbarer Bereich zwischen -6 und 6
	 * @return Vector2f mit Pixelkoordinaten
	 */
	public static Vector2f toPixelCoordinates(float x, float y) {
		// [-8,8]x[-6,6] abgebildet auf [0,1200]x[0,900]
		float w = Launch.WIDTH;
		float h = Launch.HEIGHT;
		return new Vector2f(x * w / 8 + w, y * h / 6 + h);
	}
	
	/**
	 * Wandelt Pixelkoordinaten in interne Koordinaten
	 * @param p Vector2f mit Pixelkoordinaten
	 * @return Vector2f mit internen Koordinaten
	 */
	public static Vector2f toInternCoordinates(Vector2f p) {
		// [0,1200]x[0,900] abgebildet auf [-8,8]x[-6,6]
		float w = Launch.WIDTH;
		float h = Launch.HEIGHT;
		return new Vector2f((p.x - w) * 8 / w, (p.y - h) * 6 / h);
	}
}