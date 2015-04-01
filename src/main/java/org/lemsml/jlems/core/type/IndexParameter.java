package org.lemsml.jlems.core.type;

import org.lemsml.jlems.core.sim.ContentError;

public class IndexParameter extends Parameter {
	
	
	@Override
	public void resolve(final LemsCollection<Dimension> dimensions) throws ContentError {
		    
	        if (dimension != null) {
	           throw new ContentError("Index paramters must be dimensionless: " + name);
	            
	        }  
	        r_dimension = new Dimension();
	    }
}
