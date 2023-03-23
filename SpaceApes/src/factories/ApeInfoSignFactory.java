package factories;

import java.awt.Font;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Vector2f;

import eea.engine.component.render.ImageRenderComponent;
import eea.engine.interfaces.IEntityFactory;
import entities.Ape;
import entities.ApeInfoSign;
import spaceapes.Constants;
import spaceapes.SpaceApes;
import utils.Utils;

public class ApeInfoSignFactory implements IEntityFactory {
	
	private final Vector2f coordinates;
	private Ape ape;
	
	public ApeInfoSignFactory(Vector2f coordiantes, Ape ape) {
		this.coordinates = coordiantes;
		this.ape = ape;
	}

	@Override
	public ApeInfoSign createEntity() {

		ApeInfoSign apeInfoSign = new ApeInfoSign(Constants.APE_INFO_SIGN_ID);
		
		apeInfoSign.setPosition(Utils.toPixelCoordinates(coordinates));
		apeInfoSign.setRotation(0);
		float apeInfoSignInPixel = 700;
		float desiredapeInfoSignInWorldUnits = 1.1f;
		float signScalingFactor = desiredapeInfoSignInWorldUnits / Utils.pixelLengthToWorldLength(apeInfoSignInPixel);
		apeInfoSign.setScale(signScalingFactor);
		apeInfoSign.setApe(ape);
		
		int fontSize = Math.round(signScalingFactor * 200);
		
		if (SpaceApes.renderImages) {
			apeInfoSign.setFont(new TrueTypeFont(new Font("Times New Roman", Font.BOLD, fontSize), true));
		} else {
			//System.out.println("noRenderImages: assign apeInfoSign font.");
		}
		
		if (SpaceApes.renderImages) {
			try {
				ImageRenderComponent imageRenderComponent = new ImageRenderComponent(new Image("img/assets/ape_info_sign.png"));
				apeInfoSign.addComponent(imageRenderComponent);
				apeInfoSign.setImageRenderComponent(imageRenderComponent);
			} catch (SlickException e) {
				System.err.println("Problem with planet panel image");
			}
		} else {
			//System.out.println("noRenderImages: assign apeInfoSign image.");
		}
		
		return apeInfoSign;
	}

}
