package org.lemsml.run;
 
import java.util.ArrayList;

import org.lemsml.behavior.Behavior;
import org.lemsml.behavior.Record;
import org.lemsml.behavior.Show;
import org.lemsml.display.DataViewer;
import org.lemsml.sim.RunnableAccessor;
import org.lemsml.type.Component;
import org.lemsml.util.ContentError;
import org.lemsml.util.RuntimeError;


public class RunDisplay {

    StateRunnable stateRunnable;
    ArrayList<DisplayItem> displayItems = new ArrayList<DisplayItem>();
 

    public RunDisplay(StateRunnable sr) {
        stateRunnable = sr;
    }

    @Override
    public String toString() {
        return "RunDisplay for " + stateRunnable.getID() + " with disp items: " + displayItems;
    }

  

    public void appendState(double t, DataViewer sv) throws ContentError, RuntimeError {
        for (DisplayItem dit : displayItems) {
            dit.appendState(t, sv);
        }
    }

    

    public ArrayList<DisplayItem> getDisplayItems() {
        return displayItems;
    }

   

    public void addShow(Component cpt, Show show) throws ContentError {
        addShow(cpt, show, 1.0);
    }

    public void addShow(Component cpt, Show show, double defscl) throws ContentError {
        double dscale = defscl;
        if (show.scale != null) {
            dscale = cpt.getParamValue(show.scale).getDoubleValue();
        }
        if (cpt.hasChildrenAL(show.src)) {
            ArrayList<Component> alc = cpt.getChildrenAL(show.src);

            for (Component c : alc) {
                Behavior b = c.getComponentType().getBehavior();
                //System.out.println("c: "+c);

                if (b != null) {
                    for (Show sub : b.shows) {
                        addShow(c, sub, dscale);
                    }
                    for (Record r : b.records) {
                        addRecord(c, r, dscale);
                    }
                }
            }
        }
    }

    private void addRecord(Component c, Record r, double tscale) throws ContentError {
        double yscale = c.getParamValue(r.scale).getDoubleValue();
        String color = c.getStringValue(r.color);
        String path = c.getStringValue(r.quantity);

        DisplayItem dit = new DisplayItem(c.getID(), path, tscale, yscale, color);
 

        // TODO need reimplementing outside of the display structure: getStringValue should throw an exception
        // if the value isn't there, not ignore it silently
        // String save = c.getStringValue(r.save);
        // dit.setSave(save);
        
        displayItems.add(dit);

    }

    public void connectRunnable(RunnableAccessor ra) throws ConnectionError {
        for (DisplayItem dit : displayItems) {

            dit.connectRunnable(ra);
        }
    }
}
