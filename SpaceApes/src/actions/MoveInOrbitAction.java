package actions;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.action.Action;
import eea.engine.component.Component;
import eea.engine.entity.Entity;
import map.Map;
import utils.Utils;

public class MoveInOrbitAction implements Action {

		private Entity entity;
		private Vector2f orbitCenter;
		private float orbitRadius;
		private float angleInOrbit; // 0 entspricht Osten
		private int timeForCompleteOrbit; // in ms, positiv: Bewegung im Uhrzeigersinn
		private float zeroRoation; // alignment for angleInOrbit = 0
		

	public MoveInOrbitAction(Entity entity, Vector2f orbitCenter, float orbitRadius, float angleInOrbit, int timeForCompleteOrbit) {
		this.entity = entity;
		this.orbitCenter = orbitCenter;
		this.orbitRadius = orbitRadius;
		this.angleInOrbit = angleInOrbit;
		this.timeForCompleteOrbit = timeForCompleteOrbit;
		this.zeroRoation = entity.getRotation();
		
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
		stepInOrbit(delta);
	}
	
	private void stepInOrbit(int delta) {
		this.angleInOrbit += 360f * delta / timeForCompleteOrbit;
		if (this.angleInOrbit > 360f) {
			this.angleInOrbit -= 360f;
		} else if (this.angleInOrbit < 0f) {
			this.angleInOrbit += 360f;
		}
		Vector2f relativPosInOrbit = Utils.toCartesianCoordinates(this.orbitRadius, this.angleInOrbit);
		Vector2f globalPos = relativPosInOrbit.add(this.orbitCenter);
		this.entity.setPosition(Utils.toPixelCoordinates(globalPos)); // TODO update Map
		this.entity.setRotation(this.angleInOrbit + this.zeroRoation);
	}
}
