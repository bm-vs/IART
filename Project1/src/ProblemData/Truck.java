package problemData;
public class Truck {

	private int fuel;
	private int load;
	private Location location;
	private int fuelPerKm;
	
	public Truck(int fuel, int load, Location location, int fuelPerKm) {
		this.fuel = fuel;
		this.load = load;
		this.location = location;
		this.fuelPerKm = fuelPerKm;
	}
	
	public void print() {
		System.out.println(fuel + " " + load);
		location.print();
	}
	
	public int getFuel() {
		return fuel;
	}

	public int getLoad() {
		return load;
	}

	public Location getLocation() {
		return location;
	}
	
	public int getFuelPerKm() {
		return fuelPerKm;
	}
}
