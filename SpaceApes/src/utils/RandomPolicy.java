package utils;

import entities.Ape;

public class RandomPolicy extends Policy {
	private float desiredPosition;
	private float desiredPositionInInterval; // cliped to [0, 360)
	private float desiredPower;
	private float desiredAngle;

	public RandomPolicy() {
		super("RandomPolicy");
	}

	@Override
	public void initTurn() {

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
		} else if (ape.getThrowStrength() > desiredPower + 0.01f) {
			action = PolicyAction.PowerDown;
		} else if (ape.getThrowStrength() < desiredPower - 0.01f) {
			action = PolicyAction.PowerUp;
		} else if (ape.getLocalAngleOfView() > desiredAngle + 0.3f) {
			action = PolicyAction.AngleDown;
		} else if (ape.getLocalAngleOfView() < desiredAngle - 0.3f) {
			action = PolicyAction.AngleUp;
		}

		this.setCurrentAction(action);
	}

}
