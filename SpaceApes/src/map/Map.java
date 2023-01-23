package map;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import entities.Ape;
import entities.ControlPanel;
import entities.Planet;
import entities.Projectile;
import interfaces.IMap;
import spaceapes.Launch;

public class Map implements IMap {
	private static Map map = new Map();
	private List<Entity> entities; // Brauchen wird das wirklich?
	private List<Ape> apes;
	private List<Planet> planets;
	private List<Projectile> livingProjectiles; // alle lebenden Projektile (ohne DummyProjectiles der Aimline)
	private boolean hasProjectileExploded;
	
	/**
	 * Erzeugt ein leeres Map Objekt. Mit den init-Methoden koennen Entitys der Map
	 * hinzugefuegt werden.
	 */
	public Map() {
		entities = new ArrayList<Entity>();
		apes = new ArrayList<Ape>();
		planets = new ArrayList<Planet>();
		livingProjectiles = new ArrayList<Projectile>();
		hasProjectileExploded = false;
	}

	public static Map getInstance() {
		return map;
	}
	
	public void parse() {
		Parser parser = new Parser();
		parser.parseMap();
	}
	
	@Override
	public List<Entity> getEntities() {
		return entities;
	}
	
	@Override
	public void addEntity(Entity entity) {
		entities.add(entity);
	}
	
	@Override
	public void removeEntity(Entity entity) {
		entities.remove(entity);
	}
	
	public boolean hasProjectileExploded() {
		return hasProjectileExploded;
	}
	
	public void setProjectileExploded(boolean hasExploded) {
		hasProjectileExploded = hasExploded;
	}
	
	public List<Projectile> getLivingProjectiles() {
		return livingProjectiles;
	}
	
	public void addLivingProjectile(Projectile projectile) {
		livingProjectiles.add(projectile);
	}
	
	public void clearLivingProjectiles() {
		livingProjectiles.clear();
	}
	
	public List<Planet> getPlanets() {
		return planets;
	}
	
	public void addPlanet(Planet planet) {
		planets.add(planet);
	}
	
	public List<Ape> getApes() {
		return apes;
	}
	
	public void addApe(Ape ape) {
		apes.add(ape);
	}
	
	public Ape getActiveApe() {
		for(Ape ape : apes) {
			if(ape.isActive()) {
				return ape;
			}
		}
		return apes.get(0); // Hier sollte er nie hinkommen
	}
	
	public void changeActiveApeToNextApe() {
		int indexActiveApe = apes.indexOf(getActiveApe());
		int indexNextPlayer = indexActiveApe + 1;
		if (indexNextPlayer >= apes.size()) {
			indexNextPlayer = 0; // Nach dem letzten Spieler in der Liste, ist wieder der erste dran
		}
		Ape activeApe = apes.get(indexActiveApe);
		activeApe.setActive(false);
		activeApe.setInteractionAllowed(false);
		Ape nextApe = apes.get(indexNextPlayer);
		nextApe.setActive(true);
		nextApe.setInteractionAllowed(true);
		java.lang.System.out.println("Am Zug: " + nextApe.getID());
		StateBasedEntityManager entityManager = StateBasedEntityManager.getInstance();
		((ControlPanel) entityManager.getEntity(Launch.GAMEPLAY_STATE, "ControlPanel")).setPanelAndComponentsVisible(true);
	}
	
}
