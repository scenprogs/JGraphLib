package de.jgraphlib.gui;

import java.awt.Color;

public class VisualPath extends VisualObject {
	
	public VisualPath(VisualObject predecessor, int ID, Color color) {
		super(predecessor, ID);
		setForegroundColor(color, this);
	}
}
