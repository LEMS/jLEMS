package org.lemsml.jlems.core.expression;

import java.util.HashMap;

import org.lemsml.jlems.core.run.RuntimeError;
import org.lemsml.jlems.core.sim.ContentError;

public abstract class AbstractComparisonNode extends AbstractFloatOperatorNode implements BooleanParseTreeNode {


	public AbstractComparisonNode(String s) {
		super(s);
	}
 
	
	public abstract boolean compare(double x, double y) throws RuntimeError;
	
	 
    @Override
	public Dimensional dimop(Dimensional dl, Dimensional dr) throws ContentError {
		Dimensional ret = null;
		if (dl.matches(dr) || dl.isAny() || dr.isAny()) {
			ret = new ExprDimensional();
		} else {
			throw new ContentError("Mismatched dimensions in comparison, left: (" + dl + "), right: (" + dr+")");
		}
		return ret;
	}


    @Override
	public void checkDimensions(HashMap<String, Dimensional> dimHM) throws ContentError {
		getDimensionality(dimHM); 
	}

	public abstract boolean compareInts(long ileft, long iright);


    @Override
	public Dimensional evaluateDimensional(HashMap<String, Dimensional> dhm) throws ContentError {
		throw new ContentError("Can't apply boolean operations to comparisons");
	}
	

}
