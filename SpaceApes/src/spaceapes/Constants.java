package spaceapes;

import java.awt.Font;

import org.newdawn.slick.TrueTypeFont;

public class Constants {
// TODO Constants vllt ins package utils verschieben? und package spaceapes in gamestates umbennen?
// TODO vllt zum Interface machen. Alle Klassen die Zugriff auf die Konstanten benoetigen koennen das dann implementieren

//////////////////////// World Constants //////////////////////////

	public static final float WORLD_WIDTH = 16;
	public static final float WORLD_HEIGHT = WORLD_WIDTH * SpaceApes.HEIGHT / SpaceApes.WIDTH;
	// public static final float WORLD_HEIGHT = 12; //MR Habe das aktiviert, weil es
	// beim oberen abgebrochen ist.
	// MR da ist anscheinend ein Initialisierungsproblem zur Laufzeit
	public static final float GRAVITATION_CONSTANT = 0.25f;

/////////////////////// Planet Constants //////////////////////////////

	public static final float MINIMUM_RADIUS_PLAYER_PLANET = 0.75f;
	public static final float MAXIMUM_RADIUS_PLAYER_PLANET = 1.5f;

	public static final float BLACKHOLE_PROBABILITY = 0.4f;
	public static final float ANTI_PLANET_PROBABILITY = 0.3f;

/////////////////////// Ape Constants //////////////////////////////

	public static final int APE_MAX_HEALTH = 100;
	public static final int APE_MAX_ENERGY = 100;
	public static final float APE_MOVMENT_SPEED = 0.05f;

	public static final int APE_PIXEL_HEIGHT = 300;
	public static final int APE_PIXEL_FEET_TO_CENTER = 130;
	public static final float APE_DESIRED_SIZE = 0.6f; // desired size in world units

////////////////////// Projectile Constants //////////////////////////////

	// AimLine
	public static final int AIMLINE_LENGTH = 500; // TODO Projektile koennen unterschiedlich Lange Hilfslinien haben

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
	public static final String ENERGY_PACK_ID = "ENERGY_PACK";
	public static final int ENERGY_PACK_VALUE = 20;
	public static final float ENERGY_PACK_SPAWN_POSSIBILITY = 0.15f;

	// Health Pack
	public static final String HEALTH_PACK_ID = "HEALTH_PACK";
	public static final int HEALTH_PACK_VALUE = 20;
	public static final float HEALTH_PACK_SPAWN_POSSIBILITY = 0.15f;

	// Coins general
	public static final String COIN_ID = "COIN";
	public static final float COIN_SPAWN_POSSIBILITY = 0.4f;
	// This is the possibility if any coin is spawned

	// Copper Coin
	public static final int COPPER_COIN_VALUE = 1;
	public static final float COPPER_COIN_SPAWN_POSSIBILITY = 0.6f;

	// Gold Coin
	public static final int GOLD_COIN_VALUE = 3;
	public static final float GOLD_COIN_SPAWN_POSSIBILITY = 0.3f;

	// Diamond Coin
	public static final int DIAMOND_COIN_VALUE = 5;
	// DIAMOND_COIN_SPAWN_POSSIBILITY = 1 - (COPPER_COIN_SPAWN_POSSIBILITY +
	// GOLD_COIN_SPAWN_POSSIBILITY)

////////////////////// More Constants //////////////////////////////

	// ID Hintergrund
	public static final String BACKGROUND_ID = "BACKGROUND";
	// ID Projektil
	public static final String PROJECTILE_ID = "PROJECTILE";
	public static final String SHOP_PROJECTILE_ID = "SHOP_PROJECTILE";
	public static final String DUMMY_PROJECTILE_ID = "DUMMY_PROJECTILE";
	// ID Hilfslinien Punkte
	public static final String AIMLINE_DOT_ID = "AIMLINE_DOT";
	// ID ApeInfoSign
	public static final String APE_INFO_SIGN_ID = "APE_INFO_SIGN";
	// ID Explosionen
	public static final String EXPLOSION_ID = "EXPLOSION";
	// Damage Display Entity
	public static final String DAMAGE_DISPLAY_ID = "DAMAGE_DISPLAY";
	public static final TrueTypeFont DAMAGE_FONT = new TrueTypeFont(
			new Font("Times New Roman", Font.BOLD, Math.round(0.02f * SpaceApes.WIDTH)), true);
	// ID Control Panel
	public static final String CONTROL_PANEL_ID = "CONTROL_PANEL";

}
