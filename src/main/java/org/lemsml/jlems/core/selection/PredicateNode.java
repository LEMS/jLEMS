package org.lemsml.jlems.core.selection;

import java.util.ArrayList;
import java.util.HashMap;

import org.lemsml.jlems.core.expression.AbstractComparisonNode;
import org.lemsml.jlems.core.expression.AbstractFloatResultNode;
import org.lemsml.jlems.core.expression.ConstantNode;
import org.lemsml.jlems.core.expression.FunctionNode;
import org.lemsml.jlems.core.expression.IntegerConstantNode;
import org.lemsml.jlems.core.expression.Node;
import org.lemsml.jlems.core.expression.ParseError;
import org.lemsml.jlems.core.run.ConnectionError;
import org.lemsml.jlems.core.run.RuntimeError;
import org.lemsml.jlems.core.run.StateInstance;
import org.lemsml.jlems.core.run.StateRunnable;
import org.lemsml.jlems.core.sim.ContentError;

public class PredicateNode extends Node {

	ArrayList<Node> children;
	
	
	boolean gathered = false;
	
	public PredicateNode() {
		this(null);
	}
	

	
	public PredicateNode(Node p) {
		super(p);
		children = new ArrayList<Node>();
	}
	
	
	public boolean evaluate(StateRunnable si) throws ContentError, ConnectionError, RuntimeError {
		boolean ret = false;
		if (children.size() == 1) {
			Node n = children.get(0);
			if (n instanceof AbstractComparisonNode) {
				AbstractComparisonNode cn = (AbstractComparisonNode)n;
				double dleft = evaluateFloat(cn.left, si);
				double dright = evaluateFloat(cn.right, si);
				
				// quick hack for equals. evaluateFloat should be replaced by evalyateNumber with a rich retyrn
				// type that can be an int or a double
				long ileft = Math.round(dleft);
				long iright = Math.round(dright);
				double eps = 1.e-6;
				if (Math.abs(dleft - ileft) < eps && Math.abs(dright - iright) < eps) {
					ret = cn.compareInts(ileft, iright);
				} else {		
					ret = cn.compare(dleft, dright);
				}
				
			} else {
				throw new ContentError("Child node for predicate must be a condition, not " + n);
			}
			
		} else {
			throw new ContentError("Predicate node is incompletely parsed: nchildren= " + children.size() + "\n" +
					children);
		}
		return ret;
	}
	
	
	public String toString() {
		return "[" + (children.isEmpty() ? "" : first().siblingsToString()) + "]";
	}
	
	public void remove(Node n) {
		children.remove(n);
		// E.info("remove in group node... " + childHS.size());
	}
	
	public void add(Node n) {
		children.add(n);
		n.setParent(this);
	}
	
	public void addAll(ArrayList<Node> ns) {
		children.addAll(ns);
		for (Node n : children) {
			n.setParent(this);
		}
	}
	
	public ArrayList<Node> getChildren() {
		return children;
	}
	
	
	public Node first() {
		return children.get(0);
	}

	
	
	public void gatherPreceeding() throws ParseError {
		
		Node pre = previous();
		pre.detachNext();
 	
		while (pre != null) {
			if (pre instanceof OpenPredicateNode) {
				if (pre.previous() != null) {
					pre.previous().replaceNext(this);
				}
				pre.dispose();
				break;					
					
			} else if (pre instanceof PredicateNode && !((PredicateNode)pre).gathered) {
				throw new ParseError("predicate close bracket in wrong place");
			}
			pre.deparent();
			add(pre);
			pre = pre.previous();
		}		
		gathered = true;
	}
	
	
	
	
	public double evaluateFloat(Node n, StateRunnable si) throws ContentError, ConnectionError, RuntimeError {
		double ret = 0;
		if (n instanceof SlashNode) {
			ret = ((SlashNode)n).evaluateFloat(si);
		
		} else if (n instanceof ColonNode) {
			ret = ((ColonNode)n).evaluateFloat(si);
			
		} else if (n instanceof SelectorNode) {
			ret = ((SelectorNode)n).getFloat(si);
		
		} else if (n instanceof AbstractFloatResultNode) {
			AbstractFloatResultNode frn = (AbstractFloatResultNode)n;
			double dleft = evaluateFloat(frn.left, si);
			double dright = evaluateFloat(frn.right, si);
			
			ret = frn.op(dleft, dright);
			
		} else if (n instanceof IntegerConstantNode) {
			ret = ((IntegerConstantNode)n).getValue();
			
		} else if (n instanceof ConstantNode) {
			ret = ((ConstantNode)n).getDoubleValue();
			
		} else if (n instanceof FunctionNode) {
			FunctionNode fn = (FunctionNode)n;
			double arg = evaluateFloat(fn.right, si);
			ret = fn.call(arg);
			
		} else {
			throw new ContentError("Cant evaluate float from " + n + " " + n.getClass().getName());
		}

		
		return ret;
	}
		
	
	
	
	public void supplantByChild() throws ParseError {
		// TODO - any case for this? nodes[1] still a predicate, or replaced by an index selector?
		/*
		if (children.size() == 1) {
			Node cn = children.get(0);
			replaceWith(cn);
 		
		} else {
			throw new ParseError("too many child nodes left : " + children.size());
		}
		*/
	}
	
	@Override
	public void replaceSymbols(HashMap<String, String> map) {
		for (Node n : children) {
			n.replaceSymbols(map);
		}
	    
    }
}
