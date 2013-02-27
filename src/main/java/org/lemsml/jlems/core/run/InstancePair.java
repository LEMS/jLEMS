package org.lemsml.jlems.core.run;

public class InstancePair<T> {

	T pitem;
	T qitem;
	
	public InstancePair(T p, T q) {
		pitem = p;
		qitem = q;
	}
	
	public T  getP() {
		return pitem;
	}
	
	public T getQ() {
		return qitem;
	}
	
}
