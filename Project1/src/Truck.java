public class Truck {
	private int fuel;
	private int load;
	
	public Truck(int fuel, int load) {
		this.fuel = fuel;
		this.load = load;
	}
	
	public void print() {
		System.out.println(fuel + " " + load);
	}
	
}
