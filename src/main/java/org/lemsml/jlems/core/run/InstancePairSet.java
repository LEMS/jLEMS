package org.lemsml.jlems.core.run;

import java.util.ArrayList;

import org.lemsml.jlems.core.logging.E;

public class InstancePairSet<T> {

	String name;
	
	ArrayList<T> pitems;
	ArrayList<T> qitems;
	
	StateInstance parent;
	 
	
	public final static int OUTERPRODUCT = 0;
	public final static int EXPLICIT = 1;
	
	public int mode = OUTERPRODUCT;
	// outer product mode just means the pairs consist of each element of pitems with each element of qitems
	// in this case there is no need to explicitly enumerate them yet
	
	ArrayList<InstancePair<T>> pairs;
	
	
	public InstancePairSet(String nm, StateInstance p) {
		name = nm;
		parent = p;
	}

	public void setItems(ArrayList<T> ps, ArrayList<T> qs) {
		pitems = ps;
		qitems = qs;
	}
	
	public String toString() {
		String ret = "InstancePairs set: " + name + "(" + pitems.size() + "," +qitems.size() +") mode=" + mode;
		return ret;
	}
	

	public ArrayList<InstancePair<T>> getPairs() {
		ArrayList<InstancePair<T>> ret = null;
		if (mode == EXPLICIT) {
			ret = pairs;
		} else {
			ret = new ArrayList<InstancePair<T>>();
			for (T p : pitems) {
				for (T q : qitems) {
					ret.add(new InstancePair<T>(p, q));
				}
			}
		}
		return ret;
	}
	

	public String getName() {
		return name;
	}
 
	public StateInstance getParent() {
		return parent;
	}

	
	public void explicitize() {
		if (mode == OUTERPRODUCT) {
			pairs = getPairs();
			mode = EXPLICIT;
		}
	}
	
	public void empty() {
		mode = EXPLICIT;
		if (pairs == null) {
			pairs = new ArrayList<InstancePair<T>>(); 
		} else {
			pairs.clear();
		} 
    }	 

	public void addPair(InstancePair<T> work) {
		if (mode != EXPLICIT) {
			E.error("must make pair set explicit before adding individual pairs");
		}
		pairs.add(work);
    }
	
}
