package org.lemsml.jlems.type;

import org.lemsml.jlems.annotation.ModelProperty;
import org.lemsml.jlems.annotation.ModelElement;
import org.lemsml.jlems.sim.ContentError;

@ModelElement(info="Duplicates some functionality of PathParameter - the two should be merged.")
public class Path implements Named  {
	@ModelProperty(info="")
	public String name;

    public Path() {
    }

    public Path(String name) {
        this.name = name;
    }

    


	public void resolve(LemsCollection<Dimension> dimensions) throws ContentError {
	 	
	}


	public String getName() {
		return name;
	}

	
	
	
	
 
}
