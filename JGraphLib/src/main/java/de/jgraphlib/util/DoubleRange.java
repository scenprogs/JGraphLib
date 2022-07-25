package de.jgraphlib.util;

import java.text.DecimalFormat;

public class DoubleRange extends Range<Double> {

	public DoubleRange(double minMax) {
		super(minMax);
	}
	
	public DoubleRange(double min, double max) {
		super(min, max);
	}
	
	@Override
	public Double abs() {
		return Math.abs(getMax()-getMin());
	}

	@Override
	public String toString() {
		DecimalFormat decimalFormat = new DecimalFormat("#.00");
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("(min: ").append(decimalFormat.format(getMin()));
		stringBuilder.append(", max: ").append(decimalFormat.format(getMax())).append(")");
		return stringBuilder.toString();
	}

	@Override
	public boolean contains(Double d) {	
		return d >= getMin() && d <= getMax();	
	}
}
