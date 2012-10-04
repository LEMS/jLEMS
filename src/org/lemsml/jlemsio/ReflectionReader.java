package org.lemsml.jlemsio;

import org.lemsml.jlems.io.LemsMap;
import org.lemsml.jlems.io.NameMapper;
import org.lemsml.jlems.type.Lems;
import org.lemsml.jlems.type.dynamics.Dynamics;
import org.lemsml.jlems.type.procedure.Procedure;
import org.lemsml.jlems.type.simulation.Simulation;
import org.lemsml.jlems.type.structure.Structure;
import org.lemsml.jlems.util.ContentError;
import org.lemsml.jlemsio.xmlio.ReflectionInstantiator;
import org.lemsml.jlemsio.xmlio.XMLReader;

public class ReflectionReader {
	
	
	// NB this is old code - just here in case we want it again. 
	// probably doesn't run
	
	
	public void reflectionReadModel() throws ContentError {
		ReflectionInstantiator ri = new ReflectionInstantiator();
		ri.addSearchPackage(Lems.class.getPackage());
		ri.addSearchPackage(Dynamics.class.getPackage());
		ri.addSearchPackage(Structure.class.getPackage());
		ri.addSearchPackage(Simulation.class.getPackage());
		ri.addSearchPackage(Procedure.class.getPackage());
		
		NameMapper cm = new LemsMap();
		ri.setImportNameMapper(cm);
		reflectionReadModel(ri, false);
		 
	}

 
	public void reflectionReadModel(ReflectionInstantiator refin, boolean loose) throws ContentError {
	    String stxt = getSourceText();

	    XMLReader xmlr = new XMLReader(refin);
	
	     try {
	        Lems lems = (Lems) (xmlr.read(stxt));
	     
	        if (loose) {
	        	lems.setResolveModeLoose();
	        }
	        
	        lems.deduplicate();
	        lems.resolve();
	        lems.evaluateStatic();
	 
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    }          
	}


	private String getSourceText() {
		// TODO Auto-generated method stub
		return null;
	}
}
