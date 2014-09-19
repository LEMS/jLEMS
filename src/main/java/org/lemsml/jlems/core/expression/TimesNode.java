package org.lemsml.jlems.core.expression;

import java.util.HashMap;

import org.lemsml.jlems.core.eval.AbstractDVal;
import org.lemsml.jlems.core.eval.Times;
import org.lemsml.jlems.core.sim.ContentError;

public class TimesNode extends AbstractFloatResultNode {

    public static final String SYMBOL = "*";

    public TimesNode() {
        super(SYMBOL);
    }

    @Override
    public TimesNode copy() {
        return new TimesNode();
    }

    @Override
    public int getPrecedence() {
        return 3;
    }

    @Override
    public double op(double x, double y) {
        return x * y;
    }

    @Override
    public AbstractDVal makeEvaluable(HashMap<String, Double> fixedHM) throws ContentError {
        checkLeftRight();
        return new Times(leftEvaluable.makeEvaluable(fixedHM), rightEvaluable.makeEvaluable(fixedHM));
    }

    @Override
    public Dimensional dimop(Dimensional dl, Dimensional dr) {
        Dimensional ret = dl.getTimes(dr);
        return ret;
    }

    @Override
    public void doVisit(ExpressionVisitor ev) throws ContentError {
        checkLeftRight();
        ev.visitTimesNode(leftEvaluable, rightEvaluable);
    }

}
