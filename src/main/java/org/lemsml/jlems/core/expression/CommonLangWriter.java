/**
 * 
 */
package org.lemsml.jlems.core.expression;

import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.sim.ContentError;

/**
 * @author boris
 *
 */
public class CommonLangWriter implements ExpressionVisitor {
	StringBuilder sb;

	private String _argStart = "(";
	private String _argEnd = ")";
	private String _mult = " * ";
	private String _add = " + ";
	private String _sub = " - ";
	private String _div = " / ";
	private String _pow = " ^ ";
	private String _mod = " % ";


	public String serialize(ParseTree pt) throws ContentError {
		sb = new StringBuilder();
		pt.visitAll(this);
		return sb.toString();
	}

	public void visitVariable(String svar) {
		sb.append(svar);
	}


	public void visitFunctionNode(String fname, DoubleParseTreeNode argEvaluable) throws ContentError {
		sb.append(fname);
		sb.append(getArgStart());
		argEvaluable.doVisit(this);
		sb.append(getArgEnd());
	}


	public void visitConstant(double dval) {
		sb.append(dval);
	}
	

	protected void visitOp(String opname, DoubleParseTreeNode leftEvaluable, DoubleParseTreeNode rightEvaluable) throws ContentError {
		sb.append(getArgStart());
		if (leftEvaluable != null) {
			leftEvaluable.doVisit(this);
		}
		sb.append(opname);
		rightEvaluable.doVisit(this);
		sb.append(getArgEnd());
	}
	

	public void visitPlusNode(DoubleParseTreeNode leftEvaluable, DoubleParseTreeNode rightEvaluable) throws ContentError {
		visitOp(getAdd(), leftEvaluable, rightEvaluable);
	}


	public void visitTimesNode(DoubleParseTreeNode leftEvaluable, DoubleParseTreeNode rightEvaluable) throws ContentError {
		visitOp(getMult(), leftEvaluable, rightEvaluable);
	}


	public void visitPowerNode(DoubleParseTreeNode leftEvaluable, DoubleParseTreeNode rightEvaluable) throws ContentError {
		visitOp(getPow(), leftEvaluable, rightEvaluable);
	}


	public void visitMinusNode(DoubleParseTreeNode leftEvaluable, DoubleParseTreeNode rightEvaluable) throws ContentError {
		visitOp(getSub(), leftEvaluable, rightEvaluable);
	}


	public void visitUnaryMinusNode(DoubleParseTreeNode rightEvaluable) throws ContentError {
		visitOp(getSub(), null, rightEvaluable);
	}


	public void visitDivideNode(DoubleParseTreeNode leftEvaluable, DoubleParseTreeNode rightEvaluable) throws ContentError {
		visitOp(getDiv(), leftEvaluable, rightEvaluable);
	}

	public void visitModuloNode(DoubleParseTreeNode leftEvaluable, DoubleParseTreeNode rightEvaluable) throws ContentError {
		visitOp(getMod(), leftEvaluable, rightEvaluable);
	}


	public void visitOrNode(OrNode orNode) {
		// TODO Auto-generated method stub
		E.missing();
	}

	public void visitNotEqualsNode(DoubleParseTreeNode leftEvaluable, DoubleParseTreeNode rightEvaluable) {
		// TODO Auto-generated method stub
		E.missing();
	}

	public void visitAndNode(BooleanParseTreeNode leftEvaluable, BooleanParseTreeNode rightEvaluable) {
		// TODO Auto-generated method stub	
		E.missing();
	}

	public void visitLessThanOrEqualsNode(DoubleParseTreeNode leftEvaluable, DoubleParseTreeNode rightEvaluable) {
		// TODO Auto-generated method stub	
		E.missing();
	}

	public void visitLessThanNode(DoubleParseTreeNode leftEvaluable, DoubleParseTreeNode rightEvaluable) {
		// TODO Auto-generated method stub	
		E.missing();
	}

	public void visitGreaterThanOrEqualsNode(DoubleParseTreeNode leftEvaluable, DoubleParseTreeNode rightEvaluable) {
		// TODO Auto-generated method stub
		E.missing();
	}


	public void visitGreaterThanNode(DoubleParseTreeNode leftEvaluable, DoubleParseTreeNode rightEvaluable) {
		E.missing();
	}


	public void visitEqualsNode(DoubleParseTreeNode leftEvaluable, DoubleParseTreeNode rightEvaluable) {
		E.missing();
	}


	/**
	 * @return the _argStart
	 */
	public String getArgStart() {
		return _argStart;
	}


	/**
	 * @param _argStart the _argStart to set
	 */
	public void setArgStart(String _argStart) {
		this._argStart = _argStart;
	}


	/**
	 * @return the _argEnd
	 */
	public String getArgEnd() {
		return _argEnd;
	}


	/**
	 * @param _argEnd the _argEnd to set
	 */
	public void setArgEnd(String _argEnd) {
		this._argEnd = _argEnd;
	}


	/**
	 * @return the _mult
	 */
	public String getMult() {
		return _mult;
	}


	/**
	 * @param _mult the _mult to set
	 */
	public void setMult(String _mult) {
		this._mult = _mult;
	}


	/**
	 * @return the _add
	 */
	public String getAdd() {
		return _add;
	}


	/**
	 * @param _add the _add to set
	 */
	public void setAdd(String _add) {
		this._add = _add;
	}


	/**
	 * @return the _sub
	 */
	public String getSub() {
		return _sub;
	}


	/**
	 * @param _sub the _sub to set
	 */
	public void setSub(String _sub) {
		this._sub = _sub;
	}


	/**
	 * @return the _div
	 */
	public String getDiv() {
		return _div;
	}


	/**
	 * @param _div the _div to set
	 */
	public void setDiv(String _div) {
		this._div = _div;
	}


	/**
	 * @return the _pow
	 */
	public String getPow() {
		return _pow;
	}


	/**
	 * @param _pow the _pow to set
	 */
	public void setPow(String _pow) {
		this._pow = _pow;
	}


	/**
	 * @return the _mod
	 */
	public String getMod() {
		return _mod;
	}


	/**
	 * @param _mod the _mod to set
	 */
	public void setMod(String _mod) {
		this._mod = _mod;
	}

}

