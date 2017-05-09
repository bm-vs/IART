package ProblemData;
import java.util.ArrayList;

public class Location implements Comparable<Location> {
	private int id;
	private int x;
	private int y;
	private boolean fuel;
	private ArrayList<Connection> connections; //Connected nodes and respective fuel consumption per distance unit
	private double g;
	private double f;
	private double h;
	private Location parent;
	
	public Location(){}
	
	public Location(int id, int x, int y, boolean fuel) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.fuel = fuel;
		connections = new ArrayList<Connection>();
	}
	
	public void print() {
		System.out.println(id + " " + x + " " + y + " " + fuel);
		for (Connection c: connections) {
			System.out.println(c);
		}
	}
	
	/*=======================================================================================*/
	/* Gets */
	
	public int getID() {
		return id;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public boolean getFuel() {
		return fuel;
	}
	
	public ArrayList<Connection> getConnections() {
		return connections;
	}
	
	public double getG() {
		return g;
	}
	
	public double getF() {
		return f;
	}
	
	public double getH() {
		return h;
	}
	
	public Location getParent() {
		return parent;
	}
	
	
	/*=======================================================================================*/
	/* Edit */
	
	public void addConnection(Connection c) {
		if (!connections.contains(c)) {
			connections.add(c);
		}
	}
	
	public void setConnections(ArrayList<Connection> connections) {
		this.connections = connections;
	}
	
	public void setFuel(boolean fuel) {
		this.fuel = fuel;
	}
	
	public void setG(double g) {
		this.g = g;
	}
	
	public void setF(double f) {
		this.f = f;
	}
	
	public void setH(double h) {
		this.h = h;
	}
	
	public void setParent(Location parent) {
		this.parent = parent;
	}
	
	public void setAStarVars(double g, double f, double h, Location parent) {
		this.g = g;
		this.f = f;
		this.h = h;
		this.parent = parent;
	}
	
	/*=======================================================================================*/
	/* Util */
	
	public double linearDistance(Location location) {
		return Math.sqrt(Math.pow((x-location.getX()), 2) +  Math.pow((y-location.getY()), 2));
	}
	
	public boolean equals(Location l) {
		return l.getID() == id;
	}
	
	public int compareTo(Location l) {		
		return Double.compare(f, l.getF());
	}
	
	public String toString() {
		return ((Integer) id).toString();
	}
}
