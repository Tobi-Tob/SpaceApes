package factories;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import actions.ProjectileMovementAction;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.event.basicevents.LoopEvent;
import eea.engine.interfaces.IEntityFactory;
import entities.Projectile;

public class ProjectileFactory implements IEntityFactory {
	
	private final String name;
	private final Vector2f position;
	private final Vector2f velocity;
	private final boolean visible;

	public ProjectileFactory(String name, Vector2f position, Vector2f velocity, boolean visible) {
		this.name = name;
		this.position = position;
		this.velocity = velocity;
		this.visible = visible;
	}

	@Override
	public Entity createEntity() {

		Projectile projectile = new Projectile(name);
		
		projectile.setCoordinatesWorld(position);
		projectile.setVelocity(velocity);
		
		if (visible) {
			try {
				projectile.addComponent(new ImageRenderComponent(new Image("/assets/coconut.png")));
			} catch (SlickException e) {
				System.err.println("Cannot find file assets/coconut.png");
				e.printStackTrace();
			}
		}
		
		// Loop Event
		LoopEvent projectileLoop = new LoopEvent();
		projectileLoop.addAction(new ProjectileMovementAction(projectile));
		projectile.addComponent(projectileLoop);
		
		return projectile;
	}

}
