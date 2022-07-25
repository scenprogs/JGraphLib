package de.jgraphlib.gui;

import java.awt.Color;

import de.jgraphlib.maths.Point2D;

public class VisualVertex extends VisualObject {
		
	public VisualVertex(VisualObject predecessor, int ID, Point2D position, Color backgroundColor, Color borderColor, String text) {		
		super(predecessor, ID);		
		setPosition(position);
		setName(text);
		setText(text);
		addBackgroundColor(backgroundColor, this);
		addBorderColor(borderColor, this);		
	}		
	
	public String toString() {
		return String.format("VisualVertex ID={%d} name={%s}", ID, name);
	}
}
