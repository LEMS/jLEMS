package org.lemsml.jlems.core.expression;

import java.util.HashMap;

import org.lemsml.jlems.core.eval.AbstractDVal;
import org.lemsml.jlems.core.eval.DCon;
import org.lemsml.jlems.core.sim.ContentError;

public class ConstantNode extends Node implements DoubleParseTreeNode {

    String sval = null;

    double dval;

    public ConstantNode(String s) {
        super();

        sval = s;
        dval = Double.parseDouble(s);
    }

    @Override
    public String toString() {
        return "{Constant: " + sval + "}";
    }

    @Override
    public String toExpression() {

        return dval + "";
    }

    public double evalD(HashMap<String, Double> valHS) {
        return dval;
    }

    @Override
    public AbstractDVal makeEvaluable(HashMap<String, Double> fixedHM) {
        return new DCon(dval);
    }

    public double getDoubleValue() {
        return dval;
    }

    @Override
    public Dimensional getDimensionality(HashMap<String, Dimensional> dimHM) throws ContentError {
        ExprDimensional ed = new ExprDimensional();
        if (dval == 0) {
            ed.setZero();
        }
        ed.setDoubleValue(dval);
        return ed;
    }

    @Override
    public Dimensional evaluateDimensional(HashMap<String, Dimensional> dhm) throws ContentError {
        throw new ContentError("Can't use constants with  dimensions");
    }

    @Override
    public void substituteVariables(HashMap<String, String> varHM) throws ContentError {
        if (varHM.containsKey(sval)) {
            sval = varHM.get(sval);
        }

    }

    @Override
    public void doVisit(ExpressionVisitor ev) throws ContentError {
        ev.visitConstant(dval);

    }

}
