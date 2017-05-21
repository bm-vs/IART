package problemData;
import java.util.ArrayList;
import java.util.LinkedHashSet;

import algorithms.AStar;
import algorithms.BreadthFirst;
import algorithms.Route;
import userInterface.GraphDisplay;

public class Solve {	
	public Solve() {}
	
	public void run(String opt, boolean fuel) {
		ArrayList<Location> nodes = new ArrayList<Location>();
		ArrayList<Package> packages = userInterface.UserInterface.deliveryInfo.getDeliveries();
		for (int i = 0; i < packages.size(); i++) {
			nodes.add(packages.get(i).getLocation());
		}
		
		Location startLocation = userInterface.UserInterface.deliveryInfo.getTruck().getLocation();
		nodes.add(startLocation);
		nodes = new ArrayList<Location>(new LinkedHashSet<Location>(nodes));
		
		AStar.resetCalculatedDistances();
		aStar(startLocation, packages, opt, fuel);		
		System.out.println();
		AStar.resetCalculatedDistances();
		bfs(startLocation, packages, opt, fuel);
	}
	
	private void aStar(Location startLocation, ArrayList<Package> packages, String opt, boolean fuel) {
		GraphDisplay aStarDisplay = new GraphDisplay();
		aStarDisplay.display();
		
		System.out.println("======================================================================");
		System.out.println("A*");
		
		ArrayList<Location> importantNodes = new ArrayList<Location>();
		for (Package p : userInterface.UserInterface.deliveryInfo.getDeliveries()) {
			importantNodes.add(p.getLocation());
		}
		
		importantNodes.add(startLocation);
		importantNodes = new ArrayList<Location>(new LinkedHashSet<Location>(importantNodes));
		
		for (Location node : importantNodes) {
			aStarDisplay.setNodeDelivery(node);
		}
		aStarDisplay.setNodeStart(startLocation);
		
		ArrayList<Location> fuelNodes = new ArrayList<Location>();
		for (Location l : userInterface.UserInterface.deliveryInfo.getLocations().values()) {
			if (l.getFuel()) {
				fuelNodes.add(l);
			}
		}
		
		Route aStarRoute;
		long tstart = System.currentTimeMillis();
		aStarRoute = AStar.run(importantNodes, fuelNodes, startLocation, opt, aStarDisplay, fuel);
		System.out.println("Time elapsed: " + (System.currentTimeMillis()-tstart)/1000.0);
		
		System.out.println("Best Route: " + aStarRoute);
		System.out.println("Distance: " + aStarRoute.getDistance());
		System.out.println("Load used: " + aStarRoute.getLoad());
		
		if (opt.equals("delivery_count")) {
			System.out.println("Number packages: " + aStarRoute.getNPackages());
		}
		else if (opt.equals("delivery_value")) {
			System.out.println("Value delivered: " + aStarRoute.getValue());
		}	
		
		aStarDisplay.addPath(aStarRoute.getRoute(), startLocation, packages, "astar");
	}
	
	private void bfs(Location startLocation, ArrayList<Package> packages, String opt, boolean fuel) {
		GraphDisplay bfsDisplay = new GraphDisplay();
		bfsDisplay.display();
		
		System.out.println("======================================================================");
		System.out.println("BFS");
		
		ArrayList<Location> importantNodes = new ArrayList<Location>();
		for (Package p : userInterface.UserInterface.deliveryInfo.getDeliveries()) {
			importantNodes.add(p.getLocation());
		}
		
		importantNodes.add(startLocation);
		importantNodes = new ArrayList<Location>(new LinkedHashSet<Location>(importantNodes));
		
		for (Location node : importantNodes) {
			bfsDisplay.setNodeDelivery(node);
		}
		bfsDisplay.setNodeStart(startLocation);
		
		ArrayList<Location> fuelNodes = new ArrayList<Location>();
		for (Location l : userInterface.UserInterface.deliveryInfo.getLocations().values()) {
			if (l.getFuel()) {
				fuelNodes.add(l);
			}
		}
		
		Route bfsRoute;
		long tstart = System.currentTimeMillis();
		bfsRoute = BreadthFirst.run(importantNodes, fuelNodes, startLocation, opt, bfsDisplay, fuel);
		System.out.println("Time elapsed: " + (System.currentTimeMillis()-tstart)/1000.0);
		
		System.out.println("Best Route: " + bfsRoute);
		System.out.println("Distance: " + bfsRoute.getDistance());
		System.out.println("Load used: " + bfsRoute.getLoad());
		
		if (opt.equals("delivery_count")) {
			System.out.println("Number packages: " + bfsRoute.getNPackages());
		}
		else if (opt.equals("delivery_value")) {
			System.out.println("Value delivered: " + bfsRoute.getValue());
		}	
		
		bfsDisplay.addPath(bfsRoute.getRoute(), startLocation, packages, "bfs");
	}
}
