package UserInterface;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

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
			for (Location location: info.getLocation(key).getConnections().keySet()) {
				String node1 = ((Integer) info.getLocation(key).getID()).toString();
				String node2 = ((Integer) location.getID()).toString();
				
				Edge e1 = graph.getEdge(node1+node2);
				Edge e2 = graph.getEdge(node2+node1);
				if (e1 == null && e2 == null) {
					graph.addEdge(node1+node2, node1, node2);
				}
			}
		}
	}
	
	public void setNodeVisited(Location location) {
		org.graphstream.graph.Node n = graph.getNode(location.getID());
		if (n != null) {
			n.addAttribute("ui.class", "visited");
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