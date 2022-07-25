package de.jgraphlib.algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.jgraphlib.graph.WeightedGraph;
import de.jgraphlib.graph.elements.Path;
import de.jgraphlib.graph.elements.Vertex;
import de.jgraphlib.graph.elements.WeightedEdge;
import de.jgraphlib.util.Tuple;

public class DepthFirstTraversal<V extends Vertex<?>, E extends WeightedEdge<W>, W> {

	protected WeightedGraph<V, ?, E, ?> graph;

	public DepthFirstTraversal(WeightedGraph<V, ?, E, ?> graph) {
		this.graph = graph;
	}
	
	public List<List<Integer>> computeVertexIDs(V source, V target) {

		List<List<Integer>> paths = new ArrayList<List<Integer>>();

		class DepthFirstTraversalRecursion {
			void call(Integer currentID, Integer targetID, Set<Integer> visitedVertices, List<Integer> path) {

				if (currentID.equals(targetID)) {
					paths.add(path);
					return;
				}

				visitedVertices.add(currentID);

				for (V nextHop : graph.getNextHopsOf(graph.getVertex(currentID))) {
					if (!visitedVertices.contains(nextHop.getID())) {
						path.add(graph.getVertex(nextHop).getID());

						List<Integer> newPath = new ArrayList<Integer>();
						newPath.addAll(path);

						call(nextHop.getID(), target.getID(), visitedVertices, newPath);
						path.remove(path.size() - 1);
					}
				}

				visitedVertices.remove(currentID);
			}
		}

		new DepthFirstTraversalRecursion().call(source.getID(), target.getID(), new HashSet<Integer>(),
				new ArrayList<Integer>(Arrays.asList(source.getID())));

		return paths;
	}

	public List<Path<V, E, W>> compute(V source, V target) {

		List<Path<V, E, W>> paths = new ArrayList<Path<V, E, W>>();		
		Path<V, E, W> path = new Path<V, E, W>(source, target);

		class DepthFirstTraversalRecursion {
			void call(V current, V target, Set<V> visitedVertices, Path<V, E, W> path) {

				if (current.equals(target)) {
					paths.add(path);
					return;
				}

				visitedVertices.add(current);

				for (V nextHop : graph.getNextHopsOf(current)) {
					if (!visitedVertices.contains(nextHop)) {
						path.add(new Tuple<E, V>(graph.getEdge(current, nextHop), nextHop));
						
						Path<V, E, W> newPath = new Path<V, E, W>(path.getSource(), path.getTarget());	
						newPath.update(path);
						
						call(nextHop, target, visitedVertices, newPath);
						path.removeLast();
					}
				}

				visitedVertices.remove(current);
			}
		}

		new DepthFirstTraversalRecursion().call(source, target, new HashSet<V>(), path);

		return paths;
	}	
}
