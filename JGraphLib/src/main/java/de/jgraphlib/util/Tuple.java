package de.jgraphlib.util;

public class Tuple<F, S> {

	private F first;
	private S second;

	public Tuple() {}

	public Tuple(F first, S second) {
		this.first = first;
		this.second = second;
	}

	public void setFirst(F first) {
		this.first = first;
	}

	public void setSecond(S second) {
		this.second = second;
	}

	public F getFirst() {
		return first;
	}

	public S getSecond() {
		return second;
	}

	@Override
	public String toString() {
		return new StringBuffer().append("First: ").append(this.getFirst()).append(", Second: ").append(this.getSecond())
				.toString();
	}
	
	public Tuple<S,F> reverse(){
		return new Tuple<S,F>(getSecond(),getFirst());
	}
	
	public boolean equals(Object other) {
		
		if(other instanceof Tuple) {		
			Tuple<F,S> otherTuple = (Tuple<F,S>) other;
			return otherTuple.getFirst().equals(getFirst()) && otherTuple.getSecond().equals(getSecond());
		}
		
		return false; 	
	}
}