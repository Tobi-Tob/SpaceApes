package utils;

import spaceapes.SpaceApes;

public class Constants {
// TODO vllt zum Interface machen. Alle Klassen die Zugriff auf die Konstanten benoetigen koennen das dann implementieren

//////////////////////// World Constants //////////////////////////

	public static final float WORLD_WIDTH = 16;
	public static final float WORLD_HEIGHT = WORLD_WIDTH * SpaceApes.HEIGHT / SpaceApes.WIDTH;

	public static final float GRAVITATION_CONSTANT = 0.25f;
	public static final float AIR_RESISTANCE = 0.15f;

/////////////////////// Planet Constants //////////////////////////////

	public static final float MINIMUM_RADIUS_PLAYER_PLANET = 0.75f;
	public static final float MAXIMUM_RADIUS_PLAYER_PLANET = 1.5f;

	// Moon
	public static final float MOON_ORBIT_HEIGHT = 1f;
	public static final float MOON_RADIUS = 0.25f;
	public static final int TIME_FOR_COMPLETE_MOON_ORBIT = 10000; // in ms

	// Atmosphere
	public static final float ATMOSPHERE_PROBABILITY = 0.4f;
	public static final float ATMOSPHERE_MINIMUM_RADIUS = 0.6f;
	public static final float ATMOSPHERE_MAXIMUM_RADIUS = 1f;

	public static final float BLACKHOLE_PROBABILITY = 0.4f;
	public static final float ANTI_PLANET_PROBABILITY = 0.3f;

/////////////////////// Ape Constants //////////////////////////////

	public static final int APE_MAX_HEALTH = 100;
	public static final int APE_MAX_ENERGY = 100;
	public static final float APE_MOVMENT_SPEED = 0.05f;
	public static final float ENERGY_USED_PER_STEP = 0.01f;
	public static final float APE_SIZE = 0.6f;

////////////////////// Projectile Constants //////////////////////////////

	// AimLine
	public static final int AIMLINE_LENGTH = 500; // TODO Projektile koennen unterschiedlich Lange Hilfslinien haben

	// Coconut
	public static final int COCONUT_MAX_DAMAGE = 20;
	public static final float COCONUT_DAMAGE_RADIUS = 0.5f;
	public static final int COCONUT_PRIZE = 0;
	public static final float COCONUT_SIZE = 0.32f;

	// Spikeball
	public static final int SPIKEBALL_MAX_DAMAGE = 40;
	public static final float SPIKEBALL_DAMAGE_RADIUS = 0.5f;
	public static final int SPIKEBALL_PRIZE = 3;
	public static final float SPIKEBALL_SIZE = 0.3f;

	// Bomb
	public static final int BOMB_MAX_DAMAGE = 60;
	public static final float BOMB_DAMAGE_RADIUS = 0.8f;
	public static final int BOMB_PRIZE = 5;
	public static final float BOMB_SIZE = 0.35f;

	// Shard
	public static final int SHARD_MAX_DAMAGE = 10;
	public static final float SHARD_DAMAGE_RADIUS = 0.2f;
	public static final int SHARD_PRIZE = 1;
	public static final float SHARD_SIZE = 0.22f;

	// Crystal
	public static final int CRYSTAL_MAX_DAMAGE = 90;
	public static final float CRYSTAL_DAMAGE_RADIUS = 0.2f;
	public static final int CRYSTAL_PRIZE = 8;
	public static final float CRYSTAL_SIZE = 0.4f;

	// Turtle
	public static final int TURTLE_MAX_DAMAGE = 30;
	public static final float TURTLE_DAMAGE_RADIUS = 0.2f;
	public static final int TURTLE_PRIZE = 2;
	public static final float TURTLE_SIZE = 0.35f;

////////////////////// Item Constants //////////////////////////////

	// Energy Pack
	public static final int ENERGY_PACK_VALUE = 50;
	public static final float ENERGY_PACK_SPAWN_POSSIBILITY = 0.04f;

	// Health Pack
	public static final int HEALTH_PACK_VALUE = 30;
	public static final float HEALTH_PACK_SPAWN_POSSIBILITY = 0.06f;

	// Coins general
	public static final float COIN_SPAWN_POSSIBILITY = 0.30f;
	// This is the possibility if any coin is spawned

	// Copper Coin
	public static final int COPPER_COIN_VALUE = 2;
	public static final float COPPER_COIN_SPAWN_POSSIBILITY = 0.60f;

	// Gold Coin
	public static final int GOLD_COIN_VALUE = 5;
	public static final float GOLD_COIN_SPAWN_POSSIBILITY = 0.30f;

	// Diamond Coin
	public static final int DIAMOND_COIN_VALUE = 8;
	// DIAMOND_COIN_SPAWN_POSSIBILITY = 1 - (COPPER_COIN_SPAWN_POSSIBILITY +
	// GOLD_COIN_SPAWN_POSSIBILITY)

////////////////////// More Constants //////////////////////////////

	// ID Hintergrund
	public static final String BACKGROUND_ID = "BACKGROUND";
	// ID Projektil
	public static final String PROJECTILE_ID = "PROJECTILE";
	public static final String SHOP_PROJECTILE_ID = "SHOP_PROJECTILE";
	public static final String DUMMY_PROJECTILE_ID = "DUMMY_PROJECTILE";
	// ID Items
	public static final String COIN_ID = "COIN";
	public static final String ENERGY_PACK_ID = "ENERGY_PACK";
	public static final String HEALTH_PACK_ID = "HEALTH_PACK";
	// ID Hilfslinien Punkte
	public static final String AIMLINE_DOT_ID = "AIMLINE_DOT";
	// ID ApeInfoSign
	public static final String APE_INFO_SIGN_ID = "APE_INFO_SIGN";
	// ID Explosionen
	public static final String EXPLOSION_ID = "EXPLOSION";
	// ID Control Panel
	public static final String CONTROL_PANEL_ID = "CONTROL_PANEL";
	// ID Damage Display
	public static final String DAMAGE_DISPLAY_ID = "DAMAGE_DISPLAY";

}
