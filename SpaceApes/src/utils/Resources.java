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

	public static Sound EXPLOSION_SOUND;
	public static Sound PLOP_SOUND;
	public static Sound THROW_SOUND;
	public static Sound HEALTH_SOUND;
	public static Sound ENERGY_SOUND;
	public static Sound COIN_SOUND;
	public static Sound REFUSED;

	/**
	 * Initialise font and sound objects
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
				EXPLOSION_SOUND = new Sound("snd/small_explosion.wav");
				PLOP_SOUND = new Sound("snd/plop.wav");
				THROW_SOUND = new Sound("snd/throw.wav");
				HEALTH_SOUND = new Sound("snd/health_collected.wav");
				ENERGY_SOUND = new Sound("snd/energy_collected.wav");
				COIN_SOUND = new Sound("snd/coin_collected.wav");
				REFUSED = new Sound("snd/refused.wav");
			} catch (SlickException e) {
				System.err.println("Problem with Sound");
			}
		}
	}

}
