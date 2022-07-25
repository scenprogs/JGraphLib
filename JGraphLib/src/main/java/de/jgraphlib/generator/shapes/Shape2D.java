package de.jgraphlib.generator.shapes;

import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.util.RandomNumbers;

public interface Shape2D {
	
	public abstract boolean contains(Position2D position2D);
	
	public abstract double getArea();
	
	public abstract Position2D generatePosition2D(RandomNumbers random);
	
	public abstract String toString();
}