package org.lemsml;
 
import java.io.File;

import org.lemsml.sim.Sim;
import org.lemsml.util.E;
import org.lemsml.util.FileUtil;
 

public class Main {

	 static String usage = "USAGE: java -jar lems-0.X.X.jar model-file [-options]\n\n"
         + "or use the included make.bat/lems.bat (Win), make.sh/lems (Linux/Mac):\n\n"
          + "\n"
         + "Running LEMS without any optional arguments makes the interpreter run a " +
         		"simulation of the component specified by the 'DefaultRun' element in the model file.\n"
         + "\n"
         + "Optional attributes include:\n"
         + "    -nogui       Run simulation of model in interpreter but suppress any graphical elements (just save results)\n"
         + "    -c           Generate canonical form of model\n";
 
	
	 
	 
	 public static void showUsage() {
		 E.info(usage);
	 }
	 
	 
	
    public static void main(String[] argv) {

        
        if (argv.length == 0) {
            showUsage();
            System.exit(1);
        }
        File simFile = new File(argv[0]);
 
        if (!simFile.exists()) {
        	E.error("No such file: " + simFile.getAbsolutePath());
        	showUsage();
        	System.exit(1);
        }

        Sim sim = new Sim(simFile);

        try {
            
              sim.readModel();
              sim.build();
           
          
                // sim.readMixedModel();
                // sim.build();
           
 
            boolean doRun = true;
            
            if (argv.length > 1) {
                String opt = argv[1];
                
                if (opt.equals("-c")) {
                	doRun = false;
                	  String sc = argv[0].replace(".xml", "-ccl.xml");
                      File fout = new File(sc);
                      E.info("Writing out canonical form to: " + fout.getAbsolutePath());
                      FileUtil.writeStringToFile(sim.canonicalText(), fout);

                } else {
                	E.info("Error, unrecognized command line element: (" + opt + ")");
                	showUsage();
                }
            }
          
            
            if (doRun) {
                sim.run();
                E.info("Finished reading, building, running & displaying LEMS model");
            }


        } catch (Exception ex) {
            E.info("Problem reading model from " + simFile.getAbsolutePath());
        	E.info(ex.getMessage());
            ex.printStackTrace();
        }
    }
}
