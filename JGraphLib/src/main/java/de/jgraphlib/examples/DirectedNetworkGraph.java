package de.jgraphlib.examples;

import java.lang.reflect.InvocationTargetException;

import de.jgraphlib.algorithms.RandomPath;
import de.jgraphlib.generator.NetworkGraphGenerator;
import de.jgraphlib.generator.connectivity.Association;
import de.jgraphlib.generator.connectivity.DefaultConnectivity;
import de.jgraphlib.generator.properties.NetworkGraphProperties;
import de.jgraphlib.generator.shapes.Rectangle;
import de.jgraphlib.graph.DirectedWeighted2DGraph;
import de.jgraphlib.graph.elements.EdgeDistance;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.graph.suppliers.Weighted2DGraphSupplier;
import de.jgraphlib.util.DoubleRange;
import de.jgraphlib.util.IntRange;

public class DirectedNetworkGraph {

	public static void main(String[] args) throws InvocationTargetException, InterruptedException {

		// @formatter:off
		
		DirectedWeighted2DGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> graph = 
				new DirectedWeighted2DGraph<>(
						new Weighted2DGraphSupplier().getVertexSupplier(),
						new Weighted2DGraphSupplier().getEdgeSupplier(),
						new Weighted2DGraphSupplier().getEdgeWeightSupplier());

		NetworkGraphGenerator<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> generator = 
				new NetworkGraphGenerator<>(
						graph, 
						new NetworkGraphProperties<>(
								/* shape*/						new Rectangle(1000, 1000),
								/* vertexQuantity*/				new IntRange(150, 150),
								/* vertexDistance */ 			new DoubleRange(50, 75),
								/* edgeDistance */ 				new DoubleRange(75, 101),
								new DefaultConnectivity<>(Association.UNIDIRECTIONAL)));
						
		generator.generate(1000);					
		graph.addPaths(new RandomPath<>(graph).compute(new IntRange(5), new IntRange(5,10), 100));
		graph.plot();
			
		// @formatter:on
	}
}