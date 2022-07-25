package de.jgraphlib.graph;

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

public class UndirectedWeighted2DGraph<V extends Vertex<Position2D>, E extends WeightedEdge<W>, W extends EdgeDistance>
		extends Weighted2DGraph<V, E, W> {

	public UndirectedWeighted2DGraph(Supplier<V> vertexSupplier, Supplier<E> edgeSupplier,Supplier<W> edgeWeightSupplier) {
		super(vertexSupplier, edgeSupplier, edgeWeightSupplier);
	}

	public UndirectedWeighted2DGraph(UndirectedWeighted2DGraph<V, E, W> graph) {
		super(graph.vertexSupplier, graph.edgeSupplier, graph.edgeWeightSupplier);
		this.edgeWeightSupplier = graph.edgeWeightSupplier;
		this.vertices = graph.vertices;
		this.edges = graph.copyEdges();
		this.sourceTargetAdjacencies = graph.sourceTargetAdjacencies;
		this.targetSourceAdjacencies = graph.targetSourceAdjacencies;
		this.edgeAdjacencies = graph.edgeAdjacencies;
	}

	@Override
	public void integrateEdges(WeightedGraph<V, Position2D, E, W> graph) {	
		for(E edge : graph.getEdges())
			addEdge(
					((UndirectedWeighted2DGraph<V,E,W>) graph).getVerticesOf(edge).getFirst(),
					((UndirectedWeighted2DGraph<V,E,W>) graph).getVerticesOf(edge).getSecond());				
	}
	
	public UndirectedWeighted2DGraph<V, E, W> copy() {
		return new UndirectedWeighted2DGraph<V, E, W>(this);
	}
	
	public Boolean isDirected() {
		return false;
	}
		
	public void integrate(Weighted2DGraph<V,E,W> graph) {	
		for(V vertex : graph.getVertices()) {	
			addVertex(vertex);
			for(E edge : getOutgoingEdgesOf(vertex)) {
				V other = getTargetOf(vertex, edge);
				addEdge(vertex, other);
				addEdge(other, vertex);
			}
		}		
	}
		
	public E addEdge(V v1, V v2) {

		if (containsEdge(v1, v2))
			return getEdge(v1, v2);
		
		E edge = edgeSupplier.get();
		edge.setID(edgeCount++);
		W weight = edgeWeightSupplier.get();
		weight.setDistance(v1.getPosition().getDistanceTo(v2.getPosition()));
		edge.setWeight(weight);
		edges.put(edge.getID(), edge);	
		
		// v1 - edge -> v2
		super.sourceTargetAdjacencies.get(v1.getID()).add(new Tuple<Integer, Integer>(edge.getID(), v2.getID()));
		super.targetSourceAdjacencies.get(v2.getID()).add(new Tuple<Integer, Integer>(edge.getID(), v1.getID()));		
		super.edgeAdjacencies.put(edge.getID(), new Tuple<Integer, Integer>(v1.getID(), v2.getID()));
	
		// v2 - edge -> v1
		super.sourceTargetAdjacencies.get(v2.getID()).add(new Tuple<Integer, Integer>(edge.getID(), v1.getID()));
		super.targetSourceAdjacencies.get(v1.getID()).add(new Tuple<Integer, Integer>(edge.getID(), v2.getID()));	
		super.edgeAdjacencies.put(edge.getID(), new Tuple<Integer, Integer>(v2.getID(), v1.getID()));
		
		return edge;
	}

	public E addEdge(V v1, V v2, W weight) {

		if (containsEdge(v1, v2))
			return getEdge(v1, v2);
		
		E edge = edgeSupplier.get();
		edge.setID(edgeCount++);
		edge.setWeight(weight);
		edges.put(edge.getID(), edge);	
		
		// v1 - edge -> v2
		super.sourceTargetAdjacencies.get(v1.getID()).add(new Tuple<Integer, Integer>(edge.getID(), v2.getID()));
		super.targetSourceAdjacencies.get(v2.getID()).add(new Tuple<Integer, Integer>(edge.getID(), v1.getID()));		
		super.edgeAdjacencies.put(edge.getID(), new Tuple<Integer, Integer>(v1.getID(), v2.getID()));
			
		// v2 - edge -> v1
		super.sourceTargetAdjacencies.get(v2.getID()).add(new Tuple<Integer, Integer>(edge.getID(), v1.getID()));
		super.targetSourceAdjacencies.get(v1.getID()).add(new Tuple<Integer, Integer>(edge.getID(), v2.getID()));	
		super.edgeAdjacencies.put(edge.getID(), new Tuple<Integer, Integer>(v2.getID(), v1.getID()));

		return edge;
	}

	public List<E> getEdgesOf(V vertex) {

		Set<E> edges = new HashSet<E>();
		
		if (this.sourceTargetAdjacencies.containsKey(vertex.getID()))
			for (Tuple<Integer, Integer> adjacency : sourceTargetAdjacencies.get(vertex.getID()))
				edges.add(this.edges.get(adjacency.getFirst()));

		if (targetSourceAdjacencies.containsKey(vertex.getID()))
			for (Tuple<Integer, Integer> adjacency : targetSourceAdjacencies.get(vertex.getID()))
				edges.add(this.edges.get(adjacency.getFirst()));

		return List.copyOf(edges);
	}

	public List<E> getOutgoingEdgesOf(V vertex) {
		return getEdgesOf(vertex);
	}

	public Tuple<V, V> getVerticesOf(E edge) {
		if(edgeAdjacencies.containsKey(edge.getID())) {
			return new Tuple<V, V>(
					getVertex(edgeAdjacencies.get(edge.getID()).getFirst()),
					getVertex(edgeAdjacencies.get(edge.getID()).getSecond()));
		}
		return null;
	}

	public V getTargetOf(V vertex, E edge) {
		if (this.edgeAdjacencies.containsKey(vertex.getID())) {
			Tuple<Integer, Integer> vertexIDs = this.edgeAdjacencies.get(edge.getID());
			if (vertex.getID() == vertexIDs.getFirst())
				return this.vertices.get(vertexIDs.getSecond());
			else if (vertex.getID() == vertexIDs.getSecond())
				return this.vertices.get(vertexIDs.getFirst());
		}
		return null;
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

	@Override
	public Boolean isUndirected() {
		return true;
	}
}
