package de.jgraphlib.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

import de.jgraphlib.graph.DirectedWeighted2DGraph;
import de.jgraphlib.graph.UndirectedWeighted2DGraph;
import de.jgraphlib.graph.Weighted2DGraph;
import de.jgraphlib.graph.elements.EdgeDistance;
import de.jgraphlib.graph.elements.Path;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.gui.style.ArrowLegStyle;
import de.jgraphlib.gui.style.ArrowStyle;
import de.jgraphlib.gui.style.VisualGraphStyle;
import de.jgraphlib.maths.Line2D;
import de.jgraphlib.maths.Point2D;
import de.jgraphlib.maths.VectorLine2D;
import de.jgraphlib.util.Tuple;

// @formatter:off

/**
 * The Class VisualGraph.
 *
 * @param <V> the value type
 * @param <E> the element type
 * @param <W> the generic type
 */
public class VisualGraph<V extends Vertex<Position2D>, E extends WeightedEdge<W>, W extends EdgeDistance> extends VisualObject {
			
	private TreeMap<Integer,VisualVertex> visualVertices = new TreeMap<Integer, VisualVertex>();
	
	private TreeMap<Integer,VisualEdge> visualEdges = new TreeMap<Integer, VisualEdge>();
	
	private ArrayList<VisualEdgeTuple> visualEdgeTuples = new ArrayList<VisualEdgeTuple>();	
	
	private ArrayList<VisualPath> visualPaths = new ArrayList<VisualPath>();
		
	private VisualGraphStyle<E,W> style;
		
	private static int visualIDs = 0;
	
	public VisualGraph(Weighted2DGraph<V, E, W> graph, VisualGraphStyle<E,W> style) {
				
		/* Visual graph is root*/
		super(null, createVisualID());
		
		this.style = style;				
		this.style.setScale(
				((double) style.getWidht() - 2 * style.padding) / (graph.getScope().getWidth().max() - graph.getScope().getWidth().min()),
				((double) style.getHeight() - 2 * style.padding) / (graph.getScope().getHeight().max() - graph.getScope().getHeight().min()));
		
		createVisualVertices(graph);
		
		if(graph.isDirected())
			createVisualEdges((DirectedWeighted2DGraph<V, E, ?>) graph);
		else
			createVisualEdges((UndirectedWeighted2DGraph<V, E, ?>) graph);
			
		createVisualPaths(graph.getPaths());
	}
	
	public static int createVisualID() {
		return visualIDs++;
	}
	
	/**
	 * Creates the visual vertices & sets positions.
	 *
	 * @param graph the graph
	 */
	private void createVisualVertices(Weighted2DGraph<V, E, ?> graph) {
						
		for (Vertex<Position2D> vertex : graph.getVertices())
			visualVertices.put(
					vertex.getID(),
					new VisualVertex(
							this,
							createVisualID(),
							new Point2D(
									((vertex.getPosition().x() - graph.getScope().getWidth().min()) * style.xScale() + style.padding) - style.vertexWidth / 2, 
									((graph.getScope().getHeight().max() - vertex.getPosition().y()) * style.yScale() + style.padding) - style.vertexWidth / 2), 
							style.vertexBackgroundColor,
							style.vertexBorderColor, 
							Integer.toString(vertex.getID())));		
	}

	private VisualEdge toVisual(E edge) {
		return visualEdges.get(edge.getID());
	}
	
	private VisualVertex toVisual(V vertex) {
		return visualVertices.get(vertex.getID());
	}
	
	private Point2D getCenter(VisualVertex visualVertex) {
		return new Point2D(
				visualVertex.getPosition().x() + style.vertexWidth / 2, 
				visualVertex.getPosition().y() + style.vertexWidth / 2);
	}
	
	/**
	 * Creates the visual edges.
	 *
	 * @param graph the graph
	 */
	private void createVisualEdges(UndirectedWeighted2DGraph<V, E, ?> graph) {
				
		for (E edge : graph.getEdges()) {
			
			Tuple<V, V> vertices = graph.getVerticesOf(edge);
										
			visualEdges.put(
					edge.getID(), 
					new VisualEdge(
							this,
							createVisualID(),
							createVisualEdgePosition(
									getCenter(toVisual(vertices.getFirst())),
									getCenter(toVisual(vertices.getSecond()))), 
							style.visualEdgeStroke,
							style.visualEdgeColor, 
							createEdgeTextPosition(
									getCenter(toVisual(vertices.getFirst())), 
									getCenter(toVisual(vertices.getSecond())), 
									EdgeTextPosition.center), 
							createEdgeText(edge),
							new ArrowStyle()));			
		}
	}

