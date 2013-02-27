package org.lemsml.jlems.core.type;

import org.lemsml.jlems.core.sim.ContentError;

public class DimensionalQuantity {

	private String originalText;

	private Dimension r_dimension;

	private double value;

	@SuppressWarnings("unused")
	private boolean hasValue = false;

	public void setNoValue() {
		hasValue = false;
	}

	public void setOriginalText(String arg) {
		originalText = arg;
	}

	public String getOriginalText() {
		return originalText;
	}

	public Dimension getDimension() {
		return r_dimension;
	}

	public double getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "DimensionalQuantity {" + (float) value + " " + r_dimension
				+ " (" + originalText + ")}";
	}

	public void setValue(double d, Unit u) {
		r_dimension = u.getDimension();
 		value = u.getAbsoluteValue(d);
 		hasValue = true;
	}

	public double getValueInUnit(Unit u) throws ContentError {
		if (!dimensionsMatch(u.getDimension())) {
			throw new ContentError("Dimension of unit " + u.summary()
					+ " does not match " + this);
		}
		return u.getLocalValue(value);
	}

	public double getDoubleValue() {
		return value;
	}

	public boolean dimensionsMatch(Dimension dimension) {
		boolean ok = false;
		if (r_dimension.matches(dimension)) {
			ok = true;
		}
		return ok;
	}

	public boolean dimensionsMatch(DimensionalQuantity ds) {
		boolean ok = false;
		if (r_dimension.matches(ds.r_dimension)) {
			ok = true;
		}
		return ok;
	}

}
