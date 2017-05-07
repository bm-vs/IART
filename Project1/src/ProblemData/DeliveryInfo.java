package ProblemData;
import java.util.*;

public class DeliveryInfo {
	/*=======================================================================================*/
	/* Paramenters and constructors */
	
	private Truck truck;
	private HashMap<Integer, Location> locations; // List of nodes
	private ArrayList<Package> packages;
	
	public DeliveryInfo() {
		locations = new HashMap<Integer,Location>();
		packages = new ArrayList<Package>();
	}
	
	/*=======================================================================================*/
	/* Gets */
	
	public Truck getTruck() {
		return truck;
	}
	
	public HashMap<Integer, Location> getLocations() {
		return locations;
	}
	
	public ArrayList<Package> getDeliveries() {
		return packages;
	}
	
	public Location getLocation(int location) {
		return locations.get(location);
	}
	
	/*=======================================================================================*/
	/* Sets */
	
	public void addLocation(int id, Location location) {
		locations.put(id, location);
	}
	
	public void setTruck(int fuel, int load, Location location) {
		truck = new Truck(fuel, load, location);
	}
	
	public void addPackage(Package delivery) {
		packages.add(delivery);
	}
	
	/*=======================================================================================*/
	/* Util */
	public void resetAStarVars() {
		for (Integer id: locations.keySet()) {
			locations.get(id).setAStarVars(0, 0, 0, null);
		}
	}
	
	/*=======================================================================================*/
	/* Print data */
	
	public void printTruck() {
		System.out.println("----Truck----");
		truck.print();
	}
	
	public void printLocations() {
		System.out.println("----Locations----");
		for (int key: locations.keySet()) {
			locations.get(key).print();
		}
	}
	
	public void printPackages() {
		System.out.println("----Packages----");
		for (int i = 0; i < packages.size(); i++) {
			packages.get(i).print();
		}
	}	
}
