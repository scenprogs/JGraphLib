package de.jgraphlib.generator.shapes;

import de.jgraphlib.graph.Weighted2DGraph;
import de.jgraphlib.graph.elements.Cluster;
import de.jgraphlib.graph.elements.EdgeDistance;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;

public class Corridor<V extends Vertex<Position2D>, E extends WeightedEdge<W>, W extends EdgeDistance> extends Cluster<V,E,W> {	
	
	private Rectangle rectangle;
	
	public Corridor(int ID, Position2D topLeft, double width, double height, Weighted2DGraph<V, E, W> graph) {	
		super(ID, graph);
		this.rectangle = new Rectangle(topLeft, width, height);
	}
	
	public String toString() {
		return String.format("top-left: %.2f/%.2f, width: %.2f, height: %.2f", 
				rectangle.getTopLeft().x(), rectangle.getTopLeft().y(), rectangle.getWidth(), rectangle.getHeight());
	}
	
	public Rectangle getRectangle() {
		return this.rectangle;
	}
}
