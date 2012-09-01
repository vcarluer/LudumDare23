package org.devince.tinyworld;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
public class Assets {
	public static final String DATA_COIN_WAV = "data/coin.wav";
	public static final String DATA_CREATEPLAN_WAV = "data/createplan.wav";
	public static final String DATA_ENHURT_WAV = "data/enhurt.wav";
	public static final String DATA_ENSHOOT_WAV = "data/enshoot.wav";
	public static final String DATA_EXTDESTROY_WAV = "data/extdestroy.wav";
	public static final String DATA_HURT_WAV = "data/hurt.wav";
	public static final String DATA_INV_WAV = "data/inv.wav";
	public static final String DATA_LIFE_WAV = "data/life.wav";
	public static final String DATA_PLANETEXPLOSE_WAV = "data/planetexplose.wav";
	public static final String DATA_PLANETHURT_WAV = "data/planethurt.wav";
	public static final String DATA_SELECT_WAV = "data/select.wav";
	public static final String DATA_SPAWN_WAV = "data/spawn.wav";
	public static final String DATA_INVINCIBLEEND_WAV = "data/endinvincible.wav";
	public static final String DATA_INVINCIBLEMUS_MP3 = "data/invincible.mp3";
	
	public static final String DATA_ALIEN_PNG = "data/alien.png";
	public static final String DATA_CONTROLS_PNG = "data/controls.png";
	public static final String DATA_HELP_PNG = "data/help.png";
	public static final String DATA_INVINCIBLE_PNG = "data/invincible.png";
	public static final String DATA_LIFE_PNG = "data/life.png";
	public static final String DATA_METEOR_PNG = "data/meteor.png";
	public static final String DATA_PLANET_PNG = "data/planet.png";
	public static final String DATA_PLANET2_PNG = "data/planet2.png";
	public static final String DATA_PLANET3_PNG = "data/planet3.png";
	public static final String DATA_PLANETAIR_PNG = "data/planetair.png";
	public static final String DATA_PLANETEXT_PNG = "data/planetext.png";
	public static final String DATA_PLANETEXT2_PNG = "data/planetext2.png";
	public static final String DATA_PLANETEXT3_PNG = "data/planetext3.png";
	public static final String DATA_PLAYER_PNG = "data/player.png";
	public static final String DATA_PLAYERINV_PNG = "data/playerinv.png";
	public static final String DATA_SCORE_PNG = "data/score.png";
	public static final String DATA_SHOOT_PNG = "data/shoot.png";
	public static final String DATA_SHOOTER_PNG = "data/shooter.png";
	public static final String DATA_STAR_PNG = "data/star.png";
	public static final String DATA_SUN_PNG = "data/sun.png";
	public static final String DATA_SUNBACK_PNG = "data/sunback.png";
	public static final String DATA_TITLE_PNG = "data/title.png";
	public static final String DATA_HUD_PNG = "data/hud.png";
	public static final String DATA_BLACK_TRANS = "data/blacktranslayer.png";
	public static final String DATA_LOGO = "data/ga.png";
	
	public static final String DATA_BACK_BUTTON = "data/backbutton.png";
	
	private static Map<String, Sound> sounds = new HashMap<String, Sound>();
	private static Map<String, Texture> textures = new HashMap<String, Texture>();
	
