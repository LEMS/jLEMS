package org.lemsml.jlems.expression;

import java.util.HashMap;

import org.lemsml.jlems.sim.ContentError;

public abstract class ComparisonNode extends FloatOperatorNode implements BooleanParseTreeNode {


	public ComparisonNode(String s) {
		super(s);
	}

/*
	public boolean evalB(HashMap<String, Double> valHS) throws ParseError {
 		 double x = (leftEvaluable != null ? leftEvaluable.evalD(valHS) : Double.NaN);
		 double y = (rightEvaluable != null ? rightEvaluable.evalD(valHS) : Double.NaN);
		 boolean ret = compare(x, y);
		 return ret;
	}
 */
	
	public abstract boolean compare(double x, double y);
	
	 
	public Dimensional dimop(Dimensional dl, Dimensional dr) throws ContentError {
		Dimensional ret = null;
		if (dl.matches(dr) || dl.isAny() || dr.isAny()) {
			ret = new ExprDimensional();
		} else {
			throw new ContentError("Mismatched dimensions in comparison, left: (" + dl + "), right: (" + dr+")");
		}
		return ret;
	}


	public void checkDimensions(HashMap<String, Dimensional> dimHM) throws ContentError {
		getDimensionality(dimHM); 
	}

	public abstract boolean compareInts(long ileft, long iright);




	public Dimensional evaluateDimensional(HashMap<String, Dimensional> dhm) throws ContentError {
		throw new ContentError("Can't apply boolean operations to comparisons");
	}
	

}
