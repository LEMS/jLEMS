package org.lemsml.jlems.core.type.simulation;
 
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.out.ResultWriter;
import org.lemsml.jlems.core.out.ResultWriterFactory;
import org.lemsml.jlems.core.run.RuntimeOutput;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.type.Component;
 

public class DataWriter {

	public String id;
	
	public String path;
	
	public String fileName;

	
	
	public RuntimeOutput getRuntimeOutput(Component cpt) throws ContentError {
		RuntimeOutput ret = new RuntimeOutput();
		ret.setID(cpt.getID());
 		if (cpt.hasTextParam(path)) {
			ret.setPath(cpt.getTextParam(path));
		}  
		ret.setFileName(cpt.getTextParam(fileName));
		return ret;
	}
	
	 
  
 
}
