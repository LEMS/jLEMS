package org.lemsml.jlems.core.type.dynamics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.lemsml.jlems.core.annotation.ModelElement;
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
import org.lemsml.jlems.core.run.KScheme;
import org.lemsml.jlems.core.run.StateType;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.type.Component;
import org.lemsml.jlems.core.type.ComponentType;
import org.lemsml.jlems.core.type.EventPort;
import org.lemsml.jlems.core.type.Exposure;
import org.lemsml.jlems.core.type.FinalParam;
import org.lemsml.jlems.core.type.InstanceProperty;
import org.lemsml.jlems.core.type.Lems;
import org.lemsml.jlems.core.type.LemsCollection;
import org.lemsml.jlems.core.type.Named;
import org.lemsml.jlems.core.type.ParamValue;
import org.lemsml.jlems.core.type.Property;
import org.lemsml.jlems.core.type.Requirement;


@ModelElement(info="Specifies the dynamical behavior of components build from this ComponentType. ")
public class Dynamics  {

	public String name;
 	 
	public boolean simultaneous = false;
	
	public LemsCollection<Super> supers = new LemsCollection<Super>();
	
	public LemsCollection<DerivedVariable> derivedVariables = new LemsCollection<DerivedVariable>();
	
	public LemsCollection<ConditionalDerivedVariable> conditionalDerivedVariables = new LemsCollection<ConditionalDerivedVariable>();
	
	public LemsCollection<StateVariable> stateVariables = new LemsCollection<StateVariable>();
	
	public LemsCollection<TimeDerivative> timeDerivatives = new LemsCollection<TimeDerivative>();
	
	public LemsCollection<KineticScheme> kineticSchemes = new LemsCollection<KineticScheme>();
	
	public LemsCollection<OnStart> onStarts = new LemsCollection<OnStart>();
	
	public LemsCollection<OnEvent> onEvents = new LemsCollection<OnEvent>();
	
	public LemsCollection<OnCondition> onConditions = new LemsCollection<OnCondition>();
 
	
	public LemsCollection<StateScalarField> stateScalarFields = new LemsCollection<StateScalarField>();
	
	public LemsCollection<DerivedScalarField> derivedScalarFields = new LemsCollection<DerivedScalarField>();
	
	public LemsCollection<DerivedPunctateField> derivedPunctateFields = new LemsCollection<DerivedPunctateField>();
	
	
	
	public LemsCollection <Regime> regimes = new LemsCollection<Regime>();
	
	
	// not declared in xml - acquired from class signature
	private final LemsCollection <RequiredVar> p_requiredVars = new LemsCollection<RequiredVar>();
	
	private final LemsCollection <ExposedVar> p_exposedVars = new LemsCollection<ExposedVar>();
	
	
	private ComponentType r_type;
	
	// If we inherit from another one, the peer gets modified, we stay the same so  we cn be rewritten
	private Dynamics r_peer;
	
	
	public void setComponentType(ComponentType t) {
		r_type = t;
	}
	
	public ComponentType getComponentType() {
		return r_type;
	}
	
	public String getName() {
		return name;
	}
	
	public Dynamics getPeer() {
		Dynamics ret = this;
		if (r_peer != null) {
			ret = r_peer;
		}
		return ret;
	}
	
     
    @Override
	public String toString() {
		return "Dynamics: " + name;
	}
	
	private HashMap<String, Valued> valHM;
	
	
	
	public HashMap<String, Valued> getValHM() throws ContentError {
		checkMakeValHM();
		return valHM;
	}
	
	
	
