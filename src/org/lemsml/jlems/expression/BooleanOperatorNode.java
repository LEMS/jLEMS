package org.lemsml.jlems.expression;

import java.util.HashMap;

import org.lemsml.jlems.sim.ContentError;

public abstract class BooleanOperatorNode extends OperatorNode implements BooleanParseTreeNode {

	BooleanParseTreeNode leftEvaluable;
	BooleanParseTreeNode rightEvaluable;
	
	public BooleanOperatorNode(String s) {
		super(s);
	}
 
	public abstract boolean bool(boolean x, boolean y);
 

	public ExpressionVisitor visitAll(ExpressionVisitor ev) throws ContentError {
		checkLeftRight();
		return ev.visitNode(leftEvaluable.visitAll(ev), this, rightEvaluable.visitAll(ev));
	}
	
	
	protected void checkLeftRight() throws ContentError {
		if (leftEvaluable == null) {
			if (left instanceof BooleanParseTreeNode) {
				leftEvaluable = (BooleanParseTreeNode)left;
			} else {
				throw new ContentError("Wrong node type in float operator: " + left);
			}
		}
		if (rightEvaluable == null) {
			if (right instanceof BooleanParseTreeNode) {
				rightEvaluable = (BooleanParseTreeNode)right;	
			} else {
				throw new ContentError("Wrong node type in float operator: " + right);
			}
		}
	}
	

	public Dimensional getDimensionality(HashMap<String, Dimensional> dimHM) throws ContentError {
		Dimensional dl = leftEvaluable.getDimensionality(dimHM);
		Dimensional dr = rightEvaluable.getDimensionality(dimHM);
		Dimensional ret = null;
		if (dl != null && dr != null) {
			ret = dimop(dl, dr);
		} else {
			throw new ContentError("Null dimension in operator: " + dl + " " + dr + " operator: " + symbol);
		}
		return ret;
	}

	public abstract Dimensional dimop(Dimensional dl, Dimensional dr) throws ContentError;
	
	 
 

	public Dimensional evaluateDimensional(HashMap<String, Dimensional> dhm) throws ContentError {
		throw new ContentError("Can't apply boolean operations to dimensions");
	}
	
	
	@Override
	public void substituteVariables(HashMap<String, String> varHM) throws ContentError {
		checkLeftRight();
		leftEvaluable.substituteVariables(varHM);
		rightEvaluable.substituteVariables(varHM);
	}
 
	
	public String toExpression() throws ContentError {
		checkLeftRight();
		return "(" + leftEvaluable.toExpression() + " " + symbol + " " + rightEvaluable.toExpression() + ")";
	}
	
}
 