package de.jgraphlib.generator.connectivity;

import java.util.List;
import java.util.Map;
import java.util.Set;

import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.util.RandomNumbers;
import de.jgraphlib.util.Tuple;

/**
 * The Class Connectivity.
 *
 * @param <V> a derived class of vertex
 */
public abstract class Weighted2DGraphConnectivity<V extends Vertex<?>> {
	
	protected RandomNumbers randomNumbers;
	protected Association association;
	
	/**
	 * Associate vertex (a new vertex added to the graph, but without any edges yet)
	 * with vertices (already connected vertices in the graph in the environment of vertex)
	 *
	 * @param vertex is new vertex added to a graph
	 * @param vertices that are already in the graph
	 * @return List contains associations created to build edges in the graph
	 */
	public abstract List<Tuple<V, V>> associate(V vertex, List<V> vertices);
	
	public abstract List<Tuple<V, V>> associate(Map<V, Set<V>> vertices);
	
	public RandomNumbers getRandomNumbers() {
		return this.randomNumbers;
	}
	
	public Association getAssociation() {
		return this.association;
	}
}
