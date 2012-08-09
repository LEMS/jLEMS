package org.lemsml.behavior;

import org.lemsml.annotation.Mat;
 



public class Record {

	@Mat(info="path to the parameter that will contain the path to the quantity to be recorded")
	public String quantity;
	
	@Mat(info="path to the element that defines the scale for rendering the quantity dimensionless")
	public String scale;
	
	@Mat(info="hex format color suggestion for how the data should be displayed")
	public String color;

	@Mat(info="Optional name of file to save recorded data in")
	public String save = "save";


    public Record() {
    }

    public Record(String quantity, String scale, String color) {
        this.quantity = quantity;
        this.scale = scale;
        this.color = color;
    }


  
}
