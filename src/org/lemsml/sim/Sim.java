package org.lemsml.sim;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.lemsml.display.ComponentBehaviorWriter;
import org.lemsml.display.DataViewPort;
import org.lemsml.display.DataViewer;
import org.lemsml.display.DataViewerFactory;
import org.lemsml.expression.ParseError;
import org.lemsml.run.ComponentBehavior;
import org.lemsml.run.ConnectionError;
import org.lemsml.run.EventManager;
import org.lemsml.run.RunConfig;
import org.lemsml.run.RuntimeOutput;
import org.lemsml.run.RuntimeRecorder;
import org.lemsml.run.StateInstance;
import org.lemsml.type.Component;
import org.lemsml.type.Lems;
import org.lemsml.type.Target;
import org.lemsml.util.ContentError;
import org.lemsml.util.E;
import org.lemsml.util.FileUtil;
import org.lemsml.util.RuntimeError;


public class Sim extends LemsProcess {

   ComponentBehavior rootBehavior;
    ComponentBehavior targetBehavior;
    
     
    HashMap<String, DataViewer> dvHM;
      
    ArrayList<RunConfig> runConfigs;
    
    EventManager eventManager;
    
    
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

 	
    	
    public void build() throws ContentError, ConnectionError, ParseError {
    	
	    Target dr = lems.getTarget();
	
	    Component simCpt = dr.getComponent();
	
	    if (simCpt == null) {
	        E.error("No such component: " + dr.component + " as referred to by default simulation.");
	        E.error(lems.textSummary());
	        throw new ContentError("No such component " + dr.component);
	    }
	
	    
	    E.info("Simulation component: " + simCpt);
	
	    rootBehavior = simCpt.getComponentBehavior();
	    
	    // collect everything in the ComponentBehavior tree that makes a display
	    ArrayList<RuntimeOutput> runtimeOutputs = new ArrayList<RuntimeOutput>();
	    OutputCollector oc = new OutputCollector(runtimeOutputs);
	    rootBehavior.visitAll(oc);
	   
	    // build the displays and keep them in dvHM
	    dvHM = new HashMap<String, DataViewer>();
	    for (RuntimeOutput ro : runtimeOutputs) {
	    	DataViewer dv = DataViewerFactory.getFactory().newDataViewer(ro.getTitle());
	    	dvHM.put(ro.getID(), dv);
	    	if (dv instanceof DataViewPort) {
	    		((DataViewPort)dv).setRegion(ro.getBox());
 	    	}
	    }
	     
	   
	    runConfigs = new ArrayList<RunConfig>();
	    RunConfigCollector rcc = new RunConfigCollector(runConfigs);
	    rootBehavior.visitAll(rcc);
	     
	    eventManager = new EventManager();
	     
	}

    public void run() throws ConnectionError, ContentError, RuntimeError, IOException, ParseError {
    	run(true);
    }
    
   
    public void run(boolean flatten) throws ConnectionError, ContentError, RuntimeError, IOException, ParseError {
    	E.info("Run configs to run: " + runConfigs.size());
    	for (RunConfig rc : runConfigs) {
    		run(rc, flatten);
    	}
        E.info("Done");
    }

  
    
    
    public void run(RunConfig rc, boolean flatten) throws ConnectionError, ContentError, RuntimeError, IOException, ParseError {
   	    	
  		ComponentBehavior raw = rc.getTarget();
  		
  		if (flatten) {
  			targetBehavior = raw.getConsolidatedComponentBehavior("root");
  		} else {
  			targetBehavior = raw;
  		}
  		
  	    StateInstance rootState = lems.build(targetBehavior, eventManager);
  	
  	    RunnableAccessor ra = new RunnableAccessor(rootState);
  	       
  	    ArrayList<RuntimeRecorder> recorders = rc.getRecorders();
  	    
  	    for (RuntimeRecorder rr : recorders) {
  	    	String disp = rr.getDisplay();
  	    	if (dvHM.containsKey(disp)) {
  	    		rr.connectRunnable(ra, dvHM.get(disp));
  	    	} else {
  	    		throw new ConnectionError("No such data viewer " + disp + " needed for " + rr);
  	    	}
  	    }
  	    
        double dt = rc.getTimestep();
        int nstep = (int) Math.round(rc.getRuntime() / dt);

        E.info("Running for " + nstep + " steps");
      
        StringBuilder info = new StringBuilder("#Report of running simulation with LEMS Interpreter\n");
        StringBuilder times = new StringBuilder();
 
        long start = System.currentTimeMillis();
  
        double t = 0;
        
       
        rootState.initialize(null);  
        EventManager eventManager = rootState.getEventManager();
        
        for (int istep = 0; istep < nstep; istep++) {
        	if (istep > 0) {
        		eventManager.advance(t);
                rootState.advance(null, t, dt);
        	}
        	
        	for (RuntimeRecorder rr : recorders) {
        		rr.appendState(t);
        	}
           
            times.append((float) (t * 1000)).append("\n");
            t += dt;
        }
        E.info("Finished " + nstep + " steps");

        
        long end = System.currentTimeMillis();
        info.append("RealSimulationTime=" + ((end - start) / 1000.0) + "\n");
       }

    
	public void printCB() throws ContentError, ParseError {
		ComponentBehaviorWriter cbw = new ComponentBehaviorWriter();
		for (RunConfig rc : runConfigs) {
			
			cbw.print(rc.getTarget());
			
		}
		
	}
	
	
	public void printFirstConsolidated() throws ContentError, ParseError {
		ComponentBehaviorWriter cbw = new ComponentBehaviorWriter();
		if (runConfigs.size() > 0) {
			RunConfig rc = runConfigs.get(0);
			
			ComponentBehavior cb = rc.getTarget().getConsolidatedComponentBehavior("root");
			cbw.print(cb);
		}
		 
		
	}
	
	
}
