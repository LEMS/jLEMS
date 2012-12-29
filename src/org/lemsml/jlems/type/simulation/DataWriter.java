package org.lemsml.jlems.type.simulation;
 
import org.lemsml.jlems.logging.E;
import org.lemsml.jlems.out.ResultWriter;
import org.lemsml.jlems.out.ResultWriterFactory;
 
import org.lemsml.jlems.run.RuntimeOutput;
import org.lemsml.jlems.sim.ContentError;
import org.lemsml.jlems.type.Component;

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
