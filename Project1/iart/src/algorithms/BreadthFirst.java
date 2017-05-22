package algorithms;

import java.util.ArrayList;

import problemData.Location;
import problemData.Package;
import userInterface.GraphDisplay;

public class BreadthFirst {
	public static Route run(ArrayList<Location> nodes, ArrayList<Location> fuelNodes, Location start, String opt, GraphDisplay aStarDisplay, boolean fuel) {
		int fuelPerKm = userInterface.UserInterface.deliveryInfo.getTruck().getFuelPerKm();
		int fuelAvailable = userInterface.UserInterface.deliveryInfo.getTruck().getFuel();
		int truckLoad = userInterface.UserInterface.deliveryInfo.getTruck().getLoad();
		ArrayList<Package> packages = userInterface.UserInterface.deliveryInfo.getDeliveries();
		
		ArrayList<Route> level = new ArrayList<Route>();
		ArrayList<Route> nextLevel = new ArrayList<Route>();
		
		Route bestRoute = new Route(start);
		nextLevel.add(bestRoute);
		int routeExplored = 0;
		
		while(!nextLevel.isEmpty()) {
			level = new ArrayList<Route>(nextLevel);
			nextLevel = new ArrayList<Route>();
			
			for (Route q : level) {
				routeExplored++;
				if (q.getFuel() > fuelAvailable || q.getLoad() > truckLoad) {
					continue;
				}
				
				for (Location newNode : nodes) {
					if (q.contains(newNode) && !newNode.equals(start)) {
						continue;
					}
					
					Route successor = new Route(q);
					successor.addLocation(newNode, fuelPerKm, fuel);
				
					if (newNode.equals(start)) {
						if (successor.getFuel() <= fuelAvailable && successor.getLoad() <= truckLoad) {
							if (opt.equals("delivery_count")) {
								if (successor.getNPackages() > bestRoute.getNPackages() || (successor.getNPackages() == bestRoute.getNPackages() && successor.getDistance() < bestRoute.getDistance())) {
									bestRoute = successor;
									aStarDisplay.addPath(bestRoute.getRoute(), start, packages, "bfs");
								}
							}
							else if (opt.equals("delivery_value")) {
								if (successor.getValue() > bestRoute.getValue() || (successor.getValue() == bestRoute.getValue() && successor.getDistance() < bestRoute.getDistance())) {
									bestRoute = successor;
									aStarDisplay.addPath(bestRoute.getRoute(), start, packages, "bfs");
								}
							}
						}
					}
					else {
						nextLevel.add(successor);
					}
				}
				
				if (fuel) {
					for (Location newNode : fuelNodes) {
						if (!newNode.equals(q.getLastNode())) {
							Route successor = new Route(q);
							successor.addLocation(newNode, fuelPerKm, fuel);
							nextLevel.add(successor);
						}
					}
				}
			}
		}
		
		System.out.println("Routes explored: " + routeExplored);
		return bestRoute;
	}
}
