package org.lemsml.type.simulation;

import org.lemsml.run.ComponentBehavior;
import org.lemsml.run.RuntimeOutput;
import org.lemsml.type.Component;
import org.lemsml.type.ComponentType;
import org.lemsml.type.Lems;
import org.lemsml.type.LemsCollection;
import org.lemsml.type.dynamics.DataDisplay;
import org.lemsml.type.dynamics.Record;
import org.lemsml.type.dynamics.Run;
import org.lemsml.util.ContentError;
import org.lemsml.util.E;


public class Simulation {

	 
	public transient LemsCollection <Record> records = new LemsCollection<Record>();
	
	public transient LemsCollection <Run> runs = new LemsCollection<Run>();
	
	public transient LemsCollection <DataDisplay> dataDisplays = new LemsCollection<DataDisplay>();


	
	public void resolve(final Lems lems, final ComponentType r_type) throws ContentError {
		for (Run run : runs) {
			run.resolve(r_type);
		}
	}

	public boolean definesRun() {
		boolean ret = false;
		if (runs.size() > 0) {
			ret = true;
		}
		return ret;
	}
	
	
	public void appendToBehavior(Component cpt, ComponentBehavior ret) throws ContentError {
	 
	 if (runs.size() > 0) {
		 for (Run run : runs) {
			 if (run.component == null) {
				 E.warning("No component defined for run? " + run);
			 } else {
				 ret.addRunConfig(run.getTargetComponent(cpt), run.getDoubleStep(cpt), run.getDoubleTotal(cpt));
			 }
		 }
	 }
	 
	 if (dataDisplays.size() > 0) {
		 for (DataDisplay dd : dataDisplays) {
			 final RuntimeOutput ro = dd.getRuntimeOutput(cpt);
			 ret.addRuntimeOutput(ro);
		 }
	 }
	 
	 if (records.size() > 0) {
		 for (Record r : records) {
			 final String path = cpt.getPathParameterPath(r.quantity);
			 if (path == null) {
				 throw new ContentError("No path specified for recorder (" + r.quantity + ") in " + cpt);
			 }
			 final Component cdisp = cpt.getInheritableLinkTarget(r.display);
			 
			 final String disp = cdisp.id;
			 if (disp == null) {
				 throw new ContentError("No display defined for recorder " + r);
			 } else {
				 final double tsc = cpt.getParamValue(r.timeScale).getDoubleValue();
				 final double ysc = cpt.getParamValue(r.scale).getDoubleValue();
				 
				 if (tsc == 0.0 || ysc == 0.0) {
					 throw new ContentError("Recorder scales cant be 0: " + r + " " + tsc + " " + ysc);
				 }
				 ret.addRecorder(cpt.id, path, tsc, ysc, cpt.getTextParam(r.color), disp);
			 }
		 }
	 }
	}
	 
}
