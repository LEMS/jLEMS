package org.lemsml.nineml;

import org.lemsml.behavior.StateAssignment;
import org.lemsml.io.FormatException;

public class NineML_StateAssignment  {

	public String variable;
	public NineML_MathInline mathInline;

 
	
	public StateAssignment getStateAssignment() throws FormatException {	
		StateAssignment sa = new StateAssignment(variable);
		if (mathInline != null) {
			sa.value = mathInline.getFortranFormatBodyValue();
		} else {
			throw new FormatException("No math inline elemnt in " + this);
		}
		
		return sa;
	}
	 

  
}
