package interfaces;

import java.util.List;

import eea.engine.entity.Entity;

public interface IMap {

	public void addEntity(Entity entity);
	
	public void removeEntity(Entity entity);
	
	public List<Entity> getEntities();
	
}
