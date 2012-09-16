package org.lemsml.expression;

import java.util.HashMap;
 

public abstract class OperatorNode extends UnaryNode implements Cloneable, Evaluable {

    public Node left;
    String symbol;

    public OperatorNode(String s) {
        super();
        symbol = s;
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

    public abstract int getPrecedence();

    public abstract OperatorNode copy();

    @Override
    public void replaceChild(Node nold, Node nnew) throws ParseError {
        if (left == nold) {
            left = nnew;
        } else if (right == nold) {
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
