package org.lemsml.jlems.expression;

import java.util.HashMap;

import org.lemsml.jlems.eval.BBase;
import org.lemsml.jlems.eval.BooleanEvaluator;
import org.lemsml.jlems.eval.DBase;
import org.lemsml.jlems.eval.DoubleEvaluator;
import org.lemsml.jlems.sim.ContentError;
import org.lemsml.jlemsviz.plot.E;

public class ParseTree {
 
	ParseTreeNode root;
	
	public ParseTree(ParseTreeNode r) { 
		root = r;
	}
	
	public String toString() {
		return root.toString();
	}
	

	public boolean isFloat() {
		boolean ret = false;
		if (root instanceof DoubleParseTreeNode) {
			ret = true;
		}
		return ret;
	}
	
	
	public boolean isBoolean() {
		boolean ret = false;
		if (root instanceof BooleanParseTreeNode) {
			ret = true;
		}
		return ret;
	}

	public BooleanEvaluator makeBooleanEvaluator() throws ContentError {
		BBase ret = null;
		if (root instanceof BooleanParseTreeNode) {
			ret= new BBase(((BooleanParseTreeNode) root).makeFixed(null));
		}
		return ret;
	}

 
	
	public BooleanEvaluator makeBooleanFixedEvaluator(HashMap<String, Double> fixedHM) throws ContentError {
		BBase ret = null;
		if (root instanceof BooleanParseTreeNode) {
			ret = new BBase(((BooleanParseTreeNode) root).makeFixed(fixedHM));
		}
		return ret;
	}
	
	

	public DoubleEvaluator makeFloatEvaluator() throws ContentError {
		DBase ret = null;
		if (root instanceof DoubleParseTreeNode) {
			ret = new DBase(((DoubleParseTreeNode) root).makeFixed(null));
		}
		return ret;
	}
	
	
	public DoubleEvaluator makeFloatFixedEvaluator(HashMap<String, Double> fixedHM) throws ContentError {
		DBase ret = null;
		if (root instanceof DoubleParseTreeNode) {
			ret = new DBase(((DoubleParseTreeNode) root).makeFixed(fixedHM));
		}
		return ret;
	}
	
 
	
	
	public Dimensional evaluateDimensional(HashMap<String, Dimensional> adml) throws ContentError {
		return root.evaluateDimensional(adml);
	}

	public Dimensional getDimensionality(HashMap<String, Dimensional> dimHM) throws ContentError {
		 return root.getDimensionality(dimHM);
	}

	public void checkDimensions(HashMap<String, Dimensional> dimHM) throws ContentError {
 		if (root instanceof BooleanParseTreeNode) {
			((BooleanParseTreeNode)root).checkDimensions(dimHM);
		} else {
			E.error("Can't check dims of expression");
		}
	}


}
