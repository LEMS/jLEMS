package org.lemsml.jlems.type.dynamics;

import org.lemsml.jlems.annotation.Mel;
import org.lemsml.jlems.type.Dimension;
 

@Mel(info="Has 'variable' and 'value' fields")
public class StateAssignment extends StateChange {

	
	public StateAssignment() {
		super();
	}
	
	
    public StateAssignment(String vnm) {
    	super(vnm);
    }

    public StateAssignment(String vnm, String value) {
        this(vnm);
        this.value = value;
    }

	public Dimension getStateVariableDimensionMultiplier() {
		return new Dimension(Dimension.NO_DIMENSION);
	}
	
}
