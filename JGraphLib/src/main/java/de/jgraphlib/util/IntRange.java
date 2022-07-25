package de.jgraphlib.util;

public class IntRange extends Range<Integer> {

	public IntRange(int minMax) {
		super(minMax);
	}
	
	public IntRange(int min, int max) {
		super(min, max);
	}
	
	public IntRange(double min, double max) {
		super((int) min, (int) Math.round(max));
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("(min: ").append(min());
		stringBuilder.append(", max: ").append(max()).append(")");
		return stringBuilder.toString();
	}

	@Override
	public Integer abs() {
		return Math.abs(max()-min());
	}

	@Override
	public boolean contains(Integer i) {
		return i >= min() && i <= max();
	}
	
	public boolean contains(Double d) {
		return d >= min() && d <= max();
	}
}
