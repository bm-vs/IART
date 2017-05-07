package GeneticAlgorithm;

import java.util.ArrayList;
import java.util.Collections;

import ProblemData.Location;

public class Population {
	private ArrayList<Route> routes;
	private ArrayList<Location> nodes;
	private int size;
	
	public Population(int population_size, ArrayList<Location> nodes) {
		routes = new ArrayList<Route>(Collections.nCopies(population_size, null));
		this.nodes = nodes;
		this.size = population_size;
	}
	
	public void initialize() {
		for (int i = 0; i < size; i++) {
			Route route = new Route(nodes);
			routes.set(i, route);
		}
	}
	
	public int getSize() {
		return size;
	}
	
	public void saveRoute(int position, Route route) {
		routes.set(position, route);
	}
	
	public Route getRoute(int i) {
		return routes.get(i);
	}
	
	public Route getFittest() {
		Route fittest = routes.get(0);
		
		for (int i = 1; i < routes.size(); i++) {
			if (fittest.getFitness() <= routes.get(i).getFitness()) {
				fittest = routes.get(i);
			}
		}
		
		return fittest;
	}
	
	public String toString() {
		return routes.toString();
	}
}
