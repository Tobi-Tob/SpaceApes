package factories;

import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import actions.ChangeAngleAction;
import actions.ChangePowerAction;
import actions.DisplayApeInfoAction;
import actions.MoveOnPlanetAction;
import actions.PolicyNextMoveAction;
import actions.ShootAction;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.ANDEvent;
import eea.engine.event.Event;
import eea.engine.event.basicevents.KeyDownEvent;
import eea.engine.event.basicevents.LoopEvent;
import eea.engine.event.basicevents.MouseClickedEvent;
import eea.engine.event.basicevents.MouseEnteredEvent;
import entities.Ape;
import entities.Planet;
import events.PolicyEvent;
import factories.ProjectileFactory.MovementType;
import policys.Policy;
import policys.Policy.PolicyAction;
import utils.Constants;
import spaceapes.Map;
import spaceapes.SpaceApes;
import utils.Utils;

public abstract class ApeFactory {

	/**
	 * This method is able to create apes with their images. All apes are already
	 * assigned to the EntityManager and the Map.
	 * 
	 * @param name                 String as Identifier
	 * @param homePlanet           Planet on wich the Ape is located
	 * @param angleOnPlanet        float defines the position on the planet
	 * @param apeImageIndex        int as index for the desired image
	 * @param isActive             boolean whether it is the apes turn
	 * @param isInteractionAllowed boolean whether ape/player intraction is allowed
	 * @param AIPolicy             Null if Ape is controlled by Player, else Policy
	 *                             object which defines behaviour
	 * @return Ape
	 */
	public static Ape createApe(String name, Planet homePlanet, float angleOnPlanet, int apeImageIndex, boolean isActive,
			boolean isInteractionAllowed, Policy policy) {

		Ape ape = new Ape(name);

		ape.setPlanet(homePlanet);
		homePlanet.setApe(ape);
		ape.setDistanceToPlanetCenter();
		ape.setAngleOnPlanet(angleOnPlanet);
		ape.setAngleOfView(0);
		ape.setHealth(Constants.APE_MAX_HEALTH);
		ape.setEnergy(Constants.APE_MAX_ENERGY);
		ape.setMovementSpeed(Constants.APE_MOVMENT_SPEED);
		ape.setThrowStrength(5f);
		ape.setActive(isActive);
		ape.setInteractionAllowed(isInteractionAllowed);
		ape.setPolicy(policy);
		ape.setPosition(Utils.toPixelCoordinates(ape.getWorldCoordinates()));
		ape.setScale(ape.getScalingFactor());
		ape.setRotation(angleOnPlanet + 90f);

		if (SpaceApes.renderImages) {
			try {
				ape.addComponent(new ImageRenderComponent(new Image("img/apes/ape" + apeImageIndex + ".png")));
			} catch (SlickException | RuntimeException e) {
				try {
					ape.addComponent(new ImageRenderComponent(new Image("img/apes/ape1.png")));
				} catch (SlickException e1) {
					System.err.println("Problem with image for ape");
				}
			}
		}

		// Zeige Informationen zum Ape, wenn auf ihn geklickt wird (nur wenn der Spieler
		// am Zug ist!)
		Event clickOnApeEvent = new ANDEvent(new MouseEnteredEvent(), new MouseClickedEvent());
		clickOnApeEvent.addAction(new DisplayApeInfoAction());
		ape.addComponent(clickOnApeEvent);

		if (policy == null) { // Direct key pressed events for apes which are controlled by a Player
			// Bewege den Affen mit der rechten Pfeiltaste nach rechts
			Event rightKeyPressed = new KeyDownEvent(Input.KEY_RIGHT);
			rightKeyPressed.addAction(new MoveOnPlanetAction(1.0f, ape));
			ape.addComponent(rightKeyPressed);

			// Bewege den Affen mit der linken Pfeiltaste nach links (nur wenn der Spieler
			// am Zug ist!) Und erzeuge eine Ziellinie
			Event leftKeyPressed = new KeyDownEvent(Input.KEY_LEFT);
			leftKeyPressed.addAction(new MoveOnPlanetAction(-1.0f, ape));
			ape.addComponent(leftKeyPressed);

		} else { // Automatic Policy Events for apes which are controlled by computer
			
			policy.setApe(ape); // Save ape object in policy
			
			Event moveRightPolicyEvent = new PolicyEvent(policy, PolicyAction.MoveRight);
			moveRightPolicyEvent.addAction(new MoveOnPlanetAction(1.0f, ape));
			ape.addComponent(moveRightPolicyEvent);

			Event moveLeftPolicyEvent = new PolicyEvent(policy, PolicyAction.MoveLeft);
			moveLeftPolicyEvent.addAction(new MoveOnPlanetAction(-1.0f, ape));
			ape.addComponent(moveLeftPolicyEvent);

			Event angleUpPolicyEvent = new PolicyEvent(policy, PolicyAction.AngleUp);
			angleUpPolicyEvent.addAction(new ChangeAngleAction(0.3f, null));
			ape.addComponent(angleUpPolicyEvent);

			Event angleDownPolicyEvent = new PolicyEvent(policy, PolicyAction.AngleDown);
			angleDownPolicyEvent.addAction(new ChangeAngleAction(-0.3f, null));
			ape.addComponent(angleDownPolicyEvent);

			Event powerUpPolicyEvent = new PolicyEvent(policy, PolicyAction.PowerUp);
			powerUpPolicyEvent.addAction(new ChangePowerAction(0.01f, null));
			ape.addComponent(powerUpPolicyEvent);

			Event powerDownPolicyEvent = new PolicyEvent(policy, PolicyAction.PowerDown);
			powerDownPolicyEvent.addAction(new ChangePowerAction(-0.01f, null));
			ape.addComponent(powerDownPolicyEvent);

			Event shootPolicyEvent = new PolicyEvent(policy, PolicyAction.Shoot);
			shootPolicyEvent.addAction(new ShootAction(MovementType.EXPLICIT_EULER, true));
			ape.addComponent(shootPolicyEvent);

			Event loopPolicyEvent = new LoopEvent();
			loopPolicyEvent.addAction(new PolicyNextMoveAction(ape));
			ape.addComponent(loopPolicyEvent);
		}

		// Zuweisung zur Map und zum EntityManager
		Map.getInstance().addApe(ape);
		StateBasedEntityManager.getInstance().addEntity(SpaceApes.GAMEPLAY_STATE, ape);

		return ape;
	}

}
