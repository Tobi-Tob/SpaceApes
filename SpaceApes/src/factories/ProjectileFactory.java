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
import utils.Utils;

public class ProjectileFactory implements IEntityFactory {

	public enum ProjectileType {
		COCONUT, SHARD, BOMB
	};

	private final String name;
	private final Vector2f position;
	private final Vector2f velocity;
	private final boolean visible;
	private final ProjectileType type;

	public ProjectileFactory(String name, Vector2f position, Vector2f velocity, boolean visible, ProjectileType type) {
		this.name = name;
		this.position = position;
		this.velocity = velocity;
		this.visible = visible;
		this.type = type;
	}

	@Override
	public Entity createEntity() {

		Projectile projectile = new Projectile(name);

		projectile.setCoordinatesWorld(position);
		projectile.setVelocity(velocity);

		if (visible) { // Image und Typspezifische Attributzuweisung
			try {
				switch (type) {

				default: // entspricht Case COCONUT
					float maxDamage = 20f;
					float damageRadius = 0.5f;
					float desiredProjectileSize = 0.32f;
					float projectileSizeInPixel = 490;

					projectile.setMaxDamage(maxDamage);
					projectile.setDamageRadius(damageRadius);
					projectile.setDesiredProjectileSize(desiredProjectileSize);
					projectile.setScale(desiredProjectileSize / Utils.pixelLengthToWorldLength(projectileSizeInPixel));
					projectile.addComponent(new ImageRenderComponent(new Image("img/projectiles/coconut.png")));
					break;

				case SHARD:
					maxDamage = 10f;
					damageRadius = 0.2f;
					desiredProjectileSize = 0.22f;
					projectileSizeInPixel = 500;

					projectile.setMaxDamage(maxDamage);
					projectile.setDamageRadius(damageRadius);
					projectile.setDesiredProjectileSize(desiredProjectileSize);
					projectile.setScale(desiredProjectileSize / Utils.pixelLengthToWorldLength(projectileSizeInPixel));
					projectile.addComponent(new ImageRenderComponent(new Image("img/projectiles/shard.png")));
					break;
					
				case BOMB:
					maxDamage = 60f;
					damageRadius = 0.8f;
					desiredProjectileSize = 0.35f;
					projectileSizeInPixel = 650;

					projectile.setMaxDamage(maxDamage);
					projectile.setDamageRadius(damageRadius);
					projectile.setDesiredProjectileSize(desiredProjectileSize);
					projectile.setScale(desiredProjectileSize / Utils.pixelLengthToWorldLength(projectileSizeInPixel));
					projectile.addComponent(new ImageRenderComponent(new Image("img/projectiles/bomb.png")));
					break;
					
				}
			} catch (SlickException e) {
				System.err.println("Problem with projectile image");
			}
		}

		// Loop Event
		LoopEvent projectileLoop = new LoopEvent();
		projectileLoop.addAction(new ProjectileMovementAction(projectile));
		projectile.addComponent(projectileLoop);

		return projectile;
	}

}
