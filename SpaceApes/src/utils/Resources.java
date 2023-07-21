package utils;

import java.awt.Font;

import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.TrueTypeFont;

import spaceapes.SpaceApes;

public class Resources {

//////////////////////// Fonts //////////////////////////

	public static final TrueTypeFont DAMAGE_FONT = new TrueTypeFont(
			new Font("Times New Roman", Font.BOLD, Math.round(0.02f * SpaceApes.WIDTH)), true);

//////////////////////// Music //////////////////////////

	public static Music MUSIC;

//////////////////////// Sounds /////////////////////////
	
	// Projektile Sounds
	public static Sound EXPLOSION_SMALL_SOUND;
	public static Sound EXPLOSION_SOUND;
	public static Sound EXPLOSION_BIG_SOUND;
	public static Sound HIT_BALCK_HOLE_SOUND;
	public static Sound WHOOSH_SOUND;
	
	// Ape Sounds
	public static Sound THROW_SOUND;
	public static Sound DEATH_SOUND;
	public static Sound STEP_SOUND;
	
	// Item Sounds
	public static Sound HEALTH_SOUND;
	public static Sound ENERGY_SOUND;
	public static Sound COIN_SOUND;
	
	// User Interaction
	public static Sound START_SOUND;
	public static Sound PAUSE_SOUND;
	// public static Sound SELECT_SOUND; // not used
	public static Sound PLOP_SOUND;
	public static Sound REFUSED;

	/**
	 * Initialise Music and Sound objects
	 */
	public static void init() {
		if (SpaceApes.PLAY_MUSIC) {
			try {
				MUSIC = new Music("snd/song1.ogg");
			} catch (SlickException e) {
				System.err.println("Problem with main menu music");
			}
		}

		if (SpaceApes.PLAY_SOUNDS) {
			try {
				EXPLOSION_SMALL_SOUND = new Sound("snd/small_explosion2.wav");
				EXPLOSION_SOUND = new Sound("snd/explosion.wav");
				EXPLOSION_BIG_SOUND = new Sound("snd/big_impact.wav");
				HIT_BALCK_HOLE_SOUND = new Sound("snd/black_hole.wav");
				WHOOSH_SOUND = new Sound("snd/whoosh.wav");
				
				THROW_SOUND = new Sound("snd/throw.wav");
				DEATH_SOUND = new Sound("snd/death.wav");
				STEP_SOUND = new Sound("snd/steps1.wav");
				
				HEALTH_SOUND = new Sound("snd/health_collected.wav");
				ENERGY_SOUND = new Sound("snd/energy_collected.wav");
				COIN_SOUND = new Sound("snd/coin_collected.wav");
				
				START_SOUND = new Sound("snd/start.wav");
				PAUSE_SOUND = new Sound("snd/pause.wav");
				PLOP_SOUND = new Sound("snd/plop.wav");
				REFUSED = new Sound("snd/refused.wav");
			} catch (SlickException e) {
				System.err.println("Problem with Sound");
			}
		}
	}

}
