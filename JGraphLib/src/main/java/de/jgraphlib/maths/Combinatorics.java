package de.jgraphlib.maths;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import de.jgraphlib.util.Tuple;

public class Combinatorics {

	public static <T> List<Tuple<T, T>> generatePairs(final List<T> list) {
		return list.stream().flatMap(t1 -> list.stream().map(t2 -> new Tuple<T, T>(t1, t2)))
				.collect(Collectors.toList());
	}

	// Generates all combinations (Tuples) of a given list, excluding identities
	// ([A,A]), excluding reverse order duplicates (return for A and B [A,B], but
	// not [B,A])
	public static <T> List<Tuple<T, T>> generateCombinations(final List<T> list) {

		List<Tuple<T, T>> combinations = new ArrayList<>();

		for (int i = 0; i < list.size(); i++)
			for (int k = 0; k < list.size(); k++)
				if (i != k) {
					Tuple<T, T> tuple = new Tuple<T, T>(list.get(i), list.get(k));
					if (!combinations.contains(tuple.reverse()))
						combinations.add(new Tuple<T, T>(list.get(i), list.get(k)));
				}

		return combinations;
	}

	public static <T> List<List<T>> cartesianProduct(List<List<T>> lists) {
		List<List<T>> resultLists = new ArrayList<List<T>>();
		if (lists.size() == 0) {
			resultLists.add(new ArrayList<T>());
			return resultLists;
		} else {
			List<T> firstList = lists.get(0);
			List<List<T>> remainingLists = cartesianProduct(lists.subList(1, lists.size()));
			for (T condition : firstList) {
				for (List<T> remainingList : remainingLists) {
					ArrayList<T> resultList = new ArrayList<T>();
					resultList.add(condition);
					resultList.addAll(remainingList);
					resultLists.add(resultList);
				}
			}
		}
		return resultLists;
	}
}
