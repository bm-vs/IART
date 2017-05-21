package algorithms;

import java.util.ArrayList;

import problemData.Location;
import problemData.Package;


public class Route implements Comparable<Route> {
	private ArrayList<Location> route;
	private double distance;
	private double fuel;
	private double load;
	private double heuristic;
	private double value;
	private int n_packages;
	private int n_fuel;
	
	public Route(Route r) {
		this.route = new ArrayList<Location>(r.getRoute());
		distance = r.getDistance();
		fuel = r.getFuel();
		load = r.getLoad();
		heuristic = 0;
		n_fuel = r.getNFuel();
		n_packages = r.getNPackages();
		value = r.getValue();
	}
	
	public Route(Location l) {
		route = new ArrayList<Location>();
		route.add(l);
		distance = 0;
		fuel = 0;
		load = 0;
		heuristic = 0;
		n_fuel = 0;
		n_packages = 0;
		value = 0;
	}
	
	public double getDistance() {
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
	
	public int compareTo(Route r) {
		return Double.compare(heuristic, r.getHeuristic());
	}
	
	public void addLocation(Location l, int fuelPerKm, boolean fuel) {
		ArrayList<Location> path = new ArrayList<Location>();
		if (route.get(route.size()-1).getFuel() && fuel) {
			this.fuel = 0;
		}
		this.fuel += AStar.shortestDistance(route.get(route.size()-1), l, path)*fuelPerKm;
		distance += AStar.shortestDistance(route.get(route.size()-1), l, path);
		
		ArrayList<Package> packages = userInterface.UserInterface.deliveryInfo.getDeliveries();
		for (Package p : packages) {
			if (l.equals(p.getLocation())) {
				load += p.getVolume();
				n_packages++;
				value += p.getValue();
			}
		}
		
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
		ArrayList<Package> packages = userInterface.UserInterface.deliveryInfo.getDeliveries();
		
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
	
	public double getValue() {
		return value;
	}
	
	public Location getLastNode() {
		return route.get(route.size()-1);
	}
	
	public int getNFuel() {
		return n_fuel;
	}
	
	public void addNFuel() {
		n_fuel++;
	}
	
	public int getSize() {
		return route.size()-n_fuel;
	}
}
