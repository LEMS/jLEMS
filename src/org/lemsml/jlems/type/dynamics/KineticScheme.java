package org.lemsml.jlems.type.dynamics;

import java.util.ArrayList;
import java.util.HashMap;

import org.lemsml.jlems.annotation.ModelProperty;
import org.lemsml.jlems.annotation.ModelElement;
import org.lemsml.jlems.run.KScheme;
import org.lemsml.jlems.sim.ContentError;
import org.lemsml.jlems.type.Children;
import org.lemsml.jlems.type.Component;
import org.lemsml.jlems.type.ComponentReference;
import org.lemsml.jlems.type.ComponentType;

@ModelElement(info="A kinetic scheme does not itself introduce any new elements or state variables. " +
		"It is rather a way of connecting quantities in existing components by saying that " +
		"quantities in the edge elements should be interpreted as transition rates among " +
		"quantities in the node elements. ")
public class KineticScheme {

	@ModelProperty(info="")
	public String name;
	 
	@ModelProperty(info="")
	public String nodes;
	
	@ModelProperty(info="")
	public String edges;
	
	public String stateVariable;

	public String edgeSource;
	
	public String edgeTarget;
	
	public String forwardRate;
	
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

 
	
	
}
