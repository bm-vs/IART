package UserInterface;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

import ProblemData.Connection;
import ProblemData.DeliveryInfo;
import ProblemData.Location;

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
		graph.addAttribute("ui.stylesheet", "url('file:///../style/style2.css')");
		
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
				
				Edge e1 = graph.getEdge(node1+node2);
				Edge e2 = graph.getEdge(node2+node1);
				if (e1 == null && e2 == null) {
					graph.addEdge(node1+node2, node1, node2);
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
		
		Edge e1 = graph.getEdge(node1+node2);
		Edge e2 = graph.getEdge(node2+node1);
		if (e1 == null && e2 == null) {
			graph.addEdge(node1+node2, node1, node2);
		}
	}
	
	public void setNodeVisited(Location location) {
		org.graphstream.graph.Node n = graph.getNode(location.getID());
		if (n != null) {
			n.addAttribute("ui.class", "visited");
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
	
	public void setEdgeVisited(Location location, Location nextLocation) {
		Edge e1 = graph.getEdge(((Integer) location.getID()).toString() + ((Integer) nextLocation.getID()).toString());
		Edge e2 = graph.getEdge(((Integer) nextLocation.getID()).toString() + ((Integer) location.getID()).toString());		

		if (e1 != null) {
			e1.addAttribute("ui.class", "visited");
		}
		else if (e2 != null) {
			e2.addAttribute("ui.class", "visited");
		}
	}
	
	public void display() {
		graph.display();
	}
}