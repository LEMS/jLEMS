package org.lemsml.sim;

import java.io.File;
import java.io.IOException;

import org.lemsml.display.ComponentBehaviorWriter;
import org.lemsml.display.DataViewer;
import org.lemsml.display.DataViewerFactory;
import org.lemsml.expression.ParseError;
import org.lemsml.run.ComponentBehavior;
import org.lemsml.run.ConnectionError;
import org.lemsml.run.EventManager;
import org.lemsml.run.RunConfig;
import org.lemsml.run.RunDisplay;
import org.lemsml.run.StateInstance;
import org.lemsml.type.Component;
import org.lemsml.type.Lems;
import org.lemsml.type.Target;
import org.lemsml.util.ContentError;
import org.lemsml.util.E;
import org.lemsml.util.FileUtil;
import org.lemsml.util.RuntimeError;


public class Sim extends LemsProcess {

    // disable display output
    private static boolean disableFrames = false;

    ComponentBehavior rootBehavior;
    ComponentBehavior targetBehavior;
    
    RunConfig runConfig;
    RunDisplay runDisplay;
    StateInstance rootState;
    
    File reportFile = null;
    File timesFile = null;

    
    
    
    public Sim(Class<?> c, String fnm) {
    	super(c, fnm);
    }

    public Sim(File file) {
       super(file);
     
    }

    public Sim(String srcStr) {
    	super(srcStr);
    }

    public Sim(Lems lems) {
       super(lems);
    }

    public static boolean isDisableFrames() {
        return disableFrames;
    }

    public static void setDisableFrames(boolean df) {
        disableFrames = df;
    }
   
    public void build() throws ContentError, ConnectionError, ParseError {
    	build(false);
    }
    	
    	
    public void build(boolean consolidate) throws ContentError, ConnectionError, ParseError {
    	
	    Target dr = lems.getTarget();
	
	    Component simCpt = dr.getComponent();
	
	    if (simCpt == null) {
	        E.error("No such component: " + dr.component + " as referred to by default simulation.");
	        E.error(lems.textSummary());
	    }
	
	    E.info("Simulation component: " + simCpt);
	
	    rootBehavior = simCpt.getComponentBehavior();
	    
	    runConfig = rootBehavior.getRunConfig();
	
	    Component runCpt = runConfig.getTarget();
	    
	    EventManager eventManager = new EventManager();
	
		targetBehavior = runCpt.getComponentBehavior();
		
		 if (consolidate) {
			 E.info("Getting consolidated version of root component");
			 targetBehavior = targetBehavior.makeConsolidatedBehavior("ROOT");
		}
		
	    rootState = lems.build(targetBehavior, eventManager);
	
	    runDisplay = simCpt.getRunDisplay(rootState);	    
	    
	    if (dr.reportFile != null) {
	    	reportFile = new File(dr.reportFile);
	    }
	    if (dr.timesFile != null) {
	    	timesFile = new File(dr.timesFile);
	    }    
	}


    public void run() throws ConnectionError, ContentError, RuntimeError, IOException {
        run(true);
    }

    public void run(boolean showFrame) throws ConnectionError, ContentError, RuntimeError, IOException {

        runUserDefined(showFrame);

        E.info("Done");
    }

    public void runUserDefined() throws ConnectionError, ContentError, RuntimeError, IOException {
        runUserDefined(true);
    }

    
    
    public void runUserDefined(boolean showFrame) throws ConnectionError, ContentError, RuntimeError, IOException {

        RunnableAccessor ra = new RunnableAccessor(rootState);

        runDisplay.connectRunnable(ra);

        DataViewer sv = DataViewerFactory.getFactory().newDataViewer();

        double dt = runConfig.getTimestep();
        int nstep = (int) Math.round(runConfig.getRuntime() / dt);

        E.info("Running for " + nstep + " steps, with " + runDisplay.getDisplayItems().size() + " display items");

      

        StringBuilder info = new StringBuilder("#Report of running simulation with LEMS Interpreter\n");
        StringBuilder times = new StringBuilder();

        if (reportFile != null) {
            E.info("Simulation report will go in " + reportFile.getAbsolutePath());
        }

        long start = System.currentTimeMillis();
  
        double t = 0;
        
       
        rootState.initialize(null);  
        EventManager eventManager = rootState.getEventManager();
        
        for (int istep = 0; istep < nstep; istep++) {
        	if (istep > 0) {
        		eventManager.advance(t);
                rootState.advance(null, t, dt);
        	}

            if (runDisplay == null) {
                rootState.exportState("", t, sv);
            } else {
                runDisplay.appendState(t, sv);
            }
            times.append((float) (t * 1000)).append("\n");
            t += dt;
        }
        E.info("Finished " + nstep + " steps");

        
        long end = System.currentTimeMillis();
        info.append("RealSimulationTime=" + ((end - start) / 1000.0) + "\n");
        start = System.currentTimeMillis();

        
        sv.showFinal();
        

        try {
            end = System.currentTimeMillis();
            info.append("SimulationSaveTime=" + ((end - start) / 1000.0) + "\n");

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

	public void printCB() {
		ComponentBehaviorWriter cbw = new ComponentBehaviorWriter();
		cbw.print(targetBehavior);
		
	}
}
