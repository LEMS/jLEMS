package org.lemsml.jlemsviz;
 
import java.io.File;
 
import org.lemsml.jlems.logging.E;
import org.lemsml.jlems.sim.Sim;
import org.lemsml.jlemsio.logging.DefaultLogger;
import org.lemsml.jlemsio.reader.FileInclusionReader;
import org.lemsml.jlemsviz.datadisplay.SwingDataViewerFactory;

public final class Main {

	 static String usage = "USAGE: java -jar lems-0.X.X.jar model-file [-options]\n";
	

	 private Main() {
		 
	 }
	 
	 
	 public static void showUsage() {
		 E.info(usage);
	 }
	
	 
	
    public static void main(String[] argv) {        
    	
    	SwingDataViewerFactory.initialize();
		DefaultLogger.initialize();
	 
    	
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

        try {
        	FileInclusionReader fir = new FileInclusionReader(simFile);
        	Sim sim = new Sim(fir.read());
            
              sim.readModel();
              sim.build();
            
            boolean doRun = true;
            
            if (argv.length > 1) {
                String opt = argv[1];
                E.info("Error, unrecognized command line element: (" + opt + ")");
                showUsage();
            }
          
            
            if (doRun) {
                sim.run();
                E.info("Finished reading, building, running & displaying LEMS model");
            }


        } catch (Exception ex) {
            String msg = "Problem reading model from " + simFile.getAbsolutePath();
            E.report(msg, ex);
        }
    }
}
