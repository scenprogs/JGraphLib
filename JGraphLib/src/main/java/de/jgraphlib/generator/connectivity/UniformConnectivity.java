package de.jgraphlib.generator.connectivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.util.RandomNumbers;
import de.jgraphlib.util.Tuple;

public class UniformConnectivity<V extends Vertex<?>> extends Weighted2DGraphConnectivity<V> {

	Direction direction;
	
	public UniformConnectivity(Direction direction) {
		this.direction = direction;
		this.randomNumbers = new RandomNumbers();
	}
	
	public UniformConnectivity(Direction direction, RandomNumbers randomNumbers) {
		this.direction = direction;
		this.randomNumbers = randomNumbers;
	}
	

	public List<Tuple<V,V>> associate(V vertex, List<V> vertices) {
	
		List<Tuple<V,V>> associations = new ArrayList<Tuple<V,V>>();
		
		switch(direction) {
		
		case BACK:	
			for(V v : vertices)
				associations.add(new Tuple<V,V>(v, vertex));
			
			;break;
		case FORTH:
			for(V v : vertices)
				associations.add(new Tuple<V,V>(vertex, v));
					
			;break;	
		}
		
		return associations;
	}

	@Override
	public List<Tuple<V, V>> associate(Map<V, Set<V>> vertices) {
		// TODO 
		return null;
	}
	
}
