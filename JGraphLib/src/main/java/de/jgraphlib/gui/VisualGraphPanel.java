package de.jgraphlib.gui;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;

import javax.swing.JPanel;

import de.jgraphlib.graph.Weighted2DGraph;
import de.jgraphlib.graph.elements.EdgeDistance;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.gui.style.ArrowLegStyle;
import de.jgraphlib.gui.style.ArrowStyle;
import de.jgraphlib.gui.style.VisualGraphStyle;
import de.jgraphlib.maths.Line2D;
import de.jgraphlib.maths.Point2D;
import de.jgraphlib.maths.VectorLine2D;

public class VisualGraphPanel<V extends Vertex<Position2D>, E extends WeightedEdge<W>, W extends EdgeDistance> extends JPanel {

	// @formatter:off
	
	private static final long serialVersionUID = 1L;
	protected Weighted2DGraph<V, E, W> graph;
	private VisualGraph<V, E, W> visualGraph;
	private VisualGraphStyle<E,W> visualGraphStyle;
		
	public VisualGraphPanel() {}

	public VisualGraphPanel(Weighted2DGraph<V, E, W> graph, VisualGraphStyle<E,W> visualGraphStyle) {
		this.graph = graph;
		this.visualGraphStyle = visualGraphStyle;	
		this.addComponentListener(new ComponentAdapter() {
			@Override
		    public void componentResized(ComponentEvent e) {}
			@Override
			public void componentMoved(ComponentEvent e) {}});
	}

	public void paintPlayground(Graphics2D g2) {

		FontMetrics fontMetrics = g2.getFontMetrics();
		Rectangle2D stringBounds = fontMetrics.getStringBounds(visualGraphStyle.headerText, g2);
		g2.setColor(Color.LIGHT_GRAY);
		
		// Draw header text
		g2.drawString(visualGraphStyle.headerText, visualGraphStyle.padding, visualGraphStyle.padding / 2 + (int) (stringBounds.getHeight() / 2));

		// Paint white background
		g2.setColor(Color.WHITE);
		g2.fillRect(
				visualGraphStyle.padding, 
				visualGraphStyle.padding, getWidth() - (2 * visualGraphStyle.padding), 
				getHeight() - (2 * visualGraphStyle.padding));

		// Paint x-Axis-Line
		g2.setStroke(visualGraphStyle.visualEdgeStroke);
		g2.setColor(Color.LIGHT_GRAY);
		
		Point2D xAxisSource = new Point2D(
				visualGraphStyle.padding, 
				(graph.getScope().y().max()-graph.getScope().y().min()) * visualGraphStyle.yScale() + visualGraphStyle.padding);
		Point2D xAxisTarget = new Point2D(
				(graph.getScope().x().max()-graph.getScope().x().min()) * visualGraphStyle.xScale() + visualGraphStyle.padding, 
				(graph.getScope().y().max()-graph.getScope().y().min()) * visualGraphStyle.yScale() + visualGraphStyle.padding);
		
		Line2D xAxis = new Line2D(xAxisSource.x(), xAxisSource.y(), xAxisTarget.x(), xAxisTarget.y());	
		g2.drawLine((int) xAxis.p1().x(), (int) xAxis.p1().y(), (int) xAxis.p2().x(), (int) xAxis.p2().y());	
		
		// Paint y-Axis-Line
		Point2D yAxisSource = new Point2D(
				visualGraphStyle.padding, 
				(graph.getScope().y().max()-graph.getScope().y().min()) * visualGraphStyle.yScale() + visualGraphStyle.padding);
		Point2D yAxisTarget = new Point2D(
				visualGraphStyle.padding, 
				visualGraphStyle.padding);
		
		Line2D yAxis = new Line2D(yAxisSource.x(), yAxisSource.y(), yAxisTarget.x(), yAxisTarget.y());
		g2.drawLine((int) yAxis.p1().x(), (int) yAxis.p1().y(), (int) yAxis.p2().x(), (int) yAxis.p2().y());	

		// Paint x-/y-Axis steps
		DecimalFormat decimalFormat = new DecimalFormat("#.00");
		int steps = 10;
		int xOffset = (int) xAxis.getLength() / steps;
		int yOffset = (int) yAxis.getLength() / steps;
		VectorLine2D xAxisVector = new VectorLine2D(xAxis.p1().x(), xAxis.p1().y(), xAxis.getSlope());
		VectorLine2D yAxisVector = new VectorLine2D(yAxis.p1().x(), yAxis.p1().y(), yAxis.getSlope());

		Color gridLineColor = new Color(235, 235, 235);
		int grindLineWidth = visualGraphStyle.padding / 5;
		
		for (int i = 1; i < steps; i++) {

			g2.setColor(Color.GRAY);

			Point2D xAxisPoint = xAxisVector.getPointInDistance(i * xOffset);
			Point2D yAxisPoint = yAxisVector.getPointInDistance(i * -yOffset);	
			
			g2.drawLine(
					(int) xAxisPoint.x(), 
					(int) xAxisPoint.y(), 
					(int) xAxisPoint.x(),
					(int) xAxisPoint.y() + grindLineWidth);

						
			g2.drawLine(
					(int) yAxisPoint.x(), 
					(int) yAxisPoint.y(),
					(int) yAxisPoint.x() - grindLineWidth, 
					(int) yAxisPoint.y());

			String xAxisPointText = decimalFormat.format(graph.getScope().x().min() + (i * ((graph.getScope().x().max()-graph.getScope().x().min())/steps)));
			fontMetrics = g2.getFontMetrics();
			stringBounds = fontMetrics.getStringBounds(xAxisPointText, g2);
			g2.drawString(
					xAxisPointText, 
					(int) xAxisPoint.x() - (int) stringBounds.getCenterX(),
					(int) xAxisPoint.y() - (int) stringBounds.getCenterY() + visualGraphStyle.padding / 2);
			
			String yAxisPointText = decimalFormat.format(graph.getScope().x().min() + (i * ((graph.getScope().y().max()-graph.getScope().y().min())/steps)));
			fontMetrics = g2.getFontMetrics();
			stringBounds = fontMetrics.getStringBounds(yAxisPointText, g2);
			g2.drawString(
					yAxisPointText, 
					(int) yAxisPoint.x() - (int) stringBounds.getWidth() - grindLineWidth,
					(int) yAxisPoint.y() + (int) (stringBounds.getHeight() / 4));
			g2.setColor(gridLineColor);
					
			// Draw x grid lines (vertical lines)
			g2.drawLine(
					(int) xAxisPoint.x(), 
					(int) xAxisPoint.y(), 
					(int) xAxisPoint.x(), 
					(int) (xAxisPoint.y() - getHeight() + (2 * visualGraphStyle.padding)));
						
			// Draw y grid lines (horizontal lines)
			g2.drawLine(
					(int) yAxisPoint.x(), 
					(int) yAxisPoint.y(), 
					(int) (yAxisPoint.x() + getWidth() - (2 * visualGraphStyle.padding)), 
					(int) (yAxisPoint.y()));
		}
	}
	
