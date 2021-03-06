package org.devince.tinyworld.world;

import org.devince.tinyworld.TinyWorld;
import org.devince.tinyworld.items.Alien;
import org.devince.tinyworld.items.AlienShooter;
import org.devince.tinyworld.items.GameItem;
import org.devince.tinyworld.items.Invincibility;
import org.devince.tinyworld.items.Life;
import org.devince.tinyworld.items.Planet;
import org.devince.tinyworld.items.Player;
import org.devince.tinyworld.items.Score;
import org.devince.tinyworld.items.Sun;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class PlanetGenerator extends GameItem {
	private static final int SPAWN_RADIUS_MIN = 3;
	private static final int SPAWN_RADIUS = 3; // On min
	private static final int MAX_ALIEN = 10;
	private static final int MAX_ITEMS = 20;
	private static float SPAWN_MAX = 2f; 
	private static float SPAWN_BASE = 12f;
	private static float SPAWN_MAX_ITEM = 2f; 
	private static float SPAWN_BASE_ITEM = 6f;
	
	private float spawnDelta;
	private float spawnTime;
	
	private float spawnDeltaItem;
	private float spawnTimeItem;
	private Sound sndCreate;
	private Planet[] planetsHolder;
	
	public PlanetGenerator() {
		this.spawnDelta = SPAWN_MAX;
		this.spawnDeltaItem = SPAWN_MAX_ITEM;
		this.sndCreate = this.sndLoad("data/spawn.wav");
		this.planetsHolder = TinyWorld.get().getGalaxy().createPlanetsStruc();
	}
	
	private float getSpawnDelta() {
		float delta = SPAWN_BASE - (TinyWorld.get().getLevel());
		if (delta < SPAWN_MAX) {
			delta = SPAWN_MAX;
		}
		
		return delta;
	}
	
	private float getSpawnDeltaItem() {
		float delta = SPAWN_BASE_ITEM - (TinyWorld.get().getLevel());
		if (delta < SPAWN_MAX_ITEM) {
			delta = SPAWN_MAX_ITEM;
		}
		
		return delta;
	}

	@Override
	protected float getRefereceWidth() {
		return 0;
	}

	@Override
	protected float getReferenceHeight() {
		return 0;
	}

	@Override
	public Actor hit(float x, float y) {
		return null;
	}	
	
	@Override
	public void act(float delta) {
		super.act(delta);
		this.spawnTime += delta;
		this.spawnTimeItem += delta;
		Planet planet = null;
		Planet free = null;
		int x = (- SPAWN_RADIUS) + (int)Math.floor((Math.random() * ((SPAWN_RADIUS * 2) + 1) )); 
		if (x > 0) {
			x += SPAWN_RADIUS_MIN;
		} else {
			x -= SPAWN_RADIUS_MIN;
		}
		
		int y = (- SPAWN_RADIUS) + (int)Math.floor((Math.random() * ((SPAWN_RADIUS * 2) + 1)));
		if (y > 0) {
			y += SPAWN_RADIUS_MIN;
		} else {
			y -= SPAWN_RADIUS_MIN;
		}
		
		boolean forceNoAliens = TinyWorld.get().getAlienCount() == 0;
		
		if (this.spawnTime > this.spawnDelta || this.spawnTimeItem > this.spawnDeltaItem || forceNoAliens) {
			Player p = TinyWorld.get().getPlayer();
			x += p.getGalaxyPoint().x;
			y += p.getGalaxyPoint().y;
			
			TinyWorld.get().getGalaxy().getAroundPlanets(x, y, this.planetsHolder);
			for(Planet pla : this.planetsHolder) {
				if (pla != null && pla.isPrimary() && !(pla instanceof Sun)) {
					free = pla;
					break;
				}
			}
		}
		
		if (this.spawnTime > this.spawnDelta || forceNoAliens) {
			if (free == null) {
				Player player = TinyWorld.get().getPlayer();
				if (!(player.x == x && player.y == y))	 {
					if (!TinyWorld.get().getGalaxy().nearSun(x, y)) {
						planet = TinyWorld.get().getGalaxy().addPlanet(x, y, true);
						this.sndCreate.play();
					}
				}
			} else {
				planet = free;
			}
			
			if (planet != null) {
				if (TinyWorld.get().getAlienCount() < TinyWorld.get().getLevel() * MAX_ALIEN) {
					double rand = Math.random();
					Alien alien = null;
					if (rand < 0.5f) {
						alien = new Alien(0, 0);
					} else {
						alien = new AlienShooter(0, 0);
					}
					
					TinyWorld.get().addGameItemOnPlanet(alien, planet);
				}
				
				this.spawnTime = 0;
				this.spawnDelta = this.getSpawnDelta();
			}
		}
		
		if (this.spawnTimeItem > this.spawnDeltaItem || forceNoAliens) {
			if (planet == null && free != null) {
				planet = free;
			}
			
			if (planet != null) {
				if (TinyWorld.get().getBonusesCount() < TinyWorld.get().getLevel() * MAX_ITEMS) {
					this.randomItem(planet);
				}
				
				this.spawnTimeItem = 0;
				this.spawnDeltaItem = this.getSpawnDeltaItem();
			}
		}
	}

	private void randomItem(Planet planet) {
		int sel = (int) Math.floor(Math.random() * 10);
		GameItem item = null;
		if (sel < 7) {
			item = new Score();
		} else if (sel < 9) {
			item = new Life();
		} else {
			item = new Invincibility();
		}
		
		TinyWorld.get().addGameItemOnPlanet(item, planet);
	}

}
