package de.jgraphlib.generator.shapes;

import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.util.RandomNumbers;

public class Circle implements Shape2D {

	private Position2D center;
    private double radius;

    public Circle(Position2D center, double radius) {
        this.center = center;
        this.radius = radius;
    }
    
    public Circle(double radius) {
        this.center = new Position2D(radius, radius);
        this.radius = radius;
    }

    public Position2D getCenter() {
    	return this.center;
    }
    
    public double getRadius() {
        return radius;
    }

    public double getArea() {
        return Math.PI * radius * radius;
    }

    public double getPerimeter() {
        return 2 * Math.PI * radius;
    }
    
    public double getDiameter() {
        return 2 * radius;
    }

	@Override
	public boolean contains(Position2D position) {
		return Math.pow(position.x() - getCenter().x(), 2) + Math.pow(position.y() - getCenter().y(), 2) < Math.pow(radius, 2);
	}

	@Override
	public Position2D generatePosition2D(RandomNumbers random) {
		return new Position2D(getCenter().x() + random.getRandom(0, radius/2), 
				getCenter().y() + random.getRandom(0, radius/2));
	}
}
