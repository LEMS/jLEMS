package org.lemsml.jlems.core.run;
 
import java.util.HashMap;
 
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.sim.ContentError;

public class TunnelBuilder extends AbstractPostBuilder {

	String from;
	String to;
	 
	String tunnelName;
	
	StateType endStateType;
	
	public TunnelBuilder(String tnm, String sf, String st, StateType est) {
		super();
		tunnelName = tnm;
		from = sf;
		to = st;
		endStateType = est;
	}

	 
	public void postBuild(StateRunnable base, HashMap<String, StateRunnable> sihm, BuildContext bc) throws ConnectionError, ContentError, RuntimeError {
  		StateRunnable sf = sihm.get(from);
		StateRunnable st = sihm.get(to);
		
	
		if (sf == null) {
			sf = base.getChild(from);
		}
		if (st == null) {
			st = base.getChild(to);
		}
		
		
		if (sf == null) {
			throw new ConnectionError("The source state instance is null when getting " + from + " on " + base);
		}
		if (st == null) {
			throw new ConnectionError("The target state instance is null when getting " + to + " on " + base);
		}
		
		
		StateInstance saf = endStateType.newInstance();
		StateInstance sat = endStateType.newInstance();
		
		 
		// TODO - method in StateRunnable?
		//((StateInstance)sf).addListChild(tunnelName, "", saf);
		//((StateInstance)st).addListChild(tunnelName, "", sat);
		
		sf.addAttachment(saf);
		st.addAttachment(sat);
		
		saf.checkBuilt();
		sat.checkBuilt();
		
		sat.addRefChild(tunnelName, saf);
		saf.addRefChild(tunnelName, sat);

		E.info(" Built a tunnel  " + tunnelName + " " + sf + ", " + st + ", " + base +
					" " + sf.hashCode() + " " +st.hashCode());
		
	}

  
	 

 
	@Override
	public void consolidateStateTypes() {
		 // nothing to do
	}

 
}
