package de.jgraphlib.graph;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Supplier;

import de.jgraphlib.graph.elements.EdgeWeight;
import de.jgraphlib.graph.elements.Path;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.gui.style.VisualGraphStyle;
import de.jgraphlib.util.RandomNumbers;
import de.jgraphlib.util.Tuple;

public abstract class WeightedGraph<V extends Vertex<L>, L, E extends WeightedEdge<W>, W extends EdgeWeight> {

	protected int vertexCount;
	protected int edgeCount;
	protected TreeMap<Integer, V> vertices;
	protected TreeMap<Integer, E> edges;
	protected Supplier<V> vertexSupplier;
	protected Supplier<E> edgeSupplier;
	protected Supplier<W> edgeWeightSupplier;
	protected List<Path<V,E,W>> paths;

	protected Map</* key=sourceVertexID */Integer, /* value= */ArrayList<Tuple</* edgeID */Integer, /*
																										 * targetVertexID
																										 */Integer>>> sourceTargetAdjacencies;
	protected Map</* key=targetVertexID */Integer, /* value= */ArrayList<Tuple</* edgeID */Integer, /*
																										 * sourceVertexID
																										 */Integer>>> targetSourceAdjacencies;
	
	protected Map</* key=edgeID */Integer, /* value= */Tuple</* sourceVertexID */Integer, /* targetVertexID */Integer>> edgeAdjacencies;

	public WeightedGraph(Supplier<V> vertexSupplier, Supplier<E> edgeSupplier, Supplier<W> edgeWeightSupplier) {
		this.vertexSupplier = vertexSupplier;
		this.edgeSupplier = edgeSupplier;
		this.edgeWeightSupplier = edgeWeightSupplier;
		this.vertices = new TreeMap<Integer, V>();
		this.edges = new TreeMap<Integer, E>();
		this.sourceTargetAdjacencies = new TreeMap<>();
		this.targetSourceAdjacencies = new TreeMap<>();
		this.edgeAdjacencies = new TreeMap<>();
		this.paths = new ArrayList<Path<V,E,W>>();
	}
	
	public void addPath(Path<V,E,W> path) {
		this.paths.add(path);
	}
	
	public void addPaths(List<Path<V,E,W>> paths) {
		this.paths.addAll(paths);
	}
	
	public void setPaths(List<Path<V,E,W>> paths) {
		this.paths = paths;
	}
	
	public List<Path<V,E,W>> getPaths() {
		return this.paths;
	}
	
	public void clearPaths() {
		this.paths = new ArrayList<Path<V,E,W>>();
	}
		
	public Path<V,E,W> buildPath(List<V> vertices) {
		
		Path<V,E,W> path = new Path<>(vertices.get(0), vertices.get(vertices.size()-1));
		
		path.add(new Tuple<E,V>(null, vertices.get(0)));
		
		for(int i = 1; i < vertices.size(); i+=2) 
			path.add(new Tuple<E,V>(getEdge(vertices.get(i), vertices.get(i+1)), vertices.get(i+1)));	
		
		return path;	
	}
	
	public abstract Boolean isDirected();
	
	public abstract Boolean isUndirected();
	
	public abstract void plot() throws InvocationTargetException, InterruptedException;
	
	public abstract void plot(VisualGraphStyle<E,W> graphStyle) throws InvocationTargetException, InterruptedException;

	public abstract E addEdge(V source, V target, W weight);

	public abstract E addEdge(V source, V target);

	public abstract boolean removeEdge(E edge);
	
	public abstract void integrateEdges(WeightedGraph<V,L,E,W> graph);
		
	public void integrate(WeightedGraph<V,L,E,W> graph) {
		
		for(V vertex : graph.getVertices())
			addVertex(vertex);
		
		integrateEdges(graph);
	}
		
	public int size() {
		return vertexCount;
	}
	
	protected Supplier<V> getVertexSupplier() {
		return vertexSupplier;
	}
	
	protected Supplier<E> getEdgeSupplier() {
		return edgeSupplier;
	}
	
	protected Supplier<W> getEdgeWeightSupplier() {
		return edgeWeightSupplier;
	}

	public abstract WeightedGraph<V, L, E, W> copy();
	
	public TreeMap<Integer, V> copyVertices() {
		TreeMap<Integer, V> vertexCopies = new TreeMap<Integer, V>();
		for (V vertex : getVertices()) {
			V vertexCopy = vertexSupplier.get();
			vertexCopy.setID(vertex.getID());
			vertexCopy.setPosition(vertex.getPosition());
			vertexCopies.put(vertexCopy.getID(), vertexCopy);
		}
		return vertexCopies;
	}
	
	public TreeMap<Integer, E> copyEdges() {
		TreeMap<Integer, E> edgeCopies = new TreeMap<Integer, E>();
		for (E edge : getEdges()) {
			E edgeCopy = edgeSupplier.get();
			edgeCopy.setID(edge.getID());
			W edgeWeight = edgeWeightSupplier.get();
			edgeCopy.setWeight(edgeWeight);
			edgeCopies.put(edgeCopy.getID(), edgeCopy);
		}
		return edgeCopies;
	}
	
