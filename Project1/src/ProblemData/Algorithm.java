package ProblemData;
import java.util.ArrayList;
import java.util.LinkedHashSet;

import GeneticAlgorithm.Route;

public class Algorithm {	
	public Algorithm() {
		UserInterface.UserInterface.gui.display();
	}
	
	public void run(String opt) {
		if (opt.equals("max_deliveries")) {
			ArrayList<Location> nodes = new ArrayList<Location>();
			ArrayList<Package> packages = UserInterface.UserInterface.deliveryInfo.getDeliveries();
			for (int i = 0; i < packages.size(); i++) {
				nodes.add(packages.get(i).getLocation());
			}
			
			Location startLocation = UserInterface.UserInterface.deliveryInfo.getTruck().getLocation();
			nodes.add(startLocation);
			nodes = new ArrayList<Location>(new LinkedHashSet<Location>(nodes));
			
			Route route = GeneticAlgorithm.GeneticAlgorithm.run(nodes, 100, 50);
			System.out.println("============================");
			System.out.println(route.getDistance());
			System.out.println(route.getRoute());
			
			for (int i = 0; i < route.getPath().size(); i++) {
				ArrayList<Location> path = route.getPath().get(i);
				for (int j = 0; j < path.size(); j++) {
					System.out.print(" " + path.get(j) + "->");
					if (path.get(j).equals(startLocation)) {
						UserInterface.UserInterface.gui.setNodeStart(path.get(j));
					}
					else if (packages.contains(path.get(j))) {
						
					}
					else {
						UserInterface.UserInterface.gui.setNodeVisited(path.get(j));
						
						for (int k = 0; k < packages.size(); k++) {
							if (packages.get(k).getLocation().equals(path.get(j))) {
								UserInterface.UserInterface.gui.setNodeDelivery(path.get(j));
							}
						}
					}
					UserInterface.UserInterface.gui.setEdgeVisited(path.get(j), path.get((j+1)%path.size()));
				}
				
				System.out.println();
			}
		}
	}		
}
