import java.util.HashMap;

public class Location {
	private int id;
	private int x;
	private int y;
	private boolean fuel;
	private HashMap<Location, Integer> connections;

	public Location(int id, int x, int y, boolean fuel) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.fuel = fuel;
		connections = new HashMap<Location, Integer>();
	}
	
	public void print() {
		System.out.println(id + " " + x + " " + y + " " + fuel);
		for (Location key: connections.keySet()) {
			System.out.println("   " + key.getID() + " " + connections.get(key));
		}
	}
	
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
	
	public void addConnection(Location location, int distance) {
		connections.put(location, distance);
	}
	
}
