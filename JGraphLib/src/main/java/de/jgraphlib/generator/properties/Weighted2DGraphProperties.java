package de.jgraphlib.generator.properties;

import de.jgraphlib.generator.connectivity.Weighted2DGraphConnectivity;
import de.jgraphlib.generator.shapes.Shape2D;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.util.DoubleRange;
import de.jgraphlib.util.IntRange;

public class Weighted2DGraphProperties<V extends Vertex<Position2D>> {
	
	protected Shape2D shape;
	protected Weighted2DGraphConnectivity<V> connectivity;
	
	protected IntRange vertexQuantity;
	protected DoubleRange vertexDistance;
	protected IntRange edgeQuantity;
	protected DoubleRange edgeDistance;
			
	public Weighted2DGraphProperties(
			Shape2D shape, 
			IntRange vertexQuantity, 
			DoubleRange vertexDistance, 
			IntRange edgeQuantity, 
			DoubleRange edgeDistance, 
			Weighted2DGraphConnectivity<V> connectivity) {
		
		this.shape = shape;
		this.vertexQuantity = vertexQuantity;
		this.vertexDistance = vertexDistance;
		this.edgeQuantity = edgeQuantity;
		this.edgeDistance = edgeDistance;
		this.connectivity = connectivity;
	}
	
	public Weighted2DGraphProperties(
			DoubleRange vertexDistance, 
			IntRange edgeQuantity, 
			DoubleRange edgeDistance, 
			Weighted2DGraphConnectivity<V> connectivity) {
		
		this.vertexDistance = vertexDistance;
		this.edgeQuantity = edgeQuantity;
		this.edgeDistance = edgeDistance;
		this.connectivity = connectivity;
	}
	
	public Shape2D getShape() {		
		return this.shape;
	}
	
	public void setShape(Shape2D shape) {
		this.shape = shape;
	}
	
	public void setConnectivity(Weighted2DGraphConnectivity<V> connectivity) {
		this.connectivity = connectivity;
	}
	
	public Weighted2DGraphConnectivity<V> getConnectivity() {
		return this.connectivity;
	}

	public IntRange getVertexQuantity() {
		return vertexQuantity;
	}
	
	public void setVertexQuantity(IntRange vertexQuantity) {
		this.vertexQuantity = vertexQuantity;
	}
	
	public void setVertexQuantity(int vertexQuantity) {
		this.vertexQuantity = new IntRange(vertexQuantity);
	}

	public DoubleRange getVertexDistance() {
		return vertexDistance;
	}
	
	public void setVertexDistance(DoubleRange vertexDistance) {
		this.vertexDistance = vertexDistance;	
	}

	public IntRange getEdgeQuantity() {
		return edgeQuantity;
	}
	
	public void setEdgeQuantity(IntRange edgeQuantity) {
		this.edgeQuantity = edgeQuantity;
	}

	public DoubleRange getEdgeDistance() {
		return edgeDistance;
	}
	
	public void setEdgeDistance(DoubleRange edgeDistance) {
		this.edgeDistance = edgeDistance;
	}
	
	public enum EdgeStyle {
		UNIDIRECTIONAL, BIDIRECTIONAL;
	}

	@Override
	public String toString() {
		/*
		 * StringBuilder stringBuilder = new StringBuilder();
		 * stringBuilder.append("width: ").append(this.width.toString());
		 * stringBuilder.append(", height: ").append(this.height.toString());
		 * stringBuilder.append(", vertexCount: ").append(this.vertexCount.toString());
		 * stringBuilder.append(", vertexDistance: ").append(this.vertexDistance.
		 * toString()); stringBuilder.append(", edgeCount: ").append(this.edgeCount);
		 * stringBuilder.append(", edgeDistance: ").append(this.edgeDistance.toString())
		 * ; return stringBuilder.toString();
		 */
		return "";
	}
}