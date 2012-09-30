package org.lemsml.jlems.type.dynamics;

import org.lemsml.jlems.expression.DoubleEvaluable;
import org.lemsml.jlems.expression.ParseError;
import org.lemsml.jlems.run.BuilderElement;
import org.lemsml.jlems.run.EventConnectionBuilder;
import org.lemsml.jlems.type.Component;
import org.lemsml.jlems.type.ComponentType;
import org.lemsml.jlems.type.Dimension;
import org.lemsml.jlems.type.Lems;
import org.lemsml.jlems.type.LemsCollection;
import org.lemsml.jlems.type.ParamValue;
import org.lemsml.jlems.type.structure.Assign;
import org.lemsml.jlems.type.structure.BuildElement;
import org.lemsml.jlems.util.ContentError;
import org.lemsml.jlems.util.E;

public class EventConnection extends BuildElement {

	public String from;
	public String to;

    public String delay;
	
	public String receiver;
	public String receiverContainer;
	public String sourcePort;
	public String targetPort;

	public LemsCollection<Assign> assigns = new LemsCollection<Assign>();


	@Override
	public void resolveLocal(Lems lems, ComponentType ct) throws ContentError, ParseError {
		for (Assign ass : assigns) {
			DoubleEvaluable de = lems.getParser().parseExpression(ass.getExpression());
			ass.setDoubleEvaluable(de);
		}
	}
	
	@Override
	public BuilderElement makeBuilder(Component cpt) throws ContentError, ParseError {

        //E.info("makeBuilder on "+cpt+" from: "+from+" ("+sourcePort+"), to: "+to+" ("+targetPort+") -> "+ receiver +", assigns: "+assigns);
		EventConnectionBuilder ret = new EventConnectionBuilder(from, to);
	
		if (sourcePort != null && cpt.hasAttribute(sourcePort))  {
			ret.setSourcePortID(cpt.getStringValue(sourcePort));
		}
		if (targetPort != null && cpt.hasAttribute(targetPort)) {
			ret.setTargetPortID(cpt.getStringValue(targetPort));
		}

		if (delay != null && cpt.hasAttribute(delay)) {
			//			ret.setDelay(Double.parseDouble(cpt.getStringValue(delay)));
			// RCC - taking account of dimension/units in delay
			ParamValue pv = cpt.getParamValue(delay);
			ret.setDelay(pv.getDoubleValue(Dimension.TIME_DIMENSION));
			
			
		}
		
		if (receiver != null && cpt.hasAttribute(receiver)) {
			Component receiverComponent = cpt.getChild(receiver);
			ret.setReceiverComponentBehavior(receiverComponent.getComponentBehavior());

            for (Assign ass : assigns) {
                String ea = ass.getExposeAs();
                if (ea != null) {
                    E.warning("Expose as in EventConnection is not used");
                 }
                ret.addAssignment(ass.getProperty(), ass.getDoubleEvaluable());
            }
		}
		if (receiverContainer != null && cpt.hasAttribute(receiverContainer)) {
			String sv = cpt.getStringValue(receiverContainer);
			ret.setReceiverContainer(sv);
		}
	
		return ret;
	}
	
}
