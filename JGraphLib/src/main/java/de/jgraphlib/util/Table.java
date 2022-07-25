package de.jgraphlib.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

class Cell<T> {
	
	private int column, row;
	private T content;
	
	public Cell() {}
	
	public Cell(int row, int column){
		this.row = row;
		this.column = column;
	}
	
	public void set(int row, int column) {
		this.row = row;
		this.column = column;
	}
		
	public void set(T t) {
		this.content = t;
	}
	
	public T get() {
		return this.content;
	}			
	
	public int getRow() {
		return this.row;
	}
	
	public int getColumn() {
		return this.column;
	}
	
	public String toString() {
		return String.format("row: %d, column: %d = {%s}", column, row, content);
	}
	
	public boolean equals(Cell<T> cell) {
		return this.row == cell.getRow() && this.column == cell.getColumn();
	}
}	

class HashTable<T, C extends Cell<T>> extends Table<T,C>{
	
	protected final Map<T, C> map; 
	
	public HashTable(int rows, int columns, Supplier<C> cellSupplier) {
		super(rows, columns, cellSupplier);
		this.map = new HashMap<>();
	}
	
	protected C getCell(T t){
		if(map.containsKey(t))
			return map.get(t);
		else
			return null;
	}
	
	@Override 
	public boolean set(int row, int column, T t){
		super.set(row, column, t);
		C cell = getCell(row, column);
		if(cell != null) {
			map.put(t, cell);
			return true;
		}
		return false;	
	}
	
	public C getNeighborOf(C cell, Site site) {	
		return getNeighborOf(cell, site);			
	}	
	
	public Set<C> getNeighborhoodOf(C cell) {			
		return getNeighborhoodOf(cell);
	}	
}

class Table<T, C extends Cell<T>> implements Iterable<C> {
	
	protected final int rows, columns;
	protected final List<List<C>> cells;	
	protected Supplier<C> cellSupplier;
	
	public Table(int rows, int columns, Supplier<C> cellSupplier) {
		this.rows = rows;
		this.columns = columns;
		this.cellSupplier = cellSupplier;
		this.cells = new ArrayList<>();
		
		for(int row = 0; row < rows; row++) {
			cells.add(new ArrayList<>());
			for(int column = 0; column < columns; column++) {
				C cell = cellSupplier.get();
				cell.set(row, column);
				cells.get(row).add(cell);
			}			
		}
	}	
	
	public int getRows() {
		return this.rows;
	}
	
	public int getColumns() {
		return this.columns;
	}
	
	public List<List<C>> getCells(){
		return this.cells;
	}
	
	public List<C> getCellsFlat(){
		return cells.stream().flatMap(Collection::stream).collect(Collectors.toList());
	}
	
	protected C getCell(int row, int column){
		if((row >= 0 && row < rows) && (column >=0 && column < columns)) 
			return cells.get(row).get(column);
		else
			return null;
	}
	
	public T get(int row, int column){
		return getCell(row, column).get();
	}
				
	public boolean set(int row, int column, T t) {
		if((row >= 0 && row < rows) && (column >=0 && column < columns))
			cells.get(row).get(column).set(t);
		else
			return false;
		
		return true;		
	}
	
	public Set<T> getContentOf(Set<C> cells){
		return cells.stream().map(c -> c.get()).filter(c -> c != null).collect(Collectors.toSet());
	}

		
	protected C getAdjacentCellOf(C cell, Site site) {
					
		if(cell != null) {
		
			switch(site) {
				case LEFT: 
					return getCell(cell.getRow(), cell.getColumn()-1);
				case TOP: 
					return getCell(cell.getRow()-1, cell.getColumn());
				case RIGHT: 
					return getCell(cell.getRow(), cell.getColumn()+1);
				case BOTTOM: 
					return getCell(cell.getRow()+1, cell.getColumn());
				default: break;	
			}		
		}
				
		return null;
	}
	
	public C getAdjacentCellOf(int row, int column, Site site) {
		return getAdjacentCellOf(getCell(row, column), site);		
	}
	
