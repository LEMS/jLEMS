package org.lemsml.jlems.core.run;

import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.out.EventResultWriter;

public class InPortRecorder implements InPortReceiver
{

    String name;
    EventResultWriter erw;

    public InPortRecorder(String name, EventResultWriter erw)
    {

        this.name = name;
        this.erw = erw;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public void receive() throws RuntimeError
    {
        //E.info("Receiving event on port " + name);
        erw.recordEvent(name);
    }

}
