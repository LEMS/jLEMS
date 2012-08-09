package org.lemsml.nineml;

// we don't implenent IOFace as the container does it all. 
public class NineML_Trigger {

	
	public NineML_MathInline mathInline;
	
	
	public String getExpression() {
		return mathInline.getFortranFormatBodyValue();
	}
	
	
}
