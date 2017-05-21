package userInterface;
import java.util.Scanner;

import problemData.DeliveryInfo;
import problemData.Solve;

public class UserInterface {
	public static DeliveryInfo deliveryInfo;
	
	public static void main(String[] args) {
		Scanner reader = new Scanner(System.in);
		int decision;
		
		System.out.println("1 - Create file");
		System.out.println("2 - Read file");
		System.out.println("3 - Exit");
		System.out.println("4 - Test All");
		decision = reader.nextInt();
		
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
			xmlParser.XMLParser.write(nLocations, maxX, maxY, connectionLevel, nFuel, fuelPerKm, fuel, load, start, nPackages, maxVolume, maxValue);
			System.out.println("Finished generating scenario");
		}
		else if (decision == 2){
			System.out.print("Document name: ");
			String file = reader.nextLine(); file = reader.nextLine();
			deliveryInfo = new DeliveryInfo();
			xmlParser.XMLParser.read(file, deliveryInfo);
			Solve alg = new Solve();
			
			System.out.println("Optimize: ");
			System.out.println("1 - Delivery Count");
			System.out.println("2 - Delivery Value");
			int opt = reader.nextInt();
			
			System.out.println("Refuel?");
			System.out.println("1 - Yes");
			System.out.println("2 - No");
			int opt_fuel = reader.nextInt();
			boolean fuel;
			if (opt_fuel == 1) {
				fuel = true;
			}
			else {
				fuel = false;
			}
			
			
			if (opt == 1) {
				alg.run("delivery_count", fuel);
			}
			else if (opt == 2) {
				alg.run("delivery_value", fuel);
			}
			
		}
		else if (decision == 4) {
			lel("limited_fuel", false);
		}
		else if (decision != 3) {
			reader.close();
			return;
		}
		
		
		reader.close();
	}
	
	
	public static void lel(String s, boolean b) {
		for (int i = 3; i <= 10; i++) {
			System.out.println("-------------------------------------------------------------------------------------------");
			System.out.println(i);
			deliveryInfo = new DeliveryInfo();
			if (i == 10) {
				xmlParser.XMLParser.read(s + "/" + i, deliveryInfo);
			}
			else {
				xmlParser.XMLParser.read(s + "/0" + i, deliveryInfo);
			}
			Solve alg = new Solve();
			alg.run("delivery_count", b);
		}
	}
}
