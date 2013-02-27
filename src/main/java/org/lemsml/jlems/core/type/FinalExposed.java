package org.lemsml.jlems.core.type;

import org.lemsml.jlems.core.expression.Dimensional;
import org.lemsml.jlems.core.expression.Valued;
import org.lemsml.jlems.core.logging.E;

public class FinalExposed implements Valued {

    public String name;
    public Dimension r_dimension;
    double dvalue = Double.NaN;

    public FinalExposed(String nm, Dimension dim) {

        name = nm;
        r_dimension = dim;

    }

    @Override
    public String toString() {
        return name + "(" + r_dimension.getName() + ")";
    }

    public String getName() {
        return name;
    }

    public Dimension getDimension() {
        return r_dimension;
    }

    public double getValue() {
        if (Double.isNaN(dvalue)) {
            E.error("Accessed a value before it has been set? " + name + "(" + r_dimension + ")");
        }
        return dvalue;
    }

    public boolean isFixed() {
        return false;
    }

    public Dimensional getDimensionality() {
        return r_dimension;
    }

    public FinalExposed makeCopy() {
        FinalExposed ret = new FinalExposed(getName(), getDimension());
        return ret;
    }
}
