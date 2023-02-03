package entities;

import eea.engine.entity.Entity;

public class Healthpack extends Entity {
	
	private int value;

	public Healthpack(String iD) {
		super(iD);
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
