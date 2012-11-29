package org.lemsml.jlems.run;

import java.util.ArrayList;
import java.util.HashMap;

import org.lemsml.jlems.logging.E;
import org.lemsml.jlems.selection.SelectionExpression;
import org.lemsml.jlems.sim.ContentError;

public class GatherPairsBuilder extends AbstractPostBuilder {

	
	
	SelectionExpression pselexp;
	SelectionExpression qselexp;
	
	String col;
	
	public GatherPairsBuilder(SelectionExpression pex, SelectionExpression qex, String c) {
		super();
		pselexp = pex;
		qselexp = qex;
		col = c;
	}

	@Override
	public void postBuild(StateInstance tgt, HashMap<String, StateInstance> sihm, BuildContext bc) throws ConnectionError,
			ContentError, RuntimeError {
	 		
			InstanceCollector pic = new InstanceCollector(pselexp);
			ArrayList<StateInstance> pmatches = pic.collect(tgt.getParent());

			
			InstanceCollector qic = new InstanceCollector(qselexp);
			ArrayList<StateInstance> qmatches = qic.collect(tgt.getParent());
			
			InstancePairSet<StateInstance> iset = tgt.getInstancePairSet(col);
			iset.setItems(pmatches, qmatches);
			
			
			E.info("Selected pairs " + iset);
			
			BuildContext cbc = new BuildContext();
			cbc.setWorkPairs(iset);
			postChildren(tgt, null, cbc);	
	}

	@Override
	public void consolidateStateTypes() {
		// TODO Auto-generated method stub
		
	}
 
 

}
