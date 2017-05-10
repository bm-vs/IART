package AStar;

import java.util.ArrayList;
import ProblemData.Location;


public class AStarRoute implements Comparable<AStarRoute> {
	private ArrayList<Location> route;
	private double cost;
	private double heuristic;
	
	public AStarRoute(AStarRoute r) {
		this.route = new ArrayList<Location>(r.getRoute());
		cost = 0;
		heuristic = 0;
	}
	
	public AStarRoute(Location l) {
		route = new ArrayList<Location>();
		route.add(l);
		cost = 0;
		heuristic = 0;
	}
	
	public double getDistance() {
		double distance = 0;
		
		for (int i = 0; i < route.size()-1; i++) {
			ArrayList<Location> partial_path = new ArrayList<Location>();
			distance += AStar.shortestDistance(route.get(i), route.get(i+1), partial_path);
		}
		
		return distance;
	}
	
	public boolean contains(Location location) {
		return route.contains(location);		
	}
	
	public ArrayList<Location> getRoute() {
		return route;
	}
	
	public String toString() {
		return route.toString();
	}
	
	public int compareTo(AStarRoute r) {
		return Double.compare(heuristic, r.getHeuristic());
	}
	
	public void addLocation(Location l) {
		route.add(l);
	}
	
	public double getHeuristic() {
		return heuristic;
	}
	
	public void setHeuristic(double f) {
		heuristic = f;
	}
	
	public double getCost() {
		return cost;
	}
	
	public void setCost(double c) {
		cost = c;
	}
}
