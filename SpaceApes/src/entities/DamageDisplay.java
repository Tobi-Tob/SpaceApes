package entities;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import spaceapes.SpaceApes;
import utils.Constants;
import utils.Utils;

/**
 * Klasse zum Visualisieren von Damage Werten an Affen
 * 
 * @author Tobi
 *
 */
public class DamageDisplay extends Entity {

	private Ape ape;
	private Color color;
	private String damageString;

	private float posX; // In Pixel Koordinaten
	private float posY;

	private int timer; // in ms
	private int displayTime; // in ms

	public DamageDisplay(Ape ape, int damage, int displayTime) {
		super(Constants.DAMAGE_DISPLAY_ID);
		this.ape = ape;
		this.color = this.colorFunction(damage);
		this.damageString = Integer.toString(-damage);

		Vector2f pos = getPositionAboveApe(0.6f);

		this.posX = pos.x - Constants.DAMAGE_FONT.getWidth(damageString) / 2;
		this.posY = pos.y - Constants.DAMAGE_FONT.getHeight() / 2;

		this.timer = 0;
		this.displayTime = displayTime;
	}

	/**
	 * Berechnet Position Ueber dem Affen in Pixel Koordinaten
	 * 
	 * @param distanceAboveApe Abstand zum Affen in Weltkoordinaten
	 * @return Pixel Koordinaten
	 */
	private Vector2f getPositionAboveApe(float distanceAboveApe) {
		Vector2f relativPos = Utils.toCartesianCoordinates(ape.getDistanceToPlanetCenter() + distanceAboveApe,
				ape.getAngleOnPlanet());
		// Koordinaten des Planeten + relative Koordinaten vom Planeten zum Text ueber
		// dem Affen
		return Utils.toPixelCoordinates(relativPos.add(ape.getPlanet().getCoordinates()));
	}

	private Color colorFunction(int damage) {
		int r, g, b;
		r = g = b = 255;
		b -= (int) (12f * (damage - 10)); // mit groesserem damage wird die Farbe gelblicher
		g -= (int) (8f * (damage - 30)); // noch groesserer damage und die Farbe wird roetlicher
		if (damage >= 100) {
			r = 100; // dei damage ueber 100 blau
			g = b = 255;
		}

		return new Color(r, g, b);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) {
		timer += delta;
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		if (timer < displayTime) {
			Constants.DAMAGE_FONT.drawString(posX, posY, damageString, color);
		} else {
			StateBasedEntityManager.getInstance().removeEntity(SpaceApes.GAMEPLAY_STATE, this);
		}
	}

}
