package org.devince.tinyworld.world;
import java.util.HashSet;
import java.util.Set;

import org.devince.tinyworld.TinyWorld;
import org.devince.tinyworld.items.Planet;

public class World {
	public static final int TILESIZE = 16;
	private Set<Planet> items;
	private Planet startPlanet;
	
	public World() {
		this.items = new HashSet<Planet>();
		
		this.initWorld();
	}

	private void initWorld() {
		this.startPlanet = this.addPlanet(0, 0);
	}
	
	private Planet addPlanet(int x, int y) {
		Planet planet = new Planet(0, 0);
		this.items.add(planet);
		TinyWorld.get().addGameItem(planet);
		return planet;
	}

	public Planet getStartPlanet() {
		return this.startPlanet;
	}
}