	public void paintVisualEdgeTuple(Graphics2D g2, VisualEdgeTuple visualEdgeTuple) {	
		
		paintVisualEdge(g2, visualEdgeTuple.getFirst());
		
		paintVisualEdge(g2, visualEdgeTuple.getSecond());
		
	}
			
	public void paintVisualEdge(Graphics2D g2, VisualEdge edge) {
										
		Line2D edgeLine = new Line2D(edge.getStartPosition(), edge.getTargetPosition());
		g2.setStroke(edge.getStroke());
		g2.setColor(edge.getForegroundColor());
		
		// Paint edge line or visualPaths
		if (edge.getVisualPaths().isEmpty()) {
			g2.drawLine(edgeLine.x1().intValue(), edgeLine.y1().intValue(), edgeLine.x2().intValue(), edgeLine.y2().intValue());
			if(edge.getArrowStyle().isVisible())
				paintArrow(g2, edgeLine, edge.getStartPosition(), edge.getTargetPosition(), edge.getArrowStyle());	
		}
		else
			paintVisualPaths(g2, edge, edgeLine);
		
		// Paint edge text
		FontMetrics fontMetrics = g2.getFontMetrics();
		Rectangle2D stringBounds = fontMetrics.getStringBounds(edge.getText(), g2);
		g2.setColor(edge.getForegroundColor());
		g2.drawString(
				edge.getText(), 
				(int) edge.getTextPosition().x() - (int) stringBounds.getCenterX(), 
				(int) edge.getTextPosition().y() - (int) stringBounds.getCenterY());				
	}
	
	public void paintVisualPaths(Graphics2D g2, VisualEdge edge, Line2D edgeLine) {
			
		VectorLine2D startPositionLine = new VectorLine2D(edgeLine.p1(), edgeLine.getPerpendicularSlope());
		VectorLine2D targetPositionLine = new VectorLine2D(edgeLine.p2(), edgeLine.getPerpendicularSlope());
		
		int offset = visualGraphStyle.visualPathLineDistance;
		
		for(int i=0; i < edge.getVisualPaths().size(); i++) {		
			Point2D pathStartPosition = startPositionLine.getPointInDistance(i*offset);
			Point2D pathTargetPosition = targetPositionLine.getPointInDistance(i*offset);
			
			g2.setStroke(visualGraphStyle.visualPathStroke);
			g2.setColor(edge.getVisualPaths().get(i).getForegroundColor());
			g2.drawLine((int) pathStartPosition.x(), (int) pathStartPosition.y(), (int) pathTargetPosition.x(), (int) pathTargetPosition.y());	
	
			if(graph.isDirected())
				paintArrow(
						g2, 
						edgeLine, 
						pathStartPosition, 
						pathTargetPosition, 
						new ArrowStyle(
								edge.getVisualPaths().get(i).getForegroundColor(), 
								visualGraphStyle.visualPathStroke, 
								ArrowLegStyle.twoLegged, 10));		
		}
	}
		
