package de.jgraphlib.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.jgraphlib.maths.Point2D;

public abstract class VisualObject {

	protected int ID;
	
	protected VisualObject predecessor;
	
	protected Point2D position;	// upper left
	
	protected String name;
	
	protected String text;	
		
	protected Set<VisualObject> succesors = new HashSet<>();
	
	/* Background colors can be mapped to other visual objects (e.g. collections, paths, etc.) */
	protected Map<VisualObject, Color> foregroundColors = new HashMap<>();
	
	/* Background colors can be mapped to other visual objects (e.g. collections, paths, etc.) */
	protected Map<VisualObject, Color> backgroundColors = new HashMap<>();
	
	/* Border colors can be mapped to other visual objects (e.g. collections, paths, etc.) */
	protected Map<VisualObject, Color> borderColors = new HashMap<>();
	
	public VisualObject(VisualObject predecessor, int ID) 
	{
		this.predecessor = predecessor;
		this.ID = ID;
	}
		
	public int getID() {
		return this.ID;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Point2D getPosition() {
		return this.position;
	}
	
	protected void setPosition(Point2D centerPosition) {
		this.position = centerPosition;
	}
	
	public VisualObject getPredecessor() {
		return this.predecessor;
	}
	
	protected void addSuccesor(VisualObject visualObject) {
		succesors.add(visualObject);
	}
	
	protected void addSuccesors(Set<VisualObject> visualObjects) {
		succesors.addAll(visualObjects);
	}
	
	public String getText() {
		return this.text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
		
	protected void setForegroundColor(Color color, VisualObject visualObject) {
		foregroundColors.put(visualObject, color);	
	}
	
	protected List<Color> getForegroundColors() {
		return new ArrayList<Color>(foregroundColors.values());
	}
	
	protected Color getForegroundColor() {
		return this.foregroundColors.get(this);
	}
	
	protected void removeForegroundColor(VisualObject visualObject) {
		foregroundColors.remove(visualObject);
	}
	
	protected void addBackgroundColor(Color color, VisualObject visualObject) {
		backgroundColors.put(visualObject, color);	
	}
	
	protected List<Color> getBackgroundColors() {
		return new ArrayList<Color>(backgroundColors.values());
	}
	
	protected Color getBackgroundColor() {
		return this.backgroundColors.get(this);
	}
	
	protected void removeBackgroundColor(VisualObject visualObject) {
		backgroundColors.remove(visualObject);
	}
		
	protected void addBorderColor(Color color, VisualObject visualObject) {
		borderColors.put(visualObject, color);
	}

	protected Map<VisualObject, Color> getBorderColorsX() {
		return borderColors;
	}
	
	protected List<Color> getBorderColors() {
		return new ArrayList<Color>(borderColors.values());
	}
	
	protected Color getBorderColor() {
		return this.borderColors.get(this);
	}
	
	protected Color removeBorderColor(VisualObject visualObject) {
		return borderColors.remove(visualObject);
	}	
	
	public String toString() {
		return String.format("VisualObject ID={%d} name={%s}", ID, name);
	}
}
