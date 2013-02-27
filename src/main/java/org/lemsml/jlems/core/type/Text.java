package org.lemsml.jlems.core.type;

import org.lemsml.jlems.core.annotation.ModelElement;
import org.lemsml.jlems.core.annotation.ModelProperty;
import org.lemsml.jlems.core.sim.ContentError;

@ModelElement(info="Holds textual information that does not change the model but is needed for other purposes such as " +
		"labelling graphs.")
public class Text implements Named  {
	@ModelProperty(info="")
	public String name;

    
	public Text() {
		// empty
	}

    public Text(String s) {
    	name = s;
    }
	
	public void resolve(LemsCollection<Dimension> dimensions) throws ContentError {
		// nothing to do
	}


	public String getName() {
		return name;
	}

 
 
}
