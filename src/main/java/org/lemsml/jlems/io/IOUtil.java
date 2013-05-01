package org.lemsml.jlems.io;

import java.io.File;
import java.io.IOException;

import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.run.RuntimeError;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.sim.Sim;
import org.lemsml.jlems.core.type.Lems;
import org.lemsml.jlems.core.type.Target;
import org.lemsml.jlems.io.util.FileUtil;

public class IOUtil {


    public static void saveReportAndTimesFile(Sim sim) throws ContentError, RuntimeError
    {

        File reportFile = null;
        File timesFile = null;
        
        Lems lems = sim.getLems();
        
        Target t = lems.getTarget();
	    
	    if (t.reportFile != null) {
	    	reportFile = new File(t.reportFile);
	    } else {
        	//E.info("No reportFile specified in Target element");
	    }
	    if (t.timesFile != null) {
	    	timesFile = new File(t.timesFile);
	    }
        
	    StringBuilder info = new StringBuilder("# Report of running simulation with jLEMS v" + org.lemsml.jlems.io.Main.VERSION + "\n");
        StringBuilder times = new StringBuilder();

        if (reportFile != null) {
            E.info("Simulation report will be saved in " + reportFile.getAbsolutePath());
        }

        for(double time: sim.times) {
        	times.append((float)time+"\n");
        }

        try {

            info.append("RealSimulationTime=" + ((sim.simulationEndTime - sim.simulationStartTime) / 1000.0) + "\n");
            info.append("SimulationSaveTime=" + ((sim.simulationSaveTime - sim.simulationEndTime) / 1000.0) + "\n");
            info.append("SimulatorVersion=" + org.lemsml.jlems.io.Main.VERSION + "\n");

            if (reportFile != null) {
                FileUtil.writeStringToFile(info.toString(), reportFile);
            }
            if (timesFile != null) {
                FileUtil.writeStringToFile(times.toString(), timesFile);
            }

        } catch (IOException ex) {
            throw new RuntimeError("Problem saving traces to file", ex);
        }
    }
}
