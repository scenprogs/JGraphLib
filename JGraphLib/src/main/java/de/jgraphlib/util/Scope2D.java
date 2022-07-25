package de.jgraphlib.util;

import de.jgraphlib.graph.elements.Position2D;

public class Scope2D {
	
	private IntRange width;
	private IntRange height;
	
	public Scope2D() {}
	
	public Scope2D(IntRange width, IntRange height) {
		this.width = width;
		this.height = height;
	}
	
	public Scope2D(DoubleRange width, DoubleRange height) {
		this.width = new IntRange(width.getMin(), width.getMax());
		this.height = new IntRange(height.getMin(), height.getMax());
	}
	
	public void set(IntRange width, IntRange height) {
		this.width = width;
		this.height = height;
	}
	
	public void set(DoubleRange width, DoubleRange height) {
		this.width = new IntRange(width.getMin(), width.getMax());
		this.height = new IntRange(height.getMin(), height.getMax());
	}
	
	public Position2D getCenter() {
		return new Position2D(
				width.getMin() + (width.getMax()-width.getMin())/2, 
				height.getMin() + (height.getMax()-height.getMin())/2);
	}
	
	public IntRange getHeight() {
		return height;
	}
	
	public void setHeight(IntRange height) {
		this.height = height;
	}
	
	public void setHeight(DoubleRange height) {
		this.height = new IntRange(height.getMin(), height.getMax());
	}
	
	public IntRange getWidth() {
		return width;
	}
	
	public void setWidth(IntRange width) {
		this.width = width;
	}
	
	public void setWidth(DoubleRange width) {
		this.width = new IntRange(width.getMin(), width.getMax());
	}
	
	@Override
	public String toString() {
		return String.format("width: %s, height: %s", width, height);
	}
	
	public IntRange x() {
		return width;
	}
	
	public IntRange y() {
		return height;
	}
	
	public boolean contains(Scope2D scope){	
		return ( width.getMin() < scope.getWidth().min() && 
				width.getMax() > scope.getWidth().getMax() ) && 
				( height.getMin() < scope.getHeight().min() && 
						height.getMax() > scope.getHeight().getMax());	
	}
	
	public boolean contains(Position2D position2D){	
		return getWidth().contains(position2D.x()) && getHeight().contains(position2D.y());
	}
	
	public double distance(Position2D position2D) {
		  double dx = Math.max(Math.max(width.min() - position2D.x(), 0), position2D.x() - width.max());
		  double dy = Math.max(Math.max(height.min() - position2D.y(), 0), position2D.y() - height.max());
		  return Math.sqrt(dx*dx + dy*dy);
	}
	
	public double distance(Scope2D scope) {  
		return Math.max(
				Math.abs(getCenter().x() - scope.getCenter().x()) - ((getWidth().abs() + scope.getWidth().abs())/2)  , 
				Math.abs(getCenter().y() - scope.getCenter().y()) - ((getHeight().abs() + scope.getHeight().abs())/2) );	
	}
}