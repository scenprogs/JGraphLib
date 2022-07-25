package de.jgraphlib.graph.elements;

public class Position2D {

	private final double x;
	private final double y;

	public Position2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double x() {
		return this.x;
	}

	public double y() {
		return this.y;
	}
	
	public double getDistanceTo(Position2D position2D) {
		return Math.sqrt(Math.pow(this.x() - position2D.x(), 2) + Math.pow(this.y() - position2D.y(), 2));	
	}
	
	@Override
	public String toString() {
		return String.format("x=%f,y=%f", x, y);
	}
}
