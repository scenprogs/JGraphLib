package de.jgraphlib.examples;

import java.lang.reflect.InvocationTargetException;

import de.jgraphlib.algorithms.RandomPath;
import de.jgraphlib.generator.TreeGraphGenerator;
import de.jgraphlib.generator.connectivity.Direction;
import de.jgraphlib.generator.connectivity.UniformConnectivity;
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

public class DirectedTreeGraph {

	public static void main(String[] args) throws InvocationTargetException, InterruptedException {

		// @formatter:off		
				
		DirectedWeighted2DGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> graph = 
				new DirectedWeighted2DGraph<>(
						new Weighted2DGraphSupplier().getVertexSupplier(),
						new Weighted2DGraphSupplier().getEdgeSupplier(),
						new Weighted2DGraphSupplier().getEdgeWeightSupplier());

		TreeGraphGenerator<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> generator = 
				new TreeGraphGenerator<>(
						graph, 
						new Weighted2DGraphProperties<>(
								/* boundaries of the graph */					new Rectangle(1000, 1000),
								/* number of vertices */ 						new IntRange(100, 150),
								/* distance between vertices */ 					new DoubleRange(50d, 100d), 
								/* connectivity */ 								new IntRange(1,3),
								/* edge distance */ 								new DoubleRange(50d, 100d),
								/* new vertices point back to its predecessor*/ new UniformConnectivity<>(Direction.BACK)));
		
		generator.generate(1000);
		graph.addPaths(new RandomPath<>(graph).compute(new IntRange(5), new IntRange(5,10), 1000));
		graph.plot();
	
		// @formatter:on
	}
	
}
