package spaceapes;

import java.util.List;
import java.util.ArrayList;

import org.newdawn.slick.geom.Vector2f;

import eea.engine.entity.Entity;

public class Parser {
	
	private List<Entity> playerPlanets = new ArrayList<Entity>();
	
	public IMap parseMap() {
		
		//Hier werden alle Entities, die auf der Map vorkommen erstellt
		Map map = initBackground(Map.getInstance());
		map = parsePlanet(map);
		map = parseApe(map); //parsePlanets() muss unbedingt davor ausgeführt werden!
		//map = parseControlPanel(map); -> MR: Das passt hier schlecht hin, wegen dem Render der korresponiderenden Entities... 
		
		return map;
	}
	
	protected Map initBackground(Map map) {
		map.addEntity(new BackgroundFactory().createEntity());
		return map;
	}

	protected Map parsePlanet(Map map) {
			
		float xBorder = Utils.worldWidth / 2;
		float yBorder = Utils.worldHeight / 2;
		
		//MR: die Erzeugung der Planeten muss später auch noch in eine Schleife,
		// wenn es mehr als 2 Speieler geben können soll...
		//Planet 1 für Spieler 1 in der linken Hälfte platzieren
		String namePlanetOne = "Planet1";
		float xPlanetOne = Utils.randomFloat(-xBorder * 0.6f, -xBorder * 0.3f);
		float yPlanetOne = Utils.randomFloat(-yBorder * 0.5f, yBorder * 0.5f);
		Vector2f coordinatesPlanetOne = new Vector2f(xPlanetOne, yPlanetOne);
		float radiusPlanetOne = Utils.randomFloat(0.75f, 1.5f);
		int massPlanetOne = (int) (radiusPlanetOne * Utils.randomFloat(0.91f, 1.1f) * 65);
		
		Entity planetOne = new PlanetFactory(namePlanetOne, radiusPlanetOne, massPlanetOne,
				coordinatesPlanetOne).createEntity();
		System.out.println("Planet1 Entity: " + planetOne.toString());
		// Spielerplaneten und für die Berechnungen notwendige Planetendaten werden in
		// der Instanz von Map abgelegt. Somit kann man von überall darauf zugreifen
		playerPlanets.add(planetOne);
		map.addPlanetData(xPlanetOne, yPlanetOne, massPlanetOne, radiusPlanetOne);
		map.addPlanet((Planet) planetOne);
		System.out.println("Planet1 Data: " + map.getPlanetData().get(0).toString());
		map.addEntity(planetOne);
		
		//Planet 2 für Spieler 2 in der rechten Hälfte platzieren
		String namePlanetTwo = "Planet2";
		float xPlanetTwo = Utils.randomFloat(xBorder * 0.3f, xBorder * 0.6f);
		float yPlanetTwo = Utils.randomFloat(-yBorder * 0.5f, yBorder * 0.5f);
		Vector2f coordinatesPlanetTwo = new Vector2f(xPlanetTwo, yPlanetTwo);
		float radiusPlanetTwo = Utils.randomFloat(0.75f, 1.5f);
		int massPlanetTwo = (int) (radiusPlanetTwo * Utils.randomFloat(0.91f, 1.1f) * 65);
		
		Entity planetTwo = new PlanetFactory(namePlanetTwo, radiusPlanetTwo, massPlanetTwo,
				coordinatesPlanetTwo).createEntity();
		playerPlanets.add(planetTwo);
		map.addPlanetData(xPlanetTwo, yPlanetTwo, massPlanetTwo, radiusPlanetTwo);
		map.addPlanet((Planet) planetTwo);
		map.addEntity(planetTwo);
		
		//Weitere Planeten...
		
		return map;
	}
	
	protected Map parseApe(Map map) {
		
		for (int i = 0; i < Launch.players.size(); i++) {
			
			String nameApe = "ape" + (i + 1);
			Planet homePlanet = (Planet) playerPlanets.get(i);
			int health = 100;
			int energy = 50;
			int apeImage = i+1;
			boolean apeActive = (i==0);
			boolean apeInteraction = (i==0);
			float movementSpeed = 0.08f;
			float angleOnPlanet = Utils.randomFloat(0, 360);
			float angleOfView = 0;
			float throwStrength = 5f;
			
			//MR folgende Zeilen überarbeiten!!
			float apePixelHeight = 300;
			float pixelfromFeetToCenter = 130;
			float desiredApeSizeInWorldUnits = 0.6f;
			float scalingFactor = desiredApeSizeInWorldUnits / Utils.pixelLengthToWorldLength(apePixelHeight);
			float distancePlanetCenter = homePlanet.getRadius() + Utils.pixelLengthToWorldLength(pixelfromFeetToCenter * scalingFactor);
			if (distancePlanetCenter > 0.1f) {
				//nichts
			} else
				throw new RuntimeException("Radius ist zu nah an null");
			
			Entity ape = new ApeFactory(nameApe, homePlanet, health, energy,
					apeImage, apeActive, apeInteraction, movementSpeed, angleOnPlanet,
					angleOfView, throwStrength, distancePlanetCenter).createEntity();
			map.addApe((Ape) ape);
			map.addEntity(ape);
		}
		
		return map;
	}
	
	protected Map parseControlPanel(Map map) {
		
		ControlPanel controlPanel = new ControlPanel("ControlPanel");
		controlPanel.initControlPanel();
		map.addEntity(controlPanel);
		
		//map.addEntity(new ControlPanelFactory().createEntity()); -> MR: Factory für Panel benutzen?
		
		return map;
	}
	
}
