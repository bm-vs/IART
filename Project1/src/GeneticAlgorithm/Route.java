package GeneticAlgorithm;

import java.util.ArrayList;
import java.util.Collections;

import AStar.AStar;
import ProblemData.Location;


public class Route {
	private ArrayList<Location> route;
	private ArrayList<ArrayList<Location>> path;
	private double fitness;
	private double distance;
	
	public Route(int n_nodes) {
		route = new ArrayList<Location>(Collections.nCopies(n_nodes, null));
		path = new ArrayList<ArrayList<Location>>();
		fitness = 0;
		distance = 0;
	}
	
	public Route(ArrayList<Location> nodes, Location start) {
		route = new ArrayList<Location>();
		for (Location l: nodes) {
			if (!l.equals(start)) {
				route.add(l);
			}
		}
		
		path = new ArrayList<ArrayList<Location>>();
		Collections.shuffle(route);
		route.add(0, start);
		fitness = 0;
		distance = 0;
	}
	
	public double getFitness() {
		if (fitness == 0) {
			fitness = 1/(double) getDistance();
		}
		
		return fitness;
	}
	
	public double getDistance() {
		if (distance == 0) {
			for (int i = 0; i < route.size(); i++) {
				Location start = route.get(i);
				Location end = route.get((i+1) % route.size());
				
				ArrayList<Location> partial_path = new ArrayList<Location>();
				distance += AStar.shortestDistance(start, end, partial_path);
				path.add(partial_path);
			}
		}
		
		return distance;
	}
	
	public int getSize() {
		return route.size();
	}
	
	public Location getLocation(int i) {
		return route.get(i);
	}
	
	public void setLocation(int i, Location location) {
		route.set(i, location);
	}
	
	public boolean contains(Location location) {
		return route.contains(location);		
	}
	
	public ArrayList<ArrayList<Location>> getPath() {
		return path;
	}
	
	public ArrayList<Location> getRoute() {
		return route;
	}
	
	public String toString() {
		return route.toString();
	}
	
}
