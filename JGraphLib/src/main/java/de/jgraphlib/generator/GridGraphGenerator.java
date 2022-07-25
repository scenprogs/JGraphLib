package de.jgraphlib.generator;

import java.util.List;

import de.jgraphlib.generator.properties.GridGraphProperties;
import de.jgraphlib.graph.Weighted2DGraph;
import de.jgraphlib.graph.elements.EdgeDistance;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.util.RandomNumbers;

public class GridGraphGenerator<V extends Vertex<Position2D>, E extends WeightedEdge<W>, W extends EdgeDistance> extends Weighted2DGraphGenerator<V, E, W> {

	GridGraphProperties<V> properties;
	
	public GridGraphGenerator(Weighted2DGraph<V, E, W> graph, GridGraphProperties<V> properties) {
		super(graph, properties);
		this.properties = properties;
	}
	
	public GridGraphGenerator(Weighted2DGraph<V, E, W> graph, GridGraphProperties<V> properties, RandomNumbers random) {
		super(graph, properties, random);
		this.properties = properties;
	}

	public List<V> generate(int attempts) {
		
		V currentVertex = createVertex(new Position2D(0, 0));

		while ((currentVertex.getPosition().x() <= properties.getWidth() - properties.getVertexDistance().max())) {
			
			if (getGraph().size() > 1) {
				
				double xOffset = 
						random.getRandom(properties.getVertexDistance().min(), properties.getVertexDistance().max());
				
				V newVertex = createVertex(
						new Position2D(currentVertex.getPosition().x() + xOffset, 0));
				
				currentVertex = newVertex;
								
				generateEdges(newVertex, getProperties().getEdgeDistance().max());
			}

			while (currentVertex.getPosition().y() <= (properties.getHeight() - properties.getVertexDistance().max())) {
				
				double yOffset = 
						random.getRandom(properties.getVertexDistance().min(), properties.getVertexDistance().max());
				
				V newVertex = createVertex(
						new Position2D(
								currentVertex.getPosition().x(), 
								currentVertex.getPosition().y() + yOffset));
				
				currentVertex = newVertex;
				
				generateEdges(newVertex, getProperties().getEdgeDistance().max());
			}
		}
		
		return getGraph().getVertices();
	}
}
