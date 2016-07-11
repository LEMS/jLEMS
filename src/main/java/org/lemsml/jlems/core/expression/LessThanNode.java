package org.lemsml.jlems.core.expression;

import java.util.HashMap;

import org.lemsml.jlems.core.eval.AbstractBComp;
import org.lemsml.jlems.core.eval.LTComp;
import org.lemsml.jlems.core.sim.ContentError;

public class LessThanNode extends AbstractComparisonNode {

    public static final String SYMBOL = ".lt.";

    public LessThanNode() {
        super(SYMBOL);
    }

    @Override
    public LessThanNode copy() {
        return new LessThanNode();
    }

    @Override
    public int getPrecedence() {
        return 10;
    }

    @Override
    public boolean compare(double x, double y) {
        return (x < y);
    }

    @Override
    public AbstractBComp makeEvaluable(HashMap<String, Double> fixedHM) throws ContentError {
        checkLeftRight();
        return new LTComp(leftEvaluable.makeEvaluable(fixedHM), rightEvaluable.makeEvaluable(fixedHM));
    }

    @Override
    public boolean compareInts(long ix, long iy) {
        return (ix < iy);
    }

    @Override
    public void doVisit(ExpressionVisitor ev) throws ContentError {
        checkLeftRight();
        ev.visitLessThanNode(leftEvaluable, rightEvaluable);
    }

}
