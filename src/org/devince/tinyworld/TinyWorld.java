package org.devince.tinyworld;

import org.devince.tinyworld.items.GameItem;
import org.devince.tinyworld.items.Planet;
import org.devince.tinyworld.items.Player;
import org.devince.tinyworld.world.World;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class TinyWorld extends Game {
	private static final float DEFAULT_ZOOM = 0.3f;
	private static TinyWorld game;
	public static final String TITLE = "Tiny World";
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	
	private Stage stage;
	private World world;
	private Player player;
	
	private OrthographicCamera cam;
	
	public static TinyWorld get() {
		if (game == null) {
			game = new TinyWorld();
		}
		
		return game;
	}
	
	public void create() {
		this.cam = new OrthographicCamera(WIDTH, HEIGHT);
		this.cam.position.set(0, 0, 0);
		this.cam.zoom = DEFAULT_ZOOM;
		
		this.stage = new Stage(WIDTH, HEIGHT, true);
		Gdx.input.setInputProcessor(this.stage);
		
		this.world = new World();
		
		this.player = new Player(0, 0);
		this.addGameItem(this.player);
		
		this.setPlayerOnPlanet(this.world.getStartPlanet());
		
	}
	
	public void render() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		this.cam.update();
		this.cam.apply(Gdx.gl10);
		
		this.stage.setKeyboardFocus(this.player);
		this.stage.act(Gdx.graphics.getDeltaTime());
		this.stage.draw();
		super.render();
	}

	public void resize(int width, int height) {
		this.stage.setViewport(width, height, true);
		super.resize(width, height);
	}

	public void addGameItem(GameItem item) {
		// to add in render
		this.stage.addActor(item);
	}
	
	public OrthographicCamera getCamera() {
		return this.cam;
	}
	
	private void setPlayerOnPlanet(Planet planet) {
		this.player.x = planet.x;
		this.player.y = planet.y + planet.height / 2f + this.player.height / 2f;
	}
}
