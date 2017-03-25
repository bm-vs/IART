public class Package {
	public static void main(String[] args) {
		DeliveryInfo deliveryInfo = new DeliveryInfo();
		deliveryInfo.printTruck();
		deliveryInfo.printLocations();
		deliveryInfo.printPackages();
		deliveryInfo.createDocument();
	}
	
	private Location location;
	private int volume;
	private int value;
	
	public Package(Location location, int volume, int value) {
		this.location = location;
		this.volume = volume;
		this.value = value;
	}
	
	public void print() {
		System.out.println(location.getID() + " " + volume + " " + value);
	}
}
