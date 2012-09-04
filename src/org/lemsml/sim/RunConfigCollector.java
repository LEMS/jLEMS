package org.lemsml.sim;

import java.util.ArrayList;

import org.lemsml.run.ComponentBehavior;
import org.lemsml.run.RunConfig;
import org.lemsml.run.RuntimeOutput;
import org.lemsml.run.RuntimeRecorder;
import org.lemsml.util.E;

public class RunConfigCollector implements ComponentBehaviorVisitor {

	ArrayList<RunConfig> runConfigs;
	
	public RunConfigCollector(ArrayList<RunConfig> al) {
		runConfigs = al;
	}

	@Override
	public void visit(ComponentBehavior cb) {
		RunConfig rc = cb.getRunConfig();
		if (rc != null) {
			runConfigs.add(rc);
			
			ArrayList<RuntimeRecorder> arc = new ArrayList<RuntimeRecorder>();
			RecorderCollector recc = new RecorderCollector(arc);
			cb.visitAll(recc);
			 
			rc.setRecorders(arc);
			
			
			
			
			
			E.info("Added a run config " + rc);
		}
	}

	
	
}
