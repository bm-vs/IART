import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

public class GUI {
	private DeliveryInfo info;
	
	public GUI(DeliveryInfo info) {
		this.info = info;
	}
	
	public void display() {
		Graph graph = new SingleGraph("Delivery Info");
		
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
		
		graph.display();
	}
}
