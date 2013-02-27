package org.lemsml.jlems.core.run;

import java.util.ArrayList;
import java.util.HashMap;

import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.selection.SelectionExpression;
import org.lemsml.jlems.core.sim.ContentError;

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
	public void postBuild(StateRunnable tgt, HashMap<String, StateRunnable> sihm, BuildContext bc) throws ConnectionError,
			ContentError, RuntimeError {
	 		
		
			InstanceCollector pic = new InstanceCollector(pselexp);
			ArrayList<StateRunnable> pmatches = pic.collect(tgt.getParent());

			
			InstanceCollector qic = new InstanceCollector(qselexp);
			ArrayList<StateRunnable> qmatches = qic.collect(tgt.getParent());
			
			InstancePairSet<StateRunnable> iset = ((StateInstance)tgt).getInstancePairSet(col);
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
