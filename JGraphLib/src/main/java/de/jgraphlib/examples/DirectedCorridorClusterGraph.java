package de.jgraphlib.examples;

import java.lang.reflect.InvocationTargetException;

import de.jgraphlib.algorithms.RandomPath;
import de.jgraphlib.generator.CorridorClusterGraphGenerator;
import de.jgraphlib.generator.NetworkGraphGenerator;
import de.jgraphlib.generator.connectivity.Association;
import de.jgraphlib.generator.connectivity.DefaultConnectivity;
import de.jgraphlib.generator.properties.CorridorClusterGraphProperties;
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

public class DirectedCorridorClusterGraph {

	public static void main(String[] args) throws InvocationTargetException, InterruptedException {

		// @formatter:off

		DirectedWeighted2DGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> graph = 
				new DirectedWeighted2DGraph<>(
				new Weighted2DGraphSupplier().getVertexSupplier(), 
				new Weighted2DGraphSupplier().getEdgeSupplier(),
				new Weighted2DGraphSupplier().getEdgeWeightSupplier());

		CorridorClusterGraphGenerator<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> generator = 
				new CorridorClusterGraphGenerator<>(
						graph,
						new CorridorClusterGraphProperties<>(
								new Rectangle(1000, 1000), 
								/* clusterQuantity */ 		new IntRange(3),
								/* corridorAlingment */ 	CorridorClusterGraphProperties.CorridorDirection.HORIZONTAL,
								/* vertexQuantity*/			new IntRange(225),
								/* corridorDistance*/		new DoubleRange(75, 100),
								/* edgeQuantity*/			new IntRange(5),
								/* edgeDistance*/			new DoubleRange(350),
								/* edgeConnectivity*/		new DefaultConnectivity<>(Association.BIDIRECTIONAL)),
						/*new RecursiveGraphGenerator<>(
								new Weighted2DGraphProperties<>(
									 		new DoubleRange(50d, 100d), 
											new IntRange(2,3),
										 	new DoubleRange(50d, 150d),
											new DefaultConnectivity<>(Association.UNIDIRECTIONAL))));*/
						new NetworkGraphGenerator<>(
								new NetworkGraphProperties<>(
										/* vertexDistance */ 	new DoubleRange(50d, 100d), 
										/* edgeDistance */ 		new DoubleRange(25d, 101d),
										/* edgeConnectivity*/ 	new DefaultConnectivity<>(Association.UNIDIRECTIONAL))));
	
		generator.generate(100);
		graph.addPaths(new RandomPath<>(graph).compute(new IntRange(5), new IntRange(5,10), 100));
		graph.plot();

		// @formatter:on
	}
}
 