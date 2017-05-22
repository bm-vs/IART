package algorithms;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.PriorityQueue;

import problemData.Connection;
import problemData.Location;
import problemData.Package;
import userInterface.GraphDisplay;

public class AStar {
	private static HashMap<String, Distance> calculated_distances = new HashMap<String, Distance>();
	
	public static Route run(ArrayList<Location> nodes, ArrayList<Location> fuelNodes, Location start, String opt, GraphDisplay aStarDisplay, boolean fuel) {
		int fuelPerKm = userInterface.UserInterface.deliveryInfo.getTruck().getFuelPerKm();
		int fuelAvailable = userInterface.UserInterface.deliveryInfo.getTruck().getFuel();
		int truckLoad = userInterface.UserInterface.deliveryInfo.getTruck().getLoad();
		ArrayList<Package> packages = userInterface.UserInterface.deliveryInfo.getDeliveries();
		
		for (int i = 0; i < nodes.size(); i++) {
			nodes.get(i).setAStarVars(0, 0, 0, null);
		}
		
		PriorityQueue<Route> open = new PriorityQueue<Route>();
		ArrayList<Route> closed = new ArrayList<Route>();
		
		Route startRoute = new Route(start);
		open.add(startRoute);
		
		Route bestRoute = new Route(start);
		int routeExplored = 0;
		
		while (open.size() != 0) {
			Route q = open.poll();
			routeExplored++;
			
			if (q.getFuel() > fuelAvailable || q.getLoad() > truckLoad) {
				System.out.println("Routes explored: " + routeExplored);
				return bestRoute;
			}
			
			for (Location newNode : nodes) {
				if (q.contains(newNode) && !newNode.equals(start)) {
					continue;
				}
				
				Route successor = new Route(q);
				successor.addLocation(newNode, fuelPerKm, fuel);
				
				double g=0, h=0;
				
				ArrayList<Location> p = new ArrayList<Location>();
				g = Double.max(successor.getDistance()*fuelPerKm/fuelAvailable, successor.getLoad()/truckLoad);
				h = shortestDistance(newNode, start, p)*fuelPerKm/fuelAvailable;
				
				double f = g + h;
				
				if (newNode.equals(start)) {
					if (successor.getFuel() <= fuelAvailable && successor.getLoad() <= truckLoad) {
						if (successor.getNPackages() == packages.size()) {
							System.out.println("Routes explored: " + routeExplored);
							return successor;
						}
						
						if (opt.equals("delivery_count")) {
							if (successor.getNPackages() > bestRoute.getNPackages() || (successor.getNPackages() == bestRoute.getNPackages() && successor.getDistance() < bestRoute.getDistance())) {
								bestRoute = successor;
								aStarDisplay.addPath(bestRoute.getRoute(), start, packages, "astar");
							}
						}
						else if (opt.equals("delivery_value")) {
							if (successor.getValue() > bestRoute.getValue() || (successor.getValue() == bestRoute.getValue() && successor.getDistance() < bestRoute.getDistance())) {
								bestRoute = successor;
								aStarDisplay.addPath(bestRoute.getRoute(), start, packages, "astar");
							}
						}
					}
				}
				else {
					successor.setHeuristic(f);
					open.add(successor);
				}
			}
			
			if (fuel) {
				for (Location newNode : fuelNodes) {
					if (!newNode.equals(q.getLastNode())) {
						Route successor = new Route(q);
						successor.addLocation(newNode, fuelPerKm, fuel);
						
						ArrayList<Location> p = new ArrayList<Location>();
						double g = successor.getDistance()*fuelPerKm/fuelAvailable + successor.getLoad()/truckLoad;
						double h = shortestDistance(newNode, start, p)*fuelPerKm/fuelAvailable;
						
						double f = g + h;
						
						successor.setHeuristic(f);
						open.add(successor);
					}
				}
			}
			
			closed.add(q);
		}
		
		System.out.println("Routes explored: " + routeExplored);
		return bestRoute;
	}	
	
	public static double shortestDistance(Location start, Location goal, ArrayList<Location> path) {		
		if (start.equals(goal)) {
			return 0;
		}
		
		String name1 = start.getID() + "_" + goal.getID();
		String name2 = goal.getID() + "_" + start.getID();
		
		if (calculated_distances.containsKey(name1)) {
			Distance d = calculated_distances.get(name1);
			setPath(path, d, false);
			return d.getWeight();
		}
		
		if (calculated_distances.containsKey(name2)) {
			Distance d = calculated_distances.get(name2);
			setPath(path, d, true);
			return d.getWeight();
		}
		
		userInterface.UserInterface.deliveryInfo.resetAStarVars();
		ArrayList<Location> closed = new ArrayList<Location>();
		PriorityQueue<Location> open = new PriorityQueue<Location>();
		
		open.add(start);
		
		while (open.size() != 0) {
			Location q = open.poll();
			
			for (Connection connection: q.getConnections()) {
				Location successor;
				if (connection.getLocation1() == q) {
					successor = connection.getLocation2();
				}
				else {
					successor = connection.getLocation1();
				}
				
				if (successor.equals(goal)) {
					closed.add(q);
					closed.add(successor);
					successor.setParent(q);
					double weight = q.getG() + successor.linearDistance(q);
					
					setPath(path, start, goal);
					
					Distance d = new Distance(start, goal, path, weight);
					calculated_distances.put(name1, d);
					
					return weight;
				}
				
				double g = q.getG() + successor.linearDistance(q);
				double h = heuristic(successor, goal);
				double f = g + h;
				
				if (inContainer(successor, open)) {
					if (successor.getF() > f) {
						open.remove(successor);
						successor.setAStarVars(g, h, f, q);
						open.add(successor);
					}
				}
				else if (inContainer(successor, closed)) {
					if (successor.getF() > f) {
						closed.remove(successor);
						successor.setAStarVars(g, h, f, q);
						open.add(successor);
					}
				}
				else {
					successor.setAStarVars(g, h, f, q);
					open.add(successor);
				}
			}
			
			closed.add(q);
		}
		
		return Integer.MAX_VALUE;
	}
	
	public static double heuristic(Location l, Location goal) {
		return l.linearDistance(goal);
	}

	public static boolean inContainer(Location successor, AbstractCollection<Location> container) {
		Iterator<Location> it = container.iterator();
		while (it.hasNext()) {
			Location l = it.next();
			if (l.equals(successor)) {
				return true;
			}
		}
		
		return false;
	}
	
	public static void setPath(ArrayList<Location> path, Distance d, boolean reverse) {
		path.clear();
		if (reverse) {
			for (int i = d.getPath().size()-1; i >= 0; i--) {
				path.add(d.getPath().get(i));
			}
		}
		else {
			for (int i = 0; i < d.getPath().size(); i++) {
				path.add(d.getPath().get(i));
			}
		}	
	}
	
	public static void setPath(ArrayList<Location> path, Location start, Location goal) {
		path.clear();
		
		Location l = goal;
		while (l != null) {
			path.add(l);
			l = l.getParent();
		}
		
		Collections.reverse(path);
	}
	
	public static double getTotalWeight(ArrayList<Location> list) {
		double total = 0;
		
		for (int i = 0; i < list.size()-1; i++) {
			ArrayList<Location> path = new ArrayList<Location>();
			total += AStar.shortestDistance(list.get(i), list.get(i+1), path);
		}
		
		return total;
	}
	
	public static void resetCalculatedDistances() {
		calculated_distances = new HashMap<String, Distance>();
	}	
}