	protected TreeMap<Integer, ArrayList<Tuple<Integer,Integer>>> copySourceTargetAdjacencies(){

		TreeMap<Integer, ArrayList<Tuple<Integer,Integer>>> sourceTargetAdjacenciesCopy = 
				new TreeMap<Integer, ArrayList<Tuple<Integer,Integer>>>();
		
		for(Integer vertexID : this.sourceTargetAdjacencies.keySet()) 
			sourceTargetAdjacenciesCopy.put(
					vertexID,
					new ArrayList<Tuple<Integer,Integer>>(this.sourceTargetAdjacencies.get(vertexID)));
				
		return sourceTargetAdjacenciesCopy;
	}
	
	protected TreeMap<Integer, ArrayList<Tuple<Integer,Integer>>> copyTargetSourceAdjacencies(){

		TreeMap<Integer, ArrayList<Tuple<Integer,Integer>>> targetSourceAdjacenciesCopy = 
				new TreeMap<Integer, ArrayList<Tuple<Integer,Integer>>>();
		
		for(Integer vertexID : this.targetSourceAdjacencies.keySet()) 
			targetSourceAdjacenciesCopy.put(
					vertexID, 
					new ArrayList<Tuple<Integer,Integer>>(this.targetSourceAdjacencies.get(vertexID)));
				
		return targetSourceAdjacenciesCopy;
	}
	
	protected TreeMap<Integer, Tuple<Integer,Integer>> copyEdgeAdjacencies(){

		TreeMap<Integer, Tuple<Integer,Integer>> edgeAdjacenciesCopy = 
				new TreeMap<Integer, Tuple<Integer,Integer>>();
		
		for(Integer vertexID : this.edgeAdjacencies.keySet()) 
			edgeAdjacenciesCopy.put(
					vertexID,
					new Tuple<Integer,Integer>(this.edgeAdjacencies.get(vertexID).getFirst(),this.edgeAdjacencies.get(vertexID).getSecond()));
			
		return edgeAdjacenciesCopy;
	}

	public V addVertex(L position) {
		V vertex = vertexSupplier.get();
		vertex.setID(vertexCount++);
		vertex.setPosition(position);
		vertices.put(vertex.getID(), vertex);
		sourceTargetAdjacencies.put(vertex.getID(), new ArrayList<Tuple<Integer, Integer>>());
		targetSourceAdjacencies.put(vertex.getID(), new ArrayList<Tuple<Integer, Integer>>());
		return vertex;
	}

	public void addVertex(V vertex) {
		vertex.setID(vertexCount++);
		vertices.put(vertex.getID(), vertex);
		sourceTargetAdjacencies.put(vertex.getID(), new ArrayList<Tuple<Integer, Integer>>());
		targetSourceAdjacencies.put(vertex.getID(), new ArrayList<Tuple<Integer, Integer>>());
	}
	
	public void removeVertices(List<V> vertices) {
		for (V vertex : vertices)
			removeVertex(vertex);
	}

	public V removeVertex(V vertex) {

		if (vertices.containsKey(vertex.getID())) {
			for (Tuple<Integer, Integer> adjacency : sourceTargetAdjacencies.remove(vertex.getID())) {
				int edgeID = adjacency.getFirst();
				int targetVertexID = adjacency.getSecond();
				targetSourceAdjacencies.get(targetVertexID).removeIf(a -> a.getSecond().equals(vertex.getID()));
				edgeAdjacencies.remove(edgeID);
				edges.remove(edgeID);
			}

			for (Tuple<Integer, Integer> adjacency : targetSourceAdjacencies.remove(vertex.getID())) {
				int edgeID = adjacency.getFirst();
				int sourceVertexID = adjacency.getSecond();
				sourceTargetAdjacencies.get(sourceVertexID).removeIf(a -> a.getSecond().equals(vertex.getID()));
				edgeAdjacencies.remove(edgeID);
				edges.remove(edgeID);
			}
			return vertices.remove(vertex.getID());
		}
		
		return null;
	}

	public List<E> getEdges() {
		return new ArrayList<E>(edges.values());
	}

	public List<V> getVertices() {
		return new ArrayList<V>(vertices.values());
	}

	public V getFirstVertex() {
		return this.vertices.get(0);
	}

	public V getLastVertex() {
		return this.vertices.get(vertexCount - 1);
	}

	public V getVertex(V vertex) {
		return this.vertices.get(vertex.getID());
	}

	public V getVertex(int ID) {
		return this.vertices.get(ID);
	}
	
	public V getRandomVertex() {
		return this.vertices.get(new RandomNumbers().getRandom(0, size()-1));
	}

	public E getEdge(E edge) {
		return edges.get(edge.getID());
	}

	public E getEdge(int ID) {
		return edges.get(ID);
	}

