package org.lemsml.run;

import java.util.ArrayList;
import java.util.HashMap;

import org.lemsml.selection.SelectionExpression;
import org.lemsml.util.ContentError;
import org.lemsml.util.RuntimeError;

public class GatherBuilder extends PostBuilder {

	
SelectionExpression selexp;
	
	String col;
	
	public GatherBuilder(SelectionExpression ex, String c) {
		selexp = ex;
		col = c;
	}

	@Override
	public void postBuild(StateInstance tgt, HashMap<String, StateInstance> sihm, BuildContext bc) throws ConnectionError,
			ContentError, RuntimeError {
	 		
			InstanceCollector ic = new InstanceCollector(selexp);
			ArrayList<StateInstance> matches = ic.collect(tgt.getParent());

			InstanceSet<StateInstance> iset = tgt.getInstanceSet(col);
			iset.setItems(matches);
			
			// E.info("GATHER BUILDER set " + tgt.id + " " + iset.size() + " from " + selexp);
	}

	@Override
	public void consolidateComponentBehaviors() {
		// TODO Auto-generated method stub
		
	}
	

	
 
 

}
