package org.lemsml.run;

import java.util.HashMap;

import org.lemsml.util.ContentError;
import org.lemsml.util.E;

public class PairsEventConnectionBuilder extends PostBuilder {

	String pairs;
	
	ComponentBehavior receiverCB;
	
	String destAttachments;
	
	public PairsEventConnectionBuilder(String sp) {
		pairs = sp;
	}

	 
	public void postBuild(StateInstance base, HashMap<String, StateInstance> sihm, BuildContext bc) throws ConnectionError, ContentError {
		InstancePairSet<StateInstance> ips = base.getInstancePairSet(pairs);
		
		
		BuildContext cbc = new BuildContext();
		bc.setWorkPairs(ips);
		postChildren(base, null, cbc);	
		
	
		int np = 0;
		for (InstancePair<StateInstance> ip : ips.getPairs()) {
			connectInstances(ip.getP(), ip.getQ());
			np += 1;
		}
		E.info("Connected " + np + " pairs");
	}
		
	
	
	
	private void connectInstances(StateInstance sf, StateInstance st) throws ContentError, ConnectionError {

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


	public void setReceiverComponentBehavior(ComponentBehavior cb) {
		receiverCB = cb;
	}


	public void setReceiverContainer(String sv) {
		destAttachments = sv;
		
	}
 

	@Override
	public void consolidateComponentBehaviors() {
		 if (receiverCB != null) {
			 receiverCB = receiverCB.getConsolidatedComponentBehavior();
		 }	
	}

	

}
