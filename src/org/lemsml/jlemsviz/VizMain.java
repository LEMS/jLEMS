package org.lemsml.jlemsviz;
 
import org.lemsml.jlems.expression.ParseError;
import org.lemsml.jlems.run.ConnectionError;
import org.lemsml.jlems.run.RuntimeError;
import org.lemsml.jlems.sim.ContentError;
import org.lemsml.jlems.sim.ParseException;
import org.lemsml.jlems.type.BuildException;
import org.lemsml.jlems.xml.XMLException;
import org.lemsml.jlemsio.Main;
import org.lemsml.jlemsio.logging.DefaultLogger;
import org.lemsml.jlemsviz.datadisplay.SwingDataViewerFactory;

public final class VizMain {
 
	 private VizMain() {
		 
	 }
	 
  
	
    public static void main(String[] argv) throws ConnectionError, ContentError, RuntimeError, ParseError, ParseException, BuildException, XMLException {        
    	
    	SwingDataViewerFactory.initialize();
		DefaultLogger.initialize();
	 
    	Main.main(argv);
    }
}
