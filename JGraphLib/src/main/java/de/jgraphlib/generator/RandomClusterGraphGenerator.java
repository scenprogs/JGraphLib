package de.jgraphlib.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.jgraphlib.generator.properties.ClusterGraphProperties;
import de.jgraphlib.generator.properties.Weighted2DGraphProperties;
import de.jgraphlib.graph.Weighted2DGraph;
import de.jgraphlib.graph.elements.Cluster;
import de.jgraphlib.graph.elements.EdgeDistance;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.util.IntRange;
import de.jgraphlib.util.RandomNumbers;
import de.jgraphlib.util.SearchGrid2D;
import de.jgraphlib.util.Tuple;

// @formatter:off

public class RandomClusterGraphGenerator<V extends Vertex<Position2D>, E extends WeightedEdge<W>, W extends EdgeDistance>
		extends ClusterGraphGenerator<V, E, W> {
	
	public RandomClusterGraphGenerator(
			Weighted2DGraph<V, E, W> graph, 
			ClusterGraphProperties<V> properties,
			Weighted2DGraphGenerator<V, E, W> generator, 
			RandomNumbers random) {
		
		super(graph, properties, generator, random);
	}
	
	public RandomClusterGraphGenerator(
			Weighted2DGraph<V, E, W> graph, 
			ClusterGraphProperties<V> properties,
			Weighted2DGraphGenerator<V, E, W> generator) {
		
		super(graph, properties, generator);
	}

	public List<V> generate(int attempts) {

		clusters = new ArrayList<Cluster<V,E,W>>();
		
		int clusterQuantity = getRandomNumbers().getRandom(
				properties.getClusterQuantity().min(), 
				properties.getClusterQuantity().max());
		
		int targetSize = random.getRandom(
				properties.getVertexQuantity().min(), 
				properties.getVertexQuantity().max());
		
		int clusterTargetSize = targetSize / clusterQuantity;
						
		/* (1) Generate clusters */
		for(int i=0; i < clusterQuantity; i++) {	
			
			// Instantiate a new graph for the cluster which is a copy of the given base graph
			clusters.add(new Cluster<>(i, getGraph().copy()));
			
			// Add the cluster's graph to the generator
			generator.setGraph(clusters.get(i).getGraph());
			
			// Set generator properties
			generator.setProperties(
					new Weighted2DGraphProperties<>(
							getProperties().getShape(), 
							new IntRange(clusterTargetSize), 
							generator.getProperties().getVertexDistance(), 
							generator.getProperties().getEdgeQuantity(), 
							generator.getProperties().getEdgeDistance(), 
							generator.getProperties().getConnectivity()));	
									
			// Initialize generator
			generator.initialize(generator.getProperties());
						
			// Add vertex position condition: Distance between vertices of different clusters
			generator.addVertexPoistionCondition(
					(Function<Position2D, Boolean>)(position2D) -> {				
						for(Cluster<V,E,W> cluster : clusters)				
							if(cluster.getSearchGrid() != null) 											
								if(cluster.getSearchGrid().getScope().distance(position2D) < getProperties().getVertexDistance().min() || 
										cluster.getSearchGrid().getScope().distance(position2D) == 0d)
									return false;								
						return true;
					});
			
			// Generate the clusters
			generator.generate(1000);	
			
			// Generate a search grid of the complete cluster (used later for edge generation) 
			clusters.get(i).setSearchGrid(
					new SearchGrid2D<V>(
							clusters.get(i).getGraph().getScope(), 
							getProperties().getVertexDistance(),
							clusters.get(i).getGraph().getVertices().stream().map(v -> new Tuple<Position2D, V>(v.getPosition(),v)).collect(Collectors.toList())));
		}
			
		// (2) Integrate clusters (graphs) into base graph
		for(Cluster<V,E,W> cluster : clusters) 
			graph.integrate(cluster.getGraph());
		
		/* (3) Generate amongst cluster edges */				
		generateClusterEdges();
		
		return getGraph().getVertices();
	}	
}
