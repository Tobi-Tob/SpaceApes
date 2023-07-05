package entities;

import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import utils.Constants;
import spaceapes.SpaceApes;
import utils.Utils;

public class ApeInfoSign extends Entity {

	private final float apeInfoSignInPixel = 700;
	private final float desiredapeInfoSignInWorldUnits = 1.1f;
	private final float signScalingFactor = desiredapeInfoSignInWorldUnits / Utils.pixelLengthToWorldLength(apeInfoSignInPixel);
	private final int fontSize = Math.round(signScalingFactor * 200);

	private ImageRenderComponent imageRenderComponent = null;
	private TrueTypeFont font;
	private Ape ape; // zugehoeriger Ape

	public ApeInfoSign() {
		super(Constants.APE_INFO_SIGN_ID);
	}

	/**
	 * Instanciate ApeInfoSign for the given Ape
	 */
	public void initApeInfoSign(Ape ape) {
		Vector2f coordinates = ape.getPlanet().getCoordinates();

		this.setPosition(Utils.toPixelCoordinates(coordinates));
		this.setRotation(0);
		this.setScale(signScalingFactor);
		this.setApe(ape);
		
		StateBasedEntityManager.getInstance().addEntity(SpaceApes.GAMEPLAY_STATE, this);

		if (SpaceApes.renderImages) {
			this.setFont(new TrueTypeFont(new Font("Times New Roman", Font.BOLD, fontSize), true));
			try {
				ImageRenderComponent imageRenderComponent = new ImageRenderComponent(new Image("img/assets/ape_info_sign.png"));
				this.addComponent(imageRenderComponent);
				this.setImageRenderComponent(imageRenderComponent);
			} catch (SlickException e) {
				System.err.println("Problem with ApeInfoSign image");
			}
		}
	}

	public void setImageRenderComponent(ImageRenderComponent imageRenderComponent) {
		this.imageRenderComponent = imageRenderComponent;
	}

	public TrueTypeFont getFont() {
		return font;
	}

	public void setFont(TrueTypeFont font) {
		this.font = font;
	}

	public Ape getApe() {
		return ape;
	}

	public void setApe(Ape ape) {
		this.ape = ape;
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		if (this.isVisible() && !(imageRenderComponent == null)) {

			imageRenderComponent.render(container, game, g);

			Vector2f healthPos = relativPosOnPanelToPixelPos(0, -310);
			font.drawString(healthPos.x, healthPos.y, Integer.toString(ape.getHealth()), Color.black);

			Vector2f energyPos = relativPosOnPanelToPixelPos(0, -100);
			font.drawString(energyPos.x, energyPos.y, Integer.toString(Math.round(ape.getEnergy())), Color.black);

			Vector2f coinPos = relativPosOnPanelToPixelPos(0, 100);
			font.drawString(coinPos.x, coinPos.y, Integer.toString(ape.getCoins()), Color.black);
		}
	}

	private Vector2f relativPosOnPanelToPixelPos(float x, float y) {
		return new Vector2f(x, y).scale(getScale()).add(getPosition());
	}

}
