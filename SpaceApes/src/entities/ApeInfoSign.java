package entities;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;

public class ApeInfoSign extends Entity {

	private ImageRenderComponent imageRenderComponent = null;
	private TrueTypeFont font;
	private Ape ape;

	public ApeInfoSign(String entityID) {
		super(entityID);
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
