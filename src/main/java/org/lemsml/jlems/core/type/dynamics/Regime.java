package org.lemsml.jlems.core.type.dynamics;

import java.util.HashMap;
import java.util.HashSet;

import org.lemsml.jlems.core.eval.BooleanEvaluator;
import org.lemsml.jlems.core.eval.DoubleEvaluator;
import org.lemsml.jlems.core.expression.Dimensional;
import org.lemsml.jlems.core.expression.ExprDimensional;
import org.lemsml.jlems.core.expression.ParseError;
import org.lemsml.jlems.core.expression.ParseTree;
import org.lemsml.jlems.core.expression.Parser;
import org.lemsml.jlems.core.expression.Valued;
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.run.ActionBlock;
import org.lemsml.jlems.core.run.ComponentRegime;
import org.lemsml.jlems.core.run.ConditionAction;
import org.lemsml.jlems.core.run.EventAction;
import org.lemsml.jlems.core.run.StateType;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.type.Component;
import org.lemsml.jlems.core.type.ComponentType;
import org.lemsml.jlems.core.type.FinalParam;
import org.lemsml.jlems.core.type.Lems;
import org.lemsml.jlems.core.type.LemsCollection;
import org.lemsml.jlems.core.type.Named;
import org.lemsml.jlems.core.type.ParamValue;
import org.lemsml.jlems.core.type.Requirement;
import org.lemsml.jlems.core.annotation.ModelElement;
import org.lemsml.jlems.core.annotation.ModelProperty;

@ModelElement(info = "Allows the dynamics of a ComponentType to be expressed via a finite state machine. " + 
"Each regime has its internal dynamics, and conditions on which transitions between regimes occur are specified " +
"using the OnCondition element"
)
public class Regime implements Named {
	
	@ModelProperty(info="The name of the regime")
	public String name;
	
	@ModelProperty(info="'True' if this is the initial regime of the system")
	public String initial;
	public boolean b_initial;
	
 	 
	public LemsCollection<DerivedVariable> derivedVariables = new LemsCollection<DerivedVariable>();
	
	public LemsCollection<StateVariable> stateVariables = new LemsCollection<StateVariable>();
	
	public LemsCollection<TimeDerivative> timeDerivatives = new LemsCollection<TimeDerivative>();
	
	public LemsCollection <OnStart> onStarts = new LemsCollection<OnStart>();
	
	public LemsCollection <OnEntry> onEntrys = new LemsCollection<OnEntry>();
	
	public LemsCollection <OnEvent> onEvents = new LemsCollection<OnEvent>();
	
	public LemsCollection <OnCondition> onConditions = new LemsCollection<OnCondition>();


	public LemsCollection <RequiredVar> requiredVars = new LemsCollection<RequiredVar>();
	
	private HashMap<String, Valued> valHM;
	 
	private Dynamics r_behavior;
	
	
	public Regime() {
		// TODO one
	}
	
	
	public Regime(String s) {
		name = s;
	}


    @Override
	public String toString() {
		return "Regime: "+name + " (initial = " + isInitial() + ")"; //  + hashCode();
	}
	
	
	public void setBehavior(Dynamics b) {
		r_behavior = b;
	}

    public boolean isInitial(){
        return (initial!=null && initial.equals("true"));
    }
	
	
  
	
	public HashMap<String, Valued> getValHM() throws ContentError {
		checkMakeValHM();
		return valHM;
	}
	
	
	
