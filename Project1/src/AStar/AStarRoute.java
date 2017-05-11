package AStar;

import java.util.ArrayList;
import ProblemData.Location;
import ProblemData.Package;


public class AStarRoute implements Comparable<AStarRoute> {
	private ArrayList<Location> route;
	private double fuel;
	private double load;
	private double heuristic;
	private int n_packages;
	
	public AStarRoute(AStarRoute r) {
		this.route = new ArrayList<Location>(r.getRoute());
		fuel = 0;
		load = 0;
		heuristic = 0;
	}
	
	public AStarRoute(Location l) {
		route = new ArrayList<Location>();
		route.add(l);
		fuel = 0;
		load = 0;
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
	
	public double getFuel() {
		return fuel;
	}
	
	public void setFuel(double c) {
		fuel = c;
	}
	
	public double getLoad() {
		return load;
	}
	
	public void setLoad() {
		load = 0;
		n_packages = 0;
		ArrayList<Package> packages = UserInterface.UserInterface.deliveryInfo.getDeliveries();
		
		for (Package p : packages) {
			if (route.contains(p.getLocation())) {
				load += p.getVolume();
				n_packages++;
			}
		}
	}
	
	public int getNPackages() {
		return n_packages;
	}
	
}
