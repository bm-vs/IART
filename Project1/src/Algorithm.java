import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.PriorityQueue;

public class Algorithm {
	private DeliveryInfo deliveryInfo;
	private GraphDisplay gui;
	
	public Algorithm(DeliveryInfo deliveryInfo, GraphDisplay gui) {
		this.deliveryInfo = deliveryInfo;
		this.gui = gui;
		this.gui.display();
	}
	
	public void run(String opt) {
		if (opt.equals("max_deliveries")) {
			ArrayList<Location> closed = new ArrayList<Location>();
			Location startLocation = deliveryInfo.getTruck().getLocation();
			
			aStar(startLocation, deliveryInfo.getLocation(5), closed);
			
			System.out.println(closed);
		}
	}
	
	public double aStar(Location start, Location goal, ArrayList<Location> closed) {
		PriorityQueue<Location> open = new PriorityQueue<Location>();
		
		open.add(start);
		
		while (open.size() != 0) {
			Location q = open.poll();
			if (q.getParent() != null) {
				gui.setEdgeVisited(q, q.getParent());
			}
			gui.setNodeVisited(q);
			
			for (Location successor: q.getConnections().keySet()) {
				if (successor.equals(goal)) {
					closed.add(q);
					closed.add(successor);
					gui.setEdgeVisited(successor, q);
					gui.setNodeVisited(successor);
					return q.getG() + successor.distance(q);
				}
				
				double g = q.getG() + successor.distance(q);
				double h = heuristic(successor, goal);
				double f = g + h;
				
				if (inContainer(successor, open)) {
					if (successor.getF() > f) {
						open.remove(successor);
						successor.setAlgVars(g, h, f, q);
						open.add(successor);
					}
				}
				else if (inContainer(successor, closed)) {
					if (successor.getF() > f) {
						closed.remove(successor);
						successor.setAlgVars(g, h, f, q);
						open.add(successor);
					}
				}
				else {
					successor.setAlgVars(g, h, f, q);
					open.add(successor);
				}
			}
			
			closed.add(q);
		}
		
		return Integer.MAX_VALUE;
	}
	
	public double heuristic(Location l, Location goal) {
		return l.distance(goal);
	}
	
	public boolean inContainer(Location successor, AbstractCollection<Location> container) {
		Iterator<Location> it = container.iterator();
		while (it.hasNext()) {
			Location l = it.next();
			if (l.equals(successor)) {
				return true;
			}
		}
		
		return false;
	}
}
