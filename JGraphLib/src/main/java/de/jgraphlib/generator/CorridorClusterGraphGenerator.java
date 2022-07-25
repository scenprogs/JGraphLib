package de.jgraphlib.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import de.jgraphlib.generator.properties.CorridorClusterGraphProperties;
import de.jgraphlib.generator.properties.Weighted2DGraphProperties;
import de.jgraphlib.generator.shapes.Corridor;
import de.jgraphlib.graph.Weighted2DGraph;
import de.jgraphlib.graph.elements.EdgeDistance;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.util.IntRange;
import de.jgraphlib.util.RandomNumbers;
import de.jgraphlib.util.SearchGrid2D;
import de.jgraphlib.util.Tuple;

public class CorridorClusterGraphGenerator<V extends Vertex<Position2D>, E extends WeightedEdge<W>, W extends EdgeDistance> extends ClusterGraphGenerator<V, E, W> {

	protected CorridorClusterGraphProperties<V> properties;
	private int corridorQuantity;
	
	public CorridorClusterGraphGenerator(
			Weighted2DGraph<V,E,W> graph,
			CorridorClusterGraphProperties<V> properties, 
			Weighted2DGraphGenerator<V, E, W> generator) {
		
		super(graph, properties, generator);
		this.properties = properties;
	}
		
	public CorridorClusterGraphGenerator(
			Weighted2DGraph<V,E,W> graph, 
			CorridorClusterGraphProperties<V> properties, 
			Weighted2DGraphGenerator<V, E, W> generator, 
			RandomNumbers randomNumbers) {
		
		super(graph, properties, generator, randomNumbers);
		this.properties = properties;
	}
		
	private List<Corridor<V,E,W>> generateCorridors(){
		
		List<Corridor<V,E,W>> corridors = new ArrayList<Corridor<V,E,W>>();
				
		corridorQuantity = getRandomNumbers().getRandom(
				properties.getCorridorQuantity().min(), 
				properties.getCorridorQuantity().max());
		
		double corridorDistance = getRandomNumbers().getRandom(
				properties.getCorridorDistance().min(), 
				properties.getCorridorDistance().max());
		
		switch(properties.getCorridorDirection()) {
			
			case HORIZONTAL:{	
			
				double totalCorridorDistance = (corridorQuantity - 1) * corridorDistance;
			
				double corridorsHeight = 
					(properties.getShape().getScope().getHeight().abs() - totalCorridorDistance) / corridorQuantity;	
				
				for(int i=0; i < corridorQuantity; i++)
					if(i != 0)
						corridors.add(
								new Corridor<V,E,W>(
										/* ID*/ 		i,
										/* top left */	new Position2D(0, ((corridorsHeight+totalCorridorDistance) * i)),
										/* width */ 	properties.getShape().getScope().getWidth().abs(), 
										/* height */ 	corridorsHeight,
										/* graph*/ 		(Weighted2DGraph<V, E, W>) graph.copy()));				
					else
						corridors.add(
							new Corridor<V,E,W>(
									/* ID */			i,
									/* top left */		new Position2D(0, 0),
									/* width */ 		properties.getShape().getScope().getWidth().abs(), 
									/* height */ 		corridorsHeight,
									 /* graph*/ 		(Weighted2DGraph<V, E, W>) graph.copy()));		
				break;
			}
			case VERTICAL:
				// TODO 
				break;
			default:
				break;
		}	
		
		return corridors;
	}
	
	@Override
	public List<V> generate(int attempts) {
				
		List<Corridor<V,E,W>> corridors = generateCorridors();
	
		int corridorVertexQuantity = random.getRandom(
				properties.getVertexQuantity().min(), 
				properties.getVertexQuantity().max()) / corridorQuantity;
				
		// (1) Generate corridors (vertices and edges inside each corridor) */				
		for(Corridor<V,E,W> corridor : corridors) {		
						
			generator.setGraph(corridor.getGraph());
						
			generator.setProperties(
					new Weighted2DGraphProperties<>(
							corridor.getRectangle(), 
							new IntRange(corridorVertexQuantity), 
							generator.getProperties().getVertexDistance(), 
							generator.getProperties().getEdgeQuantity(), 
							generator.getProperties().getEdgeDistance(), 
							generator.getProperties().getConnectivity()));
						
			generator.initialize(generator.getProperties());
			
			generator.generate(attempts);			
		}
		
		// (2) Integrate corridor graphs into base graph
		for(Corridor<V,E,W> corridor : corridors) 
			graph.integrate(corridor.getGraph());
			
		// (3) Generate corridor edges 			
		
		for(Corridor<V,E,W> corridor : corridors) 
			clusters.add(corridor);

		for(int i=0; i < clusters.size(); i++)
			clusters.get(i).setSearchGrid(
					new SearchGrid2D<V>(
							clusters.get(i).getGraph().getScope(), 
							getProperties().getVertexDistance(),
							clusters.get(i).getGraph().getVertices().stream().map(v -> new Tuple<Position2D, V>(v.getPosition(),v)).collect(Collectors.toList())));
		
		generateClusterEdges();
		
		return getGraph().getVertices();
	}
}
