package de.jgraphlib.generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import de.jgraphlib.generator.properties.Weighted2DGraphProperties;
import de.jgraphlib.generator.shapes.RectangularShape2D;
import de.jgraphlib.graph.Weighted2DGraph;
import de.jgraphlib.graph.elements.EdgeDistance;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.util.DoubleRange;
import de.jgraphlib.util.IntRange;
import de.jgraphlib.util.Log;
import de.jgraphlib.util.RandomNumbers;
import de.jgraphlib.util.Scope2D;
import de.jgraphlib.util.SearchGrid2D;
import de.jgraphlib.util.Tuple;

public abstract class Weighted2DGraphGenerator<V extends Vertex<Position2D>, E extends WeightedEdge<W>, W extends EdgeDistance> {

	protected Log log;
	protected RandomNumbers random;
	protected Weighted2DGraph<V, E, W> graph;
	protected Weighted2DGraphProperties<V> properties;
	protected SearchGrid2D<V> searchGrid;
	protected EdgeCounter edgeCounter;
	protected int targeSize;
	protected Set<V> blacklist;
	protected List<Function<Position2D, Boolean>> vertexPositionConditions;

	public Weighted2DGraphGenerator(Weighted2DGraph<V, E, W> graph, Weighted2DGraphProperties<V> properties, RandomNumbers random) {
		this.graph = graph;
		this.properties = properties;
		this.random = random;
		initialize(properties);
	}

	public Weighted2DGraphGenerator(Weighted2DGraph<V, E, W> graph, Weighted2DGraphProperties<V> properties) {
		this.graph = graph;
		this.properties = properties;
		this.random = new RandomNumbers();
		initialize(properties);
	}

	public Weighted2DGraphGenerator(Weighted2DGraphProperties<V> properties) {
		this.properties = properties;
		this.random = new RandomNumbers();
		initialize(properties);
	}

	public Weighted2DGraphGenerator() {
		this.random = new RandomNumbers();
		initialize(properties);
	}
		
	protected abstract List<V> generate(int attempts);
		
	public void initialize(Weighted2DGraphProperties<V> properties) {

		this.blacklist = new HashSet<>();
		this.log = new Log("");
		this.vertexPositionConditions = new ArrayList<>();
	
		// A position for a new vertex must be within the given shape
		this.vertexPositionConditions.add(
				(Function<Position2D, Boolean>)(position2D) -> getProperties().getShape().contains(position2D));
				
		// A position for a new vertex must not have vertices in distance of "getProperties().getVertexDistance().min()"
		this.vertexPositionConditions.add(
				(Function<Position2D, Boolean>)(position2D) -> !vertexInRadius(position2D, getProperties().getVertexDistance().min()));
		
		// Initialize edge counter if edgeQuantity is not null
		if(properties.getEdgeQuantity() != null) 
			this.edgeCounter = new EdgeCounter(properties.getEdgeQuantity());
		else 
			this.edgeCounter = null;

		// Initialize target size if vertexQuantity is not null (in case vertexQuantity is null, this instance probably is used within another generate  e.g. for generating clusters)
		if (getProperties().getVertexQuantity() != null) 
			this.targeSize = random.getRandom(
					getProperties().getVertexQuantity().getMin(),
					getProperties().getVertexQuantity().getMax());
	
		// Initialize the a grid to reduce complexity and runtime in searching vertices in the graph
		if (properties.getShape() instanceof RectangularShape2D)
			this.searchGrid = new SearchGrid2D<V>(
					((RectangularShape2D) properties.getShape()).getScope(),
					properties.getEdgeDistance());
		}	
	
	public Set<V> getBlacklist() {
		return this.blacklist;
	}

	public void setBlacklist(Set<V> blacklist) {
		this.blacklist = blacklist;
	}

	public Weighted2DGraph<V, E, W> getGraph() {
		return this.graph;
	}

	public void setScope(Scope2D scope) {
		this.searchGrid = new SearchGrid2D<V>(scope,properties.getVertexDistance());	
	}
	
	public Scope2D getScope() {
		return this.searchGrid.getScope();
	}
	
	public SearchGrid2D<V> getSearchGrid(){
		return this.searchGrid;
	}

