package org.lemsml.jlems.core.type.structure;

import org.lemsml.jlems.core.expression.ParseError;
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.run.BuilderElement;
import org.lemsml.jlems.core.run.StateType;
import org.lemsml.jlems.core.run.TunnelBuilder;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.type.Component;
import org.lemsml.jlems.core.type.ComponentType;
import org.lemsml.jlems.core.type.Lems;

public class Tunnel extends BuildElement {

	public String name;
	public String from;
	public String to;
	
	public String expose;
	public String as;
	
	public String component;
	
	
	@Override
	public void resolveLocal(Lems lems, ComponentType ct) throws ContentError,
			ParseError {
		// TODO Auto-generated method stub
		if (component == null) {
			throw new ContentError("Component must be specified in " + this);
		}
		
	}

	@Override
	public BuilderElement makeBuilder(Component cpt) throws ContentError,
			ParseError {
		
		StateType est = null;
		
		if (component != null) {
			Component tcpt = cpt.getRelativeComponent(component);
            E.info("Tunneld builder: [" + component + "] resolved to: [" + tcpt + "]");
			est = tcpt.getStateType();

			/*
            for (Assign ass : assigns) {
                String ea = ass.getExposeAs();
                if (ea != null) {
                    E.warning("Expose as in EventConnection is not used");
                 }
                ret.addAssignment(ass.getProperty(), ass.getDoubleEvaluator());
            }
            */
		}

		TunnelBuilder tb = null;
		if (est != null) {
			tb = new TunnelBuilder(name, from, to, est);
		} else {
			throw new ContentError("Cant locate tunnel component " + component);
		}
		
		return tb;
	}

	
}