	private void checkMakeValHM() throws ContentError {
		if (valHM == null) {
			valHM = new HashMap<String, Valued>();
			ComponentType extt = r_type.getExtends();
			if (extt != null) {
				if (extt.hasBehavior()) {
					Dynamics b = extt.getDynamics();
					HashMap<String, Valued> extHM = b.getValHM();
					for (String s : extHM.keySet()) {
						valHM.put(s, extHM.get(s));
					}
				}
			}
		 
			addToMap(p_requiredVars, valHM);
			
			addToMap(stateVariables, valHM);
			addToMap(derivedVariables, valHM);
			addToMap(conditionalDerivedVariables, valHM);
			addToMap(r_type.getFinalParams(), valHM);
			addToMap(r_type.getInstancePropertys(), valHM);
		}
	}
	
	
	
	
	public void resolve(Lems lems, Parser parser) throws ContentError, ParseError {
		if (r_peer != null) {
			r_peer.realResolve(lems, parser);
		} else {
			realResolve(lems, parser);
		}
	}
		
	private void realResolve(Lems lems, Parser parser) throws ContentError, ParseError {
		if (lems.looseResolving()) {
			// then we expose anything with a name that matches the name of an exposure
			HashSet<String> expHS = new HashSet<String>();
			for (Exposure exp : getComponentType().getExposures()) {
				expHS.add(exp.getName());
			}
			
			for (StateVariable sv : stateVariables) {
				if (sv.exposure == null && expHS.contains(sv.name)) {
					sv.exposure = sv.name;
					E.shortWarning("Implicitly exposing a state variable because its name matches an exposure: " + sv);
				}
			}
			for (DerivedVariable dv : derivedVariables) {
				if (dv.exposure == null && expHS.contains(dv.name)) {
					dv.exposure = dv.name;
					E.shortWarning("Implicitly exposing a derived variable because its name matches an exposure: " + dv);
				}
			}
			
			for (ConditionalDerivedVariable dv : conditionalDerivedVariables) {
				if (dv.exposure == null && expHS.contains(dv.name)) {
					dv.exposure = dv.name;
					E.shortWarning("Implicitly exposing a derived variable because its name matches an exposure: " + dv);
				}
			}
			
		}
		
		
		
		HashMap<String, Integer> exposedHM = new HashMap<String, Integer>();
		
		for (Requirement req : getComponentType().getRequirements()) {
			p_requiredVars.add(new RequiredVar(req.getName(), req.getDimension()));
 		}
		
		for (Exposure exp : getComponentType().getExposures()) {
			p_exposedVars.add(new ExposedVar(exp.getName(), exp.getDimension()));
			exposedHM.put(exp.getName(), 0);
		}
		
		checkMakeValHM();
		
		addToMap(lems.getGlobals(), valHM);
		
		addToMap(lems.getConstantValueds(), valHM);
		 	
		for (DerivedVariable dvar : derivedVariables) {
	 		dvar.resolve(lems, lems.getDimensions(), r_type, valHM, parser);
	 		if (dvar.hasExposure()) {
	 			countExposure(dvar.getExposure(), exposedHM);
	 		}
		}
		
		for (ConditionalDerivedVariable dvar : conditionalDerivedVariables) {
	 		dvar.resolve(lems, lems.getDimensions(), r_type, valHM, parser);
	 		if (dvar.hasExposure()) {
	 			countExposure(dvar.getExposure(), exposedHM);
	 		}
		}
		
		for (StateVariable sv : stateVariables) {
			sv.resolve(r_type, lems.getDimensions());
			if (sv.hasExposure()) {
				countExposure(sv.getExposure(), exposedHM);
			}
		}
		
		for (StateScalarField scf : stateScalarFields) {
			scf.resolve(r_type, lems.getDimensions());
		}
		
		for (DerivedScalarField dcf : derivedScalarFields) {
			dcf.resolve(r_type, lems.getDimensions());
		}
		
		for (DerivedPunctateField dpf : derivedPunctateFields) {
			dpf.resolve(r_type, lems.getDimensions());
		}
		
		
		for (TimeDerivative sd : timeDerivatives) {
			sd.resolve(stateVariables, valHM, parser);
		}
		
		for (OnStart os : onStarts) {
			os.resolve(this, stateVariables, valHM, parser);
		}
		
		for (OnEvent oe : onEvents) {
			oe.resolve(this, stateVariables, valHM, parser);
		}
		
		for (OnCondition oc : onConditions) {
			oc.resolve(this, stateVariables, valHM, parser);
		}
		
		for (Regime reg : regimes) {
			reg.setBehavior(this);
			reg.resolve(stateVariables, lems, parser, exposedHM);
		}
	
		for (KineticScheme ks : kineticSchemes) {
			ks.resolve(r_type);
		}
	
		for (String enm : exposedHM.keySet()) {
			if (exposedHM.get(enm) == 0) {
				E.oneLineWarning("No internal variable is linked to the exposure: " + enm + " for ComponentType " +
						getComponentType().getName());
			}
		}
	
	}
 
	 
	

