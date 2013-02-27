package org.lemsml.jlems.core.run;
 

import org.lemsml.jlems.core.logging.E;

public class KScheme {

	String name;
	
	int nstate;
	int nrate;
	
	int[][] rateTable;
	String forwardVar;
	String reverseVar;
	
	String nodesName;
	String edgesName;
	
	String stateVarname;
 	
	
	public KScheme(String snm, int[][] tbl, String sn, String se, String sov, String fv, String rv) {
		name = snm; 
		rateTable = tbl;
		 nodesName = sn;
		 edgesName = se;
		 stateVarname = sov;
		 forwardVar = fv;
		 reverseVar = rv;
	
		 nrate = rateTable.length;
		 nstate = -1;
		 // don't have nstate directly - get it from the largest index
		 for (int i = 0; i < nrate; i++) {
			 int[] ij = rateTable[i];
			 if (ij[0] > nstate) {
				 nstate = ij[0];
			 }
			 if (ij[1] > nstate) {
				 nstate = ij[1];
			 }
		 }
		 nstate += 1;
	}


	public KScheme makeCopy() {
		KScheme ret = new KScheme(name, rateTable, nodesName, edgesName, stateVarname, forwardVar, reverseVar);
		return ret;
	}


	public String getNodesName() {
		return nodesName;
	}


	public String getEdgesName() {
		return edgesName;
	}

	
	public KSchemeInst makeInstance(MultiInstance smi, MultiInstance rmi) {
		KSchemeInst ret = new KSchemeInst(this, smi, rmi);
		int ns = smi.size();
		for (int i = 0; i < ns; i++) {
			smi.setDouble(i, stateVarname, 0.);
		}
		smi.setDouble(0, stateVarname, 1.);
 
		return ret;
	}


	public String getName() {
		return name;
	}

	
	public void evaluate(StateInstance psi, KSchemeInst inst) throws RuntimeError {
		E.missing();
	}
	
	public void advance(StateInstance psi, KSchemeInst inst, double dt) throws RuntimeError {
		double[] fr = inst.rateMI.getDoubles(forwardVar);
		double[] rr = inst.rateMI.getDoubles(reverseVar);
	
		double[] wkocc = inst.stateMI.getDoubles(stateVarname);
		
		
		Matrix m = new Matrix(nstate);
		double[][] a = m.getData();
		for (int i = 0; i < nrate; i++) {
			int isrc = rateTable[i][0];
			int itgt = rateTable[i][1];
			a[isrc][isrc] -= fr[i];
			a[itgt][isrc] += fr[i];
		
			a[itgt][itgt] -= rr[i];
			a[isrc][itgt] += rr[i];
		}
		
		boolean ok = true;
		try {
			Matrix me = m.expOf(dt);
			me.multiplyInto(wkocc);
		
		} catch (MatrixException mex) {
			ok = false;
		}
		if (!ok) {
			throw new RuntimeError("Matrix calculation failed while advancing kinetich schem " + m.dump());
		}
		
		inst.stateMI.setDoubles(stateVarname, wkocc);
 
	}



 

 
	
}