	private void checkMakeValHM() throws ContentError {
		if (valHM == null) {
			valHM = new HashMap<String, Valued>();
			
			HashMap<String, Valued> pHM = r_behavior.getValHM();
			for (String s : pHM.keySet()) {
				valHM.put(s, pHM.get(s));
			}
		 
		 
			addToMap(requiredVars, valHM);
			addToMap(stateVariables, valHM);
			addToMap(derivedVariables, valHM);
		}
	}
	
	 
	
	
	public void resolve(LemsCollection<StateVariable> psv, Lems lems, Parser parser, HashMap<String, Integer> exposedHM) throws ContentError, ParseError {
		
		LemsCollection<StateVariable> allSV = new LemsCollection<StateVariable>();
		allSV.addAll(psv);
		allSV.addAll(stateVariables);
		
		b_initial = false;
		if (initial != null) {
			if (initial.equals("true")) {
				b_initial = true;
			} else if (initial.equals("false")) {
				b_initial = false;
			} else {
				throw new ContentError("unrecognized value for initial in regime: " + initial);
			}
		}
		
		for (Requirement req : getComponentType().getRequirements()) {
			requiredVars.add(new RequiredVar(req.getName(), req.getDimension()));
		}
		 
		
		 checkMakeValHM();
	 
		ComponentType typ = r_behavior.getComponentType();
		
	 
		
		for (DerivedVariable dvar : derivedVariables) {
	 		dvar.resolve(lems, lems.getDimensions(), typ, valHM, parser);
	 		if (dvar.hasExposure()) {
	 			countExposure(dvar.getExposure(), exposedHM);
	 		}
		}
		
	 
		
		for (StateVariable sv : stateVariables) {
			sv.resolve(r_behavior.getComponentType(), lems.getDimensions());
			if (sv.hasExposure()) {
	 			countExposure(sv.getExposure(), exposedHM);
	 		}
		}
		for (TimeDerivative sd : timeDerivatives) {
			sd.resolve(allSV, valHM, parser);
		}
		
		for (OnStart os : onStarts) {
			os.resolve(r_behavior, allSV, valHM, parser);
		}
		
		for (OnEvent oe : onEvents) {
			oe.resolve(r_behavior, allSV, valHM, parser);
		}
		
		for (OnEntry oe : onEntrys) {
			oe.resolve(r_behavior,  allSV, valHM, parser);
		}
		
		for (OnCondition oc : onConditions) {
			oc.resolve(r_behavior, allSV, valHM, parser);
		}
	}
	

	   private void countExposure(Named exposure, HashMap<String, Integer> exposedHM) throws ContentError {
		   String nm = exposure.getName();
		   if (exposedHM.containsKey(nm)) {
			   exposedHM.put(nm, exposedHM.get(nm) + 1);
		   } else {
			   throw new ContentError("Variable revers to non-existent exposure: " + nm);
		   }
		}

	
	private void addToMap(LemsCollection<? extends Valued> col, HashMap<String, Valued> valHM) {
		for (Valued vin : col) {
			valHM.put(vin.getName(), vin);
		}
	}
	
	


	public ComponentRegime makeComponentRegime(StateType cb, Component cpt, HashMap<String, Double> fixedHM) throws ContentError {
		 ComponentRegime ret = new ComponentRegime(cb, name, cpt.getComponentType().getName());
		 
		 if (b_initial) {
			 ret.setInitial(true);
		 }
		 
		 for (ParamValue pv : cpt.getParamValues()) {
			 String qn = pv.getName();
			 double qv = pv.getDoubleValue();
			 ret.addFixed(qn, qv);
			 fixedHM.put(qn, qv);
		 }
		 
		 
		 HashSet<StateVariable> varHS = new HashSet<StateVariable>();
		 for (StateVariable sv : stateVariables) {
			varHS.add(sv);
			ret.addStateVariable(sv.getName());
		 }
		 
		 for (RequiredVar rv : requiredVars) {
			 ret.addIndependentVariable(rv.getName());
		 }
		 
		 
		 /*
		 for (ExternalQuantity equan : externalQuantitys) {
			 String qn = equan.getName();
			 double qv = PathEvaluator.getValue(cpt, equan.getPath());
			 ret.addFixed(qn, qv);
			 fixedHM.put(qn, qv); // MUSTDO we don't need both of these: 
			 // either Ext quans shouldn't say they are fixed and we should use ret.addFixed, or it should 
			 // and use fixedHM.put.
		 }
		 */
		 
 	 
		 for (DerivedVariable dv : derivedVariables) {
			 if (dv.hasExpression()) {
				 ParseTree pt = dv.getParseTree();
				 DoubleEvaluator db = pt.makeFloatFixedEvaluator(fixedHM);
				 ret.addExpressionDerived(dv.getName(), db, dv.getDimensionString());
			 } else if (dv.hasSelection()) {
				 
				 ret.addPathDerived(dv.getName(), dv.getPath(), dv.getFunc(), dv.isRequired(), dv.getReduce(), dv.getDimensionString());
			 }
		 }
		 
		 for (TimeDerivative sd : timeDerivatives) {
			 StateVariable sv = sd.getStateVariable();
			 varHS.remove(sv);
			 ParseTree pt = sd.getParseTree();
			 DoubleEvaluator db = pt.makeFloatFixedEvaluator(fixedHM);
			 
			 ret.addRate(sv.getName(), db, sd.getDimensionString());
		 }
		 
		 for (OnStart os : onStarts) {
 			 ActionBlock ea = os.makeEventAction(fixedHM);
			 ret.addInitialization(ea);
		 }
		 
		 for (OnEntry oe : onEntrys) {
 			 ActionBlock ea = oe.makeEventAction(fixedHM);
			 ret.addEntry(ea);
		 }
		 
		 for (OnEvent oe : onEvents) {
			 EventAction er = new EventAction(oe.getPortName());
			 
			 ActionBlock ea = oe.makeEventAction(fixedHM);
			 if (ea == null) {
				 throw new ContentError("Null action block from OnEvent " + oe);
			 }
			 er.setAction(ea);
			 ret.addEventResponse(er);
		 }
		 
		 for (OnCondition oc : onConditions) {
			 
			 ParseTree pt = oc.getParseTree();
			 BooleanEvaluator bb = pt.makeBooleanFixedEvaluator(fixedHM);
			 
			
			 ConditionAction cr = new ConditionAction(bb);
			 ActionBlock ea = oc.makeEventAction(fixedHM);
			 cr.setAction(ea);
			 ret.addConditionResponse(cr);
		 }
		 
		  
		 
		 
		 // TODO - could do something with children of parent type here, but don't have to as they 
		 // come in again via the structure of the component itself.
		 
		 /*
		 for (StateVariable sv : varHS) {
			// E.info("sv without derivative " + sv + " in mcb for " + cpt.getID() + " " + cpt.hashCode());
		 }
		 */
		 
		 
		 
		 ret.fix();
		 return ret;
	}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LemsCollection<OnCondition> getOnConditions() {
        return onConditions;
    }


