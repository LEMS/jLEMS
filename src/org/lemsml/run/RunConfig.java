package org.lemsml.run;

import org.lemsml.type.Component;

public class RunConfig {

	Component target;
	double step;
	double total;
	
	

	public RunConfig(Component cpt, double st, double tot) {
		target = cpt;
		step = st;
		total = tot;
	}

    @Override
    public String toString() {
        return "RunConfig {" + "target=" + target + ", step=" + step + ", total=" + total + '}';
    }

    


	public Component getTarget() {
		return target;
	}


	public double getTimestep() {
		return step;
	}

	public double getRuntime() {
		return total;
	}

	public RunConfig makeCopy() {
		RunConfig ret = new RunConfig(target, step, total);
		return ret;
	}
}
