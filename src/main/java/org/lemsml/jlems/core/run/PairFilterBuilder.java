package org.lemsml.jlems.core.run;

import java.util.ArrayList;
import java.util.HashMap;

import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.selection.SelectionExpression;
import org.lemsml.jlems.core.sim.ContentError;

public class PairFilterBuilder extends AbstractPostBuilder {

	
	SelectionExpression selexp;
	
	 
	public PairFilterBuilder(SelectionExpression ex) {
		super();
		selexp = ex;
	}

 
	@SuppressWarnings("unchecked")
	@Override
	public void postBuild(StateRunnable tgt, HashMap<String, StateRunnable> sihm, BuildContext bc) throws ConnectionError,
			ContentError, RuntimeError {
	
		InstancePairSet<StateRunnable> pairs = bc.getWorkPairs();
	
		E.info("ips " + pairs);
		
		StateInstance baseContainer = new StateInstance();
		baseContainer.startArray("x");
		
		ArrayList<StateRunnable> asi = new ArrayList<StateRunnable>();
		
		int n0 = 0;
		for (InstancePair<StateRunnable> pair : pairs.getPairs()) {
			StateInstance pc = new StateInstance();
			pc.setParent(tgt);
			baseContainer.addToArray("x", pc);
			asi.add(pc);
			pc.addChild("p", (StateInstance)pair.getP());
			pc.addChild("q", (StateInstance)pair.getQ());
			pc.setWork("pair", pair);
			n0 += 1;
		}
		
//		InstanceSet<StateInstance> sis = new InstanceSet<StateInstance>("x", tgt);
//		sis.setItems(asi);
//		tgt.addInstanceSet(sis);
		
		ArrayList<StateRunnable> aic = selexp.getMatches(baseContainer);
		
		 
		pairs.empty();
		int n1 = 0;
		for (StateRunnable ic : aic) {
			pairs.addPair((InstancePair<StateRunnable>)ic.getWork());
			n1 += 1;
		}
		
		E.info("Pair filter kept " + n1 + " out of " + n0 + " based on " + selexp);
		 
	}

	@Override
	public void consolidateStateTypes() {
		// TODO Auto-generated method stub
		
	}
	

	
}