    public LemsCollection<OnEntry> getOnEntrys() {
        return onEntrys;
    }


    public LemsCollection<OnEvent> getOnEvents() {
        return onEvents;
    }


    public LemsCollection<OnStart> getOnStarts() {
        return onStarts;
    }


    public LemsCollection<TimeDerivative> getTimeDerivatives() {
        return timeDerivatives;
    }


 
	
	
	
	private ComponentType getComponentType() {
		return r_behavior.getComponentType();
	}




	public Valued getValued(String select) throws ContentError {
		checkMakeValHM();
		return valHM.get(select);
	}



	public void checkEquations() throws ContentError {
		HashMap<String, Dimensional> dimHM = new HashMap<String, Dimensional>();
		
		for (Requirement req : getComponentType().getRequirements()) {
			dimHM.put(req.getName(), req.getDimensionality());
		}
		for (StateVariable sv : stateVariables) {
			dimHM.put(sv.getName(), sv.getDimensionality());
		}
	 	
		
		ComponentType typ = r_behavior.getComponentType();
		for (FinalParam fp : typ.getFinalParams()) {
			dimHM.put(fp.getName(), fp.getDimensionality());
		}
		ExprDimensional tdim = new ExprDimensional();
		tdim.setT(1);
		dimHM.put("t", tdim);
		
	 	
		
		for (DerivedVariable dv : derivedVariables) {
			try {
				dimHM.put(dv.getName(), dv.getDimensionality(dimHM));
			} catch (ContentError ce) {
				E.error("checking " + dv + " in " + typ + " " + ce.getMessage());
			}
		}
		 
		
		for (TimeDerivative td : timeDerivatives) {
			td.checkDimensions(dimHM);
		}
		for (PointResponse pr : onStarts) {
			pr.checkEquations(dimHM);
		}
		for (PointResponse pr : onEvents) {
			pr.checkEquations(dimHM);
		}
		for (OnCondition oc : onConditions) {
			oc.checkEquations(dimHM);
			oc.checkConditionDimensions(dimHM);
		}
	}

	
	
	public void addTimeDerivative(TimeDerivative td) {
		timeDerivatives.add(td);
	}
	
	public void addOnCondition(OnCondition oc) {
		onConditions.add(oc);
	}


	public Regime makeCopy() {
		E.missing();
		return null;
	}
	
	
	
	
}
