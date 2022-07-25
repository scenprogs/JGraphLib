package de.jgraphlib.util;

import java.util.List;
import java.util.function.Supplier;

import de.jgraphlib.graph.elements.Position2D;

public class Grid2D<T> extends Table<T, Box2D<T>> {

	protected Scope2D scope;
	protected Dimension2D boxSize;

	public Grid2D(Scope2D scope, Dimension2D boxSize, int rows, int columns) {

		super(rows, columns, new Supplier<Box2D<T>>() {
			@Override
			public Box2D<T> get() {
				return new Box2D<T>();
			}
		});

		this.boxSize = boxSize;
		
		this.scope = new Scope2D(
				new IntRange(scope.getWidth().min(), scope.getWidth().min() + (boxSize.getWidth() * columns)), 
				new IntRange(scope.getHeight().min(), scope.getHeight().min() + (boxSize.getHeight() * rows)));
	
		for (int row = 0; row < rows; row++)
			for (int column = 0; column < columns; column++)
				getCell(row, column).scope = new Scope2D(
						/* width */ new IntRange(
								/* left border */ scope.getWidth().min() + boxSize.getWidth() * column,
								/* right border */ scope.getWidth().min() + boxSize.getWidth() * (column + 1)),
						/* height */ new IntRange(
								/* top border */ scope.getHeight().min() + boxSize.getHeight() * row,
								/* bottom border */ scope.getHeight().min() + boxSize.getHeight() * (row + 1)));	
	}

	public List<List<Box2D<T>>> getBoxes(){
		return getCells();
	}
	
	public List<Box2D<T>> getBoxesFlat(){
		return getCellsFlat();
	}
	
	public Box2D<T> getBox(Position2D position) {
			
		if(scope.contains(position)) {
			
			int row, column;
			row = column = 0;
			
			while (row < rows && column < columns)
				if (getCell(row, column).getScope().getWidth().contains(position.x()))
					if (getCell(row, column).getScope().getHeight().contains(position.y()))
						return getCell(row, column);
					else
						row++;
				else
					column++;
		}

		return null;
	}
	
	public Dimension2D getBoxSize() {
		return boxSize;
	}

	public Scope2D getScope() {
		return this.scope;
	}

	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();

		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < columns; column++) {
				stringBuilder.append(getCell(row, column).toString()).append("\n");
			}
		}
			
		return stringBuilder.toString();
	}
}

class Grid2DSize extends TableSize {
	
	private final Scope2D scope;
	
	public Grid2DSize(Scope2D scope, int rows, int columns) {		
		super(rows, columns);
		this.scope = scope;		
	}
	
	public Scope2D getScope() {
		return this.scope;
	}
}
