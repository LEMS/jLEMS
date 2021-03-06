package org.lemsml.jlems.core.type.structure;

import org.lemsml.jlems.core.expression.ParseError;
import org.lemsml.jlems.core.expression.ParseTree;
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.run.BuilderElement;
import org.lemsml.jlems.core.run.StateType;
import org.lemsml.jlems.core.run.TunnelBuilder;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.type.Component;
import org.lemsml.jlems.core.type.ComponentType;
import org.lemsml.jlems.core.type.Lems;
import org.lemsml.jlems.core.type.LemsCollection;

public class Tunnel extends BuildElement {

	public String name;
	public String endA;
	public String endB;
	
	public String expose;
	public String as;
	
	public String componentA;
	public String componentB;
    
	public LemsCollection<Assign> assigns = new LemsCollection<Assign>();
	
	
	@Override
	public void resolveLocal(Lems lems, ComponentType ct) throws ContentError,
			ParseError {
		// TODO Auto-generated method stub
		if (componentA == null) {
			throw new ContentError("ComponentA must be specified in " + this);
		}
		if (componentB == null) {
			throw new ContentError("ComponentB must be specified in " + this);
		}
		for (Assign ass : assigns) {
			ParseTree pt = lems.getParser().parseExpression(ass.getExpression());
			ass.setDoubleEvaluator(pt.makeFloatEvaluator());
		}
		
	}

	@Override
	public BuilderElement makeBuilder(Component cpt) throws ContentError,
			ParseError {
		
		StateType stA = null;
		StateType stB = null;
		
		if (componentA != null) {
			Component tcpt = cpt.getRelativeComponent(componentA);
            //E.info("Tunnel builder: [" + component + "] resolved to: [" + tcpt + "]");
			stA = tcpt.getStateType();
		}
		if (componentB != null) {
			Component tcpt = cpt.getRelativeComponent(componentB);
            //E.info("Tunnel builder: [" + component + "] resolved to: [" + tcpt + "]");
			stB = tcpt.getStateType();
		}

		TunnelBuilder tb = null;
		if (stA != null && stB!=null) {
			tb = new TunnelBuilder(name, endA, endB, stA, stB);
		} else {
			throw new ContentError("Can't locate tunnel component " + componentA +" and/or "+componentB);
		}
        

        for (Assign ass : assigns) {
            String ea = ass.getExposeAs();
            if (ea != null) {
                E.warning("Expose as in Tunnel is not used");
             }
            String dim = "unknown";
            tb.addAssignment(ass.getProperty(), ass.getDoubleEvaluator(), dim);
        }

		return tb;
	}

	
}