	public void paintArrow(Graphics2D g2, Line2D line, Point2D startPosition, Point2D targetPosition, ArrowStyle arrowStyle) {

		double shortSide = Math.sqrt(Math.pow(arrowStyle.getLegLength(), 2)/2);
	
		/*		s 	: 	source (given)
		 * 		t 	: 	target (given)
		 * 		l-t	:	arrowLegLength (given)
		 * 		a 	:   (sought)
		 * 		l 	:	leftLegEndPosition (sought)
		 * 		r 	: 	rightLegtEndPosition (sought)
		 * 		 
		 * 							l	
		 * 							|\
		 * 							| \
		 * 		s ------------------a--t
		 * 							| /
		 * 							|/
		 * 							r
		 */
		
		// 2D vector line pointing from target t towards point a
		VectorLine2D vectorLine1 = new VectorLine2D(targetPosition, startPosition, line.getSlope());
		
		// 2D vector line pointing from point a towards points l and r
		VectorLine2D vectorLine2 = new VectorLine2D(vectorLine1.getPointInDistance(shortSide), line.getPerpendicularSlope());
		
		g2.setStroke(arrowStyle.getStroke());
		g2.setColor(arrowStyle.getColor());
		
		switch(arrowStyle.getLegStyle()) {
			case leftLegged:
				Point2D leftLegEndPosition = vectorLine2.getPointInDistance(shortSide);
				g2.drawLine((int) targetPosition.x(), (int) targetPosition.y(), (int) leftLegEndPosition.x(), (int) leftLegEndPosition.y());
				break;
			case rightLegged:
				Point2D rightLegEndPosition = vectorLine2.getPointInDistance(-shortSide);
				g2.drawLine((int) targetPosition.x(), (int) targetPosition.y(), (int) rightLegEndPosition.x(), (int) rightLegEndPosition.y());
				break;
			case twoLegged:
				leftLegEndPosition = vectorLine2.getPointInDistance(shortSide);
				rightLegEndPosition = vectorLine2.getPointInDistance(-shortSide);
				g2.drawLine((int) targetPosition.x(), (int) targetPosition.y(), (int) leftLegEndPosition.x(), (int) leftLegEndPosition.y());
				g2.drawLine((int) targetPosition.x(), (int) targetPosition.y(), (int) rightLegEndPosition.x(), (int) rightLegEndPosition.y());
				break;
		}	
		
	}

	public void paintVertex(Graphics2D g2, VisualVertex vertex) {
			
		if (vertex.getBackgroundColors().size() > 1) {
			// TBC
		}
		else {
			g2.setColor(vertex.getBackgroundColors().get(0));
			g2.fillOval(
				/*x*/		(int) vertex.getPosition().x(), 
				/*y*/		(int) vertex.getPosition().y(), 
				/*width*/	visualGraphStyle.vertexWidth, 
				/*height*/	visualGraphStyle.vertexWidth);
		}

		if (vertex.getBorderColors().size() > 1) {
			int angle = 360 / vertex.getBorderColors().size();					
			int angleOffset = 0;

			for (Color color : vertex.getBorderColors()) {
				g2.setStroke(visualGraphStyle.visualPathStroke);
				g2.setColor(color);
				g2.drawArc(
						/*x*/			(int) vertex.getPosition().x(), 
						/*y*/			(int) vertex.getPosition().y(), 
						/*width*/		visualGraphStyle.vertexWidth, 
						/*height*/		visualGraphStyle.vertexWidth, 
						/*startAngle*/	angleOffset, 
						/*angle*/		angle);
				angleOffset += angle;
			}		
		} else {
			g2.setStroke(visualGraphStyle.visualVertexStroke);
			g2.setColor(vertex.getBorderColors().get(0));
			g2.drawOval(
					(int) vertex.getPosition().x(), 
					(int) vertex.getPosition().y(), 
					visualGraphStyle.vertexWidth, 
					visualGraphStyle.vertexWidth);
		}

		FontMetrics fm = g2.getFontMetrics();		
		Rectangle2D stringBounds = fm.getStringBounds(vertex.getText(), g2);
		
		Point vertexCenter = new Point(
				(int) vertex.getPosition().x() + (visualGraphStyle.vertexWidth / 2), 
				(int) vertex.getPosition().y() + (visualGraphStyle.vertexWidth / 2));
		
		g2.setColor(Color.BLACK);		
		g2.drawString(
				vertex.getText(), 
				vertexCenter.x - (int) stringBounds.getCenterX(),
				vertexCenter.y - (int) stringBounds.getCenterY());
	}
	
	@Override
	protected void paintComponent(Graphics g) {
			
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
								
		visualGraphStyle.setDimension(getHeight(), getWidth());		

		visualGraph = new VisualGraph<V, E, W>(graph, visualGraphStyle);
		
		if (visualGraph.getVisualVertices().isEmpty()) return;
				
		paintPlayground(g2);
		
		for (VisualEdge edge : visualGraph.getVisualEdges())
			paintVisualEdge(g2, edge);	
		
		for (VisualVertex vertex : visualGraph.getVisualVertices())
			paintVertex(g2, vertex);
	}
}