	public static void load() {
		sounds.put(DATA_COIN_WAV, createSound(DATA_COIN_WAV));
		sounds.put(DATA_CREATEPLAN_WAV, createSound(DATA_CREATEPLAN_WAV));
		sounds.put(DATA_ENHURT_WAV, createSound(DATA_ENHURT_WAV));
		sounds.put(DATA_ENSHOOT_WAV, createSound(DATA_ENSHOOT_WAV));
		sounds.put(DATA_EXTDESTROY_WAV, createSound(DATA_EXTDESTROY_WAV));
		sounds.put(DATA_HURT_WAV, createSound(DATA_HURT_WAV));
		sounds.put(DATA_INV_WAV, createSound(DATA_INV_WAV));
		sounds.put(DATA_INVINCIBLEEND_WAV, createSound(DATA_INVINCIBLEEND_WAV));
		sounds.put(DATA_INVINCIBLEMUS_MP3, createSound(DATA_INVINCIBLEMUS_MP3));
		sounds.put(DATA_LIFE_WAV, createSound(DATA_LIFE_WAV));
		sounds.put(DATA_PLANETEXPLOSE_WAV, createSound(DATA_PLANETEXPLOSE_WAV));
		sounds.put(DATA_PLANETHURT_WAV, createSound(DATA_PLANETHURT_WAV));
		sounds.put(DATA_SELECT_WAV, createSound(DATA_SELECT_WAV));
		sounds.put(DATA_SPAWN_WAV, createSound(DATA_SPAWN_WAV));
		
		textures.put(DATA_ALIEN_PNG, createTexture(DATA_ALIEN_PNG));
		textures.put(DATA_HELP_PNG, createTexture(DATA_HELP_PNG));
		textures.put(DATA_INVINCIBLE_PNG, createTexture(DATA_INVINCIBLE_PNG));
		textures.put(DATA_LIFE_PNG, createTexture(DATA_LIFE_PNG));
		textures.put(DATA_METEOR_PNG, createTexture(DATA_METEOR_PNG));
		textures.put(DATA_PLANET_PNG, createTexture(DATA_PLANET_PNG));
		textures.put(DATA_PLANET2_PNG, createTexture(DATA_PLANET2_PNG));
		textures.put(DATA_PLANET3_PNG, createTexture(DATA_PLANET3_PNG));
		textures.put(DATA_PLANETAIR_PNG, createTexture(DATA_PLANETAIR_PNG));
		textures.put(DATA_PLANETEXT_PNG, createTexture(DATA_PLANETEXT_PNG));
		textures.put(DATA_PLANETEXT2_PNG, createTexture(DATA_PLANETEXT2_PNG));
		textures.put(DATA_PLANETEXT3_PNG, createTexture(DATA_PLANETEXT3_PNG));
		textures.put(DATA_PLAYER_PNG, createTexture(DATA_PLAYER_PNG));
		textures.put(DATA_PLAYERINV_PNG, createTexture(DATA_PLAYERINV_PNG));
		textures.put(DATA_SCORE_PNG, createTexture(DATA_SCORE_PNG));
		textures.put(DATA_SHOOT_PNG, createTexture(DATA_SHOOT_PNG));
		textures.put(DATA_SHOOTER_PNG, createTexture(DATA_SHOOTER_PNG));
		textures.put(DATA_STAR_PNG, createTexture(DATA_STAR_PNG));
		textures.put(DATA_SUN_PNG, createTexture(DATA_SUN_PNG));
		textures.put(DATA_SUNBACK_PNG, createTexture(DATA_SUNBACK_PNG));
		textures.put(DATA_TITLE_PNG, createTexture(DATA_TITLE_PNG));
		textures.put(DATA_HUD_PNG, createTexture(DATA_HUD_PNG));
		textures.put(DATA_BLACK_TRANS, createTexture(DATA_BLACK_TRANS));
		textures.put(DATA_LOGO, createTexture(DATA_LOGO));
		textures.put(DATA_BACK_BUTTON, createTexture(DATA_BACK_BUTTON));
	}
	
	private static Sound createSound(String path) {
		return Gdx.audio.newSound(Gdx.files.internal(path));
	}
	
	private static Texture createTexture(String path) {
		return new Texture(Gdx.files.internal(path));
	}

	public static Sound getSound(String path) {
		return sounds.get(path);
	}
	
	public static Texture getTexture(String path) {
		return textures.get(path);
	}
	
	public static BitmapFont getNewFont() {
		return new BitmapFont(Gdx.files.internal("data/ar.fnt"), Gdx.files.internal("data/ar.png"), false);
	}
	
	private static Map<String, Sprite> sprites = new HashMap<String, Sprite>();
	
	public static Sprite getSprite(String path) {
		Sprite s = null;
		if (!sprites.containsKey(path)) {
			s = new Sprite(Assets.getTexture(path));
			sprites.put(path, s);
		} else {
			s = sprites.get(path);
		}
		
		return s;
	}
}
