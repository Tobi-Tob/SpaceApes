package entities;

import eea.engine.entity.Entity;
import factories.ItemFactory.ItemType;

public class Item extends Entity {
	
	private int value;
	private final ItemType itemType;

	public Item(String entityID, ItemType type) {
		super(entityID);
		this.itemType = type;
	}

	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}

	public ItemType getItemType() {
		return itemType;
	}

}
