package org.devince.tinyworld;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class TinyWorldDesktop {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new LwjglApplication(TinyWorld.get(), TinyWorld.TITLE, TinyWorld.WIDTH, TinyWorld.HEIGHT, false);
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
	}

}