	/**
	 * Creates the visual edges.
	 *
	 * @param graph the graph
	 */
	private void createVisualEdges(DirectedWeighted2DGraph<V, E, ?> graph) {

		Set<E> memorize = new HashSet<E>();
		
		for (E edge : graph.getEdges()) {
			
			if (!memorize.contains(edge)) {
				V source = graph.getSourceOf(edge);
				V target = graph.getTargetOf(edge);
								
				if (graph.containsEdge(target, source)) {		
					
					Tuple<Tuple<Point2D, Point2D>,Tuple<Point2D, Point2D>> visualEdgeTuplePosition = 
							createVisualEdgeTuplePosition(
									getCenter(toVisual(source)), 
									getCenter(toVisual(target)));	
					
					VisualEdgeTuple visualEdgeTuple = new VisualEdgeTuple();
					
					visualEdgeTuple.setFirst(
							new VisualEdge(
									this,
									createVisualID(),
									visualEdgeTuplePosition.getFirst(), 
									style.visualEdgeStroke,
									style.visualEdgeColor, 
									createEdgeTextPosition(
											getCenter(toVisual(source)), 
											getCenter(toVisual(target)), 
											EdgeTextPosition.top_center),
									createEdgeText(edge),
									new ArrowStyle(
											style.visualEdgeArrowColor, 
											style.visualEdgeArrowStroke, 
											ArrowLegStyle.twoLegged, style.visualEdgeArrowLegLength)));
					
					visualEdges.put(edge.getID(), visualEdgeTuple.getFirst());
					
					E opposedEdge = graph.getEdge(target, source);
							
					visualEdgeTuple.setSecond(
							new VisualEdge(
									this,
									createVisualID(),
									visualEdgeTuplePosition.getSecond(), 
									style.visualEdgeStroke,
									style.visualEdgeColor, 
									createEdgeTextPosition(
											getCenter(toVisual(graph.getSourceOf(opposedEdge))),
											getCenter(toVisual(graph.getTargetOf(opposedEdge))),										
											EdgeTextPosition.top_center),
									createEdgeText(opposedEdge),
									new ArrowStyle(
											style.visualEdgeArrowColor, 
											style.visualEdgeArrowStroke, 
											ArrowLegStyle.twoLegged, style.visualEdgeArrowLegLength)));
					
					visualEdges.put(opposedEdge.getID(), visualEdgeTuple.getSecond());
					
					visualEdgeTuples.add(visualEdgeTuple);
					
					memorize.add(opposedEdge);
				}
				else visualEdges.put(
						edge.getID(),
						new VisualEdge(
								this,
								createVisualID(),
								createVisualEdgePosition(
										getCenter(toVisual(source)), 
										getCenter(toVisual(target))), 
								style.visualEdgeStroke,
								style.visualEdgeColor, 
								createEdgeTextPosition(
										getCenter(toVisual(source)), 
										getCenter(toVisual(target)), 
										EdgeTextPosition.center), 
								createEdgeText(edge), 
								new ArrowStyle(
										style.visualEdgeArrowColor, 
										style.visualEdgeArrowStroke, 
										ArrowLegStyle.twoLegged, style.visualEdgeArrowLegLength)));
			}
		}
	}

	/**
	 * Creates the edge text position.
	 *
	 * @param source the source
	 * @param target the target
	 * @param edgeTextPosition the edge text position
	 * @return the point 2 D
	 */
	private Point2D createEdgeTextPosition(Point2D source, Point2D target, EdgeTextPosition edgeTextPosition) {	
		
		Point2D center = new Point2D(source.x() / 2 + target.x() / 2, source.y() / 2 + target.y() / 2);
		
		switch(edgeTextPosition) {
		// Place edge text directly into the center of the line
		case center:
			return center;
		// Place edge text in between the center and the target position
		case top_center:	
			Line2D centerTargetLine = new Line2D(center.x(), center.y(), target.x(), target.y());
			return new Point2D(centerTargetLine.x1() / 2 + centerTargetLine.x2() / 2, centerTargetLine.y1() / 2 + centerTargetLine.y2() / 2);
		// Place edge text in between the center and the source position
		case bottom_center:
			Line2D centerSourceLine = new Line2D(center.x(), center.y(), source.x(), source.y());
			return new Point2D(centerSourceLine.x1() / 2 + centerSourceLine.x2() / 2, centerSourceLine.y1() / 2 + centerSourceLine.y2() / 2);
		default:
			break;			
		}
		return null;	
	}
	
	/**
	 * The Enum EdgeTextPosition.
	 */
	private enum EdgeTextPosition{
		
		/** The center. */
		center,
		
		/** The top center. */
		top_center,
		
		/** The bottom center. */
		bottom_center
	}
	
