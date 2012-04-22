package org.devince.tinyworld.world;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.devince.tinyworld.TinyWorld;
import org.devince.tinyworld.items.Planet;
import org.devince.tinyworld.items.Point;
import org.devince.tinyworld.items.Sun;

public class Galaxy {
	public static final int TILESIZE = 16;
	public static int BOTTOM_LEFT = 0;
	public static int MIDDLE_LEFT = 1;
	public static int TOP_LEFT = 2;
	public static int BOTTOM = 3;
	public static int CENTER = 4;
	public static int TOP = 5;
	public static int BOTTOM_RIGHT = 6;
	public static int MIDDLE_RIGHT = 7;
	public static int TOP_RIGHT = 8;
	
	
	private HashMap<Point, Planet> planets;
	private Planet startPlanet;
	private Point comparePoint;
	
	public Galaxy() {
		this.planets = new HashMap<Point, Planet>();
		this.comparePoint = new Point();
		this.initWorld();
	}

	private void initWorld() {
		this.startPlanet = this.addPlanet(-5, -5, true);
		this.addPlanet(new Sun(0, 0));
		this.addPlanet(new Sun(1, 0));
		this.addPlanet(new Sun(0, 1));
		this.addPlanet(new Sun(1, 1));
	}
	
	public Planet addPlanet(int x, int y) {
		return this.addPlanet(x, y, false);
	}
	
	public Planet addPlanet(int x, int y, boolean primary) {
		this.comparePoint.x = x;
		this.comparePoint.y = y;
		Planet planet = null;
		if (!this.planets.containsKey(this.comparePoint)) {
			planet = new Planet(x, y, primary);
			this.planets.put(planet.getGalaxyPoint(), planet);
			TinyWorld.get().addGameItem(planet);
		}
		
		return planet;
	}
	
	public Planet addPlanet(Planet planet) {
		if (!this.planets.containsKey(planet.getGalaxyPoint())) {
			this.planets.put(planet.getGalaxyPoint(), planet);
			TinyWorld.get().addGameItem(planet);
		}
		
		return planet;
	}

	public Planet getStartPlanet() {
		return this.startPlanet;
	}
	
	public Point getGalaxyCoordinate(float x, float y) {
		Point point = new Point();
		this.getGalaxyCoordinate(point, x, y);
		return point;
	}
	
	public Planet[] getAroundPlanetsFromGamePosition(float x, float y) {
		float shiftX = x + TILESIZE / 2f;
		float shiftY = y + TILESIZE / 2f;
		// No point creation by calling getGalaxyCoordinate for optim
		return this.getAroundPlanets((int)Math.floor(shiftX / TILESIZE), (int)Math.floor(shiftY / TILESIZE));
	}
	
	public Planet[] getAroundPlanets(int x, int y) {
		// always returns planets in same order
		Planet[] aroundPlanets = new Planet[9];
		int cpt = 0;
		for (int cX = x - 1; cX < x + 2; cX++) {
			for (int cY = y - 1; cY < y + 2; cY++) {
				aroundPlanets[cpt] = this.getPlanet(cX, cY);
				cpt++;
			}
		}
		
		return aroundPlanets;
	}
	
	public boolean contains(int x, int y) {
		this.comparePoint.setX(x);
		this.comparePoint.setY(y);
		return this.planets.containsKey(this.comparePoint);
	}
	
	public boolean contains(Point galaxyPoint) {
		return this.planets.containsKey(galaxyPoint);
	}
	
	private Planet getPlanet(int x, int y) {
		this.comparePoint.setX(x);
		this.comparePoint.setY(y);
		return this.planets.get(this.comparePoint);
	}

	public void getGalaxyCoordinate(Point galaxyPoint, float x, float y) {
		float shiftX = x + TILESIZE / 2f;
		float shiftY = y + TILESIZE / 2f;
		
		
		galaxyPoint.setX((int)Math.floor(shiftX / TILESIZE));
		galaxyPoint.setY((int)Math.floor(shiftY / TILESIZE));
	}

	public void removePlanet(Planet planet) {
		this.planets.remove(planet.getGalaxyPoint());
		TinyWorld.get().addItemToRemove(planet);
		
	}
}
