package org.lemsml.jlems.nineml;

import org.lemsml.jlems.io.FormatException;
import org.lemsml.jlems.io.IOFace;
import org.lemsml.jlems.type.dynamics.DerivedVariable;

public class NineML_Alias implements IOFace {

	public String name;
	public String dimension;
	public NineML_MathInline mathInline;
	
	
	public Object getInternal() throws FormatException {
		return getDerivedVariable();
	}
	
	
	public DerivedVariable getDerivedVariable() throws FormatException {
		
		DerivedVariable dv = new DerivedVariable(name);
		dv.dimension = dimension;
		
		if (mathInline != null) {
			dv.value = mathInline.getFortranFormatBodyValue();
		} else {
			throw new FormatException("No math inline elemnt in " + this);
		}
		return dv;
	}
	
}
