package org.lemsml.run;

import org.lemsml.display.DataViewer;

import org.lemsml.sim.RunnableAccessor;
import org.lemsml.util.ContentError;
import org.lemsml.util.RuntimeError;

public class RuntimeRecorder {

	String id;
	String quantity;
	String scale;
	String color;
	String display;
	 
	  StateWrapper stateWrapper;

	  DataViewer dataViewer;
	  
	  double tscale;
	  double yscale;
	  

	public RuntimeRecorder(String aid, String q, String sc, String col, String d) {
		 id = aid;
		 quantity = q;
		 scale = sc;
		 color = col;
		 display = d;
	}


	public String getID() {
		return id;
	}


	public String getDisplay() {
		return display;
	}

 

	public void connectRunnable(RunnableAccessor ra, DataViewer dataViewer) throws ConnectionError {
        stateWrapper = ra.getStateWrapper(quantity);
        if (stateWrapper == null) {
            throw new ConnectionError("unable to access state variable: " + quantity);
        }
    }

    public void appendState(double ft) throws ContentError, RuntimeError {

        double x = ft / tscale;
        double y = stateWrapper.getValue() / yscale;
        //E.info("Adding point: ("+x+", "+y+")");
        dataViewer.addPoint(id, x, y, color);
       
    }
}
