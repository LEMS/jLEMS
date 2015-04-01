package org.lemsml.jlems.core.run;
 
import java.util.HashMap;
 
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.sim.ContentError;

public class TunnelBuilder extends AbstractPostBuilder {

    
	String endA;
	String endB;
	 
	String tunnelName;
	
	StateType endStateTypeA;
	StateType endStateTypeB;
	
	public TunnelBuilder(String tnm, String endA, String endB, StateType stA, StateType stB) {
		super();
		tunnelName = tnm;
		this.endA = endA;
		this.endB = endB;
		endStateTypeA = stA;
		endStateTypeB = stB;
	}

	 
	public void postBuild(StateRunnable base, HashMap<String, StateRunnable> sihm, BuildContext bc) throws ConnectionError, ContentError, RuntimeError {
  		StateRunnable srA = sihm.get(endA);
		StateRunnable srB = sihm.get(endB);
		
	
		if (srA == null) {
			srA = base.getChild(endA);
		}
		if (srB == null) {
			srB = base.getChild(endB);
		}
		
		
		if (srA == null) {
			throw new ConnectionError("The source state instance is null when getting " + endA + " on " + base);
		}
		if (srB == null) {
			throw new ConnectionError("The target state instance is null when getting " + endB + " on " + base);
		}
		
		
		StateInstance siA = endStateTypeA.newInstance();
		StateInstance siB = endStateTypeB.newInstance();
		
		 
		// TODO - method in StateRunnable?
		//((StateInstance)sf).addListChild(tunnelName, "", saf);
		//((StateInstance)st).addListChild(tunnelName, "", sat);
		
		srA.addAttachment(siA);
		srB.addAttachment(siB);
		
		siA.checkBuilt();
		siB.checkBuilt();
		
		siB.addRefChild(tunnelName, siA);
		siA.addRefChild(tunnelName, siB);

		//E.info(" Built a tunnel  " + tunnelName + " " + sf + ", " + st + ", " + base +
		//			" " + sf.hashCode() + " " +st.hashCode());
		
	}

  
	 

 
	@Override
	public void consolidateStateTypes() {
		 // nothing to do
	}

 
}
