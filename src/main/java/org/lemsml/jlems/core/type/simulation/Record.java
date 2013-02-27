package org.lemsml.jlems.core.type.simulation;

import org.lemsml.jlems.core.annotation.ModelProperty;
 



public class Record {

	@ModelProperty(info="path to the parameter that will contain the path to the quantity to be recorded")
	public String quantity;
	
	
	public String timeScale;
	
	
	@ModelProperty(info="path to the element that defines the scale for rendering the quantity dimensionless")
	public String scale;
	
	@ModelProperty(info="hex format color suggestion for how the data should be displayed")
	public String color;

	
	public String destination;
	
 
    public String toString() {
    	return "Recorder q=" + quantity + ", scale=" + scale + ", display=" + destination + ", color=" + color;
    }
  
}
