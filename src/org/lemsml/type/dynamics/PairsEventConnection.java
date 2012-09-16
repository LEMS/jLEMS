package org.lemsml.type.dynamics;

import org.lemsml.expression.ParseError;
import org.lemsml.run.BuilderElement;
import org.lemsml.run.PairsEventConnectionBuilder;
import org.lemsml.type.Component;
import org.lemsml.type.ComponentType;
import org.lemsml.type.Lems;
import org.lemsml.util.ContentError;

public class PairsEventConnection extends BuildElement {

	public String pairs;
	
	public String receiver;
	public String receiverContainer;
	public String sourcePort;
	public String targetPort;
	
	@Override
	public BuilderElement makeBuilder(Component cpt) throws ContentError, ParseError {
		// TODO Auto-generated method stub
		PairsEventConnectionBuilder ret = new PairsEventConnectionBuilder(pairs);
	
		if (sourcePort != null && cpt.hasAttribute(sourcePort))  {
			ret.setSourcePortID(cpt.getStringValue(sourcePort));
		}
		if (targetPort != null && cpt.hasAttribute(targetPort)) {
			ret.setTargetPortID(cpt.getStringValue(targetPort));
		}
		
		if (receiver != null && cpt.hasAttribute(receiver)) {
			Component receiverComponent = cpt.getChild(receiver);
			ret.setReceiverComponentBehavior(receiverComponent.getComponentBehavior());
		}
		if (receiverContainer != null && cpt.hasAttribute(receiverContainer)) {
			String sv = cpt.getStringValue(receiverContainer);
			ret.setReceiverContainer(sv);
		}
	
		return ret;
	}

	@Override
	public void resolveLocal(Lems lems, ComponentType ct) throws ContentError, ParseError {
		// TODO Auto-generated method stub
		
	}
	
}