   private void countExposure(Named exposure, HashMap<String, Integer> exposedHM) throws ContentError {
	   String nm = exposure.getName();
	   if (exposedHM.containsKey(nm)) {
		   exposedHM.put(nm, exposedHM.get(nm) + 1);
	   } else {
		   throw new ContentError("Variable refers to non-existent exposure: " + nm);
	   }
	}

    public LemsCollection<OnCondition> getOnConditions() {
            return onConditions;
    }

    public LemsCollection<OnEvent> getOnEvents() {
            return onEvents;
    }

    public LemsCollection<OnStart> getOnStarts() {
            return onStarts;
    }

    public LemsCollection<StateVariable> getStateVariables() {
            return stateVariables;
    }

    public LemsCollection<DerivedVariable> getDerivedVariables() {
            return derivedVariables;
    }

    public LemsCollection<ConditionalDerivedVariable> getConditionalDerivedVariables() {
        return conditionalDerivedVariables;
    }
    
    
    public LemsCollection<ExposedVar> getExposedVars() {
            return p_exposedVars;
    }

	public LemsCollection<Regime> getRegimes() {
		return regimes;
	}



    public LemsCollection<TimeDerivative> getTimeDerivatives() {
            return timeDerivatives;
    }

        
	
	
	
	private void addToMap(LemsCollection<? extends Valued> col, HashMap<String, Valued> valHM) {
		for (Valued vin : col) {
			valHM.put(vin.getName(), vin);
		}
	}
	 

	public StateType makeStateType(Component cpt, HashMap<String, Double> fixedHM) throws ContentError, ParseError {
 		
         StateType ret = new StateType(cpt.getID(), cpt.getComponentType().getName());
		 
         ret.setSimultaneous(simultaneous);
         
		 for (ParamValue pv : cpt.getParamValues()) {
			 String qn = pv.getName();
			 double qv = pv.getDoubleValue();
			 ret.addFixed(qn, qv);
			 fixedHM.put(qn, qv);
		 }
		 
		 for (RequiredVar rv : p_requiredVars) {
			 ret.addIndependentVariable(rv.getName());
		 }
		 
		 for (ExposedVar ev : p_exposedVars) {
			 ret.addExposedVariable(ev.getName());
		 }
		 
		  
		 
		 HashSet<StateVariable> varHS = new HashSet<StateVariable>();
		 for (StateVariable sv : stateVariables) {
			varHS.add(sv); 
			ret.addStateVaraible(sv.getName());
			if (sv.hasExposure()) {
				ret.addExposureMapping(sv.getName(), sv.getExposure().getName());
			}
		 }
		 
		 for (Property p : cpt.getComponentType().getPropertys()) {
			 String pnm = p.getName();
			 ret.addExposureMapping(pnm, pnm);
			// E.info("Added Exposure mapping for " + pnm + " in " + cpt);
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
		 			 
				 DoubleEvaluator db = dv.getParseTree().makeFloatFixedEvaluator(fixedHM);
				 
				 ret.addExpressionDerived(dv.getName(), db);
             	 
			 } else if (dv.hasSelection()) {	 
				 ret.addPathDerived(dv.getName(), dv.getPath(), dv.getFunc(), dv.isRequired(), dv.getReduce());
				 
			 } else {
				 throw new ContentError("Derived variable needs as selection or an expression");
			 }
            if (dv.hasExposure()) {
                ret.addExposureMapping(dv.getName(), dv.getExposure().getName());
            }
		 }
		 
		 
		 for (ConditionalDerivedVariable cdv : conditionalDerivedVariables) {
			 DoubleEvaluator db = cdv.makeFloatFixedEvaluator(fixedHM);
				 
			 ret.addExpressionDerived(cdv.getName(), db);
             	 
			 
            if (cdv.hasExposure()) {
                ret.addExposureMapping(cdv.getName(), cdv.getExposure().getName());
            }
		 }
		 
		 
		 
