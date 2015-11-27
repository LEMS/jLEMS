package org.lemsml.jlems.core.type.simulation;
 
import org.lemsml.jlems.core.run.RuntimeEventOutput;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.type.Component;
 

public class EventWriter {

	public String id;
	
	public String path;
	
	public String fileName;
	
	
	public RuntimeEventOutput getRuntimeEventOutput(Component cpt) throws ContentError {
		RuntimeEventOutput ret = new RuntimeEventOutput();
		ret.setID(cpt.getID());
 		if (cpt.hasTextParam(path)) {
			ret.setPath(cpt.getTextParam(path));
		}  
		ret.setFileName(cpt.getTextParam(fileName));
		return ret;
	}
 
}
