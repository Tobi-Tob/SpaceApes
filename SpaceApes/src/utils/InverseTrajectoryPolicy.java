package utils;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.geom.Vector2f;

import entities.Ape;
import map.Map;

public class InverseTrajectoryPolicy extends Policy {
	private float desiredPosition;
	private float desiredPositionInInterval; // cliped to [0, 360)
	private float desiredPower = 6;
	private float desiredAngle = 30;

	private Vector2f target;
	private boolean trajectoryFound;

	public InverseTrajectoryPolicy() {
		super("RandomPolicy");
	}

	@Override
	public void initTurn() {
		if (Utils.randomFloat(0, 1) < 0.4) { // Wahrscheinlichkeit fuer Bewegung
			this.desiredPosition = getApe().getAngleOnPlanet() + Utils.randomFloat(-180, 180);
		} else {
			this.desiredPosition = getApe().getAngleOnPlanet();
		}
		this.desiredPositionInInterval = desiredPosition;
		if (desiredPositionInInterval < 0) {
			this.desiredPositionInInterval += 360f;
		}
		if (desiredPositionInInterval >= 360f) {
			this.desiredPositionInInterval -= 360f;
		}

		this.trajectoryFound = false;
		this.target = findEnemyApePosition();
	}

	@Override
	public void calcNextAction(int delta) {
		Ape ape = getApe();
		PolicyAction action = null;

		/* 1. Movement */
		if (Math.abs(ape.getAngleOnPlanet() - desiredPositionInInterval) > 5 && ape.getEnergy() > 0) {
			if (ape.getAngleOnPlanet() < desiredPosition) {
				action = PolicyAction.MoveRight;
			} else {
				action = PolicyAction.MoveLeft;
			}

		} else if (!trajectoryFound) {
			trajectoryFound = true;

		} else if (trajectoryFound) {
			action = PolicyAction.Shoot;
			if (ape.getThrowStrength() > desiredPower + 0.05f) {
				action = PolicyAction.PowerDown;
			} else if (ape.getThrowStrength() < desiredPower - 0.05f) {
				action = PolicyAction.PowerUp;
			} else if (ape.getLocalAngleOfView() > desiredAngle + 1) {
				action = PolicyAction.AngleDown;
			} else if (ape.getLocalAngleOfView() < desiredAngle - 1) {
				action = PolicyAction.AngleUp;
			}
		}
		this.setCurrentAction(action);

	}

	/**
	 * Sampels a random enemy ape as target.
	 * 
	 * @return Vector2f in world coordinates
	 */
	private Vector2f findEnemyApePosition() {

		List<Ape> enemyApes = new ArrayList<Ape>(Map.getInstance().getApes());
		enemyApes.remove(getApe());

		int numberOfApes = enemyApes.size();
		int indexOfTarget = (int) Utils.randomFloat(0, numberOfApes);
		Ape targetApe = enemyApes.get(indexOfTarget);

		return targetApe.getWorldCoordinates();
	}

}
