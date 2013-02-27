package org.lemsml.jlems.test.dev;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.junit.Test;
import org.lemsml.jlems.core.expression.ParseError;
import org.lemsml.jlems.core.flatten.ComponentFlattener;
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.run.ConnectionError;
import org.lemsml.jlems.core.run.RuntimeError;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.sim.ParseException;
import org.lemsml.jlems.core.sim.Sim;
import org.lemsml.jlems.core.type.BuildException;
import org.lemsml.jlems.core.type.Component;
import org.lemsml.jlems.core.type.ComponentType;
import org.lemsml.jlems.core.type.Lems;
import org.lemsml.jlems.core.xml.XMLException;
import org.lemsml.jlems.io.logging.DefaultLogger;
import org.lemsml.jlems.io.out.FileResultWriterFactory;
import org.lemsml.jlems.io.reader.FileInclusionReader;
import org.lemsml.jlems.viz.datadisplay.SwingDataViewerFactory;
 


public class FlatComparisionTest {

	
	 
/*
    public static void main(String[] args) {
    	DefaultLogger.initialize();
    	
    	ComponentFlatteningTest ct = new ComponentFlatteningTest();
        Result r = org.junit.runner.JUnitCore.runClasses(ct.getClass());
        MainTest.checkResults(r);

    }
*/
	
	 
    public static void main(String[] args) throws ContentError, ParseError, ConnectionError, RuntimeError, IOException, ParseException, BuildException, XMLException {
    	DefaultLogger.initialize();
 
    	FlatComparisionTest cft = new FlatComparisionTest();
    		cft.run();
    	 
    }
    
    
    
    @Test
    public void run() throws ContentError, ConnectionError, ParseError, IOException, RuntimeError, ParseException, BuildException, XMLException {
    	SwingDataViewerFactory.initialize();
		DefaultLogger.initialize();
		FileResultWriterFactory.initialize();
    	
		URL url = this.getClass().getResource("/ex-flat.xml");
		File fex = new File(url.getFile());
		
		runTree(fex);

		runNormal(fex);

		runComponentFlattened(fex, "na");
    
    
    }

	
	public void runTree(File f) throws ContentError, ParseError, ParseException, BuildException, XMLException, ConnectionError, RuntimeError {
 		FileInclusionReader fir = new FileInclusionReader(f);
		Sim sim = new Sim(fir.read()); 
		sim.setNoConsolidation();
		sim.readModel();	
		sim.build();
		sim.runTree();	 
	}
	

	public void runNormal(File f) throws ContentError, ParseError, ParseException, BuildException, XMLException, ConnectionError, RuntimeError {
		FileInclusionReader fir = new FileInclusionReader(f);	
		Sim sim = new Sim(fir.read());
		sim.readModel();	
		sim.build();
		sim.run();
	}
	 
	
    
    public void runComponentFlattened(File f, String tgtid) throws ContentError,
    		ConnectionError, ParseError, IOException, RuntimeError, ParseException, 
    		BuildException, XMLException {
    	E.info("Loading LEMS file from: " + f.getAbsolutePath());

        FileInclusionReader fir = new FileInclusionReader(f);
        Sim sim = new Sim(fir.read());

        sim.readModel();
    
        Lems lems = sim.getLems();
        Component cpt = lems.getComponent(tgtid);

        ComponentFlattener cf = new ComponentFlattener(lems, cpt);

        ComponentType ct = cf.getFlatType();
        Component cp = cf.getFlatComponent();
        
        // String typeOut = XMLSerializer.serialize(ct);
        // String cptOut = XMLSerializer.serialize(cp);
      
        // E.info("Flat type: \n" + typeOut);
        // E.info("Flat cpt: \n" + cptOut);
        
		lems.addComponentType(ct);
		lems.addComponent(cp);
	
		lems.resolve(ct);
		lems.resolve(cp);
	 
		cpt.setReplacement(cp);
		
		sim.build();
		
		sim.runTree();
		
		
		// TODO now substitue cp for cpt somehow and run...
    }
}
