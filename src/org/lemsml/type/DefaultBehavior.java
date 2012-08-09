package org.lemsml.type;

import org.lemsml.annotation.Mat;
import org.lemsml.annotation.Mel;
import org.lemsml.behavior.Behavior;

@Mel(info="If more than one Behavior definition is supplied, a DefaultBehavior element can be added to point " +
		"to the one that should be used by default in the absence of other criteria.")
public class DefaultBehavior {

	@Mat(info="The id of the Behavior element to be used as the default. The target element should be a child of the " +
			"enclosing component class. ")
	public String behavior;
	public Behavior r_behavior;
	
	
	public void resolve(ComponentType ct) {
		r_behavior = ct.getBehavior(behavior);
	}


	 
	public Behavior getBehavior() {
		return r_behavior;
	}
	
}
