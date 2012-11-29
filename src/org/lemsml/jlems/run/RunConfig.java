package org.lemsml.jlems.run;

import java.util.ArrayList;

import org.lemsml.jlems.expression.ParseError;
import org.lemsml.jlems.sim.ContentError;
import org.lemsml.jlems.type.Component;

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
	
}
