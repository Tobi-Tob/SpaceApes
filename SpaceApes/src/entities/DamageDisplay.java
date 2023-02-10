package entities;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;
import eea.engine.entity.Entity;
import spaceapes.Constants;
import utils.Utils;

/**
 * Klasse zum Visualisieren von Damage Werten an Affen
 * 
 * @author Tobi
 *
 */
public class DamageDisplay extends Entity {

	private Ape ape;
	private String damageString;

	private float posX; // In Pixel Koordinaten
	private float posY;

	private int timer; // in ms
	private int displayTime; // in ms

	public DamageDisplay(Ape ape, int damage, int displayTime) {
		super(Constants.DAMAGE_DISPLAY);
		this.ape = ape;
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

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) {
		timer += delta;
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		if (timer < displayTime) {
			Constants.DAMAGE_FONT.drawString(posX, posY, damageString, Color.red);
		}
	}

}
