package org.lemsml.jlems.nineml;

import org.lemsml.jlems.io.FormatException;
import org.lemsml.jlems.type.dynamics.TimeDerivative;

public class NineML_TimeDerivative {

	public String variable;
 	public NineML_MathInline mathInline;
	
	 
	
	
	public TimeDerivative getTimeDerivative() throws FormatException {
		TimeDerivative td = new TimeDerivative(variable);
	 	
		if (mathInline != null) {
			td.value = mathInline.getFortranFormatBodyValue();
		} else {
			throw new FormatException("No math inline elemnt in " + this);
		}
		return td;
	}
}
