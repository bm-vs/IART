package UserInterface;
import java.util.Scanner;

import ProblemData.Algorithm;
import ProblemData.DeliveryInfo;
import ProblemData.XMLParser;

public class UserInterface {
	public static DeliveryInfo deliveryInfo;
	public static GraphDisplay gui;
	
	public UserInterface() {
		Scanner reader = new Scanner(System.in);
		int decision;
		
		do {
			System.out.println("1 - Create file");
			System.out.println("2 - Read file");
			System.out.println("3 - Exit");
			decision = reader.nextInt();
			
			if (decision == 1) {
				System.out.println("----Locations----");
				System.out.print("Number of locations: ");
				int nLocations = reader.nextInt();
				System.out.print("Max X: ");
				int maxX = reader.nextInt();
				System.out.print("Max Y: ");
				int maxY = reader.nextInt();
				System.out.print("Aprox. nr. of fuel nodes (%): ");
				int nFuel = reader.nextInt();
				
				System.out.println("----Connections----");
				System.out.print("Number of connections: ");
				int nConnections = reader.nextInt();
				System.out.print("Max. fuel per km: ");
				int maxFuel = reader.nextInt();
				
				System.out.println("----Truck----");
				System.out.print("Fuel: ");
				int fuel = reader.nextInt();
				System.out.print("Load: ");
				int load = reader.nextInt();
				System.out.print("Start node id: ");
				int start = reader.nextInt();
				
				System.out.println("----Packages----");
				System.out.print("Number of packages: ");
				int nPackages = reader.nextInt();
				System.out.print("Max. volume: ");
				int maxVolume = reader.nextInt();
				System.out.print("Max. value: ");
				int maxValue = reader.nextInt();
				
				XMLParser parser = new XMLParser();
				parser.write(nLocations, maxX, maxY, nFuel, nConnections, maxFuel, fuel, load, start, nPackages, maxVolume, maxValue);
			}
			else if (decision == 2){
				System.out.print("Document name: ");
				String file = reader.nextLine(); file = reader.nextLine();
				deliveryInfo = new DeliveryInfo();
				XMLParser parser = new XMLParser();
				parser.read(file, deliveryInfo);
				gui = new GraphDisplay();				
				Algorithm alg = new Algorithm();
				alg.run("max_deliveries");
			}
		} while (decision != 3);
		
		reader.close();
	}
}
