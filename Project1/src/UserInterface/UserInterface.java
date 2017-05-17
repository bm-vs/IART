package UserInterface;
import java.util.Scanner;

import ProblemData.Algorithm;
import ProblemData.DeliveryInfo;

public class UserInterface {
	public static DeliveryInfo deliveryInfo;
	public static GraphDisplay gui;
	
	public UserInterface() {
		Scanner reader = new Scanner(System.in);
		int decision;
		
		System.out.println("1 - Create file");
		System.out.println("2 - Read file");
		System.out.println("3 - Exit");
		decision /*= 2;*/= reader.nextInt();
		
		if (decision == 1) {
			System.out.println("----Locations----");
			System.out.print("Number of locations: ");
			int nLocations = reader.nextInt();
			System.out.print("Max X: ");
			int maxX = reader.nextInt();
			System.out.print("Max Y: ");
			int maxY = reader.nextInt();
			System.out.print("Nr. of fuel nodes: ");
			int nFuel = reader.nextInt();
			
			System.out.println("----Connections----");
			System.out.print("Connection level: ");
			int connectionLevel = reader.nextInt();
			
			System.out.println("----Truck----");
			System.out.print("Fuel: ");
			int fuel = reader.nextInt();
			System.out.print("Load: ");
			int load = reader.nextInt();
			System.out.print("Start node id: ");
			int start = reader.nextInt();
			System.out.print("Fuel per km: ");
			int fuelPerKm = reader.nextInt();
			
			System.out.println("----Packages----");
			System.out.print("Number of packages: ");
			int nPackages = reader.nextInt();
			System.out.print("Max. volume: ");
			int maxVolume = reader.nextInt();
			System.out.print("Max. value: ");
			int maxValue = reader.nextInt();
			XMLParser.XMLParser.write(nLocations, maxX, maxY, connectionLevel, nFuel, fuelPerKm, fuel, load, start, nPackages, maxVolume, maxValue);
			System.out.println("Finished generating scenario");
		}
		else if (decision == 2){
			System.out.print("Document name: ");
			String file = reader.nextLine(); file = reader.nextLine();
			deliveryInfo = new DeliveryInfo();
			XMLParser.XMLParser.read(file, deliveryInfo);
			gui = new GraphDisplay();
			Algorithm alg = new Algorithm();
			
			System.out.println("Optimize: ");
			System.out.println("1 - Delivery Count");
			System.out.println("2 - Delivery Value");
			int opt = reader.nextInt();
			
			if (opt == 1) {
				alg.run("delivery_count");
			}
			else if (opt == 2) {
				alg.run("delivery_value");
			}
			
		}
		else if (decision != 3) {
			reader.close();
			return;
		}
		
		reader.close();
	}
}
