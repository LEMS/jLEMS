package org.lemsml.jlems.core.type;

import org.lemsml.jlems.core.annotation.ModelElement;
import org.lemsml.jlems.core.annotation.ModelProperty;
import org.lemsml.jlems.core.sim.ContentError;

@ModelElement(info = "An instance that must be supplied at build time. " +
		"Expressions can contain references to quantities in the instance")
public class InstanceRequirement implements Named {

    @ModelProperty(info = "name")
    public String name;
    
    public String type;
    
    public String description;
 

    public void resolve(LemsCollection<Dimension> dimensions) throws ContentError {
    	// nothing to do
    }

    public String getName() {
        return name;
    }

   
    public InstanceRequirement makeCopy() {
        InstanceRequirement ret = new InstanceRequirement();
        ret.name = name;
       
        return ret;
    }


	protected void setName(String rn) {
		name = rn;
	}
	
	 
}
