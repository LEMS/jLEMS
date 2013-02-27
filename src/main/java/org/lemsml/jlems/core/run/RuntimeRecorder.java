package org.lemsml.jlems.core.run;

import org.lemsml.jlems.core.display.DataViewer;
import org.lemsml.jlems.core.out.ResultWriter;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.sim.RunnableAccessor;


public class RuntimeRecorder {

	String id;
	String quantity;
 	String color;
	String display;
	 
	  StateWrapper stateWrapper;

	  // on or the other of these will be set - maybe common interface?
	  DataViewer dataViewer;
	  ResultWriter resultWriter;
	  
	  double tscale;
	  double yscale;
	  

	public RuntimeRecorder(String aid, String q, double tsc, double ysc, String col, String d) {
		 id = aid;
		 quantity = q;
		 tscale = tsc;
		 yscale = ysc;
		 color = col;
		 display = d;
	}

	
	public String toString() {
		return "Recorder, " + id + " of " + quantity + " tscale=" + tscale + " yscale=" + yscale + 
				" display=" + display + " color=" + color;
	}
	

	public String getID() {
		return id;
	}


	public String getDisplay() {
		return display;
	}

 

	public void connectRunnable(RunnableAccessor ra, DataViewer dv) throws ConnectionError {
		if (quantity == null) {
			throw new ConnectionError("Recorder has null quantity " + toString());
		}
        stateWrapper = ra.getStateWrapper(quantity);
        if (stateWrapper == null) {
            throw new ConnectionError("unable to access state variable: " + quantity);
        }
        dataViewer = dv;
    }
	
	
	public void connectRunnable(RunnableAccessor ra, ResultWriter rw) throws ConnectionError {
		if (quantity == null) {
			throw new ConnectionError("Recorder has null quantity " + toString());
		}
        stateWrapper = ra.getStateWrapper(quantity);
        if (stateWrapper == null) {
            throw new ConnectionError("unable to access state variable: " + quantity);
        }
        resultWriter = rw;
    }
	

    public void appendState(double ft) throws ContentError, RuntimeError {

        double x = ft / tscale;
        double y = stateWrapper.getValue() / yscale;
        //E.info("Adding point: ("+x+", "+y+")");

        if (dataViewer != null) {
        	dataViewer.addPoint(id, x, y, color);
        }
        if (resultWriter != null) {
        	resultWriter.addPoint(id, x, y);
        }
    }
}
