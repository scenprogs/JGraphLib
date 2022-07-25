package de.jgraphlib.util;

public class Dimension2D {

	double width;
	double height;
	
	public Dimension2D(int side) {
		this.width = side;
		this.height = side;
	}
	
	public Dimension2D(double side) {
		this.width = Math.round(side);
		this.height = Math.round(side);
	}
	
	public Dimension2D(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public Dimension2D(double width, double height) {
		this.width = Math.round(width);
		this.height = Math.round(height);
	}
	
	public double getWidth() {
		return this.width;
	}
	
	public double getHeight() {
		return this.height;
	}
}
