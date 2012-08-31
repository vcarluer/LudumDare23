package org.devince.tinyworld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.devince.tinyworld.items.Alien;
import org.devince.tinyworld.items.GameItem;
import org.devince.tinyworld.items.Invincibility;
import org.devince.tinyworld.items.Life;
import org.devince.tinyworld.items.Meteor;
import org.devince.tinyworld.items.Planet;
import org.devince.tinyworld.items.Player;
import org.devince.tinyworld.items.Score;
import org.devince.tinyworld.items.Shoot;
import org.devince.tinyworld.items.ShootGenerator;
import org.devince.tinyworld.items.Star;
import org.devince.tinyworld.screens.GameScreen;
import org.devince.tinyworld.screens.Help;
import org.devince.tinyworld.screens.Title;
import org.devince.tinyworld.world.Galaxy;
import org.devince.tinyworld.world.PlanetGenerator;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class TinyWorld extends Game {
	private static final float MUSIC_TIME = 53f;
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
	private long nextId;
	private float musicDelta;
	private Music music;
	
	private boolean isGWT;
	
	private List<GameItem> aliens;
	private List<GameItem> bonuses;
	private List<GameItem> shoots;
	private List<GameItem> planets;
	private List<GameItem> meteors; // for aliens contacts
	private boolean isMusicPaused;
	private boolean paused;
	
	private GameScreen gameScreen;
	
	public IActivityRequestHandler myRequestHandler;
	
	public static TinyWorld get(IActivityRequestHandler requestHandler) {
		if (game == null) {
			game = new TinyWorld();
		}
		
		game.myRequestHandler = requestHandler;		
		
		return game;
	}
	
	public static TinyWorld get() {
		if (game == null) {
			game = new TinyWorld();
		}
		
		return game;
	}
	
	public float getZoom() {
		return DEFAULT_ZOOM;
	}
	
	protected TinyWorld() {
    }
	
	@Override
	public void create() {
		Assets.load();
		music = Gdx.audio.newMusic(Gdx.files.internal("data/tinygalaxy.mp3"));
		music.play();
		this.items = new ArrayList<GameItem>();
		this.itemsToRemove = new ArrayList<GameItem>();
		
		this.cam = new OrthographicCamera(WIDTH, HEIGHT);
		this.cam.position.set(0, 0, 0);
		this.cam.zoom = this.getZoom();
		
		this.stage = new Stage(WIDTH, HEIGHT, false);
		this.stage.setCamera(this.cam);
		Gdx.input.setInputProcessor(this.stage);
		
		this.galaxy = new Galaxy();
		
		this.viewPort = new Rectangle();
		this.gamePort = new Rectangle();
		
		this.stars = new ArrayList<Star>();
		this.batch = new SpriteBatch();
		
		this.aliens = new ArrayList<GameItem>();
		this.bonuses = new ArrayList<GameItem>();
		this.shoots = new ArrayList<GameItem>();
		this.planets = new ArrayList<GameItem>();
		this.meteors = new ArrayList<GameItem>();
		
		this.gameScreen = new GameScreen();
		
		this.init();
		
		this.setScreen(new Title());
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
		if (!this.isMusicPaused) {
			this.musicDelta += Gdx.graphics.getDeltaTime();
			if (this.musicDelta > MUSIC_TIME) {
				this.music.stop();
				this.music.play();
				this.musicDelta = 0f;
			}
		}
		
		if (this.gameStarted) {
			if (this.player.getLife() <= 0) {
				this.setGameOver();
			}
			
			if (!this.paused) {
				this.handleRemove();			
				
				this.stage.setKeyboardFocus(this.player);
				this.handleContacts();
				this.stage.act(Gdx.graphics.getDeltaTime());
			}
			
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
			this.constructViewPort();
			this.constructGamePort();
			this.cam.position.set(this.player.x, this.player.y, 0);
			this.cam.update();
			// this.cam.apply(Gdx.gl10); // does not work for gwt
			
			
			this.renderStars();
			this.galaxy.drawBack();
			this.stage.draw();
		}
		
		super.render();
	}

	private void handleContacts() {
		// Contact with players
		this.handleContactWithPlayer(this.aliens);
		this.handleContactWithPlayer(this.shoots);
		this.handleContactWithPlayer(this.bonuses);
		
		// Bonus contact with planets
		for(GameItem bonus : this.bonuses) {
			if (bonus.getEnable()) {
				if (this.galaxy.contains(bonus.getGalaxyPoint())) {
					this.addItemToRemove(bonus);
				}
			}
		}
		
		// Meteor on aliens
		for(GameItem meteor : this.meteors) {
			this.handleContactWithItem(meteor, this.aliens);
		}
		
		// Shoots on planets
		for(GameItem shoot : this.shoots) {
			if (shoot.getEnable()) {
				if (this.galaxy.contains(shoot.getGalaxyPoint())) {
					Planet planet = this.galaxy.getPlanet(shoot.getGalaxyPoint());
					planet.handleContact(shoot);
					shoot.handleContact(planet);
				}
			}
		}
	}
	
	private void handleContactWithPlayer(List<GameItem> list) {
		this.handleContactWithItem(this.player, list);
	}
	
	private void handleContactWithItem(GameItem source, List<GameItem> list) {
		if (source.getEnable()) {
			for (GameItem item : list) {
				if (item.getEnable() && source.getBoundingBox().overlaps(item.getBoundingBox())) {
					item.handleContact(source);
					source.handleContact(item);
				}
			}
		}
	}

	public void resize(int width, int height) {
		this.cam.viewportWidth = width;
		this.cam.viewportHeight = height;
		this.stage.setViewport(width, height, false);
		super.resize(width, height);
	}

	public void addGameItem(GameItem item) {
		// to add in render
		this.stage.addActor(item);
		this.items.add(item);
		if (this.isAlien(item)) {
			this.aliens.add(item);
		}
		
		if (this.isBonus(item)) {
			this.bonuses.add(item);
		}
		
		if (this.isShoot(item)) {
			this.shoots.add(item);
		}
		
		if (this.isPlanet(item)) {
			this.planets.add(item);
		}
		
		if (this.isMeteor(item)) {
			this.meteors.add(item);
		}
	}
	
	private void handleRemove() {
		if (this.itemsToRemove.size() > 0) {
			for(GameItem item : this.itemsToRemove) {
				this.items.remove(item);
				this.stage.removeActor(item);
				if (this.isAlien(item)) {
					this.aliens.remove(item);
				}
				
				if (this.isBonus(item)) {
					this.bonuses.remove(item);
				}
				
				if (this.isShoot(item)) {
					this.shoots.remove(item);
				}
				
				if (this.isPlanet(item)) {
					this.planets.remove(item);
				}
				
				if (this.isMeteor(item)) {
					this.meteors.remove(item);
				}
			}
			
			this.itemsToRemove.clear();
		}		
	}
	
	private boolean isAlien(GameItem item) {
		return item instanceof Alien;
	}
	
	private boolean isBonus(GameItem item) {
		return item instanceof Score || item instanceof Life ||  item instanceof Invincibility;
	}
	
	private boolean isShoot(GameItem item) {
		return item instanceof Shoot;
	}
	
	private boolean isPlanet(GameItem item) {
		return item instanceof Planet;
	}
	
	private boolean isMeteor(GameItem item) {
		return item instanceof Meteor;
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
		return this.aliens.size();
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
		this.nextId = 0;
		
		this.gameScreen.reset();
		
		this.items.clear();
		this.itemsToRemove.clear();
		this.bonuses.clear();
		this.aliens.clear();
		this.shoots.clear();
		this.planets.clear();
		this.meteors.clear();
				
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
			Star s = new Star(x, y, Assets.getTexture("data/star.png"));
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

	public int getBonusesCount() {
		return this.bonuses.size();
	}

	public void start() {
		this.gameStarted = true;
		TinyWorld.get().setPause(false);
		this.showAd(true);
		this.setScreen(this.gameScreen);
	}

	public void showHelp() {
		this.showAd(false);
		this.setScreen(new Help());
	}
	
	private void showAd(boolean show) {
		if (this.myRequestHandler != null) {
			this.myRequestHandler.showAds(show);
		}
	}
	
	public long getNextID() {
		return this.nextId++;
	}

	public boolean isGWT() {
		return isGWT;
	}

	public void setGWT(boolean isGWT) {
		this.isGWT = isGWT;
	}

	public int getShootsCount() {
		return this.shoots.size();
	}

	public int getMeteorsCount() {
		return this.meteors.size();
	}
	
	public void pauseMusic() {
		this.music.pause();
		this.isMusicPaused = true;
	}
	
	public void resumeMusic() {
		this.music.play();
		this.isMusicPaused = false;
	}

	public void togglePause() {
		this.paused = !this.paused;
	}
	
	public void stopMusic() {
		this.music.stop();
	}

	public GameScreen getGameScreen() {
		return this.gameScreen;
	}
	
	public String getKillString() {
		String kill = "Killed by ";
		GameItem item = this.player.getKillItem();
		if (item != null) {
			kill += item.getName();
		} else {
			kill += "Space itself";
		}
		
		kill += "!";
		
		return kill;
	}

	public void setPause(boolean isPause) {
		this.paused = isPause;
	}
}
