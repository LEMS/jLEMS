package org.lemsml.run;

import org.lemsml.expression.ParseError;
import org.lemsml.type.Component;
import org.lemsml.util.ContentError;

public class RunConfig {

	Component targetComponent;
	double step;
	double total;
	
	

	public RunConfig(Component cpt, double st, double tot) {
		targetComponent = cpt;
		step = st;
		total = tot;
	}

    @Override
    public String toString() {
        return "RunConfig {" + "target=" + targetComponent + ", step=" + step + ", total=" + total + '}';
    }

    


	public ComponentBehavior getTarget() throws ContentError, ParseError {
		return targetComponent.getComponentBehavior();
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
}
