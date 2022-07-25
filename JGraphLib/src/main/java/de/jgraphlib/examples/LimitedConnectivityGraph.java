package de.jgraphlib.examples;

import java.lang.reflect.InvocationTargetException;

import de.jgraphlib.algorithms.RandomPath;
import de.jgraphlib.generator.RecursiveGraphGenerator;
import de.jgraphlib.generator.connectivity.Association;
import de.jgraphlib.generator.connectivity.DefaultConnectivity;
import de.jgraphlib.generator.properties.Weighted2DGraphProperties;
import de.jgraphlib.generator.shapes.Rectangle;
import de.jgraphlib.graph.DirectedWeighted2DGraph;
import de.jgraphlib.graph.elements.EdgeDistance;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.graph.suppliers.Weighted2DGraphSupplier;
import de.jgraphlib.util.DoubleRange;
import de.jgraphlib.util.IntRange;
import de.jgraphlib.util.RandomNumbers;

public class LimitedConnectivityGraph {

	public static void main(String[] args) throws InvocationTargetException, InterruptedException {

		// @formatter:off		
		RandomNumbers randomNumbers = new RandomNumbers();
				
		DirectedWeighted2DGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> graph = 
				new DirectedWeighted2DGraph<>(
						new Weighted2DGraphSupplier().getVertexSupplier(),
						new Weighted2DGraphSupplier().getEdgeSupplier(),
						new Weighted2DGraphSupplier().getEdgeWeightSupplier());

		RecursiveGraphGenerator<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> generator = 
				new RecursiveGraphGenerator<>(
						graph, 
						new Weighted2DGraphProperties<>(
								/* boundaries of the graph */	new Rectangle(1024, 768),
								/* number of vertices */ 		new IntRange(100),
								/* distance between vertices */ new DoubleRange(50d, 100d), 
								/* connectivity */ 				new IntRange(2, 4),
								/* edge distance */ 			new DoubleRange(50d, 100d),
								new DefaultConnectivity<>(Association.UNIDIRECTIONAL, randomNumbers)),
						randomNumbers);
		
		generator.generate(1000);
		graph.addPaths(new RandomPath<>(graph).compute(new IntRange(5), new IntRange(5,10), 1000));
		graph.plot();
	
		// @formatter:on
	}
	
}
