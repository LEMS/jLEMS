package org.lemsml.jlems.sim;

import java.util.ArrayList;

import org.lemsml.jlems.run.StateType;
import org.lemsml.jlems.run.RunConfig;
import org.lemsml.jlems.run.RuntimeRecorder;

public class RunConfigCollector implements StateTypeVisitor {

	ArrayList<RunConfig> runConfigs;
	
	public RunConfigCollector(ArrayList<RunConfig> al) {
		runConfigs = al;
	}

	@Override
	public void visit(StateType cb) {
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
