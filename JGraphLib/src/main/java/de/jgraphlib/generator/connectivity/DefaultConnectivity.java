package de.jgraphlib.generator.connectivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.util.RandomNumbers;
import de.jgraphlib.util.Tuple;

public class DefaultConnectivity<V extends Vertex<?>> extends Weighted2DGraphConnectivity<V> {

	public DefaultConnectivity(Association association) {
		this.association = association;
		this.randomNumbers = new RandomNumbers();
	}
	
	public DefaultConnectivity(Association association, RandomNumbers randomNumbers) {
		this.association = association;
		this.randomNumbers = randomNumbers;
	}
	
	/**
	 * Associate edges of a new vertex with a list of vertices.
	 *
	 * @param vertex the new vertex added to the graph
	 * @param vertices the vertices (new) vertex will be connected with
	 * @return list with associations
	 */
	public List<Tuple<V,V>> associate(V vertex, List<V> vertices) {

		List<Tuple<V,V>> associations = new ArrayList<Tuple<V,V>>();
		
		/* (1) */
		
		// Select a vertex randomly from vertices
		V randomVertex = vertices.get(randomNumbers.getRandom(0, vertices.size() - 1));
		
		// Add association randomVertex -> vertex, first association always is towards (the new) vertex
		associations.add(new Tuple<V,V>(randomVertex, vertex));
		
		switch (association) {
		case BIDIRECTIONAL:
			associations.add(new Tuple<V,V>(vertex, randomVertex));
			break;
		default:
			break;
		}

		/* (2) */

		vertices.remove(vertex);
		vertices.remove(randomVertex);

		for (V targetVertex : vertices)			
			switch (association) {
			case UNIDIRECTIONAL:
				if (new Random().nextBoolean())
					associations.add(new Tuple<V,V>(vertex, targetVertex));
				else
					associations.add(new Tuple<V,V>(targetVertex, vertex));
				break;
			case BIDIRECTIONAL:
				associations.add(new Tuple<V,V>(targetVertex, vertex));
				associations.add(new Tuple<V,V>(vertex, targetVertex));
				break;
			default:
				break;
			}
		
		return associations;
	}
	
	public List<Tuple</*source*/V,/*target*/V>> associate(Map<V, Set<V>> vertices) {

		List<Tuple<V,V>> associations = new ArrayList<Tuple<V,V>>();
		
		/*
		
		V vertex = (new ArrayList<>(vertices.keySet())).get(randomNumbers.getRandom(0, vertices.size() - 1));
		associations.add(new Tuple<V,V>(vertex, randomNumbers.getRandomItem(vertices.get(vertex))));
		
		switch (association) {
		case BIDIRECTIONAL:
			associations.add(new Tuple<V,V>(associations.get(0).getSecond(), vertex));
			break;
		default:
			break;
		}
		
		*/
				
		for(Entry<V,Set<V>> entry : vertices.entrySet()) {		
			for(V v : entry.getValue()) {	
				switch (association) {
				case UNIDIRECTIONAL:
					if (new Random().nextBoolean())
						associations.add(new Tuple<V,V>(entry.getKey(), v));
					else
						associations.add(new Tuple<V,V>(v, entry.getKey()));
					break;
				case BIDIRECTIONAL:
					associations.add(new Tuple<V,V>(entry.getKey(), v));
					associations.add(new Tuple<V,V>(v, entry.getKey()));
					break;
				default:
					break;
				}			
			}					
		}
		
		return associations;	
	}		
}