		 for (TimeDerivative sd : timeDerivatives) {
			 StateVariable sv = sd.getStateVariable();
			 varHS.remove(sv);
			 
			 ParseTree pt = sd.getParseTree();
			 DoubleEvaluator db = pt.makeFloatFixedEvaluator(fixedHM);
			 ret.addRate(sv.getName(), db);
		 }
		 
		 for (OnStart os : onStarts) {
 			 ActionBlock ea = os.makeEventAction(fixedHM);
			 ret.addInitialization(ea);
		 }
		 
		 for (OnEvent oe : onEvents) {
			 EventAction er = new EventAction(oe.getPortName());	 
			 ActionBlock ea = oe.makeEventAction(fixedHM);
			 er.setAction(ea);
			 if (ea == null) {
				 throw new ContentError("Null action block from OnEvent " + oe);
			 }
			 ret.addEventResponse(er);
		 }
		 if (regimes.size() > 0) {
			 for (EventPort p :  r_type.getEventPorts()) {
				 if (p.isDirectionIn()) {
					 if (onEvents.hasName(p.getName())) {
						 // OK, the existing action will also send the event on to the active regime
					 } else {
 						 EventAction er = new EventAction(p.getName());
	                     ret.addEventResponse(er);
					 }
				 }
			 }
		 }
		 
		 
		 for (OnCondition oc : onConditions) {
		
			 ParseTree pt = oc.getParseTree();
			 BooleanEvaluator bb = pt.makeBooleanFixedEvaluator(fixedHM);
			
			 ConditionAction cr = new ConditionAction(bb);
			 ActionBlock ea = oc.makeEventAction(fixedHM);
			 cr.setAction(ea);
			 ret.addConditionResponse(cr);
		 }
		 
		 
		 for (KineticScheme ks : kineticSchemes) {
			 ArrayList<Component> states = cpt.getChildrenAL(ks.getNodesName());
			 ArrayList<Component> rates = cpt.getChildrenAL(ks.getEdgesName());
			 
			 KScheme scheme = ks.makeKScheme(states, rates);
			 
			 ret.addKScheme(scheme);
		 }
	 
		 for (EventPort p : r_type.getEventPorts()) {
			 if (p.isDirectionIn()) {
				 ret.addInputPort(p.getName());
			 }
			 // TODO - also need output ports done the same way, in case send action is in a sub-regime
		 }
		  	
