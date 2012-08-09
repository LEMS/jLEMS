package org.lemsml.run;

import java.util.ArrayList;
import java.util.HashMap;

import org.lemsml.selection.SelectionExpression;
import org.lemsml.util.ContentError;
import org.lemsml.util.E;

public class PairFilterBuilder extends PostBuilder {

	
	SelectionExpression selexp;
	
	 
	public PairFilterBuilder(SelectionExpression ex) {
		selexp = ex;
	 
	}

 
	@SuppressWarnings("unchecked")
	@Override
	public void postBuild(StateInstance tgt, HashMap<String, StateInstance> sihm, BuildContext bc) throws ConnectionError,
			ContentError {
	
		InstancePairSet<StateInstance> pairs = bc.getWorkPairs();
	
		E.info("ips " + pairs);
		
		StateInstance baseContainer = new StateInstance();
		baseContainer.startArray("x");
		
		ArrayList<StateInstance> asi = new ArrayList<StateInstance>();
		
		int n0 = 0;
		for (InstancePair<StateInstance> pair : pairs.getPairs()) {
			StateInstance pc = new StateInstance();
			pc.setParent(tgt);
			baseContainer.addToArray("x", pc);
			asi.add(pc);
			pc.addChild("p", pair.getP());
			pc.addChild("q", pair.getQ());
			pc.setWork("pair", pair);
			n0 += 1;
		}
		
//		InstanceSet<StateInstance> sis = new InstanceSet<StateInstance>("x", tgt);
//		sis.setItems(asi);
//		tgt.addInstanceSet(sis);
		
		ArrayList<StateInstance> aic = selexp.getMatches(baseContainer);
		
		 
		pairs.empty();
		int n1 = 0;
		for (StateInstance ic : aic) {
			pairs.addPair((InstancePair<StateInstance>)ic.getWork());
			n1 += 1;
		}
		
		E.info("Pair filter: from " + n0 + " kept " + n1);
		 
	}

	@Override
	public void consolidateComponentBehaviors() {
		// TODO Auto-generated method stub
		
	}
	

	
}
