package de.jgraphlib.gui.style;

import java.awt.Color;
import java.awt.Stroke;

public class ArrowStyle {

	private boolean visible = false;
	private Color color;
	private Stroke stroke;
	private ArrowLegStyle legStyle;
	private int legLength;
	
	public ArrowStyle() {}
	
	public ArrowStyle(Color color, Stroke stroke, ArrowLegStyle legStyle, int legLength) {
		this.color = color;
		this.stroke = stroke;
		this.legStyle = legStyle;
		this.legLength = legLength;
		this.visible = true;
	}	
	
	public boolean isVisible() {
		return this.visible;
	}
	
	public Color getColor() {
		return this.color;
	}
	
	public Stroke getStroke() {
		return this.stroke;
	}
	
	public ArrowLegStyle getLegStyle() {
		return this.legStyle;
	}
	
	public int getLegLength() {
		return this.legLength;
	}
}
