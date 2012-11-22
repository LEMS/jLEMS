package org.lemsml.jlemsio.reader;
 
import java.util.ArrayList;

import org.lemsml.jlems.type.Assertion;
import org.lemsml.jlems.type.Attachments;
import org.lemsml.jlems.type.Child;
import org.lemsml.jlems.type.Children;
import org.lemsml.jlems.type.Collection;
import org.lemsml.jlems.type.ComponentReference;
import org.lemsml.jlems.type.ComponentType;
import org.lemsml.jlems.type.ComponentTypeReference;
import org.lemsml.jlems.type.Constant;
import org.lemsml.jlems.type.DerivedParameter;
import org.lemsml.jlems.type.Dimension;
import org.lemsml.jlems.type.EventPort;
import org.lemsml.jlems.type.Exposure;
import org.lemsml.jlems.type.Fixed;
import org.lemsml.jlems.type.Insertion;
import org.lemsml.jlems.type.IntegerParameter;
import org.lemsml.jlems.type.Lems;
import org.lemsml.jlems.type.Link;
import org.lemsml.jlems.type.Location;
import org.lemsml.jlems.type.PairCollection;
import org.lemsml.jlems.type.Parameter;
import org.lemsml.jlems.type.Path;
import org.lemsml.jlems.type.PathParameter;
import org.lemsml.jlems.type.Property;
import org.lemsml.jlems.type.Requirement;
import org.lemsml.jlems.type.Target;
import org.lemsml.jlems.type.Text;
import org.lemsml.jlems.type.Unit;
import org.lemsml.jlems.type.dynamics.DerivedPunctateField;
import org.lemsml.jlems.type.dynamics.DerivedScalarField;
import org.lemsml.jlems.type.dynamics.DerivedVariable;
import org.lemsml.jlems.type.dynamics.Dynamics;
import org.lemsml.jlems.type.dynamics.EventConnection;
import org.lemsml.jlems.type.dynamics.EventOut;
import org.lemsml.jlems.type.dynamics.KineticScheme;
import org.lemsml.jlems.type.dynamics.OnCondition;
import org.lemsml.jlems.type.dynamics.OnEntry;
import org.lemsml.jlems.type.dynamics.OnEvent;
import org.lemsml.jlems.type.dynamics.OnStart;
import org.lemsml.jlems.type.dynamics.Regime;
import org.lemsml.jlems.type.dynamics.StateAssignment;
import org.lemsml.jlems.type.dynamics.StateScalarField;
import org.lemsml.jlems.type.dynamics.StateVariable;
import org.lemsml.jlems.type.dynamics.TimeDerivative;
import org.lemsml.jlems.type.dynamics.Transition;
import org.lemsml.jlems.type.geometry.Frustum;
import org.lemsml.jlems.type.geometry.Geometry;
import org.lemsml.jlems.type.geometry.ScalarField;
import org.lemsml.jlems.type.geometry.Skeleton;
import org.lemsml.jlems.type.geometry.Solid;
import org.lemsml.jlems.type.procedure.Equilibrate;
import org.lemsml.jlems.type.procedure.ForEachComponent;
import org.lemsml.jlems.type.procedure.Print;
import org.lemsml.jlems.type.procedure.Procedure;
import org.lemsml.jlems.type.simulation.DataDisplay;
import org.lemsml.jlems.type.simulation.Record;
import org.lemsml.jlems.type.simulation.Run;
import org.lemsml.jlems.type.simulation.Simulation;
import org.lemsml.jlems.type.structure.Apply;
import org.lemsml.jlems.type.structure.Assign;
import org.lemsml.jlems.type.structure.ChildInstance;
import org.lemsml.jlems.type.structure.Choose;
import org.lemsml.jlems.type.structure.CoInstantiate;
import org.lemsml.jlems.type.structure.ForEach;
import org.lemsml.jlems.type.structure.Gather;
import org.lemsml.jlems.type.structure.GatherPairs;
import org.lemsml.jlems.type.structure.If;
import org.lemsml.jlems.type.structure.IncludePair;
import org.lemsml.jlems.type.structure.MultiInstantiate;
import org.lemsml.jlems.type.structure.PairFilter;
import org.lemsml.jlems.type.structure.PairsEventConnection;
import org.lemsml.jlems.type.structure.Structure;
import org.lemsml.jlems.type.structure.With;

public class LemsClasses {

	
	public static LemsClasses instance;
	
	
	ArrayList<LemsClass> classList;
	
	
	public static LemsClasses getInstance() {
		if (instance == null) {
			instance = new LemsClasses();
		}
		return instance;
	}
	
	
	
	private LemsClasses() {
		classList = new ArrayList<LemsClass>();
	
		classList.addAll(getLemsClasses());
		classList.addAll(getComponentTypeClasses());
		classList.addAll(getDynamicsClasses());
		classList.addAll(getStructureClasses());
		classList.addAll(getSimulationClasses());
		classList.addAll(getProcedureClasses());
		classList.addAll(getGeometryClasses());
	}

	public ArrayList<LemsClass> getClasses() {
 		return classList;
	}

	

