package org.lemsml.jlems.core.expression;

import java.util.HashMap;
 
 

public abstract class AbstractOperatorNode extends AbstractUnaryNode implements Cloneable, ParseTreeNode {

    public Node left;
    String symbol;

    public AbstractOperatorNode(String s) {
        super();
        symbol = s;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
    	String ret = "{Operator " + symbol + ": left=" + left + "; right=" + right + "}";
    	return ret;
    }
    
 
    public abstract int getPrecedence();

    public abstract AbstractOperatorNode copy();

    @Override
    public void replaceChild(Node nold, Node nnew) throws ParseError {
        if (left.equals(nold)) {
            left = nnew;
        } else if (right.equals(nold)) {
            right = nnew;
        } else {
            throw new ParseError("can't replace - not present " + nold);
        }
    }

    @Override
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
            left = new ConstantNode("0");
        }
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
