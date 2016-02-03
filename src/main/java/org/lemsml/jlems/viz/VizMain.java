package org.lemsml.jlems.viz;

import java.io.File;
import java.util.HashMap;
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.run.StateInstance;
import org.lemsml.jlems.core.run.StateType;
import org.lemsml.jlems.core.sim.LEMSException;
import org.lemsml.jlems.core.sim.Sim;
import org.lemsml.jlems.io.IOUtil;
import org.lemsml.jlems.io.Main;
import org.lemsml.jlems.io.logging.DefaultLogger;
import org.lemsml.jlems.io.out.FileResultWriterFactory;
import org.lemsml.jlems.io.reader.FileInclusionReader;
import org.lemsml.jlems.viz.datadisplay.ControlPanel;
import org.lemsml.jlems.viz.datadisplay.SwingDataViewerFactory;

public final class VizMain {

    public static final String NO_GUI_FLAG = "-nogui";

    private VizMain() {

    }

    public static void main(String[] argv) throws LEMSException {
        
        boolean useGui = true;
        FileResultWriterFactory.initialize();
        
        if (argv.length>=2 && argv[argv.length-1].equals(NO_GUI_FLAG)) {
        	useGui = false;
        }
        
        if (useGui) SwingDataViewerFactory.initialize();
        DefaultLogger.initialize();

        if (argv.length == 0) {
            System.err.println("No model file specified!");
            Main.showUsage();
            System.exit(1);
        }        
        
        if (argv.length == 1 && (argv[0].equals("-h") || argv[0].equals("-help") || argv[0].equals("-?"))) {
            Main.showUsage();
            System.exit(0);
        }
        
        HashMap<String, String> argMap = Main.parseArguments(argv);
        
        String typePath = null;
        String modelName = null;
        //boolean verbose = true;
        boolean verbose = false;
        
        if (argMap.containsKey("-cp")) {
        	typePath = argMap.get("-cp");
        	argMap.remove("-cp");
        }
        
        if (argMap.containsKey("0")) {
        	modelName = argMap.get("0");
        	argMap.remove("0");
        }
        
        if (modelName == null) {
        	Main.showUsage();
        	System.exit(1);
        }
        
        final String typePathArg = typePath;
        
        ControlPanel cp = new ControlPanel(ControlPanel.DEFAULT_NAME, useGui) {

			@Override
			public Sim importFile(File simFile) throws LEMSException {
				
		        if (!simFile.exists()) {
		        	E.error("No such file: " + simFile.getAbsolutePath());
		        	System.exit(1);
		        }

		        FileInclusionReader fir = new FileInclusionReader(simFile);
		        if (typePathArg != null) {
		        	fir.addSearchPaths(typePathArg);
		        }

                Sim sim = new Sim(fir.read());
                sim.readModel();
                sim.build();

                return sim;          
			}
        	
        };
        
        File simFile = new File(modelName);
        Sim sim = cp.initialise(simFile);
        
        StateInstance si = sim.getCurrentRootState();
        StateType st = sim.getTargetBehavior();
        
        if (verbose) {

            System.out.println("Pre run StateType: \n");
            System.out.println(st.getSummary("  ", "| ")+"\n");

            System.out.println("Pre run: \n");
            System.out.println(si.getSummary("  ", "| ")+"\n");
        }
        
            
        boolean doRun = true;
            
        if (doRun) {
        	sim.run();
        	E.info("Finished reading, building, running and displaying the LEMS model");
        }    
        
        IOUtil.saveReportAndTimesFile(sim);
    }
}
