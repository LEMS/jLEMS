package org.lemsml.jlems.core.expression;

import java.util.HashMap;

import org.lemsml.jlems.core.eval.AbstractBComp;
import org.lemsml.jlems.core.eval.EQComp;
import org.lemsml.jlems.core.run.RuntimeError;
import org.lemsml.jlems.core.sim.ContentError;

public class EqualsNode extends AbstractComparisonNode {

    public static final String SYMBOL = ".eq.";

    public EqualsNode() {
        super(SYMBOL);
    }

    @Override
    public EqualsNode copy() {
        return new EqualsNode();
    }

    @Override
    public int getPrecedence() {
        return 20;
    }

    @Override
    public boolean compare(double x, double y) throws RuntimeError {
        throw new RuntimeError("Called equals comparison of doubles");
    }

    @Override
    public AbstractBComp makeEvaluable(HashMap<String, Double> fixedHM) throws ContentError {
        checkLeftRight();
        return new EQComp(leftEvaluable.makeEvaluable(fixedHM), rightEvaluable.makeEvaluable(fixedHM));
    }

    @Override
    public boolean compareInts(long ix, long iy) {
        return (ix == iy);
    }

    @Override
    public void doVisit(ExpressionVisitor ev) throws ContentError {
        checkLeftRight();
        ev.visitEqualsNode(leftEvaluable, rightEvaluable);
    }

}
