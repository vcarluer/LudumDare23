package org.devince.tinyworld.world;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.devince.tinyworld.TinyWorld;
import org.devince.tinyworld.items.Planet;
import org.devince.tinyworld.items.Point;

public class Galaxy {
	public static final int TILESIZE = 16;
	public static int BOTTOM_LEFT = 0;
	public static int MIDDLE_LEFT = 1;
	public static int TOP_LEFT = 2;
	public static int BOTTOM = 3;
	public static int TOP = 4;
	public static int BOTTOM_RIGHT = 5;
	public static int MIDDLE_RIGHT = 6;
	public static int TOP_RIGHT = 7;
	
	
	private HashMap<Point, Planet> planets;
	private Planet startPlanet;
	private Point comparePoint;
	
	public Galaxy() {
		this.planets = new HashMap<Point, Planet>();
		this.comparePoint = new Point();
		this.initWorld();
	}

	private void initWorld() {
		this.startPlanet = this.addPlanet(0, 0);
		this.addPlanet(1, 0);
		this.addPlanet(1, 1);
		this.addPlanet(2, 1);
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
		float shiftX = x + TILESIZE / 2f;
		float shiftY = y + TILESIZE / 2f;
		return this.getAroundPlanets((int)Math.floor(shiftX / TILESIZE), (int)Math.floor(shiftY / TILESIZE));
	}
	
	public Planet[] getAroundPlanets(int x, int y) {
		// always returns planets in same order
		Planet[] aroundPlanets = new Planet[8];
		int cpt = 0;
		for (int cX = x - 1; cX < x + 2; cX++) {
			for (int cY = y - 1; cY < y + 2; cY++) {
				if (cX == x && cY == y) continue;
				aroundPlanets[cpt] = this.getPlanet(cX, cY);
				cpt++;
			}
		}
		
		return aroundPlanets;
	}
	
	private boolean contains(int x, int y) {
		this.comparePoint.setX(x);
		this.comparePoint.setY(y);
		return this.planets.containsKey(this.comparePoint);
	}
	
	private Planet getPlanet(int x, int y) {
		this.comparePoint.setX(x);
		this.comparePoint.setY(y);
		return this.planets.get(this.comparePoint);
	}
}
