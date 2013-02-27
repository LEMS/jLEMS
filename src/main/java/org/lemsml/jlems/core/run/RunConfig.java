package org.lemsml.jlems.core.run;

import java.util.ArrayList;

import org.lemsml.jlems.core.expression.ParseError;
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.type.Component;

public class RunConfig {

	Component targetComponent;
	double step;
	double total;
	
	ArrayList<RuntimeRecorder> recorders;

	public RunConfig(Component cpt, double st, double tot) {
		targetComponent = cpt;
		step = st;
		total = tot;
	}

    @Override
    public String toString() {
        return "RunConfig {" + "target=" + targetComponent + ", step=" + step + ", total=" + total + '}';
    }

    

    public Component getTargetComponent() {
    	return targetComponent;
    }

	public StateType getTarget() throws ContentError, ParseError {
		return targetComponent.getStateType();
	}


	public double getTimestep() {
		return step;
	}

	public double getRuntime() {
		return total;
	}

	public RunConfig makeCopy() {
		RunConfig ret = new RunConfig(targetComponent, step, total);
		return ret;
	}

	public void setRecorders(ArrayList<RuntimeRecorder> arc) {
		recorders = arc;
	}
	
	public ArrayList<RuntimeRecorder> getRecorders() {
		if (recorders == null) {
			recorders = new ArrayList<RuntimeRecorder>();
		}
		return recorders;
	}

	public Component getControlComponent() {
		E.missing();
		return null;
	}
	
}
