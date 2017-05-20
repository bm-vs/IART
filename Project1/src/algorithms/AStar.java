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
	
	public static Route hamiltonianPathAStar(ArrayList<Location> nodes, Location start, String opt, GraphDisplay aStarDisplay) {
		int fuelPerKm = userInterface.UserInterface.deliveryInfo.getTruck().getFuelPerKm();
		int fuelAvailable = userInterface.UserInterface.deliveryInfo.getTruck().getFuel();
		int truckLoad = userInterface.UserInterface.deliveryInfo.getTruck().getLoad();
		int totalValue = 0;
		int totalLoad = 0;
		ArrayList<Package> packages = userInterface.UserInterface.deliveryInfo.getDeliveries();
		for (Package p : packages) {
			totalValue += p.getValue();
			totalLoad += p.getVolume();
		}
		
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
				successor.addLocation(newNode);
				successor.setFuel(successor.getDistance()*fuelPerKm);
				successor.setLoad();
				
				double g=0, h=0;
				
				if (opt.equals("delivery_count") || opt.equals("full_delivery")) {
					g = successor.getDistance()*fuelPerKm/fuelAvailable + successor.getLoad()/truckLoad;
				}
				else if (opt.equals("delivery_value")) {
					g = successor.getDistance()*fuelPerKm/fuelAvailable + successor.getLoad()/truckLoad + (1-successor.getValue()/totalValue);
				}
				
				h = hamiltonianPathHeuristic(successor, nodes, start)/fuelAvailable + (totalLoad-successor.getLoad())/truckLoad;
				
				double f = g + h;
				
				if (newNode.equals(start) && successor.getFuel() <= fuelAvailable && successor.getLoad() <= truckLoad) {
					if (successor.getRoute().size() == nodes.size()+1) {
						System.out.println("Routes explored: " + routeExplored);
						return successor;
					}
					else if (opt.equals("delivery_count")) {
						if (successor.getRoute().size() > bestRoute.getRoute().size() || (successor.getRoute().size() == bestRoute.getRoute().size() && successor.getDistance() < bestRoute.getDistance())) {
							bestRoute = successor;
							aStarDisplay.addPath(bestRoute.getRoute(), start, packages, "astar");
						}
					}
					else if (opt.equals("delivery_value") || opt.equals("full_delivery")) {
						if (successor.getValue() > bestRoute.getValue() || (successor.getValue() == bestRoute.getValue() && successor.getDistance() < bestRoute.getDistance())) {
							bestRoute = successor;
							aStarDisplay.addPath(bestRoute.getRoute(), start, packages, "astar");
						}
					}
				}
				else {
					successor.setHeuristic(f);
					open.add(successor);
				}
			}
			
			closed.add(q);
		}
		
		return null;
	}	
	
	public static double hamiltonianPathHeuristic(Route route, ArrayList<Location> everyNode, Location start) {		
		ArrayList<Location> remainingNodes = new ArrayList<Location>();
		
		for (Location n : everyNode) {
			if (!route.contains(n)) {
				remainingNodes.add(n);
			}
		}
		remainingNodes.add(start);
		
		return minimumSpanningTree(remainingNodes);
	}
	
	private static double minimumSpanningTree(ArrayList<Location> nodes) {
		if (nodes.size() == 0) {
			return 0;
		}
		
		ArrayList<Location> tree = new ArrayList<Location>();
		tree.add(nodes.get(0));
		
		while (tree.size() != nodes.size()) {
			Location add = null;
			double min = Integer.MAX_VALUE;
			
			for (Location l1 : tree) {
				for (Location l2 : nodes) {
					if (!tree.contains(l2)) {
						ArrayList<Location> p = new ArrayList<Location>();
						double d = shortestDistance(l1, l2, p);
						if (d < min) {
							add = l2;
							min = d;
						}
					}
				}
			}
			
			tree.add(add);
		}
		
		return 0;
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