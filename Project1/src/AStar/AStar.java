package AStar;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.PriorityQueue;

import ProblemData.Location;
import ProblemData.Connection;

public class AStar {
	private static HashMap<String, Distance> calculated_distances = new HashMap<String, Distance>();
	
	public static double circuitAstar(ArrayList<Location> nodes, Location start, ArrayList<Location> closed) {
		for (int i = 0; i < nodes.size(); i++) {
			nodes.get(i).setAStarVars(0, 0, 0, null);
		}
		
		PriorityQueue<Location> open = new PriorityQueue<Location>();
		
		open.add(start);
		
		while (open.size() != 0) {
			Location q = open.poll();
			
			for (Location newNode : nodes) {
				Location successor;
				if (newNode.equals(q)) {
					continue;
				}
				
				successor = newNode;
				
				if (successor.equals(start) && closed.size() == nodes.size()-1) {
					closed.add(q);
					closed.add(successor);
					successor.setParent(q);
					
					return getTotalWeight(closed);
				}
				
				ArrayList<Location> partial_path = new ArrayList<Location>();
				double g = q.getG() + run(q, successor, partial_path);
				double h = circuitHeuristic(nodes, successor, q);
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
	
	public static double run(Location start, Location goal, ArrayList<Location> path) {
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
		
		UserInterface.UserInterface.deliveryInfo.resetAStarVars();
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
	
	public static double circuitHeuristic(ArrayList<Location> nodes, Location l, Location q) {
		double min = Integer.MAX_VALUE;
		
		for (Location n : nodes) {
			if (n.linearDistance(l) < min && !n.equals(l) && !n.equals(q)) {
				ArrayList<Location> a = new ArrayList<Location>();
				min = run(l, n, a);//(n.linearDistance(l)+n.linearDistance(q))/2;
			}
			
		}
		
		return min;
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
			total += AStar.run(list.get(i), list.get(i+1), path);
		}
		
		return total;
	}
	
}
