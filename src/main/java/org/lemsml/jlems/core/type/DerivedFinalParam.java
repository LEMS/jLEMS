package org.lemsml.jlems.core.type;

import org.lemsml.jlems.core.sim.ContentError;

public class DerivedFinalParam extends FinalParam {

	String select;
	String value;
	
	final static int SELECT = 1;
	final static int VALUE = 2;
	int mode;
	
	public DerivedFinalParam(String nm, Dimension d, String s, String v) throws ContentError {
		super(nm, d);
		if (s != null && v != null) {
			throw new ContentError("only one of select and value can be supplied in DerivedParameter");
		} else if (s != null) {
			select = s;
			mode = SELECT;
			
		} else if (v != null) {
			value = v;
			mode = VALUE;
			
		} else {
			throw new ContentError("must suply select or value in DerivedParameter");
		}
		select = s;
	}
	
	public FinalParam makeCopy() throws ContentError {
		return new DerivedFinalParam(getName(), getDimension(), select, value);
	}

	public boolean isSelect() {
		return (mode == SELECT);
	}
	
	public boolean isValue() {
		return (mode == VALUE);
	}
	
	
	public String getSelect() {
		 return select;
	}
	
	public String getValueString() {
		return value;
	}
}
