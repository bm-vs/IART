import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.*;

// XML parser
import org.w3c.dom.*;

// Graph display
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

public class DeliveryInfo {
	/*=======================================================================================*/
	/* Paramenters and constructors */
	
	private Truck truck;
	private HashMap<Integer, Location> locations;
	private ArrayList<Package> packages;
	
	public DeliveryInfo() {
		locations = new HashMap<Integer,Location>();
		packages = new ArrayList<Package>();
		XMLParser parser = new XMLParser(this);
		parser.read("data1");
	}
	
	public DeliveryInfo(String docName) {
		locations = new HashMap<Integer,Location>();
		packages = new ArrayList<Package>();
		XMLParser parser = new XMLParser(this);
		parser.read(docName);
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
