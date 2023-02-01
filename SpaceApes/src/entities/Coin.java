package entities;

import eea.engine.entity.Entity;

public class Coin extends Entity { //TODO Elternklasse Item von Coin erzeugen?
	
	private int value;

	public Coin(String iD) {
		super(iD);
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
