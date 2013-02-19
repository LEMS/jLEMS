package org.lemsml.jlems.viz;
 
import org.lemsml.jlems.expression.ParseError;
import org.lemsml.jlems.io.Main;
import org.lemsml.jlems.io.logging.DefaultLogger;
import org.lemsml.jlems.io.out.FileResultWriterFactory;
import org.lemsml.jlems.run.ConnectionError;
import org.lemsml.jlems.run.RuntimeError;
import org.lemsml.jlems.sim.ContentError;
import org.lemsml.jlems.sim.ParseException;
import org.lemsml.jlems.type.BuildException;
import org.lemsml.jlems.viz.datadisplay.SwingDataViewerFactory;
import org.lemsml.jlems.xml.XMLException;

public final class VizMain {
 
	 private VizMain() {
		 
	 }
	 
  
	
    public static void main(String[] argv) throws ConnectionError, ContentError, RuntimeError, ParseError, ParseException, BuildException, XMLException {        
    	FileResultWriterFactory.initialize();
    	SwingDataViewerFactory.initialize();
		DefaultLogger.initialize();
	 
    	Main.main(argv);
    }
}
