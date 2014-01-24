package org.lemsml.jlems.io;
 
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.lemsml.jlems.core.expression.ParseError;
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.run.ConnectionError;
import org.lemsml.jlems.core.run.RuntimeError;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.sim.ParseException;
import org.lemsml.jlems.core.sim.Sim;
import org.lemsml.jlems.core.type.BuildException;
import org.lemsml.jlems.core.type.Lems;
import org.lemsml.jlems.core.type.Target;
import org.lemsml.jlems.core.xml.XMLException;
import org.lemsml.jlems.io.reader.FileInclusionReader;
import org.lemsml.jlems.io.util.FileUtil;
 

public final class Main {

	 public static final String VERSION = "0.9.5.3";
	 
	 static String usage = "USAGE: java -jar target/jlems-"+VERSION+".jar [-cp folderpaths] model-file\n";
	 

	 private Main() {
		 
	 }
	 
	 
	 public static void showUsage() {
		 E.info(usage);
	 }
	 
	 
	
    public static void main(String[] argv) throws ConnectionError, ContentError, RuntimeError, ParseError, ParseException, BuildException, XMLException {        
        if (argv.length == 0) {
            showUsage();
            System.exit(1);
        }
        
        HashMap<String, String> argMap = parseArguments(argv);
        
        String typePath = null;
        String modelName = null;
        
        if (argMap.containsKey("-cp")) {
        	typePath = argMap.get("-cp");
        	argMap.remove("-cp");
        }
        if (argMap.containsKey("0")) {
        	modelName = argMap.get("0");
        	argMap.remove("0");
        }
        
        if (modelName == null) {
        	showUsage();
        	System.exit(1);
        }
        
        File simFile = new File(modelName);
 
        if (!simFile.exists()) {
        	E.error("No such file: " + simFile.getAbsolutePath());
        	System.exit(1);
        }

        FileInclusionReader fir = new FileInclusionReader(simFile);
        if (typePath != null) {
        	fir.addSearchPaths(typePath);
        }
        Sim sim = new Sim(fir.read());
            
        sim.readModel();
        sim.build();
        
            
        boolean doRun = true;
            
        if (doRun) {
        	sim.run();
        	E.info("Finished reading, building, running and displaying the LEMS model");
        }    
        	
        IOUtil.saveReportAndTimesFile(sim);
        
    }
    
    
     
    private static HashMap<String, String> parseArguments(String[] argv) {
    	HashMap<String, String> ret = new HashMap<String, String>();
    	
    	int iarg = 0;
    	int ifree = 0;
    	while (true) {
    		if (iarg < argv.length) {
    			String s = argv[iarg];
      			iarg += 1;
    			if (s.startsWith("-")) {
     				if (iarg < argv.length) {
    					ret.put(s, argv[iarg]);
    					iarg += 1;
    				}
    			} else {
      				ret.put("" + ifree, s);
    				ifree += 1;
    			}
    		} else {
    			break;
    		}
    	}
    	return ret;
    }
    
}
