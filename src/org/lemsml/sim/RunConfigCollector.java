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
		E.missing();
		
	}

	
	
}
