package de.jgraphlib.gui.style;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.util.Objects;

import de.jgraphlib.graph.elements.EdgeWeight;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.gui.printer.EdgePrinter;

public class VisualGraphStyle<E extends WeightedEdge<W>, W extends EdgeWeight> {

	private int width, height;
	private double xScale, yScale;
	private EdgePrinter<E,W> edgePrinter;
	
	public String headerText = "JGraphLib";
	public int padding = 75;
	public Color backgroundColor = Color.WHITE;
	
	/* VisualVertex style*/
	public Stroke visualVertexStroke = new BasicStroke(2);
	public int vertexWidth = 40;
	public Color vertexBackgroundColor = Color.LIGHT_GRAY;
	public Color vertexBorderColor = Color.GRAY;

	/* VisualEdge style*/
	public Stroke visualEdgeStroke = new BasicStroke(1);
	public Color visualEdgeColor = Color.GRAY;	
	public int visualEdgeLineWidth = 2;
	public int visualEdgeTupleDistance = 8;		
	
	public Stroke visualEdgeArrowStroke = new BasicStroke(1);
	public Color visualEdgeArrowColor = Color.BLACK;
	public int visualEdgeArrowLegLength = 8;
	
	/* VisualPath style */	
	public Stroke visualPathStroke = new BasicStroke(3);
	public int visualPathLineWidth = 2; 
	public int visualPathLineDistance = 4;	
	public int visualPathArrowLegLength = 12;
	
	public static final Color[] KELLY_COLORS = {
		    Color.decode("0xFFB300"),    // Vivid Yellow
		    Color.decode("0x803E75"),    // Strong Purple
		    Color.decode("0xFF6800"),    // Vivid Orange
		    Color.decode("0xA6BDD7"),    // Very Light Blue
		    Color.decode("0xC10020"),    // Vivid Red
		    Color.decode("0xCEA262"),    // Grayish Yellow
		    Color.decode("0x817066"),    // Medium Gray
		    Color.decode("0x007D34"),    // Vivid Green
		    Color.decode("0xF6768E"),    // Strong Purplish Pink
		    Color.decode("0x00538A"),    // Strong Blue
		    Color.decode("0xFF7A5C"),    // Strong Yellowish Pink
		    Color.decode("0x53377A"),    // Strong Violet
		    Color.decode("0xFF8E00"),    // Vivid Orange Yellow
		    Color.decode("0xB32851"),    // Strong Purplish Red
		    Color.decode("0xF4C800"),    // Vivid Greenish Yellow
		    Color.decode("0x7F180D"),    // Strong Reddish Brown
		    Color.decode("0x93AA00"),    // Vivid Yellowish Green
		    Color.decode("0x593315"),    // Deep Yellowish Brown
		    Color.decode("0xF13A13"),    // Vivid Reddish Orange
		    Color.decode("0x232C16"),    // Dark Olive Green
		};
	
	public VisualGraphStyle() {}	
	
	public VisualGraphStyle(EdgePrinter<E,W> edgePrinter) {
		this.edgePrinter = edgePrinter;
	}	
		
	public void setScale(double xScale, double yScale) {
		this.xScale = xScale;
		this.yScale = yScale;
	}
	
	public double xScale() {
		return xScale;
	}
	
	public double yScale() {
		return yScale;
	}
	
	public int getWidht() {
		return width;
	}
	
	public void setDimension(int height, int width) {	
		this.height = height;
		this.width = width;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public EdgePrinter<E,W> getEdgePrinter(){
		return edgePrinter;
	} 
	
	public Boolean hasEdgePrinter() {
		return !Objects.isNull(edgePrinter);
	}
}
