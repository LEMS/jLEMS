package org.lemsml.jlems.sim;

import java.util.ArrayList;

import org.lemsml.jlems.run.ComponentBehavior;
import org.lemsml.jlems.run.RunConfig;
import org.lemsml.jlems.run.RuntimeOutput;
import org.lemsml.jlems.run.RuntimeRecorder;
import org.lemsml.jlems.util.E;

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
		}
	}

	
	
}
