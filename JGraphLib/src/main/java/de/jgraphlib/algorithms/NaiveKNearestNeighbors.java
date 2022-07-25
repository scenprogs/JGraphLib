package de.jgraphlib.algorithms;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.function.Function;

import de.jgraphlib.graph.WeightedGraph;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.util.Tuple;

/* Complexity: O(n) */

public class NaiveKNearestNeighbors<V extends Vertex<?>> {

	protected WeightedGraph<V, ?, ?, ?> graph;
	protected Function<Tuple<V,V>, Double> computeDistance;

	public NaiveKNearestNeighbors(WeightedGraph<V, ?, ?, ?> graph, Function<Tuple<V,V>, Double> computeDistance) {
		this.graph = graph;
		this.computeDistance = computeDistance;
	}

	public List<V> compute(V vertex, int kNeighbors) {
		
		List<V> vertices = graph.getVertices();
		
		vertices.remove(vertex);
		
		LimitedPriorityQueue<Tuple<Double,V>> priorityQueue = 
				new LimitedPriorityQueue<>(kNeighbors, new Comparator<Tuple<Double,V>>() {
					@Override
					public int compare(Tuple<Double,V> first, Tuple<Double,V> second) {
						return Double.compare(second.getFirst(), first.getFirst());
					} });
		
		for(V v : vertices) 	
			priorityQueue.add(new Tuple<Double,V>(							 
					computeDistance.apply(new Tuple<V,V>(v, vertex)), v));
			
		System.out.println(priorityQueue.size());
	
		List<V> kNearestNeighbors = new ArrayList<V>();
			
		while(!priorityQueue.isEmpty())
			kNearestNeighbors.add(priorityQueue.poll().getSecond());
		
		return kNearestNeighbors;		
	}	
	
	class LimitedPriorityQueue<T> extends PriorityQueue<T>{

		private static final long serialVersionUID = 1L;
		
		int maxSize;
		
		public LimitedPriorityQueue(int maxSize, Comparator<T> comparator) {
			super(maxSize, comparator);
			this.maxSize = maxSize;
		}
		
		@Override
		public boolean add(T item) {
			
			super.add(item);
		
			if (this.size() > maxSize)
				this.poll();		
			
			return true;		
		} 		
	}
}
