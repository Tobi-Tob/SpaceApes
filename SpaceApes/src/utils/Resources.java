package utils;

import java.awt.Font;

import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.TrueTypeFont;

import spaceapes.SpaceApes;

public class Resources {

//////////////////////// Fonts //////////////////////////

	public static TrueTypeFont DAMAGE_FONT;
	public static TrueTypeFont HIGHSCORE_FONT;
	public static TrueTypeFont HIGHSCORE_FONT_BIG;

//////////////////////// Music //////////////////////////

	public static Music TITLE_MUSIC;
	public static Music GAMEPLAY_MUSIC;

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
	public static Sound REFUSED_SOUND;
	public static Sound BELL_SOUND;

	/**
	 * Initialise Font, Music and Sound objects
	 */
	public static void init() {
		if (SpaceApes.renderImages) {
			DAMAGE_FONT = new TrueTypeFont(
					new Font("Times New Roman", Font.BOLD, Math.round(0.02f * SpaceApes.WIDTH)), true);

			int fontSize = Math.round(SpaceApes.WIDTH / 70);
			HIGHSCORE_FONT = new TrueTypeFont(new Font("Times New Roman", Font.BOLD, fontSize), true);
			
			int fontSizeBig = Math.round(SpaceApes.WIDTH / 35);
			HIGHSCORE_FONT_BIG = new TrueTypeFont(new Font("Times New Roman", Font.BOLD, fontSizeBig), true);

		}
		if (SpaceApes.PLAY_MUSIC) {
			try {
				TITLE_MUSIC = new Music("snd/title_music.wav");
				GAMEPLAY_MUSIC = new Music("snd/gameplay_music.wav");
			} catch (SlickException e) {
				System.err.println("Problem with music");
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
				REFUSED_SOUND = new Sound("snd/refused.wav");
				BELL_SOUND = new Sound("snd/bell.wav");
			} catch (SlickException e) {
				System.err.println("Problem with Sound");
			}
		}
	}

}
