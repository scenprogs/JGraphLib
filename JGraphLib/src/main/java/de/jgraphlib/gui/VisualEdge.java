package de.jgraphlib.gui;

import java.awt.Color;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;

import de.jgraphlib.gui.style.ArrowStyle;
import de.jgraphlib.maths.Point2D;
import de.jgraphlib.util.Tuple;

public class VisualEdge extends VisualObject {

	private Point2D startPosition;
	private Point2D targetPosition;
	private Point2D textPosition;
	private ArrowStyle arrowStyle;	
	private Stroke stroke;
	private List<VisualPath> visualPaths = new ArrayList<VisualPath>();
	
	public VisualEdge(VisualObject predecessor, int ID, Point2D startPosition, Point2D targetPosition, Stroke stroke, Color color, Point2D textPosition, String text, ArrowStyle arrowStyle) {
		super(predecessor, ID);
		this.startPosition = startPosition;
		this.targetPosition = targetPosition;
		this.stroke = stroke;
		this.foregroundColors.put(this, color);
		this.text = text;
		this.textPosition = textPosition;
		this.arrowStyle = arrowStyle;
	}
	
	public VisualEdge(VisualObject predecessor, int ID, Tuple<Point2D, Point2D> position, Stroke stroke, Color color, Point2D textPosition, String text, ArrowStyle arrowStyle) {
		super(predecessor, ID);
		this.startPosition = position.getFirst();
		this.targetPosition = position.getSecond();
		this.stroke = stroke;
		this.foregroundColors.put(this, color);
		this.text = text;
		this.textPosition = textPosition;
		this.arrowStyle = arrowStyle;
	}

	public Stroke getStroke() {
		return this.stroke;
	}
	
	public Point2D getStartPosition() {
		return this.startPosition;
	}

	public Point2D getTargetPosition() {
		return this.targetPosition;
	}
	
	public Point2D getTextPosition() {
		return this.textPosition;
	}

	public String getText() {
		return this.text;
	}

	public ArrowStyle getArrowStyle() {
		return this.arrowStyle;
	}
	
	public List<VisualPath> getVisualPaths() {
		return this.visualPaths;
	}
	
	public void addVisualPath(VisualPath visualPath) {
		this.visualPaths.add(visualPath);
	}
}
