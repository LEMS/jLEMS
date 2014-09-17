package org.lemsml.jlems.core.type;

import org.lemsml.jlems.core.annotation.ModelElement;
import org.lemsml.jlems.core.annotation.ModelProperty;
 
import org.lemsml.jlems.core.sim.ContentError;

@ModelElement(info = "The name of a component or component reference that must exist " +
		"in the component hierarchy")
public class ComponentRequirement implements Named {

    @ModelProperty(info = "name")
    public String name;
    
    
    public String description;
 

    public void resolve(LemsCollection<Dimension> dimensions) throws ContentError {
      // nothing to do
    }

    public String getName() {
        return name;
    }

   
    public ComponentRequirement makeCopy() {
        ComponentRequirement ret = new ComponentRequirement();
        ret.name = name;
       
        return ret;
    }


	protected void setName(String rn) {
		name = rn;
	}
	
	 
}