	/**
	 * Creates the edge text.
	 *
	 * @param edge the edge
	 * @return the string
	 */
	private String createEdgeText(E edge) {
		
		if (!Objects.isNull(edge.getWeight())) {	
			if (style.hasEdgePrinter()) 
				return style.getEdgePrinter().print(edge);
			else 
				return edge.getWeight().toString();	
		}
		else 
			return edge.toString();
	}

	
	/**
	 * Creates the positions for two opposed VisualEdges in a DirectedGraph.
	 *
	 * @param a the position of the first node
	 * @param b the position of the second node
	 * @return the four positions for 2 opposed edges 
	 */
	private Tuple<Tuple<Point2D, Point2D>,Tuple<Point2D, Point2D>> createVisualEdgeTuplePosition(Point2D a, Point2D b){
		
		// Create the positions from source node border to target node border
		Tuple<Point2D, Point2D> visualEdgePosition = createVisualEdgePosition(a, b);
		
		// A Line2D through visualEdgePosition
		Line2D visualEdgePositionLine = new Line2D(
				visualEdgePosition.getFirst(), 
				visualEdgePosition.getSecond());
		
		// An orthogonal Line2D through first visualEdgePosition
		VectorLine2D aVectorLine = new VectorLine2D(
				visualEdgePosition.getFirst(), 
				visualEdgePositionLine.getPerpendicularSlope());
				
		// An orthogonal Line2D through first visualEdgePosition
		VectorLine2D bVectorLine = new VectorLine2D(
				visualEdgePosition.getSecond(), 
				visualEdgePositionLine.getPerpendicularSlope());

		Tuple<Tuple<Point2D, Point2D>,Tuple<Point2D, Point2D>> visualEdgeTuplePosition = 
				new Tuple<Tuple<Point2D, Point2D>,Tuple<Point2D, Point2D>>();
		
		visualEdgeTuplePosition.setFirst(new Tuple<Point2D,Point2D>(
				aVectorLine.getPointInDistance(style.visualEdgeTupleDistance/2),
				bVectorLine.getPointInDistance(style.visualEdgeTupleDistance/2)));
		
		visualEdgeTuplePosition.setSecond(new Tuple<Point2D,Point2D>(
				bVectorLine.getPointInDistance(-style.visualEdgeTupleDistance/2),
				aVectorLine.getPointInDistance(-style.visualEdgeTupleDistance/2)));
		
		return visualEdgeTuplePosition;
	}
	
	/**
	 * Calculate positions for a VisualEdge from source node border to target node border.
	 * 
	 * @param source Center position of the source node
	 * @param target Center position of the target node
	 * @return the tuple
	 */
	private Tuple<Point2D, Point2D> createVisualEdgePosition(Point2D source, Point2D target) {

		// 2D line through source and target position
		Line2D edgeLine = new Line2D(source, target);

		// 2D vector line in source position
		VectorLine2D sourcePositionVectorLine = new VectorLine2D(
				new Point2D(
						source.x(), 
						source.y()), 
						edgeLine.getCenter(), 
						edgeLine.getSlope());
		
		// 2D vector line in target position
		VectorLine2D targetPositionVectorLine = new VectorLine2D(
				new Point2D(
						target.x(), 
						target.y()),
						edgeLine.getCenter(), 
						edgeLine.getSlope());

		Tuple<Point2D, Point2D> edgePosition = new Tuple<Point2D, Point2D>();

		// Calculate edge positions
		edgePosition.setFirst(sourcePositionVectorLine.getPointInDistance(style.vertexWidth / 2));
		edgePosition.setSecond(targetPositionVectorLine.getPointInDistance(style.vertexWidth / 2));
		
		return edgePosition;
	}

	/**
	 * Creates visual paths.
	 *
	 * @param <P> the generic type
	 * @param paths the paths
	 */
	public <P extends Path<V, E, W>> void createVisualPaths(List<P> paths) {
				
		for(int i=0; i < paths.size(); i++)
			if(paths.get(i).isComplete())
				createVisualPath(paths.get(i), VisualGraphStyle.KELLY_COLORS[i]);
	}

	/**
	 * Creates a visual path.
	 *
	 * @param path the path
	 */
	public void createVisualPath(Path<V, E, ?> path) {				
		createVisualPath(path, VisualGraphStyle.KELLY_COLORS[0]);
	}

	/**
	 * Creates the visual path.
	 *
	 * @param path the path
	 * @param color the color
	 */
	public void createVisualPath(Path<V, E, ?> path, Color color) {
		
		// Create visual path visual object
		VisualPath visualPath = new VisualPath(this, createVisualID(), color);
				
		for (V vertex : path.getVertices()) {	
			// Add new border color
			toVisual(vertex).addBorderColor(color, visualPath);
			
			// Remove default border color
			toVisual(vertex).removeBorderColor(toVisual(vertex));						
		}
		
		for (E edge : path.getEdges()) {
			toVisual(edge).addVisualPath(visualPath);		
		}

		// Add visual path 
		visualPaths.add(visualPath);
	}

	/**
	 * Gets the visual vertices.
	 *
	 * @return the visual vertices
	 */
	public List<VisualVertex> getVisualVertices() {
		return new ArrayList<VisualVertex>(visualVertices.values());
	}

	/**
	 * Gets the visual edges.
	 *
	 * @return the visual edges
	 */
	public List<VisualEdge> getVisualEdges() {
		return new ArrayList<VisualEdge>(visualEdges.values());
	}
	
	/**
	 * Gets the visual edge tuples.
	 *
	 * @return the visual edge tuples
	 */
	public List<VisualEdgeTuple> getVisualEdgeTuples() {
		return visualEdgeTuples;
	}

	/**
	 * Gets the style.
	 *
	 * @return the style
	 */
	public VisualGraphStyle<E, W> getStyle() {
		return this.style;
	}
}
