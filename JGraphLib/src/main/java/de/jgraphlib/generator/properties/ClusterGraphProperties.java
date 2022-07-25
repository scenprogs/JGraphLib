package de.jgraphlib.generator.properties;

import de.jgraphlib.generator.connectivity.Weighted2DGraphConnectivity;
import de.jgraphlib.generator.shapes.Shape2D;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.util.DoubleRange;
import de.jgraphlib.util.IntRange;

public class ClusterGraphProperties<V extends Vertex<Position2D>> extends Weighted2DGraphProperties<V> {

	private IntRange clusterQuantity;
		
	public ClusterGraphProperties(
			Shape2D shape, 
			IntRange clusterQuantity,
			IntRange vertexQuantity, 
			DoubleRange vertexDistance,
			IntRange edgeQuantity,
			DoubleRange edgeDistance,
			Weighted2DGraphConnectivity<V> connectivity) {
				
		super(shape, 
				vertexQuantity, 
				vertexDistance, 
				edgeQuantity, 
				edgeDistance, 
				connectivity);
	
		this.clusterQuantity = clusterQuantity;
	}
	
	public IntRange getClusterQuantity() {
		return clusterQuantity;
	}
}
