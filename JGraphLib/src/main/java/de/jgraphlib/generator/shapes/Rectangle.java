package de.jgraphlib.generator.shapes;

import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.util.DoubleRange;
import de.jgraphlib.util.RandomNumbers;
import de.jgraphlib.util.Scope2D;

public class Rectangle implements RectangularShape2D {

	private Position2D topLeft;
	private Scope2D scope;
	
	public Rectangle(Position2D topLeft, double width, double height) {
		this.topLeft = topLeft;
		this.scope = new Scope2D(
				new DoubleRange(topLeft.x(), topLeft.x() + width), 
				new DoubleRange(topLeft.y(), topLeft.y() + height));
	}
	
	public Rectangle(double width, double height) {
		this.topLeft = new Position2D(0d, 0d);
		this.scope = new Scope2D(
				/*width*/new DoubleRange(topLeft.x(), width), 
				/*height*/new DoubleRange(topLeft.y(), height));
	}
	
	public Position2D getTopLeft() {
		return topLeft;
	}
	
	public Position2D getCenter() {
		return new Position2D(topLeft.x() + scope.getWidth().abs()/2, topLeft.y() - scope.getHeight().abs()/2);
	}

	public double getHeight() {
		return this.scope.getHeight().abs();
	}
	
	public double getWidth() {
		return this.scope.getWidth().abs();
	}
	
	@Override
	public boolean contains(Position2D position) {	
		return scope.contains(position);
	}

	@Override
	public double getArea() {	
		return getWidth() * getHeight();
	}
	
	@Override
	public Position2D generatePosition2D(RandomNumbers random) {
		Position2D position2D = new Position2D(
				random.getRandom(scope.getWidth().min(), scope.getWidth().max()), 
				random.getRandom(scope.getHeight().min(), scope.getHeight().max()));
		return position2D;
	}

	@Override
	public Scope2D getScope() {
		return this.scope;
	}	
	
	@Override
	public String toString() {
		return String.format("Rectangle %s", "");
	}
}
