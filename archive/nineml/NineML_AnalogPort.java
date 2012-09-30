package org.lemsml.jlems.nineml;

import org.lemsml.jlems.type.ComponentType;
import org.lemsml.jlems.type.Exposure;
import org.lemsml.jlems.type.dynamics.DerivedVariable;
import org.lemsml.jlems.type.dynamics.Dynamics;
import org.lemsml.jlems.util.ContentError;

public class NineML_AnalogPort  {

	public String name;
	public String dimension;
	public String mode;
	public String reduce_op;
	
	
	 
	
	
	public void applyToType(ComponentType ct) throws ContentError {
		if (mode.equals("send")) {
			Exposure expo = new Exposure(name);
			expo.dimension = dimension;
			ct.exposures.add(expo);
			
		} else {
			Exposure expo = new Exposure(name);
			expo.dimension = dimension;
			ct.addExposure(expo);
			
			Dynamics b = ct.getDynamics();
			DerivedVariable dv = new DerivedVariable(name, dimension);
			dv.exposure = name;
			dv.value = "0";
			b.addDerivedVariable(dv);
 		}
	}
}
