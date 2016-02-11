package org.lemsml.jlems.core.type.simulation;
 
import org.lemsml.jlems.core.run.RuntimeEventOutput;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.type.Component;
 

public class EventWriter {

	public String id;
	
	public String path;
	
	public String fileName;
	
	public String format;
    
    public static final String FORMAT_TIME_ID = "TIME_ID";
    public static final String FORMAT_ID_TIME = "ID_TIME";
	
	
	public RuntimeEventOutput getRuntimeEventOutput(Component cpt) throws ContentError {
		RuntimeEventOutput ret = new RuntimeEventOutput();
		ret.setID(cpt.getID());
 		if (cpt.hasTextParam(path)) {
			ret.setPath(cpt.getTextParam(path));
		}  
		ret.setFileName(cpt.getTextParam(fileName));
		ret.setFormat(cpt.getTextParam(format));
		return ret;
	}
 
}
