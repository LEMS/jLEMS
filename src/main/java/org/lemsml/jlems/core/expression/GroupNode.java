package org.lemsml.jlems.core.expression;

import java.util.ArrayList;

public class GroupNode extends Node {

	ArrayList<Node> children;
	
	
	boolean gathered = false;
	
	public GroupNode() {
		this(null);
	}
	
	
	public GroupNode(Node p) {
		super(p);
		children = new ArrayList<Node>();
	}
	
	

    @Override
	public String toString() {
		return "Group[" + (children.isEmpty() ?  "" : first().siblingsToString()) + "]";
	}
	
	public void remove(Node n) {
		children.remove(n);
		// E.info("remove in group node... " + childHS.size());
	}
    
    @Override
    public void replaceChild(Node nold, Node nnew) throws ParseError {
        for (int i = 0; i < children.size(); i++) {
            Node child = children.get(i);
            if (child.equals(nold)) {
                children.set(i, nnew);
            }
        }
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

	
	public int size() {
		return children.size();
	}
	
	
	public void gatherPreceeding() throws ParseError {
		
		Node pre = previous();
		pre.detachNext();
 	
		while (pre != null) {
			if (pre instanceof OpenNode) {
				if (pre.previous() != null) {
					pre.previous().replaceNext(this);
				}
				pre.dispose();
				break;					
					
			} else if (pre instanceof GroupNode && !((GroupNode)pre).gathered) {
				throw new ParseError("close bracket in wrong place");
			}
			pre.deparent();
			add(pre);
			pre = pre.previous();
		}		
		gathered = true;
	}
	
	
	
	public void supplantByChild() throws ParseError {
		if (children.size() == 1) {
			Node cn = children.get(0);
			replaceWith(cn);
 		
		} else {
			throw new ParseError("Error in supplantByChild in "+this+"\nToo many child nodes left: " + children);
		}
	}
	
	
}