	public void setGraph(Weighted2DGraph<V, E, W> graph) {
		this.graph = graph;
	}

	public void setTargetSize(int targetSize) {
		this.targeSize = targetSize;
	}

	public int getTargetSize() {
		return this.targeSize;
	}

	public Weighted2DGraphProperties<V> getProperties() {
		return this.properties;
	}

	public RandomNumbers getRandomNumbers() {
		return this.random;
	}

	public void addVertexPoistionCondition(Function<Position2D, Boolean> condition) {
		this.vertexPositionConditions.add(condition);
	}
	
	public boolean vertexInRadius(Position2D position, double radius) {

		/* Case 1: In case a search grid was initialized, search vertices in radius through the search grid ~> O(?)*/
		//if (searchGrid != null) 			
		//	return searchGrid.search(position, radius).size() > 0;			
		/* Case 2: In case no search grid was initialized, iterate the whole graph ~> O(n)*/
		//else
			for(V vertex : graph.getVertices())
				if(position.getDistanceTo(vertex.getPosition()) <= radius)
					return true;

		return false;
	}

	public List<V> getVerticesInRadius(V vertex, double radius) {
		return getVerticesInRadius(vertex.getPosition(), radius);
	}

	public List<V> getVerticesInRadius(Position2D position, double radius) {

		List<V> verticesInRadius = new ArrayList<V>();

		/* Case 1: In case a search grid was initialized, search vertices in radius through the search grid ~> O(?)*/
		//if (searchGrid != null) {	
		//	return searchGrid.search(position, radius).stream().collect(Collectors.toList());		
		
		/* Case 2: In case no search grid was initialized, iterate the whole graph ~> O(n)*/
		//} else
			verticesInRadius = graph.getVertices().stream()
					.filter(v -> position.getDistanceTo(v.getPosition()) <= radius).collect(Collectors.toList());
								
		return verticesInRadius;
	}

	public void setProperties(Weighted2DGraphProperties<V> properties) {
		this.properties = properties;
		this.edgeCounter = new EdgeCounter(properties.getEdgeQuantity());
	}

	protected Position2D generatePosition2D(Position2D source, double vertexDistance) {
		return generatePosition2D(source, new DoubleRange(vertexDistance, vertexDistance));
	}

	protected Position2D generatePosition2D(Position2D source, DoubleRange vertexDistanceRange) {

		Position2D position2D = null;
		double angleRadians;
		double distance = random.getRandom(vertexDistanceRange.min(), vertexDistanceRange.max());
		double angleDegrees = random.getRandom(0d, 360d);

		if ((angleDegrees >= 0d) && (angleDegrees < 90d)) {
			angleRadians = Math.toRadians(angleDegrees);
			position2D = new Position2D(source.x() + distance * Math.cos(angleRadians),
					source.y() + distance * Math.sin(angleRadians));
		}

		if ((angleDegrees > 90d) && (angleDegrees <= 180d)) {
			angleRadians = Math.toRadians(180 - angleDegrees);
			position2D = new Position2D(source.x() - distance * Math.cos(angleRadians),
					source.y() + distance * Math.sin(angleRadians));
		}

		if ((angleDegrees > 180d) && (angleDegrees <= 270d)) {
			angleRadians = Math.toRadians(270 - angleDegrees);
			position2D = new Position2D(source.x() - distance * Math.sin(angleRadians),
					source.y() - distance * Math.cos(angleRadians));
		}

		if ((angleDegrees > 270d) && (angleDegrees <= 360d)) {
			angleRadians = Math.toRadians(360 - angleDegrees);
			position2D = new Position2D(source.x() + distance * Math.cos(angleRadians),
					source.y() - distance * Math.sin(angleRadians));
		}
	
		return position2D;
	}

	protected List<V> generateVertices(V predecessor, int n, int attempts) {
		return IntStream.range(0, n)
			    .mapToObj(i -> generateVertex(predecessor, attempts))
			    .collect(Collectors.toList());
	}

	protected V createVertex(Position2D position2D) {
		
		V vertex = graph.addVertex(position2D);
		
		if (searchGrid != null) 
			searchGrid.assign(vertex.getPosition(), vertex);
			
		log.info(String.format("+ vertex %d (%s)", vertex.getID(), vertex.getPosition()));
		
		return vertex;
	}
	
