package org.lemsml.jlems.core.expression;

import java.util.HashMap;

import org.lemsml.jlems.core.eval.AbstractDVal;
import org.lemsml.jlems.core.eval.Divide;
import org.lemsml.jlems.core.sim.ContentError;

public class DivideNode extends AbstractFloatResultNode {

    public static final String SYMBOL = "/";

    public DivideNode() {
        super(SYMBOL);
    }

    @Override
    public DivideNode copy() {
        return new DivideNode();
    }

    @Override
    public int getPrecedence() {
        return 2;
    }

    @Override
    public double op(double x, double y) {
        return (Double.isNaN(x) ? 1 : x) / y;
    }

    @Override
    public AbstractDVal makeEvaluable(HashMap<String, Double> fixedHM) throws ContentError {
        checkLeftRight();
        return new Divide(leftEvaluable.makeEvaluable(fixedHM), rightEvaluable.makeEvaluable(fixedHM));
    }

    @Override
    public Dimensional dimop(Dimensional dl, Dimensional dr) {
        return dl.getDivideBy(dr);
    }

    @Override
    public void doVisit(ExpressionVisitor ev) throws ContentError {
        checkLeftRight();
        ev.visitDivideNode(leftEvaluable, rightEvaluable);
    }

}
