package org.lemsml.jlems.io.reader;
 
import java.util.ArrayList;
 
import org.lemsml.jlems.core.type.About;
import org.lemsml.jlems.core.type.Assertion;
import org.lemsml.jlems.core.type.Attachments;
import org.lemsml.jlems.core.type.Child;
import org.lemsml.jlems.core.type.Children;
import org.lemsml.jlems.core.type.Collection;
import org.lemsml.jlems.core.type.Component;
import org.lemsml.jlems.core.type.ComponentReference;
import org.lemsml.jlems.core.type.ComponentRequirement;
import org.lemsml.jlems.core.type.ComponentType;
import org.lemsml.jlems.core.type.ComponentTypeReference;
import org.lemsml.jlems.core.type.Constant;
import org.lemsml.jlems.core.type.DerivedParameter;
import org.lemsml.jlems.core.type.Dimension;
import org.lemsml.jlems.core.type.EventPort;
import org.lemsml.jlems.core.type.Exposure;
import org.lemsml.jlems.core.type.Fixed;
import org.lemsml.jlems.core.type.IndexParameter;
import org.lemsml.jlems.core.type.Insertion;
import org.lemsml.jlems.core.type.InstanceRequirement;
import org.lemsml.jlems.core.type.IntegerParameter;
import org.lemsml.jlems.core.type.Lems;
import org.lemsml.jlems.core.type.Link;
import org.lemsml.jlems.core.type.Location;
import org.lemsml.jlems.core.type.Meta;
import org.lemsml.jlems.core.type.PairCollection;
import org.lemsml.jlems.core.type.Parameter;
import org.lemsml.jlems.core.type.Path;
import org.lemsml.jlems.core.type.PathParameter;
import org.lemsml.jlems.core.type.Property;
import org.lemsml.jlems.core.type.Requirement;
import org.lemsml.jlems.core.type.Target;
import org.lemsml.jlems.core.type.Text;
import org.lemsml.jlems.core.type.Unit;
import org.lemsml.jlems.core.type.dynamics.Case;
import org.lemsml.jlems.core.type.dynamics.ConditionalDerivedVariable;
import org.lemsml.jlems.core.type.dynamics.DerivedPunctateField;
import org.lemsml.jlems.core.type.dynamics.DerivedScalarField;
import org.lemsml.jlems.core.type.dynamics.DerivedVariable;
import org.lemsml.jlems.core.type.dynamics.Dynamics;
import org.lemsml.jlems.core.type.dynamics.Equilibrium;
import org.lemsml.jlems.core.type.dynamics.EventConnection;
import org.lemsml.jlems.core.type.dynamics.EventOut;
import org.lemsml.jlems.core.type.dynamics.KineticScheme;
import org.lemsml.jlems.core.type.dynamics.OnCondition;
import org.lemsml.jlems.core.type.dynamics.OnEntry;
import org.lemsml.jlems.core.type.dynamics.OnEvent;
import org.lemsml.jlems.core.type.dynamics.OnStart;
import org.lemsml.jlems.core.type.dynamics.Regime;
import org.lemsml.jlems.core.type.dynamics.StateAssignment;
import org.lemsml.jlems.core.type.dynamics.StateScalarField;
import org.lemsml.jlems.core.type.dynamics.StateVariable;
import org.lemsml.jlems.core.type.dynamics.Super;
import org.lemsml.jlems.core.type.dynamics.TimeDerivative;
import org.lemsml.jlems.core.type.dynamics.Transition;
import org.lemsml.jlems.core.type.geometry.Frustum;
import org.lemsml.jlems.core.type.geometry.Geometry;
import org.lemsml.jlems.core.type.geometry.ScalarField;
import org.lemsml.jlems.core.type.geometry.Skeleton;
import org.lemsml.jlems.core.type.geometry.Solid;
import org.lemsml.jlems.core.type.procedure.Equilibrate;
import org.lemsml.jlems.core.type.procedure.ForEachComponent;
import org.lemsml.jlems.core.type.procedure.Print;
import org.lemsml.jlems.core.type.procedure.Procedure;
import org.lemsml.jlems.core.type.simulation.DataDisplay;
import org.lemsml.jlems.core.type.simulation.DataWriter;
import org.lemsml.jlems.core.type.simulation.EventWriter;
import org.lemsml.jlems.core.type.simulation.Record;
import org.lemsml.jlems.core.type.simulation.Run;
import org.lemsml.jlems.core.type.simulation.Simulation;
import org.lemsml.jlems.core.type.structure.Apply;
import org.lemsml.jlems.core.type.structure.Assign;
import org.lemsml.jlems.core.type.structure.ChildInstance;
import org.lemsml.jlems.core.type.structure.Choose;
import org.lemsml.jlems.core.type.structure.CoInstantiate;
import org.lemsml.jlems.core.type.structure.ForEach;
import org.lemsml.jlems.core.type.structure.Gather;
import org.lemsml.jlems.core.type.structure.GatherPairs;
import org.lemsml.jlems.core.type.structure.If;
import org.lemsml.jlems.core.type.structure.IncludePair;
import org.lemsml.jlems.core.type.structure.MultiInstantiate;
import org.lemsml.jlems.core.type.structure.PairFilter;
import org.lemsml.jlems.core.type.structure.PairsEventConnection;
import org.lemsml.jlems.core.type.structure.Structure;
import org.lemsml.jlems.core.type.structure.Tunnel;
import org.lemsml.jlems.core.type.structure.With;
 
public final class LemsClasses {

	
	public static LemsClasses instance;
	
	private final ArrayList<LemsClass> classList;
	
	
	public static LemsClasses getInstance() {
		synchronized(LemsClasses.class) {
		if (instance == null) {
			instance = new LemsClasses();
		}
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
		ret.add(new LemsClass(Component.class, section));
		
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
	 	ret.add(new LemsClass(Super.class, section));
	 	
	 	ret.add(new LemsClass(ConditionalDerivedVariable.class, section));
	 	ret.add(new LemsClass(Case.class, section));
	 	ret.add(new LemsClass(Equilibrium.class, section));
	 	
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
	 	ret.add(new LemsClass(Tunnel.class, section));
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
		ret.add(new LemsClass(ComponentRequirement.class, section));
		ret.add(new LemsClass(InstanceRequirement.class, section));
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
		ret.add(new LemsClass(IndexParameter.class, section));
		ret.add(new LemsClass(About.class, section));
		ret.add(new LemsClass(Meta.class, section));
		return ret;
	}

	private ArrayList<LemsClass> getSimulationClasses() {
		ArrayList<LemsClass> ret =  new ArrayList<LemsClass>();
		String section = "simulation";
		ret.add(new LemsClass(Simulation.class, section));
		ret.add(new LemsClass(Record.class, section));
		ret.add(new LemsClass(DataDisplay.class, section));
		ret.add(new LemsClass(DataWriter.class, section));
		ret.add(new LemsClass(EventWriter.class, section));
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
