package org.lemsml.flatten;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.lemsml.expression.ParseError;
import org.lemsml.run.ComponentBehavior;
import org.lemsml.run.ConnectionError;
import org.lemsml.run.ExpressionDerivedVariable;
import org.lemsml.run.PathDerivedVariable;
import org.lemsml.run.StateInstance;
import org.lemsml.run.VariableROC;
import org.lemsml.serial.XMLSerializer;
import org.lemsml.sim.Sim;
import org.lemsml.type.Component;
import org.lemsml.type.ComponentType;
import org.lemsml.type.EventPort;
import org.lemsml.type.Exposure;
import org.lemsml.type.FinalParam;
import org.lemsml.type.Lems;
import org.lemsml.type.LemsCollection;
import org.lemsml.type.ParamValue;
import org.lemsml.type.Parameter;
import org.lemsml.type.Text;
import org.lemsml.type.dynamics.Dynamics;
import org.lemsml.type.dynamics.DerivedVariable;
import org.lemsml.type.dynamics.OnCondition;
import org.lemsml.type.dynamics.OnEvent;
import org.lemsml.type.dynamics.OnStart;
import org.lemsml.type.dynamics.StateAssignment;
import org.lemsml.type.dynamics.StateVariable;
import org.lemsml.type.dynamics.TimeDerivative;
import org.lemsml.util.ContentError;

import org.lemsml.util.E;
import org.lemsml.util.FileUtil;
import org.lemsml.util.RuntimeError;

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
