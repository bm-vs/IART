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
		decision = reader.nextInt();
		
		if (decision == 1) {
			/*
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
			System.out.print("Connection level: ");
			int connectionLevel = reader.nextInt();
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
			*/
			//XMLParser.XMLParser.write(nLocations, maxX, maxY, nFuel, maxFuel, fuel, load, start, nPackages, maxVolume, maxValue);
			XMLParser.XMLParser.write(100, 10, 10, 3, 10, 10, 10, 10, 10, 10, 10, 10);
			System.out.println("Finished generating scenario");
		}
		else if (decision == 2){
			System.out.print("Document name: ");
			String file = reader.nextLine(); file = reader.nextLine();
			deliveryInfo = new DeliveryInfo();
			XMLParser.XMLParser.read(file, deliveryInfo);
			/*
			for (Integer i : deliveryInfo.getLocations().keySet()) {
				Location l = deliveryInfo.getLocation(i);
				System.out.println(l.getID());
				System.out.println(l.getConnections());
			}
			*/
			
			gui = new GraphDisplay();				
			Algorithm alg = new Algorithm();
			alg.run("max_deliveries");
			
			//ArrayList<Location> closed = new ArrayList<Location>();
			//double total = AStar.AStar.run(deliveryInfo.getLocation(10), deliveryInfo.getLocation(34), closed) + AStar.AStar.run(deliveryInfo.getLocation(34), deliveryInfo.getLocation(32), closed) + AStar.AStar.run(deliveryInfo.getLocation(32), deliveryInfo.getLocation(3), closed) + AStar.AStar.run(deliveryInfo.getLocation(3), deliveryInfo.getLocation(62), closed);
			//double total = AStar.AStar.run(deliveryInfo.getLocation(10), deliveryInfo.getLocation(62), closed);
			
			//System.out.println(total*2);
		}
		else if (decision != 3) {
			reader.close();
			return;
		}
		
		reader.close();
	}
}
