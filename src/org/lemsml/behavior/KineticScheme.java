package org.lemsml.behavior;

import java.util.ArrayList;
import java.util.HashMap;

import org.lemsml.annotation.ExplicitChildContainer;
import org.lemsml.annotation.Mat;
import org.lemsml.annotation.Mel;
import org.lemsml.run.KScheme;
import org.lemsml.type.Children;
import org.lemsml.type.Component;
import org.lemsml.type.ComponentType;
import org.lemsml.util.ContentError;

@Mel(info="A kinetic scheme does not itself introduce any new elements or state variables. " +
		"It is rather a way of connecting quantities in existing components by saying that " +
		"quantities in the edge elements should be interpreted as transition rates among " +
		"quantities in the node elements. ")
public class KineticScheme  implements ExplicitChildContainer {

	@Mat(info="")
	public String name;
	 
	@Mat(info="")
	public Nodes nodes;
	
	@Mat(info="")
	public Edges edges;
	
	
	public Tabulable tabulable; 
	// MUSTDO use. If present, the KScheme should take responsibility for updating the rate objects only when it 
	// needs them
	
	
	Children r_nodes;
	Children r_edges; 
	
	 

	public void resolve(ComponentType r_type) throws ContentError {
		r_nodes = r_type.getChildrenByName(nodes.children);
		r_edges = r_type.getChildrenByName(edges.children);
		
		edges.resolve(r_edges);
	 
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
	
			String srcid = r.getStringValue(edges.sourceNodeName);
			String tgtid = r.getStringValue(edges.targetNodeName);
			tbl[i][0] = stateIndexHM.get(srcid);
			tbl[i][1] = stateIndexHM.get(tgtid);
		}
		
		KScheme ret = new KScheme(name, tbl, nodes.children, edges.children, nodes.variable, edges.forwardRate, edges.reverseRate);
		
		 
	
		return ret;
	}


	public String getNodesName() {
		return nodes.children;
	}


	public String getEdgesName() {
		return edges.children;
	}


	@Override
	public ArrayList<Class<?>> getChildClasses() {
		 ArrayList<Class<?>> ret = new ArrayList<Class<?>>();
		 ret.add(Nodes.class);
		 ret.add(Edges.class);
		 return ret;
	}
	
	
	
	
}
