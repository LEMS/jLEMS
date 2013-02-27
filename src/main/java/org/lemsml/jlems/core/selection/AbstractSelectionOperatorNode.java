package org.lemsml.jlems.core.selection;

import java.util.HashMap;

import org.lemsml.jlems.core.expression.Node;
import org.lemsml.jlems.core.expression.ParseError;


public abstract class AbstractSelectionOperatorNode extends AbstractSelectionNode implements Cloneable {
 
	String symbol;
 
	public Node left;
	
	public Node right;

	double fpos;
	
	public AbstractSelectionOperatorNode(String s) {
		super();
		symbol = s;
	}

	public Node getLeft() {
		return left;
	}
	
	public Node getRight() {
		return right;
	}
	
	 
	public void claimRight() throws ParseError {
		if (next() != null) {
			right = next();
			right.deparent();
			right.detachPrevious();
			right.setParent(this);
			if (right.next() != null) {
				right.next().replacePrevious(this);
			}
		 
		} else {
			throw new ParseError("no right node for operator?");
		}
	}
	
	
	
	public String getSymbol() {
		return symbol;
	}
	
        @Override
	public String toString() {
		return "(" + str(left) + " " + symbol + " " + str(right) + ")";
	}
 


	private String str(Node n) {
		String ret = (n == null ? "_" : n.toString());
		return ret;
	}
	
	 
	public double getPrecedence() {
		return 1. + fpos;
	}
	
	public abstract AbstractSelectionOperatorNode copy();
	
	
	public void replaceChild(Node nold, Node nnew) throws ParseError {
		if (left.equals(nold)) {
			left = nnew;
		} else if (right.equals(nold)) {
			right = nnew;
		} else {
			throw new ParseError("can't replace - not present " + nold);
		}
	}
	
	
	public void claim() throws ParseError {
		claimLeft();
		claimRight();
	}
	
	
	public void claimLeft() throws ParseError {
		if (previous() != null) {
			left = previous();
			left.deparent();
			left.detachNext();
			left.setParent(this);
			if (left.previous() != null) {
				left.previous().replaceNext(this);
			}
			
		} else {
			throw new ParseError("cant claim left?");
			// left = new ConstantNode("0");
		}
	}


	public void setSequencePosition(int i) {
		 fpos = 0.001 * i;
	}
	
	@Override
	public void replaceSymbols(HashMap<String, String> map) {
 		if (left != null) {
			left.replaceSymbols(map);
		}
		if (right != null) {
			right.replaceSymbols(map);
		}
	}

}
