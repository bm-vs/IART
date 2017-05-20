package algorithms;

import java.util.ArrayList;

import problemData.Location;

public class Distance {
	private Location start;
	private Location end;
	private ArrayList<Location> path;
	private double weight;
	
	public Distance(Location start, Location end, ArrayList<Location> path, double weight) {
		this.start = start;
		this.end = end;
		this.path = path;
		this.weight = weight;
	}
	
	public Location getStart() {
		return start;
	}
	
	public Location getEnd() {
		return end;
	}
	
	public ArrayList<Location> getPath() {
		return path;
	}
	
	public double getWeight() {
		return weight;
	}
}
