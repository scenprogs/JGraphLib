package de.jgraphlib.algorithms;

import java.util.ArrayList;
import java.util.List;

import de.jgraphlib.graph.WeightedGraph;
import de.jgraphlib.graph.elements.EdgeWeight;
import de.jgraphlib.graph.elements.Path;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.util.IntRange;
import de.jgraphlib.util.RandomNumbers;
import de.jgraphlib.util.Tuple;

public class RandomPath<V extends Vertex<?>, E extends WeightedEdge<W>, W extends EdgeWeight> {

	private WeightedGraph<V, ?, E, W> graph;
	private RandomNumbers randomNumbers;
	
	public RandomPath(WeightedGraph<V, ?, E, W> graph) {
		this.graph = graph;
		this.randomNumbers = new RandomNumbers();
	}
	
	public RandomPath(WeightedGraph<V, ?, E, W> graph, RandomNumbers randomNumbers) {
		this.graph = graph;
		this.randomNumbers = randomNumbers;
	}

	public Path<V, E, W> compute(V source, V target) {

		Path<V, E, W> randomPath = new Path<V, E, W>(source, target);

		while (!randomPath.isComplete()) {

			List<V> nextHops = randomPath.getUnvisitedVerticesOf(graph.getNextHopsOf(randomPath.getLastVertex()));

			if (!nextHops.isEmpty()) {
				V nextHop = nextHops.get(randomNumbers.getRandom(0, nextHops.size()));
				randomPath.add(new Tuple<E, V>(graph.getEdge(randomPath.getLastVertex(), nextHop), nextHop));
			} else
				return randomPath;
		}
				
		return randomPath;
	}

	public Path<V, E, W> compute(V source, int hops) {

		Path<V, E, W> randomPath = new Path<V, E, W>(source, null);

		for (int i = 0; i < hops; i++) {

			List<V> nextHops = randomPath.getUnvisitedVerticesOf(graph.getNextHopsOf(randomPath.getLastVertex()));

			if (!nextHops.isEmpty()) {
				V nextHop = nextHops.get(randomNumbers.getRandom(0, nextHops.size()-1));
				randomPath.add(new Tuple<E, V>(graph.getEdge(randomPath.getLastVertex(), nextHop), nextHop));
			} else
				return randomPath;
		}
		
		randomPath.setTarget(randomPath.getLastVertex());
				
		return randomPath;
	}
	
	public List<Path<V,E,W>> compute(IntRange numberOfPaths, IntRange pathLength, int attempts){
		
		List<Path<V,E,W>> paths = new ArrayList<Path<V,E,W>>();
						
		int n = randomNumbers.getRandom(numberOfPaths.getMin(), numberOfPaths.getMax());
			
		while(paths.size() < n && attempts > 0) {
		
			Path<V, E, W> randomPath = compute(
					graph.getRandomVertex(), 
					randomNumbers.getRandom(pathLength.getMin(), pathLength.getMax()));
			
			if(randomPath.isComplete())
				paths.add(randomPath);
			else
				attempts--;
		}
					
		return paths;
	}
}
