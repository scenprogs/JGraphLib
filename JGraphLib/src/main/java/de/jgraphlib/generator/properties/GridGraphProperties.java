package de.jgraphlib.generator.properties;

import de.jgraphlib.generator.connectivity.Weighted2DGraphConnectivity;
import de.jgraphlib.generator.shapes.Rectangle;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.util.DoubleRange;

public class GridGraphProperties<V extends Vertex<Position2D>> extends Weighted2DGraphProperties<V> {

	private int width;
	private int height;
	
	public GridGraphProperties(int width, int height, int vertexDistance, int edgeDistance, Weighted2DGraphConnectivity<V> connectivity) {
		super(new Rectangle(width,height), null, new DoubleRange(vertexDistance), null, new DoubleRange(edgeDistance), connectivity);
		this.width = width;
		this.height = height;
	}
	
	public double getWidth() {
		return width;
	}
	
	public double getHeight() {
		return height;
	}
}
