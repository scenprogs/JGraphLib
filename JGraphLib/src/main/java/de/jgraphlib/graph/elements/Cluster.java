package de.jgraphlib.graph.elements;

import de.jgraphlib.graph.Weighted2DGraph;
import de.jgraphlib.util.SearchGrid2D;

public class Cluster<V extends Vertex<Position2D>, E extends WeightedEdge<W>, W extends EdgeDistance> {	
	
	private int ID;
	private Weighted2DGraph<V, E, W> graph;
	private SearchGrid2D<V> searchGrid;
	
	public Cluster(int ID, Weighted2DGraph<V, E, W> graph) {
		this.ID = ID;
		this.graph = graph;
	}
	
	public int getID() {
		return this.ID;
	}
	
	public Weighted2DGraph<V, E, W> getGraph() {
		return this.graph;
	}
	
	public void setSearchGrid(SearchGrid2D<V> searchgrid) {
		this.searchGrid = searchgrid;
	}
	
	public SearchGrid2D<V> getSearchGrid() {
		return this.searchGrid;
	}
	
	public boolean equals(Cluster<V,E,W> other) {
		return other.getID() == getID();
	}
}
