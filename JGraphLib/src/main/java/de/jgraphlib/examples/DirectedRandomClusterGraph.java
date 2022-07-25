package de.jgraphlib.examples;

import java.lang.reflect.InvocationTargetException;

import de.jgraphlib.algorithms.RandomPath;
import de.jgraphlib.generator.NetworkGraphGenerator;
import de.jgraphlib.generator.RandomClusterGraphGenerator;
import de.jgraphlib.generator.connectivity.Association;
import de.jgraphlib.generator.connectivity.DefaultConnectivity;
import de.jgraphlib.generator.properties.ClusterGraphProperties;
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

public class DirectedRandomClusterGraph {

	public static void main(String[] args) throws InvocationTargetException, InterruptedException {

		// @formatter:off
						
		DirectedWeighted2DGraph<Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> graph = 
				new DirectedWeighted2DGraph<
					Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance>(
						new Weighted2DGraphSupplier().getVertexSupplier(),
						new Weighted2DGraphSupplier().getEdgeSupplier(),
						new Weighted2DGraphSupplier().getEdgeWeightSupplier());

		RandomClusterGraphGenerator<
			Vertex<Position2D>, WeightedEdge<EdgeDistance>, EdgeDistance> generator = 
				new RandomClusterGraphGenerator<>(
						graph,
						new ClusterGraphProperties<>(
								/* shape*/						new Rectangle(2000,1000), 									// graph's boundaries
								/* clusterQuantity*/				new IntRange(5),											// number of clusters
								/* vertexQuantity*/				new IntRange(150),											// total number of vertices 
								/* vertexDistance*/				new DoubleRange(150,200),									// distance between vertices of different clusters
								/* edgeQuantity*/				new IntRange(1),											// number of edges between different clusters
								/* edgeDistance*/				new DoubleRange(500),										// distance of edges between clusters
								/* clusterEdgeConnectivity*/	new DefaultConnectivity<>(Association.BIDIRECTIONAL)),		// connectivity between different clusters
						new NetworkGraphGenerator<>(
								new NetworkGraphProperties<>(
										/* vertexDistance */ 	new DoubleRange(50, 50), 									// distance between vertices within a cluster
										/* edgeDistance */ 		new DoubleRange(50, 101),									// distance of edges between clusters
										/* edgeConnectivity*/ 	new DefaultConnectivity<>(Association.UNIDIRECTIONAL))));	// connectivity inside clusters
		
		generator.generate(1000);
		graph.addPaths(new RandomPath<>(graph).compute(new IntRange(5), new IntRange(5,10), 1000));
		graph.plot();

		// @formatter:on
	}	
}