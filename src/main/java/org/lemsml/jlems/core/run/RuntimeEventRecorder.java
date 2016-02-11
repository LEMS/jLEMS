package org.lemsml.jlems.core.run;

//import org.lemsml.jlems.core.display.DataViewer;
import org.lemsml.jlems.core.out.EventResultWriter;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.sim.RunnableAccessor;

public class RuntimeEventRecorder
{

    String id;
    String quantity;
    String eventPort;
    String parent;

    StateWrapper stateWrapper;
    InPortRecorder inPort;

    EventResultWriter eventResultWriter;

    public RuntimeEventRecorder(String id, String quantity, String eventPort, String parent)
    {
        this.id = id;
        this.quantity = quantity;
        this.eventPort = eventPort;
        this.parent = parent;
    }

    @Override
    public String toString()
    {
        return "RuntimeEventRecorder, " + id + " of " + quantity+ ", port: "+eventPort;
    }

    public String getID()
    {
        return id;
    }
    
    public String getParent()
    {
        return parent;
    }

    public void connectRunnable(RunnableAccessor ra, EventResultWriter erw) throws ConnectionError, ContentError
    {
        //System.out.println("Connecting "+ra.toString()+" (?) to "+erw.getID());
        if (quantity == null)
        {
            throw new ConnectionError("Recorder has null quantity " + toString());
        }
        StateRunnable si = ra.getStateInstance(quantity);
        if (si == null)
        {
            throw new ConnectionError("unable to access state variable: " + quantity);
        }
        OutPort op = si.getOutPort(eventPort);
        inPort = new InPortRecorder(id,erw);
        op.connectTo(inPort);
        eventResultWriter = erw;
    }
   

    public String getQuantity()
    {
        return quantity;
    }
}