	public Set<C> getAdjacentCellsOf(int row, int column) {	
		Set<C> adjacentCells = new HashSet<C>();
		adjacentCells.add(getAdjacentCellOf(getCell(row, column), Site.LEFT));	
		adjacentCells.add(getAdjacentCellOf(getCell(row, column), Site.TOP));
		adjacentCells.add(getAdjacentCellOf(getAdjacentCellOf(getCell(row, column), Site.TOP), Site.LEFT));
		adjacentCells.add(getAdjacentCellOf(getAdjacentCellOf(getCell(row, column), Site.TOP), Site.RIGHT));	
		adjacentCells.add(getAdjacentCellOf(getCell(row, column), Site.RIGHT));	
		adjacentCells.add(getAdjacentCellOf(getCell(row, column), Site.BOTTOM));
		adjacentCells.add(getAdjacentCellOf(getAdjacentCellOf(getCell(row, column), Site.BOTTOM), Site.LEFT));
		adjacentCells.add(getAdjacentCellOf(getAdjacentCellOf(getCell(row, column), Site.BOTTOM), Site.RIGHT));
		adjacentCells.remove(null);
		return adjacentCells;
	}
	
	public Set<C> getAdjacentCellsOf(C cell) {
		
		Set<C> adjacentCells = null;

		if(cell != null) 	
			adjacentCells = getAdjacentCellsOf(cell.getRow(), cell.getColumn());
		
		return adjacentCells;
	}
	
	public Set<C> getAdjacentCellsOf(C cell, IntRange cellDistance){
		
		Set<C> adjacentCells = new HashSet<C>();
		
		for(int i=cellDistance.getMin(); i < cellDistance.getMax(); i++)
			adjacentCells.addAll(getAdjacentCellsOf(cell, i));
		
		return adjacentCells;		
	}
	
	/*  */
	public Set<C> getAdjacentCellsOf(C cell, int cellDistance){
		
		Set<C> adjacentCells = new HashSet<C>();
		
		int n = cellDistance;
		int row = cell.getRow() + n;
		int column = cell.getColumn() - n;
		
		while(row > (row - 2*n)) 
			adjacentCells.add(getCell(row--, column));

		while(column < (column + 2*n)) 
			adjacentCells.add(getCell(row, column++));
		
		while(row < (row + 2*n)) 
			adjacentCells.add(getCell(row++, column));
		
		while(column > (column - 2*n)) 
			adjacentCells.add(getCell(row, column--));

		if(adjacentCells.contains(null))
			adjacentCells.remove(null);
		
		return adjacentCells;
	}
	
	public C getCornerCell(Site site1, Site site2){
		
		if((site1 == Site.TOP && site2 == Site.LEFT) || (site1 == Site.LEFT && site2 == Site.TOP))
			return getCell(0, 0);
		
		if((site1 == Site.TOP && site2 == Site.RIGHT) || (site1 == Site.RIGHT && site2 == Site.TOP))
			return getCell(0, getColumns());
		
		if((site1 == Site.BOTTOM && site2 == Site.LEFT) || (site1 == Site.LEFT && site2 == Site.BOTTOM))
			return getCell(getRows(), 0);
		
		if((site1 == Site.BOTTOM && site2 == Site.RIGHT) || (site1 == Site.RIGHT && site2 == Site.BOTTOM))
			return getCell(getRows(), getColumns());
		
		return null;
	}
	
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();

		for (int row = 0; row < rows; row++) 
			for (int column = 0; column < columns; column++) 
				stringBuilder.append(getCell(row, column).toString()).append("\n");
						
		return stringBuilder.toString();
	}
		
	public enum Site {
		LEFT, TOP, RIGHT, BOTTOM;
	}

	@Override
	public Iterator<C> iterator() {
		 
		 Iterator<C> iterator = new Iterator<C>() {

			 private int row = 0;
			 private int column = 0;
			 
			 @Override
	     	 public boolean hasNext() {
				 return (row < (getRows()-1) || column < (getColumns()-1)) || row == (getRows()-1) && column == (getColumns()-1);
			 }

			 @Override
			 public C next() {
				
				 if(column < getColumns()) 
					 return getCell(row, column++);
			 
				 if (row < getRows()){		
					column = 0;
					row++;
					return getCell(row, column++);					
				 }
				 				 
				 return null;
			 }
		 };
	        
		 return iterator;
	}
}

class TableSize {
	
	private final int rows;
	private final int columns;
	
	public TableSize(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
	}
	
	public int getRows() {
		return this.rows;
	}
	
	public int getColumns() {
		return this.columns;
	}
}