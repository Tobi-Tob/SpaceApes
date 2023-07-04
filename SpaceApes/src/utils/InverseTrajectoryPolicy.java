package utils;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.geom.Vector2f;

import entities.Ape;
import map.Map;

public class InverseTrajectoryPolicy extends Policy {
	private float desiredPosition;
	private float desiredPositionInInterval; // cliped to [0, 360)
	private float desiredPower;
	private float desiredAngle;
	
	private Vector2f target;
	private boolean trajectoryFound;

	public InverseTrajectoryPolicy() {
		super("RandomPolicy");
	}

	@Override
	public void initTurn() {
		this.trajectoryFound = false;
		this.target = findEnemyApePosition();
		
		this.desiredPosition = getApe().getAngleOnPlanet() + Utils.randomFloat(-180, 180);
		this.desiredPositionInInterval = desiredPosition;
		if (desiredPositionInInterval < 0) {
			this.desiredPositionInInterval += 360f;
		}
		if (desiredPositionInInterval >= 360f) {
			this.desiredPositionInInterval -= 360f;
		}
		this.desiredPower = Utils.randomFloat(3, 6);
		this.desiredAngle = Utils.randomFloat(-80, 80);
	}

	@Override
	public void calcNextAction(int delta) {

		Ape ape = getApe();
		PolicyAction action = PolicyAction.Shoot;
		if (Math.abs(ape.getAngleOnPlanet() - desiredPositionInInterval) > 5 && ape.getEnergy() > 0) {
			if (ape.getAngleOnPlanet() < desiredPosition) {
				action = PolicyAction.MoveRight;
			} else {
				action = PolicyAction.MoveLeft;
			}
		} else if (ape.getThrowStrength() > desiredPower + 0.05f) {
			action = PolicyAction.PowerDown;
		} else if (ape.getThrowStrength() < desiredPower - 0.05f) {
			action = PolicyAction.PowerUp;
		} else if (ape.getLocalAngleOfView() > desiredAngle + 1) {
			action = PolicyAction.AngleDown;
		} else if (ape.getLocalAngleOfView() < desiredAngle - 1) {
			action = PolicyAction.AngleUp;
		}

		this.setCurrentAction(action);
	}
	
	private Vector2f findEnemyApePosition() {
		
		List<Ape> enemyApes = new ArrayList<Ape>(Map.getInstance().getApes());
		assertTrue("Ape self is not in list of Apes", enemyApes.remove(getApe()));
		int numberOfApes = enemyApes.size();
		System.out.println("enemyApes: " + numberOfApes);
		int indexOfTarget = (int) Utils.randomFloat(0, numberOfApes);
		Ape targetApe = Map.getInstance().getApes().get(indexOfTarget);
		System.out.println("targetApe: " + targetApe.getID());
		
		return targetApe.getWorldCoordinates();
	}

}
