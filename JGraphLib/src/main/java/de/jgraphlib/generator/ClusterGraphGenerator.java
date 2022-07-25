package de.jgraphlib.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.jgraphlib.generator.properties.ClusterGraphProperties;
import de.jgraphlib.graph.Weighted2DGraph;
import de.jgraphlib.graph.elements.EdgeDistance;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.graph.elements.Cluster;
import de.jgraphlib.maths.Combinatorics;
import de.jgraphlib.util.RandomNumbers;
import de.jgraphlib.util.Tuple;

public abstract class ClusterGraphGenerator<V extends Vertex<Position2D>, E extends WeightedEdge<W>, W extends EdgeDistance> extends Weighted2DGraphGenerator<V,E,W> {

	ClusterGraphProperties<V> properties;
	Weighted2DGraphGenerator<V, E, W> generator;
	List<Cluster<V,E,W>> clusters;
	
	public ClusterGraphGenerator(
			Weighted2DGraph<V, E, W> graph, 
			ClusterGraphProperties<V> properties,
			Weighted2DGraphGenerator<V, E, W> generator, 
			RandomNumbers random) {
		
		super(graph, properties, random);
		
		this.properties = properties;
		this.generator = generator;
		this.clusters = new ArrayList<>();
	}
	
	public ClusterGraphGenerator(
			Weighted2DGraph<V, E, W> graph, 
			ClusterGraphProperties<V> properties,
			Weighted2DGraphGenerator<V, E, W> generator) {
		
		super(graph, properties);
		
		this.properties = properties;
		this.generator = generator;
		this.clusters = new ArrayList<>();
	}
		
	public void generateClusterEdges() {
		
		/* (1) Generate cluster pairs */
		List<Tuple<Cluster<V,E,W>, Cluster<V,E,W>>> clusterPairs = Combinatorics.generateCombinations(clusters);

		// Remove all cluster pairs whose distance is greater than edgeDistance.min (remove cluster pairs that are beyond range for edge generation)
		clusterPairs.removeIf(clusterPair -> 
			clusterPair.getFirst().getSearchGrid().getScope().distance(
					clusterPair.getSecond().getSearchGrid().getScope()) > getProperties().getEdgeDistance().min());	
						
		/* (2) Generate cluster edges */
		for(Tuple<Cluster<V,E,W>, Cluster<V,E,W>> clusterPair : clusterPairs) {
			
			int edgeQuantity = getRandomNumbers().getRandom(
					properties.getEdgeQuantity().min(), 
					properties.getEdgeQuantity().max());
			
			// Find objects in cluster pairs that are in edge distance range
			Map<V, Set<V>> verticesInDistance = clusterPair.getFirst().getSearchGrid().search(
					/**/ clusterPair.getSecond().getSearchGrid(), 
					/**/ getProperties().getEdgeDistance().max());
			
			
					
			for(Tuple<V,V> association : getRandomNumbers().selectNrandomOfM(
					getProperties().getConnectivity().associate(verticesInDistance), edgeQuantity)) {
				
				switch(getProperties().getConnectivity().getAssociation()) {
					case UNIDIRECTIONAL:
						createEdge(association.getFirst(), association.getSecond());
						break;
					case BIDIRECTIONAL:
						createEdge(association.getFirst(), association.getSecond());
						createEdge(association.getSecond(), association.getFirst());
						break;	
				}				
			}
		}
	}
	
}
