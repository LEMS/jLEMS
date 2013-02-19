package org.lemsml.jlems.api;

import org.lemsml.jlems.type.Dimension;

public enum BaseDimension {
	
	MASS {void apply(Dimension d, int i) { d.setM(i); }},
	LENGTH {void apply(Dimension d, int i) { d.setL(i); }},
	TIME {void apply(Dimension d, int i) { d.setT(i); }},
	CURRENT {void apply(Dimension d, int i) { d.setI(i); }},
	TEMPERATURE {void apply(Dimension d, int i) { d.setK(i); }},
	AMOUNT {void apply(Dimension d, int i) 	{ d.setN(i); }},
	LUMINOUS_INTENSITY {void apply(Dimension d, int i) { d.setJ(i); }};
	
	
	abstract void apply(Dimension d, int i);
	
}