	public Tuple<V, V> getVerticesOf(E edge) {
		if (edgeAdjacencies.containsKey(edge.getID())) {
			Tuple<Integer, Integer> vertexIDs = edgeAdjacencies.get(edge.getID());
			return new Tuple<V, V>(this.vertices.get(vertexIDs.getFirst()), this.vertices.get(vertexIDs.getSecond()));
		}
		return null;
	}

	public E getEdge(V source, V target) {

		if (sourceTargetAdjacencies.containsKey(source.getID()) && sourceTargetAdjacencies.containsKey(target.getID()))
			for (Tuple<Integer, Integer> adjacency : sourceTargetAdjacencies.get(source.getID()))
				if (adjacency.getSecond() == target.getID())
					return this.edges.get(adjacency.getFirst());
		return null;
	}

	public abstract List<E> getOutgoingEdgesOf(V vertex);

	public abstract List<E> getEdgesOf(V vertex);

	public abstract List<E> getNeighboringEdgesOf(E edge);

	public boolean containsEdge(V source, V target) {
		if(source != null && target != null)
			if (source.getID() < sourceTargetAdjacencies.size())
				for (Tuple<Integer, Integer> edgeVertexTuple : sourceTargetAdjacencies.get(source.getID()))
					if (edgeVertexTuple.getSecond() == target.getID())
						return true;
		return false;
	}

	public List<V> getNextHopsOf(V vertex) {
		List<V> nextHops = new ArrayList<V>();
		if (sourceTargetAdjacencies.containsKey(vertex.getID()))
			for (Tuple<Integer, Integer> adjacency : sourceTargetAdjacencies.get(vertex.getID()))
				nextHops.add(vertices.get(adjacency.getSecond()));
		return nextHops;
	}

	public List<Tuple<E, V>> getNextPathsOf(V vertex) {
		List<Tuple<E, V>> nextPaths = new ArrayList<Tuple<E, V>>();
		if (sourceTargetAdjacencies.containsKey(vertex.getID()))
			for (Tuple<Integer, Integer> adjacency : sourceTargetAdjacencies.get(vertex.getID()))
				nextPaths.add(new Tuple<E, V>(edges.get(adjacency.getSecond()), vertices.get(adjacency.getFirst())));
		return nextPaths;
	}

	public V getTargetOf(V vertex, E edge) {
		if(edgeAdjacencies.containsKey(edge.getID())) {
			Tuple<Integer, Integer> vertexIDs = edgeAdjacencies.get(edge.getID());
			if (vertex.getID() == vertexIDs.getFirst())
				return this.vertices.get(vertexIDs.getSecond());
			else if (vertex.getID() == vertexIDs.getSecond())
				return this.vertices.get(vertexIDs.getFirst());
		}
		return null;
	}

	public E getOpposedEdge(E edge) {

		Tuple<V, V> vertices = this.getVerticesOf(edge);

		E there = this.getEdge(vertices.getFirst(), vertices.getSecond());
		E back = this.getEdge(vertices.getSecond(), vertices.getSecond());

		if (there != null && back != null) {
			if (edge == there)
				return back;
			if (edge == back)
				return there;
		}

		return null;
	}

	public Map<Integer, ArrayList<Tuple<Integer, Integer>>> getSourceTargetAdjacencies() {
		return this.sourceTargetAdjacencies;
	}

	public Map<Integer, ArrayList<Tuple<Integer, Integer>>> getTargetSourceAdjacencies() {
		return this.sourceTargetAdjacencies;
	}

	public Iterator<V> vertexIterator() {
		Iterator<V> iterator = new Iterator<V>() {
			private int i = 0;

			@Override
			public boolean hasNext() {
				return i < vertices.size() && vertices.get(i) != null;
			}

			@Override
			public V next() {
				return vertices.get(i++);
			}
		};
		return iterator;
	}

	public Iterator<E> edgeIterator() {
		Iterator<E> iterator = new Iterator<E>() {
			private int i = 0;

			@Override
			public boolean hasNext() {
				return i < edges.size() && edges.get(i) != null;
			}

			@Override
			public E next() {
				return edges.get(i++);
			}
		};
		return iterator;
	}

	public void clear() {
		this.vertices.clear();
		this.vertexCount = 0;
		this.edges.clear();
		this.edgeCount = 0;
		this.sourceTargetAdjacencies.clear();
		this.targetSourceAdjacencies.clear();
		this.edgeAdjacencies.clear();
	}

	public String toString() {

		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("Vertices:\n");

		for (V vertex : this.getVertices())
			stringBuilder.append(Integer.toString(vertex.getID())).append(": ").append(vertex.getPosition().toString())
					.append("\n");

		stringBuilder.append("Edges:\n");

		for (E edge : this.getEdges())
			stringBuilder.append(edge.getID()).append(": ").append(getVerticesOf(edge).getFirst().getID())
					.append(" ~> ").append(getVerticesOf(edge).getSecond().getID()).append("\n");

		return stringBuilder.toString();
	}
}