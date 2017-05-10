package GeneticAlgorithm;
import java.util.ArrayList;
import java.util.Random;

import ProblemData.Location;

public class GeneticAlgorithm {
	
	/* Genetic algorithm parameters */
	private static final int mutationUnit = 10000;
	private static final int mutationRate = 15;
	private static final int parentSelectionSize = 5;
	private static boolean elitism = true;
	
	public static Route run(ArrayList<Location> nodes, Location start, int number_generations, int population_size) {
		Population population = new Population(population_size, nodes, start);
		population.initialize();
		for (int i = 0; i < number_generations; i++) {
			population = evolvePopulation(population, nodes, start);
		}
		
		return population.getFittest();
	}
	
	public static Population evolvePopulation(Population population, ArrayList<Location> nodes, Location start) {
		Population newPopulation = new Population(population.getSize(), nodes, start);
		
		// elitism goes here
		int elitismOffset = 0;
		if (elitism) {
			newPopulation.saveRoute(0, population.getFittest());
			elitismOffset = 1;
		}
		
		
		for (int i = elitismOffset; i < newPopulation.getSize(); i++) {
			Route parent1 = parentSelection(population, nodes, start);
			Route parent2 = parentSelection(population, nodes, start);			
			Route child = crossover(parent1, parent2);
			newPopulation.saveRoute(i, child);
		}
		
		for (int i = elitismOffset; i < newPopulation.getSize(); i++) {
			mutate(newPopulation.getRoute(i));
		}
		
		return population;
	}
	
	public static Route parentSelection(Population population, ArrayList<Location> nodes, Location start) {
		Population selection = new Population(parentSelectionSize, nodes, start);
		
		for (int i = 0; i < parentSelectionSize; i++) {
			Random rand = new Random();
			selection.saveRoute(i, population.getRoute(rand.nextInt(population.getSize())));
		}
		
		return selection.getFittest();
	}
	
	public static Route crossover(Route parent1, Route parent2) {
		int n_nodes = parent1.getSize();
		
		Route child = new Route(n_nodes);
		
		Random rand = new Random();
		int startPos = rand.nextInt(n_nodes-1) + 1;
		int endPos = rand.nextInt(n_nodes-1) + 1;
		if (endPos < startPos) {
			int tmp = startPos;
			startPos = endPos;
			endPos = tmp;
		}
		
		for (int i = 0; i < n_nodes; i++) {
			if (startPos < endPos && i > startPos && i < endPos) {
				child.setLocation(i, parent1.getLocation(i));
			}
		}
		
		for (int i = 0; i < n_nodes; i++) {
			if (!child.contains(parent2.getLocation(i))) {
				for (int j = 0; j < child.getSize(); j++) {
					if (child.getLocation(j) == null) {
						child.setLocation(j, parent2.getLocation(i));
						break;
					}
				}
			}
		}
		
		return child;
	}
	
	private static void mutate(Route route) {
		Random rand = new Random();
		for (int i = 1; i < route.getSize(); i++) {
			if (rand.nextInt(mutationUnit) < mutationRate) {
				int j = rand.nextInt(route.getSize()-1) + 1;
				
				Location l1 = route.getLocation(i);
				Location l2 = route.getLocation(j);
				
				route.setLocation(j, l1);
				route.setLocation(i, l2);
			}
		}
	}
}































