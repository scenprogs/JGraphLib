package de.jgraphlib.algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.function.Function;

import de.jgraphlib.graph.WeightedGraph;
import de.jgraphlib.graph.elements.EdgeWeight;
import de.jgraphlib.graph.elements.Path;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.util.Tuple;

/**
 * The Class AStarShortestPath.
 *
 * @param <V> the vertex type
 * @param <E> the edge type
 * @param <W> the edgeWeight type
 * @param <P> the path type
 */
public class AStarShortestPath<V extends Vertex<?>, E extends WeightedEdge<W>, W extends EdgeWeight> {

	/** The graph. */
	protected WeightedGraph<V, ?, E, W> graph;
	
	/** Function that predicts/estimates the cost between a node of the graph and the target node. */
	protected Function<Tuple<V,V>, Double> estimateCosts;
	
	/** Function that computes the costs between two adjacent nodes. */
	protected Function<Tuple<V,V>, Double> computeCosts;

	/** The closed list. */
	private Set<V> closedList = new HashSet<>();	
	
	/** The open list. */
	private Set<V> openList = new HashSet<>();					
	
	/** Nodes predecessors. */
	private Map<V, V> predecessors = new HashMap<>();
	
	/** The costs from the source node towards a node of the graph. */
	private Map<V, Double> fromSourceCosts = new HashMap<>();		
	
	/** The estimated costs to reach the target, sorted within a PriorityQueue to allow fast access to promising nodes. */
	private PriorityQueue<Entry<Double,V>> estimatedCostsToTarget = new PriorityQueue<>();
	
	/**
	 * Instantiates a new a star shortest path.
	 *
	 * @param graph the graph
	 * @param computeCosts the function that computes the costs of two adjacent nodes
	 * @param estimateCosts the function that predicts/estimates the costs of any node of the graph and the target node
	 */
	public AStarShortestPath(WeightedGraph<V, ?, E, W> graph, Function<Tuple<V,V>, Double> computeCosts, Function<Tuple<V,V>, Double> estimateCosts) {
		this.graph = graph;
		this.computeCosts = computeCosts;
		this.estimateCosts = estimateCosts;
	}
		
	/**
	 * Computes the shortest path in a graph between a given source and target node.
	 * Target node must be reachable from source node.
	 *
	 * @param source the source node
	 * @param target the target node
	 * @return P a path
	 */
	public Path<V,E,W> compute(V source, V target) {
					
		openList.add(source);
		predecessors.put(source, source); 
		fromSourceCosts.put(source, 0d); 
		estimatedCostsToTarget.add(
				new Entry<Double, V>(
						estimateCosts.apply(new Tuple<>(source, target)), 
						source));						
		
		V current = source;
		
		while(!openList.isEmpty()) {
			
			current = estimatedCostsToTarget.poll().getValue();
			
			if(current == target) 
				return graph.buildPath(buildPath(source, target));
			
			closedList.add(current);
			openList.remove(current);
									
			for(V successor : graph.getNextHopsOf(current)) {
							
				if(!closedList.contains(successor)) {

					double fromSourceCost = 
							fromSourceCosts.get(current) + 
							computeCosts.apply(new Tuple<>(current, successor));
								
					if(!openList.contains(successor)) {					
						openList.add(successor);	
						estimatedCostsToTarget.add(
								new Entry<Double, V>(
										estimateCosts.apply(new Tuple<>(successor, target)), 
										successor));
					}
					
					fromSourceCosts.put(successor, fromSourceCost);
					predecessors.put(successor, current);
				}
			}			
		}				
		
		return null;
	}	
	
	/**
	 * Builds the path whilst backtracking predecessors list from target towards source node.
	 *
	 * @param source the source node
	 * @param target the target node
	 * @return the path as a list of vertices
	 */
	private List<V> buildPath(V source, V target){
							
		List<V> path = new ArrayList<>();
		
		V current = target;
		path.add(current);
		
		while(current != source) {
			current = predecessors.get(current);
			path.add(current);
		}	
			
		Collections.reverse(path);
			
		return path;
	}

	/**
	 * The Class Entry.
	 *
	 * @param <K> the key type
	 * @param <T> the generic type
	 */
	private class Entry<K extends Comparable<K>,T> implements Comparable<Entry<K,T>> {	
		
		/** The key. */
		private K key;	
		
		/** The value. */
		private T value;
			
		/**
		 * Instantiates a new entry.
		 *
		 * @param key the key
		 * @param value the value
		 */
		public Entry(K key, T value) {
			this.key = key;
			this.value = value;
		}	
		
		/**
		 * Gets the key.
		 *
		 * @return the key
		 */
		public K getKey() {
			return key;
		}
		
		/**
		 * Gets the value.
		 *
		 * @return the value
		 */
		public T getValue() {
			return this.value;
		}
		
		/**
		 * Compare to.
		 *
		 * @param other the other
		 * @return the int
		 */
		public int compareTo(AStarShortestPath<V, E, W>.Entry<K,T> other) {
			return getKey().compareTo(other.getKey());
		}
		
		/**
		 * Equals.
		 *
		 * @param other the other
		 * @return true, if successful
		 */
		public boolean equals(Entry<K,T> other) {
			return this.getValue().equals(other.getValue());
		}
	}
}
