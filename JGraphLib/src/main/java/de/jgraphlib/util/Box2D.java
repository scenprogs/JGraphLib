package de.jgraphlib.util;

public class Box2D<T> extends Cell<T> {

	Scope2D scope;

	public Box2D(Scope2D scope) {
		this.scope = scope;
	}

	public Box2D() {}

	public void set(Scope2D scope) {
		this.scope = scope;
	}

	public Scope2D getScope() {
		return this.scope;
	}

	public String toString() {
		return String.format("row: %d, column: %d, scope: %s = {%s}", getColumn(), getRow(), scope, get());
	}
}
