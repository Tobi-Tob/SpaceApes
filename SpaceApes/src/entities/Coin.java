package entities;

import eea.engine.entity.Entity;

public class Coin extends Entity { //TODO Elternklasse Item von Coin erzeugen? -> JA
	
	private int value;

	public Coin(String iD, int value) {
		super(iD);
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
