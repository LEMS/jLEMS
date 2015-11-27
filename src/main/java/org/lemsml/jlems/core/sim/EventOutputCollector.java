package org.lemsml.jlems.core.sim;

import java.util.ArrayList;
import org.lemsml.jlems.core.run.RuntimeEventOutput;
import org.lemsml.jlems.core.run.StateType;

public class EventOutputCollector implements StateTypeVisitor
{

    ArrayList<RuntimeEventOutput> outputs;

    public EventOutputCollector(ArrayList<RuntimeEventOutput> al)
    {
        outputs = al;
    }

    @Override
    public void visit(StateType cb)
    {
        ArrayList<RuntimeEventOutput> a = cb.getRuntimeEventOutputs();
        if (a != null)
        {
            outputs.addAll(a);
        }

    }

}
