package org.lemsml.behavior;

import org.lemsml.annotation.Mat;
import org.lemsml.annotation.Mel;
 

@Mel(info="These two attributes are paths to parameters. Values are only supplied for the " +
		"parameters when a component is built using the containing type. The 'src' attribute " +
		"should point to another element that contains Show or Record definitions. The 'timeScale' " +
		"attribute is optional, and will be superseded if this element points to a Show element" +
		" that sets a new time scale, but before pointing to a Record element at least one " +
		"element in the display chain must have set the time scale. ")
public class Show   {

	@Mat(info="path to the parameter that sets the scale for rendering times non-dimensional")
	public String scale;

	@Mat(info="path to the element that defines what should be shown")
	public String src;

	@Mat(info="path to the element that defines where it should be shown")
	public String on;

    public Show() {
    }

    public Show(String src, String scale, String on) {
        this.scale = scale;
        this.src = src;
        this.on = on;
    }


    public Show(String src) {
        this.src = src;
    }

   

 
}
