package problemData;
import java.util.ArrayList;
import java.util.LinkedHashSet;

import algorithms.AStar;
import algorithms.BreadthFirst;
import algorithms.Route;
import userInterface.GraphDisplay;

public class Solve {	
	public Solve() {}
	
	public void run(String opt) {
		ArrayList<Location> nodes = new ArrayList<Location>();
		ArrayList<Package> packages = userInterface.UserInterface.deliveryInfo.getDeliveries();
		for (int i = 0; i < packages.size(); i++) {
			nodes.add(packages.get(i).getLocation());
		}
		
		Location startLocation = userInterface.UserInterface.deliveryInfo.getTruck().getLocation();
		nodes.add(startLocation);
		nodes = new ArrayList<Location>(new LinkedHashSet<Location>(nodes));
		
		
		long tstart = System.currentTimeMillis();
		aStar(startLocation, packages, opt);
		System.out.println("Time elapsed: " + (System.currentTimeMillis()-tstart)/1000.0);
		
		System.out.println();
		AStar.resetCalculatedDistances();
		
		tstart = System.currentTimeMillis();
		bfs(startLocation, packages, opt);
		System.out.println("Time elapsed: " + (System.currentTimeMillis()-tstart)/1000.0);
	}
	
	private void aStar(Location startLocation, ArrayList<Package> packages, String opt) {
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
		
		Route aStarRoute;
		if (opt.equals("delivery_count")) {
			aStarRoute = AStar.hamiltonianPathAStar(importantNodes, startLocation, opt, aStarDisplay);
		}
		else {
			aStarRoute = AStar.hamiltonianPathAStar(importantNodes, startLocation, "full_delivery", aStarDisplay);
			if (aStarRoute.getRoute().size() != importantNodes.size()+1) {
				aStarRoute = AStar.hamiltonianPathAStar(importantNodes, startLocation, opt, aStarDisplay);
			}
		}
		
		System.out.println("Best Route: " + aStarRoute);
		System.out.println("Fuel used: " + aStarRoute.getFuel());
		System.out.println("Load used: " + aStarRoute.getLoad());
		
		if (opt.equals("delivery_count")) {
			System.out.println("Number packages: " + (aStarRoute.getRoute().size()-2));
		}
		else if (opt.equals("delivery_value")) {
			System.out.println("Value delivered: " + aStarRoute.getValue());
		}	
		
		aStarDisplay.addPath(aStarRoute.getRoute(), startLocation, packages, "astar");
	}
	
	private void bfs(Location startLocation, ArrayList<Package> packages, String opt) {
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
		
		Route bfsRoute;
		bfsRoute = BreadthFirst.run(importantNodes, startLocation, opt, bfsDisplay);
		
		System.out.println("Best Route: " + bfsRoute);
		System.out.println("Fuel used: " + bfsRoute.getFuel());
		System.out.println("Load used: " + bfsRoute.getLoad());
		
		if (opt.equals("delivery_count")) {
			System.out.println("Number packages: " + (bfsRoute.getRoute().size()-2));
		}
		else if (opt.equals("delivery_value")) {
			System.out.println("Value delivered: " + bfsRoute.getValue());
		}	
		
		bfsDisplay.addPath(bfsRoute.getRoute(), startLocation, packages, "bfs");
	}
}
