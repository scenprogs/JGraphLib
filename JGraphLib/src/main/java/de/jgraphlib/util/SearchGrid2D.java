package de.jgraphlib.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.maths.Combinatorics;

public class SearchGrid2D<O> {

	Grid2D<Set<Tuple<Position2D, O>>> grid;
		
	public SearchGrid2D(Scope2D scope, DoubleRange objectDistance, List<Tuple<Position2D, O>> objects) {		
		initialize(scope, objectDistance, objects);		
	}
	
	public SearchGrid2D(Scope2D scope, DoubleRange objectDistance) {			
		initialize(scope, objectDistance);		
	}
	
	public void initialize(Scope2D scope, DoubleRange objectDistanceRange, List<Tuple<Position2D, O>> objects) {	
		
		initialize(scope, objectDistanceRange);		
		
		for(Tuple<Position2D, O> object : objects)
			assign(object.getFirst(), object.getSecond());
	}
	
	public void initialize(Scope2D scope, DoubleRange objectDistanceRange) {
		
		int columns = (int) Math.ceil( 
				scope.getWidth().abs() / objectDistanceRange.getMax());
				
		int rows = (int) Math.ceil(
				scope.getHeight().abs() / objectDistanceRange.getMax());
				
		grid = new Grid2D<>(
				scope, 
				new Dimension2D(objectDistanceRange.max()), 
				rows, columns);
		
		for(int row=0; row < grid.getRows(); row++)
			for(int col=0; col < grid.getColumns(); col++)
				grid.getCell(row, col).set(new HashSet<Tuple<Position2D, O>>());		
	}
			
	public boolean assign(Position2D position2D, O object) {	
		
		Box2D<Set<Tuple<Position2D, O>>> box = grid.getBox(position2D);	
		
		if(box != null) 
			return box.get().add(new Tuple<>(position2D, object));
			
		return false;
	}
	
	// Find all objects within this grid that are in distance to a given position (the position can be within or outside the grid)
	public Set<O> search(Position2D position, double distance /*radius*/) {
			
		Set<O> objects = new HashSet<O>();
		
		/* Case 1: Position is inside the grid */
		if(grid.getScope().contains(position)) {
		
			// Get box that contains position
			Box2D<Set<Tuple<Position2D, O>>> box = grid.getBox(position);
						
			// Get adjacent boxes
			Set<Box2D<Set<Tuple<Position2D, O>>>> adjacentBoxes = grid.getAdjacentCellsOf(box);
								
			// Add box to adjacent boxes
			adjacentBoxes.add(box);
				
			// Iterate all objects within box & adjacent boxes, collect those who are in distance to given position
			for (Set<Tuple<Position2D, O>> objectSet : grid.getContentOf(adjacentBoxes)) 	
				objects.addAll(objectSet.stream()
						.filter(entry -> position.getDistanceTo(entry.getFirst()) <= distance)
						.map(entry -> entry.getSecond())
						.collect(Collectors.toSet()));	
			
			//System.out.println(objects.size());
		}
		/* Case 2: Position is outside the grid and radius is greater than distance to grid */
		else if (grid.getScope().distance(position) <= distance) {
								
			Iterator<Box2D<Set<Tuple<Position2D, O>>>> iterator = grid.iterator();
					
			while(iterator.hasNext()) { 		
				
				Box2D<Set<Tuple<Position2D, O>>> current = iterator.next();
				
				if(current.getScope().distance(position) <= distance || current.getScope().distance(position) == 0d)
					objects.addAll(current.get().stream()
							.filter(entry -> position.getDistanceTo(entry.getFirst()) <= distance)
							.map(entry -> entry.getSecond())
							.collect(Collectors.toSet()));			
			}						
		}
		
		return objects;						
	}
	
	// Find all objects within this grid that are in distance with the objects of another grid
	public Map<O, Set<O>> search(SearchGrid2D<O> other, double distance /*radius*/) {
		
		Map<O, Set<O>> objectAssociations = new HashMap<>();
		
		if (getGrid().getScope().distance(other.getScope()) <= distance) {
										
			List<List<Box2D<Set<Tuple<Position2D, O>>>>> boxes = new ArrayList<>();
			boxes.add(getGrid().getBoxesFlat());
			boxes.add(other.getGrid().getBoxesFlat());	
			boxes = Combinatorics.cartesianProduct(boxes);						
			boxes.removeIf(boxPair -> boxPair.get(0).getScope().distance(boxPair.get(1).getScope()) > distance);
			
			//for(List<Box2D<Set<Tuple<Position2D, O>>>> pair : boxes)
			//	System.out.println(String.format("%d/%d - %d/%d", pair.get(0).getRow(), pair.get(0).getColumn(), pair.get(1).getRow(), pair.get(1).getColumn()));
			
			// Iterate Cartesian factors (box pairs)
			for(List<Box2D<Set<Tuple<Position2D, O>>>> boxPair : boxes) {
				// Iterate each object in the first box
				for(Tuple<Position2D, O> tuple : boxPair.get(0).get()) {
				
					// Find all objects that are within distance
					Set<O> objects = boxPair.get(1).get().stream()
							.filter(t -> t.getFirst().getDistanceTo(tuple.getFirst()) <= distance)
							.map(t -> t.getSecond())
							.collect(Collectors.toSet());
					
					if(objects.size() > 0) 					
						if(objectAssociations.containsKey(tuple.getSecond()))
							objectAssociations.get(tuple.getSecond()).addAll(objects);
						else
							objectAssociations.put(tuple.getSecond(), objects);					
				}
			}
		}
								
		return objectAssociations;
	}
	
	public Scope2D getScope() {
		return this.grid.getScope();
	}
	
	public Grid2D<Set<Tuple<Position2D, O>>> getGrid() {
		return this.grid;
	}
}

