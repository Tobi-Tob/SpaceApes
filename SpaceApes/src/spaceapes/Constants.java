package spaceapes;

public final class Constants { // TODO Constants vllt ins package utils verschieben? und package spaceapes in
								// gamestates umbennen?

//////////////////////// World Constants //////////////////////////

	public static final float WORLD_WIDTH = 16;
	public static final float WORLD_HEIGHT = WORLD_WIDTH * Launch.HEIGHT / Launch.WIDTH;

/////////////////////// Ape Constants //////////////////////////////

	public static final int APE_MAX_HEALTH = 100;
	public static final int APE_MAX_ENERGY = 100;

////////////////////// Projectile Constants //////////////////////////////

	// Coconut
	public static final int COCONUT_PRIZE = 0;
	public static final int COCONUT_MAX_DAMAGE = 20;
	public static final float COCONUT_DAMAGE_RADIUS = 0.5f;
	public static final float COCONUT_DESIRED_SIZE = 0.32f;
	public static final float COCONUT_SIZE_IN_PIXEL = 490;

	// Spikeball
	public static final int SPIKEBALL_PRIZE = 3;
	public static final int SPIKEBALL_MAX_DAMAGE = 30;
	public static final float SPIKEBALL_DAMAGE_RADIUS = 0.4f;
	public static final float SPIKEBALL_SIZE_IN_COORDINATES = 0.3f;
	public static final float SPIKEBALL_SIZE_IN_PIXEL = 625;

	// Bomb
	public static final int BOMB_PRIZE = 4;
	public static final int BOMB_MAX_DAMAGE = 60;
	public static final float BOMB_DAMAGE_RADIUS = 0.8f;
	public static final float BOMB_SIZE_IN_COORDINATES = 0.35f;
	public static final float BOMB_SIZE_IN_PIXEL = 650;

	// Shard
	public static final int SHARD_PRIZE = 1;
	public static final int SHARD_MAX_DAMAGE = 10;
	public static final float SHARD_DAMAGE_RADIUS = 0.2f;
	public static final float SHARD_SIZE_IN_COORDINATES = 0.22f;
	public static final float SHARD_SIZE_IN_PIXEL = 500;

	// Crystal
	public static final int CRYSTAL_PRIZE = 8;
	public static final int CRYSTAL_MAX_DAMAGE = 90;
	public static final float CRYSTAL_DAMAGE_RADIUS = 0.2f;
	public static final float CRYSTAL_SIZE_IN_COORDINATES = 0.4f;
	public static final float CRYSTAL_SIZE_IN_PIXEL = 510;

	// Turtle
	public static final int TURTLE_PRIZE = 2;
	public static final int TURTLE_MAX_DAMAGE = 40;
	public static final float TURTLE_DAMAGE_RADIUS = 0.5f;
	public static final float TURTLE_SIZE_IN_COORDINATES = 0.35f;
	public static final float TURTLE_SIZE_IN_PIXEL = 530;

////////////////////// Item Constants //////////////////////////////

	// Energy Pack
	public static final int ENERGY_PACK_VALUE = 20;
	public static final String ENERGY_IMAGE_PATH = "img/items/energy.png";
	public static final float ENERGY_PACK_SPAWN_POSSIBILITY = 0.3f;

	// Health Pack
	public static final int HEALTH_PACK_VALUE = 20;
	public static final String HEALTH_IMAGE_PATH = "img/items/health.png";
	public static final float HEALTH_PACK_SPAWN_POSSIBILITY = 0.3f;

	// Coins general
	public static final float COIN_SPAWN_POSSIBILITY = 0.5f;

	// Copper Coin
	public static final int COPPER_COIN_VALUE = 1;
	public static final String COPPER_COIN_IMAGE_PATH = "img/items/coin1.png";
	// this is the possibility if any coin is spawned. This is determined by
	// COIN_SPAWN_POSSIBILITY.
	public static final float COPPER_COIN_SPAWN_POSSIBILITY = 0.6f;

	// Gold Coin
	public static final int GOLD_COIN_VALUE = 3;
	public static final String GOLD_COIN_IMAGE_PATH = "img/items/coin2.png";
	// this is the possibility if any coin is spawned. This is determined by
	// COIN_SPAWN_POSSIBILITY.
	public static final float GOLD_COIN_SPAWN_POSSIBILITY = 0.3f;

	// Copper Coin
	public static final int DIAMOND_COIN_VALUE = 5;
	public static final String DIAMOND_COIN_IMAGE_PATH = "img/items/coin3.png";
	// note: DIAMOND_COIN_SPAWN_POSSIBILITY = 1 - (COPPER_COIN_SPAWN_POSSIBILITY +
	// GOLD_COIN_SPAWN_POSSIBILITY)

////////////////////// More Constants //////////////////////////////

	// Id fuer Hilfslinien Punkte
	public static final String AIMLINE_DOT = "AIMLINE_DOT";

}
