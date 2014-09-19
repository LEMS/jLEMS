package org.lemsml.jlems.core.expression;

import java.util.HashMap;

import org.lemsml.jlems.core.eval.AbstractBVal;
import org.lemsml.jlems.core.eval.And;
import org.lemsml.jlems.core.sim.ContentError;

public class AndNode extends AbstractBooleanOperatorNode {

    public static final String SYMBOL = ".and.";

    public AndNode() {
        super(SYMBOL);
    }

    @Override
    public AndNode copy() {
        return new AndNode();
    }

    @Override
    public int getPrecedence() {
        return 20;// TODO: check..
    }

    @Override
    public boolean bool(boolean x, boolean y) {
        return x && y;
    }

    @Override
    public AbstractBVal makeEvaluable(HashMap<String, Double> fixedHM) throws ContentError {
        checkLeftRight();
        return new And(leftEvaluable.makeEvaluable(fixedHM), rightEvaluable.makeEvaluable(fixedHM));
    }

    @Override
    public void checkDimensions(HashMap<String, Dimensional> dimHM) throws ContentError {
        getDimensionality(dimHM);

    }

    @Override
    public void doVisit(ExpressionVisitor ev) throws ContentError {
        checkLeftRight();
        ev.visitAndNode(leftEvaluable, rightEvaluable);

    }
}
