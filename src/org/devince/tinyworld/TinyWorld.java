package org.devince.tinyworld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.devince.tinyworld.items.Alien;
import org.devince.tinyworld.items.GameItem;
import org.devince.tinyworld.items.Invincibility;
import org.devince.tinyworld.items.Life;
import org.devince.tinyworld.items.Planet;
import org.devince.tinyworld.items.Player;
import org.devince.tinyworld.items.Score;
import org.devince.tinyworld.items.ShootGenerator;
import org.devince.tinyworld.items.Star;
import org.devince.tinyworld.world.Galaxy;
import org.devince.tinyworld.world.PlanetGenerator;

import screens.GameScreen;
import screens.Help;
import screens.Title;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class TinyWorld extends Game {
	private static final int LEVEL_SCORE = 10;
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
	private Rectangle viewPort;
	private Rectangle gamePort;
	private boolean gameOver;
	
	public static int TIER1 = 5;
	public static int TIER2 = 10;
	
	private List<Star> stars;
	private SpriteBatch batch;
	private boolean gameStarted;
	
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
		this.setScreen(new Title());
		Music music = Gdx.audio.newMusic(Gdx.files.internal("data/tinygalaxy.mp3"));
		music.setLooping(true);
		music.play();
		this.items = new ArrayList<GameItem>();
		this.itemsToRemove = new ArrayList<GameItem>();
		this.handled = new HashMap<UUID, GameItem>();
		
		this.cam = new OrthographicCamera(WIDTH, HEIGHT);
		this.cam.position.set(0, 0, 0);
		this.cam.zoom = this.getZoom();
		
		this.stage = new Stage(WIDTH, HEIGHT, true);
		Gdx.input.setInputProcessor(this.stage);
		
		this.galaxy = new Galaxy();
		
		this.viewPort = new Rectangle();
		this.gamePort = new Rectangle();
		
		this.stars = new ArrayList<Star>();
		this.batch = new SpriteBatch();
		
		this.init();
	}
	
	private void constructViewPort() {
		this.viewPort.set(
				this.player.x - (WIDTH / 2f * this.getZoom()), 
				this.player.y - (HEIGHT / 2f * this.getZoom()),
				WIDTH * this.getZoom(), 
				HEIGHT * this.getZoom());
	}

	private void constructGamePort() {
		this.gamePort.set(this.viewPort.x - 50, 
				this.viewPort.y - 50,
				this.viewPort.width + 100, 
				this.viewPort.height + 100);
	}

	public void render() {
		if (this.gameStarted) {
			if (this.player.getLife() <= 0) {
				this.setGameOver();
			}
			
			for(GameItem item : this.itemsToRemove) {
				this.items.remove(item);
				this.stage.removeActor(item);
			}
			
			this.itemsToRemove.clear();
			
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
			this.constructViewPort();
			this.constructGamePort();
			this.cam.position.set(this.player.x, this.player.y, 0);
			this.cam.update();
			this.cam.apply(Gdx.gl10);
			
			this.stage.setKeyboardFocus(this.player);
			this.handleContacts();
			this.stage.act(Gdx.graphics.getDeltaTime());
			
			this.renderStars();
			this.galaxy.drawBack();
			this.stage.draw();
		}
		
		super.render();
	}

	private void handleContacts() {
		this.handled.clear();
		for(GameItem item : this.items) {
			if (item.getEnable()) {
				for(GameItem item2 : this.items) {
					if (item2.getEnable() && !this.handled.containsKey(item2.getUid())) {
						if (item != item2) {
							if (item.getBoundingBox().overlaps(item2.getBoundingBox())) {
								item.handleContact(item2);
								item2.handleContact(item);
							}
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
			if (item.getGalaxyPoint() != this.player.getGalaxyPoint()) {
				this.addGameItem(item);
			}
		}
	}
	
	public OrthographicCamera getCamera() {
		return this.cam;
	}
	
	private void setPlayerOnPlanet(Planet planet) {
		this.setItemOnPlanet(planet, this.player, Galaxy.TOP);
		Score scoreItem = new Score();
		this.addGameItem(scoreItem);
		this.setItemOnPlanet(planet, scoreItem, Galaxy.BOTTOM);
	}
	
	private void setItemOnPlanet(Planet planet, GameItem item) {
		this.setItemOnPlanet(planet, item, Galaxy.NONE);
	}
	
	private void setItemOnPlanet(Planet planet, GameItem item, int forceNormal) {
		int normal = forceNormal;
		if (forceNormal == Galaxy.NONE) {
			int direction = (int) Math.floor(Math.random() * 4);
			
			switch(direction) {
				case 0:
					normal = Galaxy.TOP;
					break;
				case 1:
					normal = Galaxy.RIGHT;
					break;
				case 2:
					normal = Galaxy.BOTTOM;
					break;
				case 3:
					normal = Galaxy.LEFT;
					break;
			}
		}
		
		item.setNormal(normal);
		item.x = item.getMiddleX(planet);
		item.y = item.getMiddleY(planet);
	}
	
	public Galaxy getGalaxy() {
		return this.galaxy;
	}

	public void setGameOver() {
		this.stage.removeActor(this.player);
		this.gameOver = true;
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
		
		if (this.score % LEVEL_SCORE == 0) {
			this.level++;
		}
	}
	
	public Rectangle getViewPort() {
		return this.viewPort;
	}
	
	public Rectangle getGamePort() {
		return this.gamePort;
	}

	public boolean isTier1() {
		return this.level >= TIER1;
	}
	
	public boolean isTier2() {
		return this.level >= TIER2;
	}
	
	public int getScore() {
		return this.score;
	}

	public boolean isGameOver() {
		return this.gameOver;
	}

	public void restart() {
		this.init();
	}
	
	private void init() {
		this.level = 1;
		this.gameOver = false;
		this.score = 0;
		
		this.items.clear();
		this.itemsToRemove.clear();
				
		this.stage.clear();
		
		this.galaxy.reset();
		this.galaxy.initWorld();
		
		this.player = new Player(0, 0);
		this.player.setPlayer(true);
		this.addGameItem(this.player);
		
		this.setPlayerOnPlanet(this.galaxy.getStartPlanet());
		
		this.constructViewPort();
		this.constructGamePort();
		
		this.planetGenerator = new PlanetGenerator();
		this.stage.addActor(this.planetGenerator);
		
		this.shootGenerator = new ShootGenerator();
		this.stage.addActor(this.shootGenerator);
		
		this.stars.clear();
		for (int i = 0; i < 150; i++) {
			float x = (- WIDTH) + (int)(Math.random() * WIDTH * 2) + this.player.x; 
			float y = (- HEIGHT) + (int)(Math.random() * HEIGHT * 2) + this.player.y;
			Star s = new Star(x, y, new Texture("data/star.png"));
			this.stars.add(s);
		}
	}
	
	private void renderStars() {
		this.batch.setProjectionMatrix(this.cam.combined);
		this.batch.begin();
		for(Star s : this.stars) {
			s.getBaseSprite().setPosition(s.x, s.y);
			s.getBaseSprite().draw(this.batch);
		}
		
		this.batch.end();
	}

	public int getItemsCount() {
		int total = 0;
		for(GameItem item : this.items) {
			if (item instanceof Score || item instanceof Life ||  item instanceof Invincibility) {
				total++;
			}
		}
		
		return total;
	}

	public void start() {
		this.gameStarted = true;
		this.setScreen(new GameScreen());
	}

	public void showHelp() {
		this.setScreen(new Help());
	}
}
