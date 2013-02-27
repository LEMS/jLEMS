package org.lemsml.jlems.core.type.simulation;

import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.run.RuntimeDisplay;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.type.Component;

public class DataDisplay {

	public String title;
	
	public String dataRegion;

  

	public RuntimeDisplay getRuntimeOutput(Component cpt) throws ContentError {
		RuntimeDisplay ret = new RuntimeDisplay();
		ret.setID(cpt.id);
		ret.setTitle(cpt.getTextParam(title));
		
		if (dataRegion != null) {
			String[] bits = dataRegion.split(",");
			if (bits.length == 4) {
				double[] box = new double[4];
				for (int i = 0; i < 4; i++) {
					box[i] = cpt.getParamValue(bits[i]).getDoubleValue();
				}
				ret.setBox(box);
				
 			} else {
 				E.warning("Expect 4 items in region, " + dataRegion);
 			}
		}
		return ret;
		
	}
	
}
