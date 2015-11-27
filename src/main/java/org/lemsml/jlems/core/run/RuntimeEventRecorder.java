package org.lemsml.jlems.core.run;

//import org.lemsml.jlems.core.display.DataViewer;
import org.lemsml.jlems.core.out.ResultWriter;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.sim.RunnableAccessor;

public class RuntimeEventRecorder
{

    String id;
    String quantity;
    //String color;
    //String display;

    StateWrapper stateWrapper;

    // on or the other of these will be set - maybe common interface?
    //DataViewer dataViewer;
    ResultWriter resultWriter;

    //double tscale;
    //double yscale;

    public RuntimeEventRecorder(String aid, String q)
    {
        id = aid;
        quantity = q;
        //tscale = tsc;
        //yscale = ysc;
        //color = col;
        //display = d;
    }

    @Override
    public String toString()
    {
        return "RuntimeEventRecorder, " + id + " of " + quantity;
    }

    public String getID()
    {
        return id;
    }
    
    /*
    public String getDisplay()
    {
        return display;
    }

    
    public void connectRunnable(RunnableAccessor ra, DataViewer dv) throws ConnectionError, ContentError
    {
        if (quantity == null)
        {
            throw new ConnectionError("Recorder has null quantity " + toString());
        }
        stateWrapper = ra.getStateWrapper(quantity);
        if (stateWrapper == null)
        {
            throw new ConnectionError("unable to access state variable: " + quantity);
        }
        dataViewer = dv;
    }*/

    public void connectRunnable(RunnableAccessor ra, ResultWriter rw) throws ConnectionError, ContentError
    {
        if (quantity == null)
        {
            throw new ConnectionError("Recorder has null quantity " + toString());
        }
        stateWrapper = ra.getStateWrapper(quantity);
        if (stateWrapper == null)
        {
            throw new ConnectionError("unable to access state variable: " + quantity);
        }
        resultWriter = rw;
    }

    public void appendState(double ft) throws ContentError, RuntimeError
    {

        double x = ft / 1;
        double y = stateWrapper.getValue() / 1;
        // E.info("Adding point: ("+x+", "+y+")");

        if (resultWriter != null)
        {
            resultWriter.addPoint(id, x, y);
        }
    }

    public String getQuantity()
    {
        return quantity;
    }
}
