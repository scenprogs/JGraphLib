package de.jgraphlib.generator;

import java.util.List;

import de.jgraphlib.generator.properties.Weighted2DGraphProperties;
import de.jgraphlib.graph.Weighted2DGraph;
import de.jgraphlib.graph.elements.EdgeDistance;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.util.RandomNumbers;

public class RecursiveGraphGenerator<V extends Vertex<Position2D>, E extends WeightedEdge<W>, W extends EdgeDistance> extends Weighted2DGraphGenerator<V, E, W> {
	
	public RecursiveGraphGenerator(Weighted2DGraph<V, E, W> graph, Weighted2DGraphProperties<V> properties, RandomNumbers random) {
		super(graph, properties, random);
	}
	
	public RecursiveGraphGenerator(Weighted2DGraph<V, E, W> graph, Weighted2DGraphProperties<V> properties) {
		super(graph, properties);
	}
	
	public RecursiveGraphGenerator(Weighted2DGraphProperties<V> properties, RandomNumbers random) {
		super(null, properties, random);
	}
	
	public RecursiveGraphGenerator(Weighted2DGraphProperties<V> properties) {
		super(null, properties);
	}
	
	public List<V> generate(int attempts) {
				
		/*** Recursion wrapper class ********************************************************************************/	
		class Recursion {
			void call(V vertex, int numberOfSuccessors, int attempts) {
											
				List<V> successors = generateVertices(vertex, numberOfSuccessors, attempts);

				/* (1.1) Connect current vertex with successors */
				for (V successor : successors) 
					createEdge(vertex, successor);	
							
				/* (1.2) Connect successors with any vertices in vertex distance */
				for (V successor : successors)
					generateEdges(successor, random.getRandom(getProperties().getEdgeDistance().min(), getProperties().getEdgeDistance().max()));

				/* (1.3) Add (recursively) new vertices after each successor until graph has reached target size */
				for (V successor : successors)
					if (getGraph().size() < getTargetSize() && successor != null)
						call(successor, getProperties().getEdgeQuantity().getMax() - edgeCounter.get(successor), attempts);					
			}
		}	
		/************************************************************************************************************/
				
		/* (1) Start recursion at root node */
		new Recursion().call(generateVertex(attempts), getProperties().getEdgeQuantity().getMin() + 1, attempts);

		/* (2) In case recursion originated from root node terminates before target size reached, take vertices below max edge quantity and continue recursion */
		while(getGraph().size() < getTargetSize() && !edgeCounter.getIncompleteConnected().isEmpty() && attempts > 0) {						
			V vertex = random.getRandomItem(edgeCounter.getIncompleteConnected());		
			new Recursion().call(vertex, getProperties().getEdgeQuantity().getMax() - edgeCounter.get(vertex), attempts--);				
		}
		
		log.info(String.format("%d/%d", getGraph().size(), getTargetSize()));
		
		return getGraph().getVertices();
	}

}
