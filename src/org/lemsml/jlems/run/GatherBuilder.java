package org.lemsml.jlems.run;

import java.util.ArrayList;
import java.util.HashMap;

import org.lemsml.jlems.logging.E;
import org.lemsml.jlems.selection.SelectionExpression;
import org.lemsml.jlems.sim.ContentError;

public class GatherBuilder extends AbstractPostBuilder {

	
SelectionExpression selexp;
	
	String col;
	
	public GatherBuilder(SelectionExpression ex, String c) {
		super();
		selexp = ex;
		col = c;
	}

	@Override
	public void postBuild(StateRunnable tgt, HashMap<String, StateRunnable> sihm, BuildContext bc) throws ConnectionError,
			ContentError, RuntimeError {
	 		
			InstanceCollector ic = new InstanceCollector(selexp);
			ArrayList<StateRunnable> matches = ic.collect(tgt.getParent());

			InstanceSet<StateRunnable> iset = tgt.getInstanceSet(col);
			iset.setItems(matches);
			
			E.info("Generated set of " + iset.size() +  " items(" + tgt.getID() + ")");
	}

	@Override
	public void consolidateStateTypes() {
		// TODO Auto-generated method stub
		
	}
	

	
 
 

}
