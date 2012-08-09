package org.lemsml.run;

import java.util.ArrayList;
import java.util.HashMap;

import org.lemsml.selection.SelectionExpression;
import org.lemsml.util.ContentError;
import org.lemsml.util.E;

public class GatherPairsBuilder extends PostBuilder {

	
	
	SelectionExpression pselexp;
	SelectionExpression qselexp;
	
	String col;
	
	public GatherPairsBuilder(SelectionExpression pex, SelectionExpression qex, String c) {
		pselexp = pex;
		qselexp = qex;
		col = c;
	}

	@Override
	public void postBuild(StateInstance tgt, HashMap<String, StateInstance> sihm, BuildContext bc) throws ConnectionError,
			ContentError {
	 		
			InstanceCollector pic = new InstanceCollector(pselexp);
			ArrayList<StateInstance> pmatches = pic.collect(tgt.getParent());

			
			InstanceCollector qic = new InstanceCollector(qselexp);
			ArrayList<StateInstance> qmatches = qic.collect(tgt.getParent());
			
			InstancePairSet<StateInstance> iset = tgt.getInstancePairSet(col);
			iset.setItems(pmatches, qmatches);
			
			
			E.info("GATHER PAIRS BUILDER, destid=" + tgt.id + " set=" + iset + " from " + pselexp + " and " + qselexp);
			
			BuildContext cbc = new BuildContext();
			cbc.setWorkPairs(iset);
			postChildren(tgt, null, cbc);	
	}

	@Override
	public void consolidateComponentBehaviors() {
		// TODO Auto-generated method stub
		
	}
 
 

}
