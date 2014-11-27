package org.lemsml.jlems.core.eval;

public class And extends AbstractBOp {

    public And(AbstractBVal dvl, AbstractBVal dvr) {
        super(dvl, dvr);
    }

    @Override
    public And makeCopy() {
        return new And(left.makeCopy(), right.makeCopy());
    }

    @Override
    public boolean eval() {
        return left.eval() && right.eval();
    }

    @Override
    public String toString() {
        return "(" + left + " AND " + right + ")";
    }

    @Override
    public String toExpression() {
        return "(" + left.toExpression() + " && " + right.toExpression() + ")";
    }

    @Override
    public String toLemsExpression() {
        return "(" + left.toExpression() + " .and. " + right.toExpression() + ")";
    }

}
