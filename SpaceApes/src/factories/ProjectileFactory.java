package factories;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import actions.ItemCollisionAction;
import actions.ProjectileBehaviorAction;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.basicevents.CollisionEvent;
import eea.engine.event.basicevents.LoopEvent;
import entities.Projectile;
import utils.Utils;
import spaceapes.Constants;
import spaceapes.SpaceApes;

public abstract class ProjectileFactory {

	public enum ProjectileType {
		COCONUT, SPIKEBALL, BOMB, SHARD, CRYSTAL, TURTLE
	};

	public enum MovementType {
		LINEAR, EXPLICIT_EULER
	};
	
	public enum ProjectileStatus {
		flying, crashingPlanet, crashingMoon, hittingApe, leavingWorld, inBlackHole, inArea 
	};

	public static Projectile createProjectile(String iD, ProjectileType type, Vector2f position, Vector2f velocity, boolean visible,
			boolean isAffectedByEnvironment, MovementType movementType) {

		Projectile projectile = new Projectile(iD, position, velocity);
		projectile.setType(type);
		projectile.setVisible(visible);

		if (iD != Constants.DUMMY_PROJECTILE_ID) {
			StateBasedEntityManager.getInstance().addEntity(SpaceApes.GAMEPLAY_STATE, projectile);
		}

		if (visible) { // Image und Typspezifische Attributzuweisung
			try {
				switch (type) {

				default: // entspricht Case COCONUT

					projectile.setPrice(Constants.COCONUT_PRIZE);
					projectile.setMaxDamage(Constants.COCONUT_MAX_DAMAGE);
					projectile.setDamageRadius(Constants.COCONUT_DAMAGE_RADIUS);
					projectile.setDesiredProjectileSize(Constants.COCONUT_DESIRED_SIZE);
					projectile.setScale(Constants.COCONUT_DESIRED_SIZE / Utils.pixelLengthToWorldLength(Constants.COCONUT_SIZE_IN_PIXEL));
					if (SpaceApes.renderImages) {
						projectile.addComponent(new ImageRenderComponent(new Image("img/projectiles/coconut.png")));
					}
					break;

				case SPIKEBALL:

					projectile.setPrice(Constants.SPIKEBALL_PRIZE);
					projectile.setMaxDamage(Constants.SPIKEBALL_MAX_DAMAGE);
					projectile.setDamageRadius(Constants.SPIKEBALL_DAMAGE_RADIUS);
					projectile.setDesiredProjectileSize(Constants.SPIKEBALL_SIZE_IN_COORDINATES);
					projectile.setScale(
							Constants.SPIKEBALL_SIZE_IN_COORDINATES / Utils.pixelLengthToWorldLength(Constants.SPIKEBALL_SIZE_IN_PIXEL));
					if (SpaceApes.renderImages) {
						projectile.addComponent(new ImageRenderComponent(new Image("img/projectiles/spikeball.png")));
					}
					break;

				case BOMB:

					projectile.setPrice(Constants.BOMB_PRIZE);
					projectile.setMaxDamage(Constants.BOMB_MAX_DAMAGE);
					projectile.setDamageRadius(Constants.BOMB_DAMAGE_RADIUS);
					projectile.setDesiredProjectileSize(Constants.BOMB_SIZE_IN_COORDINATES);
					projectile.setScale(Constants.BOMB_SIZE_IN_COORDINATES / Utils.pixelLengthToWorldLength(Constants.BOMB_SIZE_IN_PIXEL));
					if (SpaceApes.renderImages) {
						projectile.addComponent(new ImageRenderComponent(new Image("img/projectiles/bomb.png")));
					}
					break;

				case SHARD:

					projectile.setPrice(Constants.SHARD_PRIZE);
					projectile.setMaxDamage(Constants.SHARD_MAX_DAMAGE);
					projectile.setDamageRadius(Constants.SHARD_DAMAGE_RADIUS);
					projectile.setDesiredProjectileSize(Constants.SHARD_SIZE_IN_COORDINATES);
					projectile
							.setScale(Constants.SHARD_SIZE_IN_COORDINATES / Utils.pixelLengthToWorldLength(Constants.SHARD_SIZE_IN_PIXEL));
					if (SpaceApes.renderImages) {
						projectile.addComponent(new ImageRenderComponent(new Image("img/projectiles/shard.png")));
					}
					break;

				case CRYSTAL:

					projectile.setPrice(Constants.CRYSTAL_PRIZE);
					projectile.setMaxDamage(Constants.CRYSTAL_MAX_DAMAGE);
					projectile.setDamageRadius(Constants.CRYSTAL_DAMAGE_RADIUS);
					projectile.setDesiredProjectileSize(Constants.CRYSTAL_SIZE_IN_COORDINATES);
					projectile.setScale(
							Constants.CRYSTAL_SIZE_IN_COORDINATES / Utils.pixelLengthToWorldLength(Constants.CRYSTAL_SIZE_IN_PIXEL));
					if (SpaceApes.renderImages) {
						projectile.addComponent(new ImageRenderComponent(new Image("img/projectiles/crystal.png")));
					}
					break;

				case TURTLE:

					projectile.setPrice(Constants.TURTLE_PRIZE);
					projectile.setMaxDamage(Constants.TURTLE_MAX_DAMAGE);
					projectile.setDamageRadius(Constants.TURTLE_DAMAGE_RADIUS);
					projectile.setDesiredProjectileSize(Constants.TURTLE_SIZE_IN_COORDINATES);
					projectile.setScale(
							Constants.TURTLE_SIZE_IN_COORDINATES / Utils.pixelLengthToWorldLength(Constants.TURTLE_SIZE_IN_PIXEL));
					if (SpaceApes.renderImages) {
						projectile.addComponent(new ImageRenderComponent(new Image("img/projectiles/turtle.png")));
					}
					break;

				}
			} catch (SlickException e) {
				System.err.println("Problem with projectile image");
			}
		}

		if (isAffectedByEnvironment) {
			// Loop Event
			LoopEvent projectileLoop = new LoopEvent();
			projectileLoop.addAction(new ProjectileBehaviorAction(projectile));
			projectile.addComponent(projectileLoop);
		}

		// Collision Event
		CollisionEvent projectileCollision = new CollisionEvent();
		projectileCollision.addAction(new ItemCollisionAction());
		projectile.addComponent(projectileCollision);

		return projectile;
	}

}
