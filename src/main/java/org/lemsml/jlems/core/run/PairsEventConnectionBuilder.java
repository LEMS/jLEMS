package org.lemsml.jlems.core.run;

import java.util.HashMap;

import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.sim.ContentError;

public class PairsEventConnectionBuilder extends AbstractPostBuilder {

	String pairs;
	
	StateType receiverCB;
	
	String destAttachments;
	
	public PairsEventConnectionBuilder(String sp) {
		super();
		pairs = sp;
	}

	 
	public void postBuild(StateRunnable base, HashMap<String, StateRunnable> sihm, BuildContext bc) throws ConnectionError, ContentError, RuntimeError {
		InstancePairSet<StateRunnable> ips = ((StateInstance)base).getInstancePairSet(pairs);
		
		
		BuildContext cbc = new BuildContext();
		bc.setWorkPairs(ips);
		postChildren(base, null, cbc);	
		
	
		int np = 0;
		for (InstancePair<StateRunnable> ip : ips.getPairs()) {
			connectInstances(ip.getP(), ip.getQ());
			np += 1;
		}
		E.info("Connected " + np + " pairs");
	}
		
	
	
	
	private void connectInstances(StateRunnable sf, StateRunnable st) throws ContentError, ConnectionError, RuntimeError {

        //TODO: add check for named in & out ports!
		if (receiverCB != null) {
			StateInstance rsi = (StateInstance)(receiverCB.newInstance());
			InPort inPort = rsi.getFirstInPort();
			sf.getFirstOutPort().connectTo(inPort);
			st.addAttachment(destAttachments, rsi);
		//	E.info("added attachment " + rsi + " in  " + st);
			
		} else {
			InPort inPort = st.getFirstInPort();
			if (inPort == null) {
				throw new ConnectionError("no input port on " + st);
			}
			OutPort outPort = sf.getFirstOutPort();
			if (outPort == null) {
				throw new ConnectionError("no output port on " + sf);
			}
			outPort.connectTo(inPort);
		}
	}

	 
	public void setSourcePortID(String sourcePort) {
		// TODO Auto-generated method stub
		
	}


	public void setTargetPortID(String targetPort) {
		// TODO Auto-generated method stub
		
	}


	public void setReceiverStateType(StateType cb) {
		receiverCB = cb;
	}


	public void setReceiverContainer(String sv) {
		destAttachments = sv;
		
	}
 

	@Override
	public void consolidateStateTypes() throws ContentError {
		 if (receiverCB != null) {
			 receiverCB = receiverCB.getConsolidatedStateType("(evtcon)");
		 }	
	}

	

}
