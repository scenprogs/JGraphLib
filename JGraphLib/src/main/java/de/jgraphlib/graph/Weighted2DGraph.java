package de.jgraphlib.graph;

import java.lang.reflect.InvocationTargetException;
import java.util.TreeMap;
import java.util.function.Supplier;

import javax.swing.SwingUtilities;

import de.jgraphlib.graph.elements.EdgeDistance;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.gui.VisualGraphApp;
import de.jgraphlib.gui.style.VisualGraphStyle;
import de.jgraphlib.util.IntRange;
import de.jgraphlib.util.Scope2D;

public abstract class Weighted2DGraph<V extends Vertex<Position2D>, E extends WeightedEdge<W>, W extends EdgeDistance>
		extends WeightedGraph<V, Position2D, E, W> {

	Scope2D scope = new Scope2D();

	public Weighted2DGraph(Supplier<V> vertexSupplier, Supplier<E> edgeSupplier, Supplier<W> edgeWeightSupplier) {
		super(vertexSupplier, edgeSupplier, edgeWeightSupplier);
	}
	
	public void plot() throws InvocationTargetException, InterruptedException {	
		SwingUtilities.invokeAndWait(new VisualGraphApp<>(this, new VisualGraphStyle<>()));	
	}
	
	public void plot(VisualGraphStyle<E,W> graphStyle) throws InvocationTargetException, InterruptedException {	
		SwingUtilities.invokeAndWait(new VisualGraphApp<>(this, graphStyle));	
	}
	
	public abstract Weighted2DGraph<V, E, W> copy();
	
	public TreeMap<Integer, E> copyEdges() {
		TreeMap<Integer, E> linkCopies = new TreeMap<Integer, E>();
		for (E edge : getEdges()) {
			E edgeCopy = edgeSupplier.get();
			edgeCopy.setID(edge.getID());
			W edgeWeight = edgeWeightSupplier.get();
			edgeWeight.setDistance(edge.getWeight().getDistance());
			edgeCopy.setWeight(edgeWeight);
			linkCopies.put(edgeCopy.getID(), edgeCopy);
		}
		return linkCopies;
	}

	public V addVertex(Position2D position) {
		V vertex = super.addVertex(position);
		updateScope(vertex);
		return vertex;
	}

	public void addVertex(V vertex) {
		super.addVertex(vertex);
		updateScope(vertex);
	}

	public V addVertex(double x, double y) {
		V vertex = super.addVertex(new Position2D(x, y));
		updateScope(vertex);
		return vertex;
	}
	
	private void updateScope(V vertex) {
		
		if(scope.getWidth() != null) {
			if (vertex.getPosition().x() > scope.getWidth().getMax())
				scope.getWidth().setMax((int) Math.ceil(vertex.getPosition().x()));
			else if (vertex.getPosition().x() < scope.getWidth().getMin())
				scope.getWidth().setMin((int) Math.ceil(vertex.getPosition().x()));
		}	
		else
			scope.setWidth(new IntRange((int)vertex.getPosition().x()));
		
		if(scope.getHeight() != null) {			
			if (vertex.getPosition().y() > scope.getHeight().getMax())
				scope.getHeight().setMax((int) Math.ceil(vertex.getPosition().y()));
			else if (vertex.getPosition().y() < scope.getHeight().getMin())
				scope.getHeight().setMin((int) Math.ceil(vertex.getPosition().y()));		
		}
		else
			scope.setHeight(new IntRange((int)vertex.getPosition().y()));	
	}

	public Scope2D getScope() {
		return scope;
	}
}