	private ArrayList<LemsClass> getLemsClasses() {
		ArrayList<LemsClass> ret =  new ArrayList<LemsClass>();
		String section = "root";
		ret.add(new LemsClass(Lems.class, section));
		ret.add(new LemsClass(Target.class, section));
		ret.add(new LemsClass(Constant.class, section));	
		
		section = "unitsdimensions";
		ret.add(new LemsClass(Dimension.class, section));
		ret.add(new LemsClass(Unit.class, section));
		ret.add(new LemsClass(Assertion.class, section));

	 
		return ret;
	}
	
	
	private ArrayList<LemsClass> getDynamicsClasses() {
		ArrayList<LemsClass> ret =  new ArrayList<LemsClass>();
		String section = "dynamics";
		ret.add(new LemsClass(Dynamics.class, section));
		ret.add(new LemsClass(StateVariable.class, section));
		ret.add(new LemsClass(StateAssignment.class, section));
		ret.add(new LemsClass(TimeDerivative.class, section));
		ret.add(new LemsClass(DerivedVariable.class, section));
		ret.add(new LemsClass(OnStart.class, section));
	 	ret.add(new LemsClass(OnCondition.class, section));	
	 	ret.add(new LemsClass(OnEvent.class, section));
	 	ret.add(new LemsClass(EventOut.class, section));
	 	ret.add(new LemsClass(KineticScheme.class, section));
	 	ret.add(new LemsClass(Regime.class, section));
	 	ret.add(new LemsClass(OnEntry.class, section));
	 	ret.add(new LemsClass(Transition.class, section));
	 	
	 	ret.add(new LemsClass(StateScalarField.class, section));
	 	ret.add(new LemsClass(DerivedScalarField.class, section));
	 	ret.add(new LemsClass(DerivedPunctateField.class, section));
		return ret;
	}

	private ArrayList<LemsClass> getStructureClasses() {
		ArrayList<LemsClass> ret =  new ArrayList<LemsClass>();
		String section = "structure";
		ret.add(new LemsClass(Structure.class, section));
		ret.add(new LemsClass(MultiInstantiate.class, section));
		ret.add(new LemsClass(CoInstantiate.class, section));
		ret.add(new LemsClass(Assign.class, section));
		ret.add(new LemsClass(Choose.class, section));
	 	ret.add(new LemsClass(ChildInstance.class, section));	
	 	ret.add(new LemsClass(ForEach.class, section));
	 	ret.add(new LemsClass(EventConnection.class, section));
		ret.add(new LemsClass(PairsEventConnection.class, section));
		ret.add(new LemsClass(PairFilter.class, section));
		ret.add(new LemsClass(IncludePair.class, section));
	 	ret.add(new LemsClass(With.class, section));
		ret.add(new LemsClass(If.class, section));
		ret.add(new LemsClass(Apply.class, section));
		ret.add(new LemsClass(Gather.class, section));
		ret.add(new LemsClass(GatherPairs.class, section));
		return ret;
	}


	private ArrayList<LemsClass> getComponentTypeClasses() {
		ArrayList<LemsClass> ret =  new ArrayList<LemsClass>();
		
		String section = "componenttypes";
		ret.add(new LemsClass(ComponentType.class, section));
		ret.add(new LemsClass(Parameter.class, section));
		ret.add(new LemsClass(PathParameter.class, section));
		
		ret.add(new LemsClass(Property.class, section));
		ret.add(new LemsClass(DerivedParameter.class, section));
		ret.add(new LemsClass(Fixed.class, section));
		ret.add(new LemsClass(Requirement.class, section));
		ret.add(new LemsClass(Exposure.class, section));
		ret.add(new LemsClass(Child.class, section));
		ret.add(new LemsClass(Children.class, section));
		// NB Link has to come before ComponentRef here becuase it is a subclass and we want
		// link elts in the right list. TODO - make same with different scope parameter
		ret.add(new LemsClass(Link.class, section));
 		ret.add(new LemsClass(ComponentReference.class, section));
 		ret.add(new LemsClass(ComponentTypeReference.class, section));
 		ret.add(new LemsClass(Collection.class, section));
 		ret.add(new LemsClass(PairCollection.class, section));
		ret.add(new LemsClass(EventPort.class, section));
		ret.add(new LemsClass(Text.class, section));
		ret.add(new LemsClass(Path.class, section));
		ret.add(new LemsClass(Attachments.class, section));
 		ret.add(new LemsClass(Insertion.class, section));
		ret.add(new LemsClass(IntegerParameter.class, section));
		return ret;
	}

	private ArrayList<LemsClass> getSimulationClasses() {
		ArrayList<LemsClass> ret =  new ArrayList<LemsClass>();
		String section = "simulation";
		ret.add(new LemsClass(Simulation.class, section));
		ret.add(new LemsClass(Record.class, section));
		ret.add(new LemsClass(DataDisplay.class, section));
		ret.add(new LemsClass(Run.class, section));
		return ret;
	}
	
	
	private ArrayList<LemsClass> getProcedureClasses() {
		ArrayList<LemsClass> ret =  new ArrayList<LemsClass>();
		
		String section = "procedure";
		ret.add(new LemsClass(Procedure.class, section));
		ret.add(new LemsClass(Equilibrate.class, section));
		ret.add(new LemsClass(ForEachComponent.class, section));
		ret.add(new LemsClass(Print.class, section));
		return ret;
	}
	
	

	private ArrayList<LemsClass> getGeometryClasses() {
		ArrayList<LemsClass> ret =  new ArrayList<LemsClass>();
		
		String section = "geometry";
		ret.add(new LemsClass(Geometry.class, section));
		ret.add(new LemsClass(Frustum.class, section));

		ret.add(new LemsClass(Solid.class, section));
		ret.add(new LemsClass(Location.class, section));
		ret.add(new LemsClass(Skeleton.class, section));
		ret.add(new LemsClass(ScalarField.class, section));
		
		return ret;
	}
	
}
