public class Truck {
	private int fuel;
	private int load;
	private Location location;
	
	public Truck(int fuel, int load, Location location) {
		this.fuel = fuel;
		this.load = load;
		this.location = location;
	}
	
	public void print() {
		System.out.println(fuel + " " + load);
		location.print();
	}
	
}
