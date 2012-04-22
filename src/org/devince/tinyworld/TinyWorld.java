package org.devince.tinyworld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.devince.tinyworld.items.Alien;
import org.devince.tinyworld.items.GameItem;
import org.devince.tinyworld.items.Planet;
import org.devince.tinyworld.items.Player;
import org.devince.tinyworld.items.ShootGenerator;
import org.devince.tinyworld.world.Galaxy;
import org.devince.tinyworld.world.PlanetGenerator;

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
	public static final String TAG = "TinyWorld";
	
	private Stage stage;
	private Galaxy galaxy;
	private Player player;
	private PlanetGenerator planetGenerator;
	private ShootGenerator shootGenerator;
	
	private OrthographicCamera cam;
	
	private List<GameItem> items;
	private List<GameItem> itemsToRemove;
	private HashMap<UUID, GameItem> handled;
	private int level;
	private int score;
	
	
	public static TinyWorld get() {
		if (game == null) {
			game = new TinyWorld();
		}
		
		return game;
	}
	
	public float getZoom() {
		return DEFAULT_ZOOM;
	}
	
	public void create() {
		this.items = new ArrayList<GameItem>();
		this.itemsToRemove = new ArrayList<GameItem>();
		this.handled = new HashMap<UUID, GameItem>();
		
		this.cam = new OrthographicCamera(WIDTH, HEIGHT);
		this.cam.position.set(0, 0, 0);
		this.cam.zoom = this.getZoom();
		
		this.stage = new Stage(WIDTH, HEIGHT, true);
		Gdx.input.setInputProcessor(this.stage);
		
		this.galaxy = new Galaxy();
		this.galaxy.initWorld();
		
		this.player = new Player(0, 0);
		this.addGameItem(this.player);
		
		this.setPlayerOnPlanet(this.galaxy.getStartPlanet());
		
		this.planetGenerator = new PlanetGenerator();
		this.stage.addActor(this.planetGenerator);
		
		this.shootGenerator = new ShootGenerator();
		this.stage.addActor(this.shootGenerator);
		
		this.level = 1;
	}
	
	public void render() {
		if (this.player.getLife() <= 0) {
			this.setGameOver();
		}
		
		for(GameItem item : this.itemsToRemove) {
			this.items.remove(item);
			this.stage.removeActor(item);
		}
		
		this.itemsToRemove.clear();
		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		this.cam.position.set(this.player.x, this.player.y, 0);
		this.cam.update();
		this.cam.apply(Gdx.gl10);
		
		this.stage.setKeyboardFocus(this.player);
		this.handleContacts();
		this.stage.act(Gdx.graphics.getDeltaTime());
		this.stage.draw();
		super.render();
	}

	private void handleContacts() {
		this.handled.clear();
		for(GameItem item : this.items) {
			for(GameItem item2 : this.items) {
				if (!this.handled.containsKey(item2.getUid())) {
					if (item != item2) {
						if (item.getBoundingBox().overlaps(item2.getBoundingBox())) {
							item.handleContact(item2);
							item2.handleContact(item);
						}
					}
				}
			}
			
			this.handled.put(item.getUid(), item);
		}
	}

	public void resize(int width, int height) {
		this.stage.setViewport(width, height, true);
		super.resize(width, height);
	}

	public void addGameItem(GameItem item) {
		// to add in render
		this.stage.addActor(item);
		this.items.add(item);
	}
	
	public void addGameItemOnPlanet(GameItem item, Planet planet) {
		// to add in render
		this.setItemOnPlanet(planet, item);
		if (!this.galaxy.contains(item.getGalaxyPoint())) {
			this.addGameItem(item);
		}
	}
	
	public OrthographicCamera getCamera() {
		return this.cam;
	}
	
	private void setPlayerOnPlanet(Planet planet) {
		this.setItemOnPlanet(planet, this.player);
	}
	
	private void setItemOnPlanet(Planet planet, GameItem item) {
		item.x = planet.x;
		item.y = planet.y + planet.height / 2f + item.height / 2f;
	}
	
	public Galaxy getGalaxy() {
		return this.galaxy;
	}

	public void setGameOver() {
		this.stage.removeActor(this.player);
	}

	public Player getPlayer() {
		return this.player;
	}

	public void addItemToRemove(GameItem item) {
		this.itemsToRemove.add(item);
	}

	public int getAlienCount() {
		int total = 0;
		for(GameItem item : this.items) {
			if (item instanceof Alien) {
				total++;
			}
		}
		
		return total;
	}

	public int getLevel() {
		return this.level;
	}

	public void addScore(int score) {
		this.score += score;
	}
}
