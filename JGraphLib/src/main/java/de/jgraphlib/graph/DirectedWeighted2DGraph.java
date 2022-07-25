
package de.jgraphlib.graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import de.jgraphlib.graph.elements.EdgeDistance;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.util.Tuple;

public class DirectedWeighted2DGraph<V extends Vertex<Position2D>, E extends WeightedEdge<W>, W extends EdgeDistance>
		extends Weighted2DGraph<V, E, W> {

	public DirectedWeighted2DGraph(Supplier<V> vertexSupplier, Supplier<E> edgeSupplier, Supplier<W> edgeWeightSupplier) {	
		super(vertexSupplier, edgeSupplier, edgeWeightSupplier);
	}

	public DirectedWeighted2DGraph(DirectedWeighted2DGraph<V, E, W> graph) {
		super(graph.vertexSupplier, graph.edgeSupplier, graph.edgeWeightSupplier);
		this.vertices = graph.copyVertices();
		this.edges = graph.copyEdges();
		this.edgeAdjacencies = graph.copyEdgeAdjacencies();
		this.sourceTargetAdjacencies = graph.copySourceTargetAdjacencies();
		this.targetSourceAdjacencies = graph.copyTargetSourceAdjacencies();
	}
	
	@Override
	public void integrateEdges(WeightedGraph<V, Position2D, E, W> graph) {	
		for(E edge : graph.getEdges())
			addEdge(
					((DirectedWeighted2DGraph<V,E,W>) graph).getSourceOf(edge),
					((DirectedWeighted2DGraph<V,E,W>) graph).getTargetOf(edge));				
	}
	
	public DirectedWeighted2DGraph<V, E, W> copy() {
		return new DirectedWeighted2DGraph<V, E, W>(this);
	}
	
	public E addEdge(V source, V target) {

		if(source == null || target == null)
			return null;
		
		if (containsEdge(source, target))
			return getEdge(source, target);

		E edge = edgeSupplier.get();
		edge.setID(edgeCount++);
		W weight = edgeWeightSupplier.get();
		weight.setDistance(source.getPosition().getDistanceTo(target.getPosition()));
		edge.setWeight(weight);
		edges.put(edge.getID(), edge);

		super.sourceTargetAdjacencies.get(source.getID())
				.add(new Tuple<Integer, Integer>(edge.getID(), target.getID()));
		super.targetSourceAdjacencies.get(target.getID())
				.add(new Tuple<Integer, Integer>(edge.getID(), source.getID()));
		super.edgeAdjacencies.put(edge.getID(), new Tuple<Integer, Integer>(source.getID(), target.getID()));

		return edge;
	}

	public E addEdge(V source, V target, W weight) {

		if(source == null || target == null)
			return null;
		
		if (containsEdge(source, target))
			return getEdge(source, target);

		E edge = edgeSupplier.get();
		edge.setID(edgeCount++);
		weight.setDistance(source.getPosition().getDistanceTo(target.getPosition()));
		edge.setWeight(weight);
		edges.put(edge.getID(), edge);

		super.sourceTargetAdjacencies.get(source.getID())
				.add(new Tuple<Integer, Integer>(edge.getID(), target.getID()));
		super.targetSourceAdjacencies.get(target.getID())
				.add(new Tuple<Integer, Integer>(edge.getID(), source.getID()));
		super.edgeAdjacencies.put(edge.getID(), new Tuple<Integer, Integer>(source.getID(), target.getID()));

		return edge;
	}

	// Returns a list of vertices that are related to a vertex via incoming or
	// outgoing edges
	public List<V> getConnectedVertices(V vertex) {
		Set<V> vertices = new HashSet<V>();

		// Gather all targets that are connected via an outgoing link with vertex
		// (vertex -> ?)
		
		if(sourceTargetAdjacencies.containsKey(vertex.getID()))
		for (Tuple<Integer, Integer> adjacency : sourceTargetAdjacencies.get(vertex.getID()))
			vertices.add(getVertex(adjacency.getSecond()));

		// Gather all vertices that are connected via an incoming link with vertex (? ->
		// vertex)
		if(targetSourceAdjacencies.containsKey(vertex.getID()));
		for (Tuple<Integer, Integer> adjacency : targetSourceAdjacencies.get(vertex.getID()))
			vertices.add(getVertex(adjacency.getSecond()));

		return List.copyOf(vertices);
	}

	// Returns a list of incoming and outgoing edges related to a vertex
	public List<E> getEdgesOf(V vertex) {
		Set<E> edges = new HashSet<E>();
		edges.addAll(getOutgoingEdgesOf(vertex));
		edges.addAll(getIncomingEdgesOf(vertex));
		return List.copyOf(edges);
	}

	public List<E> getOutgoingEdgesOf(V vertex) {
		List<E> edges = new ArrayList<E>();
		if (sourceTargetAdjacencies.containsKey(vertex.getID()))
			for (Tuple<Integer, Integer> edgeVertexTuple : sourceTargetAdjacencies.get(vertex.getID()))
				edges.add(super.edges.get(edgeVertexTuple.getFirst()));
		return edges;
	}

	public List<E> getIncomingEdgesOf(V vertex) {
		List<E> edges = new ArrayList<E>();
		if (targetSourceAdjacencies.containsKey(vertex.getID()))
			for (Tuple<Integer, Integer> adjacency : targetSourceAdjacencies.get(vertex.getID()))
				edges.add(super.edges.get(adjacency.getFirst()));
		return edges;
	}

	public V getSourceOf(E edge) {
		if (edgeAdjacencies.containsKey(edge.getID()))
			return this.vertices.get(edgeAdjacencies.get(edge.getID()).getFirst());
		return null;
	}

	public V getTargetOf(E edge) {
		if (edgeAdjacencies.containsKey(edge.getID()))
			return this.vertices.get(edgeAdjacencies.get(edge.getID()).getSecond());
		return null;
	}

	public Boolean isDirected() {
		return true;
	}
	
	public Boolean isUndirected() {
		return false;
	}

	@Override
	public List<E> getNeighboringEdgesOf(E edge) {

		Set<E> neighboringEdges = new HashSet<E>();

		Tuple<V, V> vertices = this.getVerticesOf(edge);

		if (vertices != null) {
			
			for (E e : this.getEdgesOf(vertices.getFirst()))
				neighboringEdges.add(e);

			for (E e : this.getEdgesOf(vertices.getSecond()))
				neighboringEdges.add(e);
		}
		return neighboringEdges.stream().collect(Collectors.toList());
	}

	public List<V> getVertices(List<Integer> vertexIDs) {

		List<V> vertices = new ArrayList<V>();

		for (Integer vertexID : vertexIDs)
			vertices.add(getVertex(vertexID));

		return vertices;
	}

	@Override
	public boolean removeEdge(E edge) {

		Tuple<V, V> sourceAndSink = this.getVerticesOf(edge);
		int sourceID = sourceAndSink.getFirst().getID();
		int sinkID = sourceAndSink.getSecond().getID();

		this.sourceTargetAdjacencies.get(sourceID).removeIf(tuple -> tuple.getSecond() == sinkID);
		this.targetSourceAdjacencies.get(sinkID).removeIf(tuple -> tuple.getSecond() == sourceID);

		if (this.sourceTargetAdjacencies.get(sourceID).isEmpty())
			this.removeVertex(sourceAndSink.getFirst());

		if (targetSourceAdjacencies.get(sinkID).isEmpty())
			this.removeVertex(sourceAndSink.getSecond());

		this.edgeAdjacencies.remove(edge.getID());
		this.edges.remove(edge.getID());

		return true;
	}
}
