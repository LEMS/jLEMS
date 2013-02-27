package org.lemsml.jlems.core.type.dynamics;

import java.util.ArrayList;
import java.util.HashMap;

import org.lemsml.jlems.core.annotation.ModelElement;
import org.lemsml.jlems.core.annotation.ModelProperty;
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.run.KScheme;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.type.Children;
import org.lemsml.jlems.core.type.Component;
import org.lemsml.jlems.core.type.ComponentReference;
import org.lemsml.jlems.core.type.ComponentType;
 

@ModelElement(info="A kinetic scheme does not itself introduce any new elements or state variables. " +
		"It is rather a way of connecting quantities in existing components by saying that " +
		"quantities in the edge elements should be interpreted as transition rates among " +
		"quantities in the node elements. ")
public class KineticScheme {

	@ModelProperty(info="")
	public String name;
	 
	@ModelProperty(info="Source of notes for scheme")
	public String nodes;
	
	@ModelProperty(info="The element that provides the transitions for the scheme")
	public String edges;
	
	@ModelProperty(info="Name of state variable in state elements")
	public String stateVariable;

	@ModelProperty(info="The name of the attribute in the rate element that defines the source of the transition")
	public String edgeSource;
	
	@ModelProperty(info="Attribute tha defines the target")
	public String edgeTarget;
	
	@ModelProperty(info="Name of forward rate exposure")
	public String forwardRate;
	
	@ModelProperty(info="Name of reverse rate exposure")
	public String reverseRate;
	
	public String dependency;
	
	public String step;
	
	
	 
	
	
	
	/*
	 <KineticScheme name="ks" nodes="states" stateVariable="occupancy"
         	edges="transitions" edgeSource="from" edgeTarget="to" 
         	forwardRate="rf" reverseRate="rr" dependency="v" step="deltaV"/>
	
	*/
 
	
	Children r_nodes;
	Children r_edges; 
	
	ComponentReference r_srcRef;
	ComponentReference r_tgtRef;
	

	public void resolve(ComponentType r_type) throws ContentError {
		r_nodes = r_type.getChildrenByName(nodes);
		r_edges = r_type.getChildrenByName(edges);
	
		ComponentType tedge = r_edges.getComponentType();
			
		r_srcRef = tedge.getComponentRef(edgeSource);
		r_tgtRef = tedge.getComponentRef(edgeTarget);
	}


	public KScheme makeKScheme(ArrayList<Component> astat, ArrayList<Component> arat) throws ContentError {
		int ns = astat.size();
		int nr = arat.size();
		
		HashMap<String, Integer> stateIndexHM = new HashMap<String, Integer>();
	
		for (int i = 0; i < ns; i++) {
			stateIndexHM.put(astat.get(i).getID(), i);
		}
		
		
		int[][] tbl = new int[nr][2];
		for (int i = 0; i < nr; i++) {
			Component r = arat.get(i);
	
			String srcid = r.getStringValue(edgeSource);
			String tgtid = r.getStringValue(edgeTarget);
			tbl[i][0] = stateIndexHM.get(srcid);
			tbl[i][1] = stateIndexHM.get(tgtid);
		}
		
		KScheme ret = new KScheme(name, tbl, nodes, edges, stateVariable, forwardRate, reverseRate);
 
		return ret;
	}


	public String getNodesName() {
		return nodes;
	}


	public String getEdgesName() {
		return edges;
	}


	public KineticScheme makeCopy() {
		E.missing();
		return null;
	}

 
	
	
}
