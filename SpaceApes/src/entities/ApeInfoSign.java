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
	private TrueTypeFont fontApe;
	private TrueTypeFont fontStats;
	private Ape ape;

	public ApeInfoSign(String entityID) {
		super(entityID);
	}
	
	public void setImageRenderComponent(ImageRenderComponent imageRenderComponent) {
		this.imageRenderComponent = imageRenderComponent;
	}
	
	public TrueTypeFont getFontApe() {
		return fontApe;
	}

	public void setFontApe(TrueTypeFont font) {
		this.fontApe = font;
	}
	
	public TrueTypeFont getFontStats() {
		return fontStats;
	}

	public void setFontStats(TrueTypeFont font) {
		this.fontStats = font;
	}

	public Ape getApe() {
		return ape;
	}

	public void setApe(Ape ape) {
		this.ape = ape;
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		if (!(imageRenderComponent == null)) {

			imageRenderComponent.render(container, game, g);

			Vector2f textPos = relativPosOnPanelToPixelPos(-180, -400);
			fontApe.drawString(textPos.x, textPos.y, ape.getID(), Color.black);
			
			textPos = relativPosOnPanelToPixelPos(-350, -200);
			fontStats.drawString(textPos.x, textPos.y, "Health: " + ape.getHealth(), Color.black);
			
			textPos = relativPosOnPanelToPixelPos(-350, 0);
			fontStats.drawString(textPos.x, textPos.y, "Energy: " + (int) ape.getEnergy(), Color.black);
			
			textPos = relativPosOnPanelToPixelPos(-250, 200);
			fontStats.drawString(textPos.x, textPos.y, "Coins: " + ape.getCoins(), Color.black);
		}
	}
	
	private Vector2f relativPosOnPanelToPixelPos(float x, float y) {
		return new Vector2f(x, y).scale(getScale()).add(getPosition());
	}

}