		 for (Regime reg : regimes) {
			  ComponentRegime crb = reg.makeComponentRegime(ret, cpt, copyFixed(fixedHM));
			  ret.addComponentRegime(crb);
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

	
	private HashMap<String, Double> copyFixed(HashMap<String, Double> fixedHM) {
		 HashMap<String, Double> ret = new HashMap<String, Double>();
		 for (String s : fixedHM.keySet()) {
			 ret.put(s, fixedHM.get(s));
		 }
		 return ret;
	}
	

 
	
	public Valued getValued(String select) throws ContentError {
		checkMakeValHM();
		return valHM.get(select);
	}



	public void checkEquations(HashMap<String, Dimensional> cdimHM) throws ContentError {
		HashMap<String, Dimensional> dimHM = new HashMap<String, Dimensional>();

	 
		dimHM.putAll(cdimHM);
		
		for (RequiredVar rv : p_requiredVars) {
			dimHM.put(rv.getName(), rv.getDimensionality());
		}
	
		
		for (StateVariable sv : stateVariables) {
			dimHM.put(sv.getName(), sv.getDimensionality());
		}
		 	
		
		for (FinalParam fp : r_type.getFinalParams()) {
			dimHM.put(fp.getName(), fp.getDimensionality());
		}

        for (InstanceProperty ip: r_type.getInstancePropertys()) {
			dimHM.put(ip.getName(), ip.getDimensionality());
        }

        ExprDimensional tdim = new ExprDimensional();
        tdim.setT(1);
		dimHM.put("t", tdim);
		
	  
		
		for (DerivedVariable dv : derivedVariables) {
			try {
				dimHM.put(dv.getName(), dv.getDimensionality(dimHM));
			} catch (ContentError ce) {
				E.error("checking " + dv + " in " + r_type + " " + ce.getMessage());
			}
		}
		for (ConditionalDerivedVariable dv : conditionalDerivedVariables) {
			try {
				dimHM.put(dv.getName(), dv.getDimensionality(dimHM));
			} catch (ContentError ce) {
				E.error("checking " + dv + " in " + r_type + " " + ce.getMessage());
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

	public Regime getRegime(String regime) throws ContentError {
		return regimes.getByName(regime);
	}

	public void addDerivedVariable(DerivedVariable dv) {
		 derivedVariables.add(dv);
	}
	
	public void addStateVariable(StateVariable sv) {
		 stateVariables.add(sv);
	}
	
	public void addConditionalDerivedVariable(ConditionalDerivedVariable dv) {
		 conditionalDerivedVariables.add(dv);
	}
	

	public void addTimeDerivative(TimeDerivative td) {
		timeDerivatives.add(td);
	}

	public void addOnCondition(OnCondition oc) {
		 onConditions.add(oc);
	}

	public void addRegime(Regime regime) {
		regimes.add(regime);
	}
	
	public void addOnStart(OnStart os) {
		 onStarts.add(os);
	}
	
	public void addOnEvent(OnEvent oe) {
		 onEvents.add(oe);
	}

	public boolean inheritDynamics() {
		boolean ret = false;
		if (supers.size() > 0) {
			ret = true;
		}
		return ret;
	}

	public void inheritFrom(Dynamics src) {
		for (DerivedVariable dv : src.derivedVariables) {
			derivedVariables.add(dv.makeCopy());
		}
		for (ConditionalDerivedVariable dv : src.conditionalDerivedVariables) {
			conditionalDerivedVariables.add(dv.makeCopy());
		}
		
		for (StateVariable sv : src.stateVariables) {
			stateVariables.add(sv.makeCopy());
		}
		for (TimeDerivative td: src.timeDerivatives) {
			timeDerivatives.add(td.makeCopy());
		}
		for (KineticScheme ks : src.kineticSchemes) {
			kineticSchemes.add(ks.makeCopy());
		}
		for (OnStart os : src.onStarts) {
			onStarts.add(os.makeCopy());
		}
		for (OnEvent oe : src.onEvents) {
			onEvents.add(oe.makeCopy());
		}
		for (OnCondition oc : src.onConditions) {
			onConditions.add(oc.makeCopy());
		}
		for (Regime r : src.regimes) {
			regimes.add(r.makeCopy());
		}
	 
	}

	
	public void makePeerCopy() {
		Dynamics ret = new Dynamics();
		ret.name = name;
		ret.simultaneous = simultaneous;
		
		if (r_type != null) {
			ret.r_type = r_type;
		}
		
		ret.inheritFrom(this);
		r_peer = ret;
	}
 
}

