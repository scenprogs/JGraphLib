package de.jgraphlib.generator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

import de.jgraphlib.generator.properties.Weighted2DGraphProperties;
import de.jgraphlib.graph.Weighted2DGraph;
import de.jgraphlib.graph.elements.EdgeDistance;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.util.RandomNumbers;
import de.jgraphlib.util.Tuple;

public class TreeGraphGenerator<V extends Vertex<Position2D>, E extends WeightedEdge<W>, W extends EdgeDistance>
		extends Weighted2DGraphGenerator<V, E, W> {

	public TreeGraphGenerator(Weighted2DGraph<V, E, W> graph, Weighted2DGraphProperties<V> properties,
			RandomNumbers randomNumbers) {
		super(graph, properties, randomNumbers);
	}

	public TreeGraphGenerator(Weighted2DGraph<V, E, W> graph, Weighted2DGraphProperties<V> properties) {
		super(graph, properties);
	}

	private List<Tuple<V, Integer>> split(List<V> vertices, int numberOfVertices) {

		ArrayList<Tuple<V, Integer>> leafs = new ArrayList<>();

		int quotient = numberOfVertices / vertices.size();

		for (V vertex : vertices)
			leafs.add(new Tuple<>(vertex, quotient));

		if (vertices.size() % numberOfVertices != 0) {
			Tuple<V, Integer> leaf = leafs.get(getRandomNumbers().getRandom(0, leafs.size() - 1));
			leaf.setSecond(leaf.getSecond() + 1);
		}

		return leafs;
	}

	@Override
	public List<V> generate(int attempts) {

		Queue<Tuple<V, Integer>> queue = new LinkedList<>();

		// Add root
		queue.add(new Tuple<>(generateVertex(attempts), getTargetSize()));

		while (queue.size() > 0) {

			// Poll next unfinished leaf
			Tuple<V, Integer> leaf = queue.poll();

			// Generate number of new leafs (under leaf)
			int n_successors = getRandomNumbers().getRandom(getProperties().getEdgeQuantity().getMin(),
					getProperties().getEdgeQuantity().getMax());

			// Check if branch is allowed to produce more leafs
			if (n_successors > (leaf.getSecond() - 1))
				n_successors = leaf.getSecond() - 1;

			// Split the number of allowed vertices (of leaf) to the new leafs of the branch
			List<Tuple<V, Integer>> newLeafs = split(generateVertices(leaf.getFirst(), n_successors, attempts),
					leaf.getSecond() - 1);

			// Create edges from new leafs to leaf
			createEdges(leaf.getFirst(), newLeafs.stream().map(tuple -> tuple.getFirst()).collect(Collectors.toList()));

			// Add new leafs to queue that are allowed to generate new leafs 
			for (Tuple<V, Integer> newLeaf : newLeafs)
				if (newLeaf.getFirst() != null && newLeaf.getSecond() > 1)
					queue.add(newLeaf);
		}

		log.info(String.format("%d/%d", getGraph().size(), getTargetSize()));

		return getGraph().getVertices();
	}
}
