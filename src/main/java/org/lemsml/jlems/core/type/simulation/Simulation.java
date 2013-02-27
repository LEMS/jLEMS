package org.lemsml.jlems.core.type.simulation;

import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.out.ResultWriter;
import org.lemsml.jlems.core.run.RuntimeDisplay;
import org.lemsml.jlems.core.run.RuntimeOutput;
import org.lemsml.jlems.core.run.StateType;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.type.Component;
import org.lemsml.jlems.core.type.ComponentType;
import org.lemsml.jlems.core.type.Lems;
import org.lemsml.jlems.core.type.LemsCollection;
 


public class Simulation {

	 
	public transient LemsCollection <Record> records = new LemsCollection<Record>();
	
	public transient LemsCollection <Run> runs = new LemsCollection<Run>();
	
	public transient LemsCollection <DataDisplay> dataDisplays = new LemsCollection<DataDisplay>();

	public transient LemsCollection <DataWriter> dataWriters = new LemsCollection<DataWriter>();

	
	public static int idCounter = 0;
	
	
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
	
	
	public void appendToBehavior(Component cpt, StateType ret) throws ContentError {
	 
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
			 final RuntimeDisplay ro = dd.getRuntimeOutput(cpt);
			 ret.addRuntimeDisplay(ro);
		 }
	 }
	 
	 if (dataWriters.size() > 0) {
		 for (DataWriter dw : dataWriters) {
			 final RuntimeOutput ro = dw.getRuntimeOutput(cpt);
			 ret.addRuntimeOutput(ro);
		 }
	 }
	 
	 
	 
	 if (records.size() > 0) {
		 for (Record r : records) {
			 final String path = cpt.getPathParameterPath(r.quantity);
			 if (path == null) {
				 throw new ContentError("No path specified for recorder (" + r.quantity + ") in " + cpt);
			 }
			 Component cdisp = null;
			 if (r.destination != null) {
				 cdisp = cpt.getInheritableLinkTarget(r.destination);
			 } else {
				 cdisp = cpt.getParent();
			 }
			 
			 if (cdisp == null) {
				 throw new ContentError("No display defined for recorder " + r);				 
			 }
			 if (cdisp.id == null) {
				 cdisp.id = autoID();
			 }
			 
			 double tsc = 1.;
			 double ysc = 1.;
			 if (r.timeScale != null && cpt.hasParam(r.timeScale)) {
				 tsc = cpt.getParamValue(r.timeScale).getDoubleValue();
			 }
			if (r.scale != null && cpt.hasParam(r.scale)) {	
				ysc = cpt.getParamValue(r.scale).getDoubleValue();
			}
			 ret.addRecorder(cpt.id, path, tsc, ysc, cpt.getTextParam(r.color), cdisp.id);
		 }
	 }
	}

	
	private String autoID() {
		idCounter += 1;
		String ret = "_" + idCounter;
		return ret;
	}
	 
}
