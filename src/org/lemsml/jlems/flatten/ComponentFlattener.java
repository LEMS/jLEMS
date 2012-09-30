package org.lemsml.jlems.flatten;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.lemsml.jlems.expression.ParseError;
import org.lemsml.jlems.run.ComponentBehavior;
import org.lemsml.jlems.run.ConnectionError;
import org.lemsml.jlems.run.ExpressionDerivedVariable;
import org.lemsml.jlems.run.PathDerivedVariable;
import org.lemsml.jlems.run.StateInstance;
import org.lemsml.jlems.run.VariableROC;
import org.lemsml.jlems.serial.XMLSerializer;
import org.lemsml.jlems.sim.Sim;
import org.lemsml.jlems.type.Component;
import org.lemsml.jlems.type.ComponentType;
import org.lemsml.jlems.type.EventPort;
import org.lemsml.jlems.type.Exposure;
import org.lemsml.jlems.type.FinalParam;
import org.lemsml.jlems.type.Lems;
import org.lemsml.jlems.type.LemsCollection;
import org.lemsml.jlems.type.ParamValue;
import org.lemsml.jlems.type.Parameter;
import org.lemsml.jlems.type.Text;
import org.lemsml.jlems.type.dynamics.DerivedVariable;
import org.lemsml.jlems.type.dynamics.Dynamics;
import org.lemsml.jlems.type.dynamics.OnCondition;
import org.lemsml.jlems.type.dynamics.OnEvent;
import org.lemsml.jlems.type.dynamics.OnStart;
import org.lemsml.jlems.type.dynamics.StateAssignment;
import org.lemsml.jlems.type.dynamics.StateVariable;
import org.lemsml.jlems.type.dynamics.TimeDerivative;
import org.lemsml.jlems.util.ContentError;
import org.lemsml.jlems.util.E;
import org.lemsml.jlems.util.RuntimeError;
import org.lemsml.jlemsio.FileUtil;


public class ComponentFlattener {

    ArrayList<PathDerivedVariable> pdvA = new ArrayList<PathDerivedVariable>();
    ArrayList<ExpressionDerivedVariable> edvA = new ArrayList<ExpressionDerivedVariable>();
    ArrayList<VariableROC> rocA = new ArrayList<VariableROC>();
    ArrayList<String> svA = new ArrayList<String>();

    public void add(PathDerivedVariable pdv) {
        pdvA.add(pdv);
    }

    public void add(ExpressionDerivedVariable edv) {
        edvA.add(edv);
    }

    public void add(VariableROC vroc) {
        rocA.add(vroc);
    }

    public void addStateVariable(String sv) {
        svA.add(sv);
    }

    public void resolvePaths() {
        E.warning("may need to resolve paths??");
    }

    public void exportTo(ComponentBehavior ret) {
        for (String sv : svA) {
            ret.addStateVariable(sv);
        }
        for (PathDerivedVariable pdv : pdvA) {
            ret.addPathDerivedVariable(pdv);
        }
        for (ExpressionDerivedVariable edv : edvA) {
            ret.addExpressionDerivedVariable(edv);
        }
        for (VariableROC vr : rocA) {
            ret.addVariableROC(vr);
        }
    }

	public static void parseAndAdd(Component compNew, Component comp0, ComponentType ctNew, ComponentType ct0,
			String prefix) throws ContentError, ParseError, ConnectionError {

		for (Text t : ct0.getTexts()) {
			ctNew.texts.add(t);
		}

		for (FinalParam p : ct0.getFinalParams()) {
			ctNew.addParameter(new Parameter(prefix + p.getName(), p.getDimension()));
		}
		
		for (Exposure ex : ct0.getExposures()) {
			ctNew.addExposure(new Exposure(prefix + ex.getName(), ex.getDimension()));
		}

		Dynamics b = ctNew.getDynamics();
		if (b == null) {
			ctNew.addBehavior(new Dynamics());
			b = ctNew.getDynamics();
		}
		
		for (StateVariable sv : ct0.getDynamics().getStateVariables()) {
			StateVariable svNew = new StateVariable(prefix + sv.getName(), sv.getDimension());
			if (sv.getExposure() != null) {
				svNew = new StateVariable(prefix + sv.getName(), sv.getDimension(), sv.r_exposure);
			}
			b.addStateVariable(svNew);
		}
		
		for (OnStart os : ct0.getDynamics().getOnStarts()) {
			if (b.getOnStarts() == null || b.getOnStarts().isEmpty()) {
				b.addOnStart(new OnStart());
			}
			OnStart osNew = b.getOnStarts().first();
			for (StateAssignment sa : os.stateAssignments) {
				osNew.stateAssignments.add(sa);
			}
		}
		for (OnEvent oe : ct0.getDynamics().getOnEvents()) {
			b.addOnEvent(oe);
		}
		for (EventPort ep : ct0.getEventPorts()) {
			ctNew.addEventPort(ep);
		}
		for (OnCondition oc : ct0.getDynamics().getOnConditions()) {
			b.addOnCondition(oc);
		}

		if (b.derivedVariables == null) {
			b.derivedVariables = new LemsCollection<DerivedVariable>();
		}
		for (DerivedVariable dv : ct0.getDynamics().getDerivedVariables()) {
			b.addDerivedVariable(new DerivedVariable(prefix + dv.getName(), dv.getDimension(), dv.getEvalString(),
					dv.exposure));
		}
		for (TimeDerivative td : ct0.getDynamics().getTimeDerivatives()) {
			b.addTimeDerivative(new TimeDerivative(prefix + td.getStateVariable().getName(), td.getEvaluable()
					.toString()));
		}

		if (compNew.paramValues == null) {
			compNew.paramValues = new LemsCollection<ParamValue>();
		}
		// System.out.println("-pv0: "+comp0.paramValues);

		for (ParamValue pv : comp0.getParamValues()) {
			String newName = prefix + pv.getName();
			// System.out.println("-------------------- pv name: "+newName+", comp0: "+comp0);
			// /compNew.paramValues.add(new ParamValue(new FinalParam(newName,
			// pv.r_finalParam.getDimension())));

			compNew.setParameter(newName, comp0.getAttributes().getByName(pv.getName()).getValue());
		}
		// System.out.println("-pv: "+compNew.paramValues);

	
		// System.out.println("Childz: "+comp0.getAllChildren());
		for (Component childComp : comp0.getAllChildren()) {
			String newPrefix = prefix + childComp.getID() + "_";
			if (childComp.getID() == null) {
				newPrefix = prefix + childComp.getName() + "_";
			}
			parseAndAdd(compNew, childComp, ctNew, childComp.getComponentType(), newPrefix);
		}
	}
	

}
