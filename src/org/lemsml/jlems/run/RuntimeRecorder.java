package org.lemsml.jlems.run;

import org.lemsml.jlems.display.DataViewer;
import org.lemsml.jlems.sim.RunnableAccessor;
import org.lemsml.jlems.util.ContentError;
import org.lemsml.jlems.util.RuntimeError;


public class RuntimeRecorder {

	String id;
	String quantity;
 	String color;
	String display;
	 
	  StateWrapper stateWrapper;

	  DataViewer dataViewer;
	  
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

    public void appendState(double ft) throws ContentError, RuntimeError {

        double x = ft / tscale;
        double y = stateWrapper.getValue() / yscale;
        //E.info("Adding point: ("+x+", "+y+")");
        dataViewer.addPoint(id, x, y, color);
       
    }
}
