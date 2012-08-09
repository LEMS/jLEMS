package org.lemsml.run;
 

import org.lemsml.display.LineDisplay;
import org.lemsml.sim.RunnableAccessor;
import org.lemsml.util.ContentError;
import org.lemsml.util.RuntimeError;
 

public class DisplayItem {

    String id;
    String path;
    double tscale;
    double yscale;
    String color;
    StateWrapper stateWrapper;
   
 
    public DisplayItem(String sid, String p, double tf, double yf, String col) {
        id = sid;
        path = p;
        tscale = tf;
        yscale = yf;
        color = col;
     }

    @Override
    public String toString() {
        return "DisplayItem{id=" + id + ", path=" + path + ", tscale=" + tscale + ", yscale=" + yscale + ", color=" + color + '}';
    }


 
    public String getId() {
        return id;
    }

    public String getColor() {
        return color;
    }

    public double getTscale() {
        return tscale;
    }

    public double getYscale() {
        return yscale;
    }

    public String getInfo() {
        return "<b>" + getId() + ": " + path + "</b>, x scale: " + tscale + ", y scale: " + yscale;
    }

    public void connectRunnable(RunnableAccessor ra) throws ConnectionError {
        stateWrapper = ra.getStateWrapper(path);
        if (stateWrapper == null) {
            throw new ConnectionError("unable to access state variable: " + path);
        }
    }

    public void appendState(double ft, LineDisplay sv) throws ContentError, RuntimeError {

        double x = ft / tscale;
        double y = stateWrapper.getValue() / yscale;
        //E.info("Adding point: ("+x+", "+y+")");
        sv.addPoint(getInfo(), x, y, color);
       
    }

     
}
