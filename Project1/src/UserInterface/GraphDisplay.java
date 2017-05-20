package userInterface;
import java.util.ArrayList;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

import problemData.Connection;
import problemData.DeliveryInfo;
import problemData.Location;

public class GraphDisplay {
	private Graph graph;
	
	public GraphDisplay() {
		DeliveryInfo info = UserInterface.deliveryInfo;
		
		graph = new SingleGraph("Delivery Info");
		/*==================================*/
		/* Remove for better performance */
		graph.addAttribute("ui.antialias");
		graph.addAttribute("ui.quality");
		/*==================================*/
		graph.addAttribute("ui.stylesheet", "url('file:///../style/style.css')");
		
		// Create nodes
		for (Integer key: info.getLocations().keySet()) {
			graph.addNode(key.toString());
			org.graphstream.graph.Node n = graph.getNode(key.toString());
			n.setAttribute("x", info.getLocation(key).getX());
			n.setAttribute("y", info.getLocation(key).getY());
			n.addAttribute("ui.label", key.toString());
			n.addAttribute("layout.frozen");
		}
		
		// Create edges
		for (Integer key: info.getLocations().keySet()) {
			for (Connection connection: info.getLocation(key).getConnections()) {
				String node1 = ((Integer) connection.getLocation1().getID()).toString();
				String node2 = ((Integer) connection.getLocation2().getID()).toString();
				
				Edge e1 = graph.getEdge(node1 + "_" + node2);
				Edge e2 = graph.getEdge(node2 + "_" + node1);
				if (e1 == null && e2 == null) {
					graph.addEdge(node1 + "_" + node2, node1, node2);
				}
			}
		}
	}
	
	public GraphDisplay(boolean test) {
		graph = new SingleGraph("Delivery Info");
		/*==================================*/
		/* Remove for better performance */
		graph.addAttribute("ui.antialias");
		graph.addAttribute("ui.quality");
		/*==================================*/
		graph.addAttribute("ui.stylesheet", "url('file:///../style/style.css')");
	}
	
	public void addNode(Location l) {
		graph.addNode(((Integer)l.getID()).toString());
		org.graphstream.graph.Node n = graph.getNode(((Integer)l.getID()).toString());
		n.setAttribute("x", l.getX());
		n.setAttribute("y", l.getY());
		n.addAttribute("ui.label", ((Integer)l.getID()).toString());
		n.addAttribute("layout.frozen");
	}	
	
	public void addEdge(Connection c) {
		String node1 = ((Integer) c.getLocation1().getID()).toString();
		String node2 = ((Integer) c.getLocation2().getID()).toString();
		
		Edge e1 = graph.getEdge(node1 + "_" + node2);
		Edge e2 = graph.getEdge(node2 + "_" + node1);
		if (e1 == null && e2 == null) {
			graph.addEdge(node1 + "_" + node2, node1, node2);
		}
	}
	
	// type - "astar"/"genetic"
	public void addPath(ArrayList<Location> route, Location startLocation, ArrayList<problemData.Package> packages, String type) {
		resetNodeColors();
		resetEdgeColors();
		
		for (int i = 0; i < route.size()-1; i++) {
			ArrayList<Location> path = new ArrayList<Location>();
			algorithms.AStar.shortestDistance(route.get(i), route.get(i+1), path);
			
			for (int j = 0; j < path.size()-1; j++) {
				setNodeVisited(path.get(j), type);
				setEdgeVisited(path.get(j), path.get(j+1), type);
			}
		}
		
		setNodeStart(startLocation);
		for (problemData.Package p : packages) {
			setNodeDelivery(p.getLocation());
		}
	}
	
	public void resetNodeColors() {
		for (org.graphstream.graph.Node n : graph.getNodeSet()) {
			n.addAttribute("ui.class", "");
		}
	}
	
	public void resetEdgeColors() {
		for (Edge e : graph.getEdgeSet()) {
			e.addAttribute("ui.class", "");
		}
	}
	
	// type - "astar"/"genetic"
	public void setNodeVisited(Location location, String type) {
		org.graphstream.graph.Node n = graph.getNode(location.getID());
		if (n != null) {
			n.addAttribute("ui.class", type);
		}
	}
	
	public void setNodeStart(Location location) {
		org.graphstream.graph.Node n = graph.getNode(location.getID());
		if (n != null) {
			n.addAttribute("ui.class", "start");
		}
	}
	
	public void setNodeDelivery(Location location) {
		org.graphstream.graph.Node n = graph.getNode(location.getID());
		if (n != null) {
			n.addAttribute("ui.class", "delivery");
		}
	}
	
	// type - "astar"/"genetic"
	public void setEdgeVisited(Location location, Location nextLocation, String type) {
		Edge e1 = graph.getEdge(((Integer) location.getID()).toString() + "_" + ((Integer) nextLocation.getID()).toString());
		Edge e2 = graph.getEdge(((Integer) nextLocation.getID()).toString() + "_" + ((Integer) location.getID()).toString());		
		
		if (e1 != null) {
			e1.addAttribute("ui.class", type);
		}
		else if (e2 != null) {
			e2.addAttribute("ui.class", type);
		}
	}
	
	public void display() {
		graph.display();
	}
}