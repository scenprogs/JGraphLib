package de.jgraphlib.generator;

import java.util.List;

import de.jgraphlib.generator.properties.NetworkGraphProperties;
import de.jgraphlib.generator.properties.Weighted2DGraphProperties;
import de.jgraphlib.graph.Weighted2DGraph;
import de.jgraphlib.graph.elements.EdgeDistance;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.util.RandomNumbers;

public class NetworkGraphGenerator<V extends Vertex<Position2D>, E extends WeightedEdge<W>, W extends EdgeDistance> extends Weighted2DGraphGenerator<V, E, W> {
	
	final int m = 10;
	
	public NetworkGraphGenerator(Weighted2DGraph<V, E, W> graph, NetworkGraphProperties<V> properties, RandomNumbers random) {
		super(graph, properties, random);
	}
	
	public NetworkGraphGenerator(Weighted2DGraph<V, E, W> graph, NetworkGraphProperties<V> properties) {
		super(graph, properties);
	}
	
	public NetworkGraphGenerator(Weighted2DGraphProperties<V> properties, RandomNumbers random) {
		super(null, properties, random);
	}
	
	public NetworkGraphGenerator(Weighted2DGraphProperties<V> properties) {
		super(null, properties);
	}
	
	public List<V> generate(int attempts) {
					
		V current = generateVertex(attempts);
		
		while (getGraph().size() < getTargetSize() && attempts > 0) {
		
			V vertex = generateVertex(current, attempts);
									
			if(vertex != null) {
				current = vertex;
				generateEdges(current, getProperties().getEdgeDistance().max());		
			}
			else
				current = getGraph().getVertex(random.getRandom(0, getGraph().size()-1));
		}
		
		return getGraph().getVertices();
	}
}