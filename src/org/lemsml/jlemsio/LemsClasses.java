package org.lemsml.jlemsio;
 
import java.util.ArrayList;

import org.lemsml.jlems.type.Assertion;
import org.lemsml.jlems.type.Child;
import org.lemsml.jlems.type.Children;
import org.lemsml.jlems.type.ComponentRef;
import org.lemsml.jlems.type.ComponentType;
import org.lemsml.jlems.type.Dimension;
import org.lemsml.jlems.type.EventPort;
import org.lemsml.jlems.type.Exposure;
import org.lemsml.jlems.type.Link;
import org.lemsml.jlems.type.Parameter;
import org.lemsml.jlems.type.Path;
import org.lemsml.jlems.type.Requirement;
import org.lemsml.jlems.type.Target;
import org.lemsml.jlems.type.Text;
import org.lemsml.jlems.type.Unit;
import org.lemsml.jlems.type.dynamics.DerivedVariable;
import org.lemsml.jlems.type.dynamics.Dynamics;
import org.lemsml.jlems.type.dynamics.EventConnection;
import org.lemsml.jlems.type.dynamics.EventOut;
import org.lemsml.jlems.type.dynamics.OnCondition;
import org.lemsml.jlems.type.dynamics.OnEvent;
import org.lemsml.jlems.type.dynamics.OnStart;
import org.lemsml.jlems.type.dynamics.StateAssignment;
import org.lemsml.jlems.type.dynamics.StateVariable;
import org.lemsml.jlems.type.dynamics.TimeDerivative;
import org.lemsml.jlems.type.simulation.DataDisplay;
import org.lemsml.jlems.type.simulation.Record;
import org.lemsml.jlems.type.simulation.Run;
import org.lemsml.jlems.type.simulation.Simulation;
import org.lemsml.jlems.type.structure.ChildInstance;
import org.lemsml.jlems.type.structure.Choose;
import org.lemsml.jlems.type.structure.CoInstantiate;
import org.lemsml.jlems.type.structure.ForEach;
import org.lemsml.jlems.type.structure.MultiInstantiate;
import org.lemsml.jlems.type.structure.Structure;

public class LemsClasses {

	
	public static LemsClasses instance;
	
	
	ArrayList<Class<?>> classList;
	
	
	public static LemsClasses getInstance() {
		if (instance == null) {
			instance = new LemsClasses();
		}
		return instance;
	}
	
	
	
	private LemsClasses() {
		classList = new ArrayList<Class<?>>();
		
		classList.addAll(getComponentTypeClasses());
		classList.addAll(getDynamicsClasses());
		classList.addAll(getStructureClasses());
		classList.addAll(getSimulationClasses());
	}

	public ArrayList<Class<?>> getClasses() {
		// TODO Auto-generated method stub
		return classList;
	}

	private ArrayList<Class<?>> getDynamicsClasses() {
		ArrayList<Class<?>> ret =  new ArrayList<Class<?>>();
	
		ret.add(Dynamics.class);
		ret.add(StateVariable.class);
		ret.add(StateAssignment.class);
		ret.add(TimeDerivative.class);
		ret.add(DerivedVariable.class);
		ret.add(OnStart.class);
	 	ret.add(OnCondition.class);	
	 	ret.add(OnEvent.class);
	 	ret.add(EventOut.class);
		return ret;
	}

	private ArrayList<Class<?>> getStructureClasses() {
		ArrayList<Class<?>> ret =  new ArrayList<Class<?>>();
	
		ret.add(Structure.class);
		ret.add(MultiInstantiate.class);
		ret.add(CoInstantiate.class);
		ret.add(Choose.class);
	 	ret.add(ChildInstance.class);	
	 	ret.add(ForEach.class);
	 	ret.add(EventConnection.class);
		return ret;
	}


	private ArrayList<Class<?>> getComponentTypeClasses() {
		ArrayList<Class<?>> ret =  new ArrayList<Class<?>>();
		
		ret.add(Dimension.class);
		ret.add(Unit.class);
		ret.add(Assertion.class);
		ret.add(ComponentType.class);
		ret.add(Target.class);
		ret.add(Parameter.class);
		ret.add(Requirement.class);
		ret.add(Exposure.class);
		ret.add(Child.class);
		ret.add(Children.class);
		// NB Link has to come before ComponentRef here becuase it is a subclass and we want
		// link elts in the right list. TODO - make same with different scope parameter
		ret.add(Link.class);
		ret.add(ComponentRef.class);
		ret.add(EventPort.class);
		ret.add(Text.class);
		ret.add(Path.class);
		return ret;
	}

	private ArrayList<Class<?>> getSimulationClasses() {
		ArrayList<Class<?>> ret =  new ArrayList<Class<?>>();
		
		ret.add(Simulation.class);
		ret.add(Record.class);
		ret.add(DataDisplay.class);
		ret.add(Run.class);
		return ret;
	}
	
}
