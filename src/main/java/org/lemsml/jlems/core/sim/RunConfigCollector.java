package org.lemsml.jlems.core.sim;

import java.util.ArrayList;
import java.util.List;
import org.lemsml.jlems.core.run.RunConfig;
import org.lemsml.jlems.core.run.RuntimeEventRecorder;
import org.lemsml.jlems.core.run.RuntimeRecorder;
import org.lemsml.jlems.core.run.StateType;

public class RunConfigCollector implements StateTypeVisitor {

	List<RunConfig> runConfigs;
	
	public RunConfigCollector(List<RunConfig> runConfigs2) {
		runConfigs = runConfigs2;
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
			
			ArrayList<RuntimeEventRecorder> rers = new ArrayList<RuntimeEventRecorder>();
			EventRecorderCollector erecc = new EventRecorderCollector(rers);
			cb.visitAll(erecc);
            
            rc.setEventRecorders(rers);
		}
	}

	
	
}
