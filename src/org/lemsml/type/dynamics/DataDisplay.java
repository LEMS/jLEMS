package org.lemsml.type.dynamics;

import org.lemsml.run.RuntimeOutput;
import org.lemsml.type.Component;
import org.lemsml.util.ContentError;
import org.lemsml.util.E;

public class DataDisplay {

	public String title;
	
	public String dataRegion;

 	public DataDisplay() {
		
	}

	public RuntimeOutput getRuntimeOutput(Component cpt) throws ContentError {
		RuntimeOutput ret = new RuntimeOutput();
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
