package org.lemsml.jlems.core.expression;

import java.util.HashMap;

import org.lemsml.jlems.core.logging.E;


public class Node {
	
	Node prev;
	Node nxt;
	
	Node par;
	
	
	public final static int UNKNOWN = 0;
	public final static int OPEN = 1;
	public final static int CLOSE = 2;
	public final static int CONSTANT = 3;
	public final static int VARIABLE = 4;
	public final static int BNARY_OPERATOR = 5;
	public final static int UNARY_OPERATOR = 6;
	
	public int type = 0;
	
	
	
	public Node() {
		this(null);
	}
	
	
	public Node(Node p) {
		par = p;
	}
	
	
	public void remove(Node n) throws ParseError {
		throw new ParseError("Calling remove in bare node?" + this.getClass());
	}
	
	public void replaceChild(Node nold, Node nnew) throws ParseError {
		throw new ParseError("ReplaceChild called on bare node? me: "+this+", nold: "+nold+", nnew: "+nnew);
	}
	
    @Override
	public String toString() {
		return "Node, type: " + type + ", parent: " + par + ", next: " + nxt + ", prev: " + prev;
	}
	
	public String siblingsToString() {
		String ret = toString();
		Node p = prev;
		while (p != null) {
			ret = p.toString() + " " + ret;
			p = p.prev;
		}
		p = nxt;
		while (p != null) {
			ret = ret + " " + p.toString();
			p = p.nxt;
		}
		return ret;
	}
	
	
	public void setParent(Node p) {
		par = p;
	}
	
	
	
	
	public void linkNext(Node t) {
		nxt = t;
		nxt.prev = this;
	}
	
	public Node previous() {
		return prev;
	}
	
	public Node next() {
		return nxt;
	}

	public Node parent() {
		return par;
	}

	public void detachNext() {
		nxt.prev = null;
		nxt = null;
		
	}


	public void detachPrevious() {
		prev.nxt = null;
		prev = null;
	}
	
	public void replacePrevious(Node n) {
		prev.nxt = null;
		prev = n;
		n.nxt = this;
	}
	
	public void replaceNext(Node n) {
		nxt.prev = null;
		nxt = n;
		n.prev = this;
	}
	
	public void dispose() throws ParseError {
		if (nxt != null && nxt.prev == this) {
			nxt.prev = null;
		}
		if (prev != null && prev.nxt == this) {
			prev.nxt = null;
		}
		deparent();
	}

	public void deparent() throws ParseError {
		if (par != null) {
			par.remove(this);
			par = null;
		}
	}
	
	public boolean isOpen() {
		return type == OPEN;
	}
	
	public boolean isClose() {
		return type == CLOSE;
	}
	
	public void replaceWith(Node r) throws ParseError {
		r.prev = prev;
		r.nxt = nxt;
		nxt = null;
		prev = null;
		if (par != null) {
			par.replaceChild(this, r);
		}
	}


	public void replaceSymbols(HashMap<String, String> map) {
		E.error("Replace symbols not overridden " + this.getClass().getName());
	}
	 
	
	
}
