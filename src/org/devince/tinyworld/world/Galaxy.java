package org.devince.tinyworld.world;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.devince.tinyworld.TinyWorld;
import org.devince.tinyworld.items.Planet;
import org.devince.tinyworld.items.Point;

public class Galaxy {
	public static final int TILESIZE = 16;
	private HashMap<Point, Planet> planets;
	private Planet startPlanet;
	private Planet[] aroundPlanets;
	private Point comparePoint;
	
	public Galaxy() {
		this.planets = new HashMap<Point, Planet>();
		this.aroundPlanets = new Planet[8];
		this.comparePoint = new Point();
		this.initWorld();
	}

	private void initWorld() {
		this.startPlanet = this.addPlanet(0, 0);
	}
	
	private Planet addPlanet(int x, int y) {
		Planet planet = new Planet(x, y);
		this.planets.put(planet.getGalaxyPoint(), planet);
		TinyWorld.get().addGameItem(planet);
		return planet;
	}

	public Planet getStartPlanet() {
		return this.startPlanet;
	}
	
	public Planet[] getAroundPlanetsFromGamePosition(float x, float y) {
		float shiftX = x - TILESIZE / 2f;
		float shiftY = y - TILESIZE / 2f;
		return this.getAroundPlanets((int)Math.ceil(shiftX / TILESIZE), (int)Math.ceil(shiftY / TILESIZE));
	}
	
	public Planet[] getAroundPlanets(int x, int y) {
		// always returns planets in order from T clockwise
		
		
		
		return this.aroundPlanets;
	}
	
	private boolean contains(int x, int y) {
		this.comparePoint.setX(x);
		this.comparePoint.setY(y);
		return this.planets.containsKey(this.comparePoint);
	}
}