	protected V generateVertex(int attempts) {
		return generateVertex(null, attempts);
	}

	protected V generateVertex(V predecessor, int attempts) {

		Position2D position2D;
		
		if(predecessor == null)
			// Generate a 2D Position inside the graph's shape
			position2D = getProperties().getShape().generatePosition2D(random);
		else
			// Generate a 2D position in radius of "properties.vertexDistance" from reference vertex "predecessor" 
			position2D = generatePosition2D(predecessor.getPosition(), properties.getVertexDistance());
						
		for(Function<Position2D,Boolean> condition : vertexPositionConditions)
			if(!condition.apply(position2D)) 
				if(attempts > 0) 
					// Recursive call until a 2D position was found that matches criteria and attempts is greater than 0
					return generateVertex(predecessor, --attempts);
				else {
					return null;
				}
		
		return createVertex(position2D);	
	}

	protected E createEdge(V source, V target) {
		
		if (source == null || target == null) 
			return null;
			
		if (source == target) 
			return null;
		
		if(graph.containsEdge(source, target)) 
			return null;		
		
		if(edgeCounter != null) {
			
			if (edgeCounter.get(source) >= properties.getEdgeQuantity().max()
				|| edgeCounter.get(target) >= properties.getEdgeQuantity().max()) return null;
			
			edgeCounter.set(source, edgeCounter.get(source) + 1);
			edgeCounter.set(target, edgeCounter.get(target) + 1);
		}
			
		E edge = graph.addEdge(source, target);
			
		log.info(String.format("+ edge %d: vertex %d ~> vertex %d", edge.getID(), source.getID(), target.getID()));
		
		return edge;
	}
	
	protected List<E> generateEdges(V source, double edgeDistance) {
		return generateEdges(source, edgeDistance, new ArrayList<V>());
	}
	
	protected List<E> generateEdges(V source, double edgeDistance, List<V> exceptions) {

		if (source == null)
			return null;

		/*double edgeDistance = random.getRandom(
				properties.getEdgeDistance().min(), 
				properties.getEdgeDistance().max());*/

		List<V> verticesInRadius = getVerticesInRadius(source, edgeDistance);
			
		verticesInRadius.removeAll(exceptions);

		verticesInRadius.removeAll(getBlacklist());
					
		return createEdges(source, verticesInRadius);
	}
	
	protected List<E> createEdges(V source, List<V> vertices) {
		
		List<E> edges = new ArrayList<E>();
		
		if (vertices.size() > 0) {			
			
			if(properties.getEdgeQuantity() != null)		
				vertices = random.selectNrandomOfM(vertices, properties.getEdgeQuantity().max());
			
			for (Tuple<V, V> association : getProperties().getConnectivity().associate(source, vertices)) {
				
				E edge = createEdge(association.getFirst(), association.getSecond());
				
				if(edge != null) 
					edges.add(edge);
			}
		}	
				
		return edges;
	} 

	public EdgeCounter getEdgeCounter() {
		return this.edgeCounter;
	}

	class EdgeCounter {

		IntRange edgeQuantity;
		Map<V, Integer> edgeCount;
		Set<V> incompletelyConnected;
		Set<V> fullyConnected;

		public EdgeCounter(IntRange edgeQuantity) {
			this.edgeQuantity = edgeQuantity;
			this.edgeCount = new HashMap<>();
			this.incompletelyConnected = new HashSet<>();
			this.fullyConnected = new HashSet<>();
		}

		public void set(V v, Integer n) {

			edgeCount.put(v, n);

			if (n >= edgeQuantity.max()) {
				incompletelyConnected.remove(v);
				fullyConnected.add(v);
			} else
				incompletelyConnected.add(v);
		}

		public Integer get(V v) {

			int n = 0;

			if (!edgeCount.containsKey(v))
				set(v, n);
			else
				n = edgeCount.get(v);

			return n;
		}

		public Set<V> getFullyConnected() {
			return this.fullyConnected;
		}

		public Set<V> getIncompleteConnected() {
			return this.incompletelyConnected;
		}

		public void reset() {
			this.incompletelyConnected.clear();
			this.fullyConnected.clear();
		}
	}
}
