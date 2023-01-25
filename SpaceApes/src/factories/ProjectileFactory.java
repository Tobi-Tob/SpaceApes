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
		COCONUT, SPIKEBALL, BOMB, SHARD, CRYSTAL, TURTLE
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
					int maxDamage = 20;
					float damageRadius = 0.5f;
					float desiredProjectileSize = 0.32f;
					float projectileSizeInPixel = 490;

					projectile.setMaxDamage(maxDamage);
					projectile.setDamageRadius(damageRadius);
					projectile.setDesiredProjectileSize(desiredProjectileSize);
					projectile.setScale(desiredProjectileSize / Utils.pixelLengthToWorldLength(projectileSizeInPixel));
					projectile.addComponent(new ImageRenderComponent(new Image("img/projectiles/coconut.png")));
					break;

				case SPIKEBALL:
					// 3 Projektile hinter einander
					maxDamage = 30;
					damageRadius = 0.4f;
					desiredProjectileSize = 0.3f;
					projectileSizeInPixel = 625;

					projectile.setMaxDamage(maxDamage);
					projectile.setDamageRadius(damageRadius);
					projectile.setDesiredProjectileSize(desiredProjectileSize);
					projectile.setScale(desiredProjectileSize / Utils.pixelLengthToWorldLength(projectileSizeInPixel));
					projectile.addComponent(new ImageRenderComponent(new Image("img/projectiles/spikeball.png")));
					break;

				case BOMB:
					maxDamage = 60;
					damageRadius = 0.8f;
					desiredProjectileSize = 0.35f;
					projectileSizeInPixel = 650;

					projectile.setMaxDamage(maxDamage);
					projectile.setDamageRadius(damageRadius);
					projectile.setDesiredProjectileSize(desiredProjectileSize);
					projectile.setScale(desiredProjectileSize / Utils.pixelLengthToWorldLength(projectileSizeInPixel));
					projectile.addComponent(new ImageRenderComponent(new Image("img/projectiles/bomb.png")));
					break;

				case SHARD:
					// 10 Projektile in gleichzeitig in alle Richtungen
					maxDamage = 10;
					damageRadius = 0.2f;
					desiredProjectileSize = 0.22f;
					projectileSizeInPixel = 500;

					projectile.setMaxDamage(maxDamage);
					projectile.setDamageRadius(damageRadius);
					projectile.setDesiredProjectileSize(desiredProjectileSize);
					projectile.setScale(desiredProjectileSize / Utils.pixelLengthToWorldLength(projectileSizeInPixel));
					projectile.addComponent(new ImageRenderComponent(new Image("img/projectiles/shard.png")));
					break;

				case CRYSTAL:
					maxDamage = 100;
					damageRadius = 0.2f;
					desiredProjectileSize = 0.4f;
					projectileSizeInPixel = 500;

					projectile.setMaxDamage(maxDamage);
					projectile.setDamageRadius(damageRadius);
					projectile.setDesiredProjectileSize(desiredProjectileSize);
					projectile.setScale(desiredProjectileSize / Utils.pixelLengthToWorldLength(projectileSizeInPixel));
					projectile.addComponent(new ImageRenderComponent(new Image("img/projectiles/crystal.png")));
					break;

				case TURTLE:
					maxDamage = 40;
					damageRadius = 0.5f;
					desiredProjectileSize = 0.37f;
					projectileSizeInPixel = 530;

					projectile.setMaxDamage(maxDamage);
					projectile.setDamageRadius(damageRadius);
					projectile.setDesiredProjectileSize(desiredProjectileSize);
					projectile.setScale(desiredProjectileSize / Utils.pixelLengthToWorldLength(projectileSizeInPixel));
					projectile.addComponent(new ImageRenderComponent(new Image("img/projectiles/turtle.png")));
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
