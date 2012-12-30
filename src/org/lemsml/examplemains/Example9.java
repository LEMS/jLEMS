package org.lemsml.examplemains;

import java.io.File;

import org.lemsml.jlems.expression.ParseError;
import org.lemsml.jlems.run.ConnectionError;
import org.lemsml.jlems.run.RuntimeError;
import org.lemsml.jlems.sim.ContentError;
import org.lemsml.jlems.sim.ParseException;
import org.lemsml.jlems.type.BuildException;
import org.lemsml.jlems.xml.XMLException;
import org.lemsml.jlemsio.examples.ProcessFileExample;
import org.lemsml.jlemsio.logging.DefaultLogger;
 

public final class Example9 {
	
	private Example9() {
		
	}
	
	public static void main(String[] argv) throws ContentError, ParseError, ParseException, BuildException, XMLException, ConnectionError, RuntimeError {
	
		DefaultLogger.initialize();
		 
		File fdir = new File("../jLEMS");
		File fpd = new File(fdir, "examples");
		ProcessFileExample fe = new ProcessFileExample(fpd, "example9.xml");
		 
			fe.process();
		 
	}
	 
    
    
}
