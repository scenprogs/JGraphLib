package de.jgraphlib.generator.properties;

import de.jgraphlib.generator.connectivity.Weighted2DGraphConnectivity;
import de.jgraphlib.generator.shapes.Shape2D;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.util.DoubleRange;
import de.jgraphlib.util.IntRange;

public class NetworkGraphProperties<V extends Vertex<Position2D>> extends Weighted2DGraphProperties<V> {

    public NetworkGraphProperties(
    		Shape2D shape, 
    		IntRange vertexQuantity, 
    		DoubleRange vertexDistance, 
    		DoubleRange edgeDistance, 
    		Weighted2DGraphConnectivity<V> connectivity) {
    	
    	super(shape, vertexQuantity, vertexDistance, null, edgeDistance, connectivity);
    }
    
    public NetworkGraphProperties(
    		DoubleRange vertexDistance, 
    		DoubleRange edgeDistance, 
    		Weighted2DGraphConnectivity<V> connectivity) {
    	
    	super(null, null, vertexDistance, null, edgeDistance, connectivity);
    }
}
