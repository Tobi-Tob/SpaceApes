package entities;

import eea.engine.entity.Entity;

public class Energypack extends Entity {
	
	private int value;

	public Energypack(String iD) {
		super(iD);
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
