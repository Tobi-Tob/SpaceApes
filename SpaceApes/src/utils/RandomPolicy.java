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
	public void initTurn(Ape ape) {
		this.desiredPosition = ape.getAngleOnPlanet() + Utils.randomFloat(-180, 180);
		this.desiredPositionInInterval = desiredPosition;
		if (desiredPositionInInterval < 0) {
			this.desiredPositionInInterval += 360f;
		}
		if (desiredPositionInInterval >= 360f) {
			this.desiredPositionInInterval -= 360f;
		}
		this.desiredPower = Utils.randomFloat(3, 6);
		this.desiredAngle = Utils.randomFloat(-80, 80);
		System.out.println(desiredPosition);
		System.out.println(desiredPositionInInterval);
	}

	@Override
	public void calcNextAction(Ape ape, int delta) {

		PolicyAction action = PolicyAction.Shoot;
		if (Math.abs(ape.getAngleOnPlanet() - desiredPositionInInterval) > 5) {
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

}
