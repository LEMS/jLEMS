package org.lemsml.jlems.core.expression;

import java.util.HashMap;

import org.lemsml.jlems.core.eval.AbstractBComp;
import org.lemsml.jlems.core.eval.NEQComp;
import org.lemsml.jlems.core.run.RuntimeError;
import org.lemsml.jlems.core.sim.ContentError;

public class NotEqualsNode extends AbstractComparisonNode {

    public static final String SYMBOL = ".neq.";

    public NotEqualsNode() {
        super(SYMBOL);
    }

    @Override
    public NotEqualsNode copy() {
        return new NotEqualsNode();
    }

    @Override
    public int getPrecedence() {
        return 10;
    }

    @Override
    public boolean compare(double x, double y) throws RuntimeError {
        throw new RuntimeError("Called not equals comparison of doubles");
    }

    @Override
    public AbstractBComp makeEvaluable(HashMap<String, Double> fixedHM) throws ContentError {
        checkLeftRight();
        return new NEQComp(leftEvaluable.makeEvaluable(fixedHM), rightEvaluable.makeEvaluable(fixedHM));
    }

    @Override
    public boolean compareInts(long ix, long iy) {
        return (ix != iy);
    }

    @Override
    public void doVisit(ExpressionVisitor ev) throws ContentError {
        checkLeftRight();
        ev.visitNotEqualsNode(leftEvaluable, rightEvaluable);
    }

}
