package utils;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.geom.Vector2f;

import entities.Ape;
import entities.Projectile;
import factories.ProjectileFactory;
import factories.ProjectileFactory.MovementType;
import factories.ProjectileFactory.ProjectileStatus;
import factories.ProjectileFactory.ProjectileType;
import map.Map;
import spaceapes.Constants;
import spaceapes.SpaceApes;

public class InverseTrajectoryPolicy extends Policy {
	private float desiredPosition;
	private float desiredPositionInInterval; // cliped to [0, 360)
	private float desiredPower;
	private float desiredAngle;

	private Vector2f target;
	private boolean trajectoryFound;
	private int timeSpend; // in ms
	private int failedActionsInRow = 0;

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
		this.timeSpend = 0;
	}

	@Override
	public void calcNextAction(int delta) {
		this.timeSpend += delta;
		Ape ape = getApe();
		PolicyAction action = null;

		/* 1. Movement */
		if (Math.abs(ape.getAngleOnPlanet() - desiredPositionInInterval) > 5 && ape.getEnergy() > 0) {
			if (ape.getAngleOnPlanet() < desiredPosition) {
				action = PolicyAction.MoveRight;
			} else {
				action = PolicyAction.MoveLeft;
			}

			/* 2. Find Trajectory */
		} else if (!trajectoryFound) {
			findTrajectory(100, 10000, 0.8f);

			/* 3. Adjust Parameters */
		} else if (trajectoryFound) {
			action = PolicyAction.Shoot;
			if (ape.getThrowStrength() > desiredPower + 0.01f) {
				action = PolicyAction.PowerDown;
			} else if (ape.getThrowStrength() < desiredPower - 0.01f) {
				action = PolicyAction.PowerUp;
			} else if (ape.getLocalAngleOfView() > desiredAngle + 0.3f) {
				action = PolicyAction.AngleDown;
			} else if (ape.getLocalAngleOfView() < desiredAngle - 0.3f) {
				action = PolicyAction.AngleUp;
			}
		}
		/* 4. Init new Action if stuck */
		if (timeSpend > 10000) { // after 10 seconds
			failedActionsInRow++;
			initTurn();
		}
		/* 5. Random Action if completely stuck */
		if (failedActionsInRow >= 3) {
			action = PolicyAction.Shoot;
		}
		if (action == PolicyAction.Shoot) {
			failedActionsInRow = 0;
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

	/**
	 * The method tries to find a set of parameters that lead to a trajectory that
	 * hits the target. Random search algorithm for the complex inverse equations is
	 * used.
	 * 
	 * @param iterations int number of interations to test parameters
	 * @param maxFlightTime int time in ms for the longest possible trajectory to find
	 * @param epsilon float max distance to target 
	 */
	private void findTrajectory(int iterations, int maxFlightTime, float epsilon) {
		Ape ape = getApe();
		int searchDepth = Math.round(maxFlightTime / SpaceApes.UPDATE_INTERVAL);
		Vector2f positionOfProjectileLaunch = new Vector2f(ape.getWorldCoordinates())
				.add(Utils.toCartesianCoordinates(ape.getRadiusInWorldUnits(), ape.getAngleOnPlanet()));

		for (int i = 0; i <= iterations; i++) {
			if (trajectoryFound) {
				break; // Beende Suche bei gefundenen Parametern
			}
			float parameterAngle = Utils.randomFloat(-90, 90);
			float parameterPower = Utils.randomFloat(3, 7);

			Vector2f velocity = Utils.toCartesianCoordinates(parameterPower, ape.getAngleOnPlanet() + parameterAngle);

			Projectile dummyProjectile = ProjectileFactory.createProjectile(Constants.DUMMY_PROJECTILE_ID, ProjectileType.COCONUT,
					positionOfProjectileLaunch, velocity, false, true, MovementType.EXPLICIT_EULER);

			for (int j = 0; j <= searchDepth; j++) { // Berechne Bahn des Projektils
				ProjectileStatus status = dummyProjectile.explizitEulerStep(SpaceApes.UPDATE_INTERVAL);
				if (status != ProjectileStatus.flying) { // Wenn Kollision mit einem Objekt
					if (dummyProjectile.getCoordinates().sub(target).length() < epsilon) { // Kollision nah am Target Affen
						// Merke gefundene Parameter
						this.desiredAngle = parameterAngle;
						this.desiredPower = parameterPower;
						this.trajectoryFound = true;
					}
					break; // Beende Bahnberechnung bei Kollision
				}
			}
		}
	}

}
