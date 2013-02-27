package org.lemsml.jlems.core.type;

import org.lemsml.jlems.core.annotation.ModelElement;
import org.lemsml.jlems.core.annotation.ModelProperty;
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.sim.ContentError;

@ModelElement(info = "A port on a component that can send or receive events, depending on the direction speicfied.")

public class EventPort implements Named {

    @ModelProperty(info = "")
    public String name;
    @ModelProperty(info = "'IN' or 'OUT'")
    public String direction;
    public String description;
    
    public Dimension r_dimension;
    public final static int IN = 1;
    public final static int OUT = 2;
    private int dir = 0;

    public EventPort() {
    	// TODO - only one constructor
    }

    public EventPort(String sn) {
        name = sn;
    }

    public EventPort(String sn, String sd, int d) {
        name = sn;
        direction = sd;
        dir = d;
    }

    public void setDirection(int d) {
        dir = d;
        if (dir == IN) {
            direction = "in";
        } else if (dir == OUT) {
            direction = "out";
        }
    }

    @Override
    public String toString() {
        String sret = "EventPort " + name + " ";
        if (dir == IN) {
            sret += "IN";
        } else if (dir == OUT) {
            sret += "OUT";
        } else {
            sret += "UNDEFINED";
        }
        return sret;
    }

    public void resolve(LemsCollection<Dimension> dimensions) throws ContentError {
        if (direction.equals("in") || direction.equals("receive")) {
            dir = IN;
        } else if (direction.equals("out") || direction.equals("send")) {
            dir = OUT;
        } else {
            E.error("unrecognized direction: " + direction);
        }
    }

    public String getName() {
        return name;
    }

    public boolean isDirectionIn() {
        boolean ret = false;
        if (dir == IN) {
            ret = true;
        }
        return ret;
    }

    public boolean isDirectionOut() {
        boolean ret = false;
        if (dir == OUT) {
            ret = true;
        }
        return ret;
    }

    public EventPort makeCopy() {
        return new EventPort(name, direction, dir);
    }

  
}
