package org.lemsml.jlems.io;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.run.RuntimeError;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.sim.LEMSException;
import org.lemsml.jlems.core.sim.Sim;
import org.lemsml.jlems.core.type.Lems;
import org.lemsml.jlems.core.type.Target;
import org.lemsml.jlems.io.reader.FileInclusionReader;
import org.lemsml.jlems.io.util.FileUtil;

public class IOUtil {

    /**
     * Get complete report file name after replacing sentinels __SIMULATOR__ and __TIMESTAMP__.
     *
     * The simulator name has to be passed in, otherwise it is empty. The time stamp is calculated.
     *
         * @param reportFile report file name with its placeholders
         * @param simulator name of simulator
     *
         * @return complete report file name with placeholders replaced
     **/
    public static String getCompleteReportFileName(String reportFile, String simulator) {
        if (simulator == null) {
            simulator = new String("");
        }
        String completeReportFile = reportFile.replace("__SIMULATOR__", simulator);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime timenow = LocalDateTime.now();
        String timestamp = dtf.format(timenow);
        completeReportFile = completeReportFile.replace("__TIMESTAMP__", timestamp);

        return completeReportFile;
    }

    public static void saveReportAndTimesFile(Sim sim, File lemsFile) throws ContentError, RuntimeError
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
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {

            info.append("Simulator=jLEMS\n");
            info.append("SimulatorVersion=" + org.lemsml.jlems.io.Main.VERSION + "\n");
            info.append("SimulationFile=" + lemsFile.getAbsolutePath() + "\n");
            
            info.append("StartTime=" + format.format(sim.initTime) + "\n");
            info.append("SetupTime=" + ((sim.simulationStartTime - sim.initTime) / 1000.0) + "\n");
            info.append("RealSimulationTime=" + ((sim.simulationEndTime - sim.simulationStartTime) / 1000.0) + "\n");
            info.append("SimulationSaveTime=" + ((sim.simulationSaveTime - sim.simulationEndTime) / 1000.0) + "\n");

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
    
    public static void main(String[] argv) throws LEMSException {
        
		File f = new File("src/test/resources/example1.xml");

    	FileInclusionReader fir = new FileInclusionReader(f);
    	Sim sim = new Sim(fir.read());

        sim.readModel();
        
        sim.build();
        
        sim.getLems().setAllIncludedFiles(fir.getAllIncludedFiles());
        
        E.info("OK - laoded " + f.getAbsolutePath());
    }
}
