package org.lemsml.jlems.type.simulation;

import org.lemsml.jlems.annotation.Mat;
 



public class Record {

	@Mat(info="path to the parameter that will contain the path to the quantity to be recorded")
	public String quantity;
	
	
	public String timeScale;
	
	
	@Mat(info="path to the element that defines the scale for rendering the quantity dimensionless")
	public String scale;
	
	@Mat(info="hex format color suggestion for how the data should be displayed")
	public String color;

	
	public String display;
 

    public Record() {
    }

    public Record(String quantity, String scale, String color, String d) {
        this.quantity = quantity;
        this.scale = scale;
        this.color = color;
        this.display = d;
    }

    public String toString() {
    	return "Recorder q=" + quantity + ", scale=" + scale + ", display=" + display + ", color=" + color;
    }
  
}
