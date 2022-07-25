package de.jgraphlib.generator.properties;

import de.jgraphlib.generator.connectivity.Weighted2DGraphConnectivity;
import de.jgraphlib.generator.shapes.RectangularShape2D;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.util.DoubleRange;
import de.jgraphlib.util.IntRange;

public class CorridorClusterGraphProperties<V extends Vertex<Position2D>> extends ClusterGraphProperties<V> {

	private DoubleRange corridorDistance;
	private CorridorDirection corridorDirection; 
	private RectangularShape2D shape;
	
	public CorridorClusterGraphProperties(
			RectangularShape2D shape, 
			IntRange clusterQuantity,
			CorridorDirection corridorDirection,
			IntRange vertexQuantity, 
			DoubleRange corridorDistance,
			IntRange edgeQuantity,
			DoubleRange edgeDistance,
			Weighted2DGraphConnectivity<V> connectivity) {
		
		super(shape, 
				clusterQuantity,
				vertexQuantity, 
				corridorDistance,
				edgeQuantity,
				edgeDistance,
				connectivity);
		
		this.shape = shape;
		this.corridorDistance = corridorDistance;
		this.corridorDirection = corridorDirection;
	}
		
	public IntRange getCorridorQuantity() {
		return this.getClusterQuantity();
	}
	
	public DoubleRange getCorridorDistance() {
		return corridorDistance;
	}
	
	public CorridorDirection getCorridorDirection() {
		return corridorDirection;
	}
	
	public enum CorridorDirection {
		HORIZONTAL, VERTICAL
	}	
	
	public RectangularShape2D getShape() {
		return this.shape;
	}
}
