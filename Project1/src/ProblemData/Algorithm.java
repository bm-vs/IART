package ProblemData;
import java.util.ArrayList;
import java.util.LinkedHashSet;

import AStar.AStar;
import AStar.AStarRoute;
//import GeneticAlgorithm.Route;

public class Algorithm {	
	public Algorithm() {
		UserInterface.UserInterface.gui.display();
	}
	
	public void run(String opt) {
		ArrayList<Location> nodes = new ArrayList<Location>();
		ArrayList<Package> packages = UserInterface.UserInterface.deliveryInfo.getDeliveries();
		for (int i = 0; i < packages.size(); i++) {
			nodes.add(packages.get(i).getLocation());
		}
		
		Location startLocation = UserInterface.UserInterface.deliveryInfo.getTruck().getLocation();
		nodes.add(startLocation);
		nodes = new ArrayList<Location>(new LinkedHashSet<Location>(nodes));
		/*
		Route route = GeneticAlgorithm.GeneticAlgorithm.run(nodes, startLocation, 10000, 50);
		System.out.println();
		System.out.println("============================");
		System.out.println("Genetic");
		ArrayList<Location> fullRoute = new ArrayList<Location>(route.getRoute());
		fullRoute.add(startLocation);
		System.out.println(fullRoute);
		System.out.println(route.getDistance());
		UserInterface.UserInterface.gui.addPath(fullRoute, startLocation, packages, "genetic");
		*/
		
		System.out.println("============================");
		System.out.println("A*");
		
		ArrayList<Location> importantNodes = new ArrayList<Location>();
		for (Package p : UserInterface.UserInterface.deliveryInfo.getDeliveries()) {
			importantNodes.add(p.getLocation());
		}
		
		importantNodes.add(startLocation);
		importantNodes = new ArrayList<Location>(new LinkedHashSet<Location>(importantNodes));
		
		UserInterface.UserInterface.gui.setNodeStart(startLocation);
		for (Location node : importantNodes) {
			UserInterface.UserInterface.gui.setNodeDelivery(node);
		}
		
		AStarRoute aStarRoute = AStar.hamiltonianPathAStar(importantNodes, startLocation, opt);
		System.out.println(aStarRoute);
		System.out.println(aStarRoute.getDistance());
		UserInterface.UserInterface.gui.addPath(aStarRoute.getRoute(), startLocation, packages, "astar");
	}		
}
