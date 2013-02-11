package org.lemsml.jlems.type;

import java.util.ArrayList;
import java.util.HashMap;

import org.lemsml.jlems.annotation.ModelProperty;
import org.lemsml.jlems.expression.ParseError;
import org.lemsml.jlems.expression.ParseTree;
import org.lemsml.jlems.logging.E;
import org.lemsml.jlems.run.RuntimeType;
import org.lemsml.jlems.run.StateType;
import org.lemsml.jlems.sim.ContentError;
import org.lemsml.jlems.xml.XMLAttribute;
import org.lemsml.jlems.xml.XMLElement;


public class Component implements Attributed, IDd, Summaried, Namable, Parented {

	
	// TODO RCC - I don't think these should be here. The caller should know about the keywords.
	
    public static final String THIS_COMPONENT = "this";
    public static final String PARENT_COMPONENT = "parent";
 
    
	@ModelProperty(info="")
	public String id;

	// name is just used if the parent component contains <xyz type="abc".../>
	// in which case the
	// element is instantiated, called "xyz", and added to the components list
	
	@ModelProperty(info="Name by which the component was declared - this shouldn't be accessible.")
	public String name;
 
	
	@ModelProperty(info="")
	public String type;
	public ComponentType r_type;

	@ModelProperty(info="")
	public String eXtends;

	public LemsCollection<Attribute> attributes = new LemsCollection<Attribute>();

	public LemsCollection<ParamValue> paramValues;

	public LemsCollection<Insertion> insertions = new LemsCollection<Insertion>();
		
	public LemsCollection<Component> components = new LemsCollection<Component>();
	
	@ModelProperty(info="Metadata about a model can be included anywhere by wrapping it in an About element, though this " +
			"is not necessary: LEMS does not use the body text of XML elements itself, so this is free for the " +
			"modeler to include descriptive text or other markup of thier own.")
	public LemsCollection<About> abouts = new LemsCollection<About>();
 
	@ModelProperty(info="Structured metadata can be put in Meta elements. The content is read into a generic xml data structure. " +
			"Other tools can then do their own thing with it. Each Meta element should set the context attribute, so " +
			"tools can use the getMeta(context) method to retrieve elements that match a particular context.")
	public LemsCollection<Meta> metas = new LemsCollection<Meta>();
	 
	public double xPosition;
	public double yPosition;
	
	
	final HashMap<String, TextParam> textParamHM = new HashMap<String, TextParam>();

	HashMap<String, Component> childHM;

     HashMap<String, Component> refHM;

     
     ArrayList<Component> freeChildren;
     
	ArrayList<String> childrenNames;
	 HashMap<String, ArrayList<Component>> childrenHM;
 
	
	private boolean resolved = false;

	private boolean evaluatedStatic = false;

	private Component r_parent;

	private boolean madeCB = false;

	private StateType stateType;
 	
	
	private	 Component r_replacement;
	
	 
	// RuntimeType can be a NativeType for code generated components
	private RuntimeType runtimeType;
	
	
	
	public void setID(String s) {
		id = s;
	}

	public void setName(String s) {
		name = s;
	}

	public String getName() {
		return name;
	}
	
	
	public void setType(String s) {
		type = s;
	}

	public void setRuntimeType(RuntimeType rt) {
		runtimeType = rt;
	}
	
	public void setReplacement(Component cpt) {
		r_replacement = cpt;
	}
	
	
	 
	@Override
	public String toString() {
		return "Component(id=" + id + " type=" + type + ")";
	}

	public String summary() {
		return ComponentWriter.summarize(this);
	}
	
	

	public String details(String indent) {
		return ComponentWriter.writeDetails(this, indent);
	}
	
	
	public void addComponent(Component cpt) {
		components.add(cpt);
		cpt.r_parent = this;
	}
	

	public void setParent(Object ob) throws ContentError {
  		if (ob instanceof Lems) {
			// we're a top level component - no parent. Could use this to set a
			// flag... TODO
		} else if (ob instanceof Component) {
			r_parent = (Component) ob;
			
		} else {
            String err = "Setting parent of [" + this + "] to [" + ob +  "] failed\n";
            err += "It should be the root LEMS element or another component. ";
			throw new ContentError(err);
		}
	}

	public void addAttribute(Attribute att) {
		attributes.add(att);
	}

	public String getID() {
		return id;
	}

	public String getUniqueID() {
		String ret = "";
		if (id == null) { 
			ret = getParent().getUniqueID() + "_" + getName();
		} else {
			ret = id;
		}
		return ret;
	}

	public void setType(ComponentType ct) {
		r_type = ct;
		if (type == null) {
			type = r_type.getName();
		}
	}

	public void checkResolve(Lems n, ComponentType parentType) throws ContentError, ParseError {
		// E.info("Checking resolve on "+getID());
		if (!resolved) {
			resolve(n, parentType);
		}
	}

	public String getPathParameterPath(String paramName) throws ContentError {
		String ret = null;

		// MUSTDO - all a bit adhoc
		if (attributes.hasName(paramName)) {
			Attribute att = attributes.getByName(paramName);
			ret = att.getValue();
		} else if (paramName.indexOf("../") == 0) {
			ret = getParent().getPathParameterPath(paramName.substring(3, paramName.length()));
		}
		
		if (ret == null) {
			throw new ContentError("no value specified for parameter " + paramName);
		}

		return ret;
	}
	
	public void resolve(Lems lems, ComponentType parentType) throws ContentError, ParseError {
		resolve(lems, parentType, true);
	}
	
	
	public String makeAttributeText() {
		String ret = "";
		for (Attribute a : attributes) {
			ret += a.getName() + "=" + a.getValue() + ", ";
		}
		return ret;
	}
	
	
	
	public void resolve(Lems lems, ComponentType parentType, boolean bwarn) throws ContentError, ParseError {
		if (eXtends != null) {
			if (lems.hasComponent(eXtends)) {
				Component cp = lems.getComponent(eXtends);
				for (Insertion ins : cp.insertions) {
					insertions.add(ins.makeCopy());
				}
			}
		}
			
			
		for (Insertion ins : insertions) {
			Component cpt = lems.getComponent(ins.component);
			if (cpt != null) {
				components.add(cpt);
			}
		}
		
		
		for (Attribute att : attributes) {
			att.clearFlag();
		}

		if (paramValues == null) {
			paramValues = new LemsCollection<ParamValue>();
		}
		if (childHM == null) {
			childHM = new HashMap<String, Component>();
		}
		if (refHM == null) {
			refHM = new HashMap<String, Component>();
		}
		if (childrenHM == null) {
			childrenNames = new ArrayList<String>();
			childrenHM = new HashMap<String, ArrayList<Component>>();
		} else {
			for (String children : childrenNames) {
				for (Component comp : childrenHM.get(children)) {
					comp.checkResolve(lems, parentType);
				}
			}
		}

		// attributes.size());

		Component pinst = null;

		
		if ((eXtends != null && type == null) || (type != null && type.equals("Component"))) {			
			// an Instance without a type attribute - it must extend something else 
		
			if (eXtends != null) {
				if (lems.hasComponent(eXtends)) {
					pinst = lems.getComponent(eXtends);
					pinst.checkResolve(lems, parentType);
					r_type = pinst.getComponentType();

				} else {
					throw new ContentError("no such component " + eXtends + " (needed for proto of " + id);
				}

			} else {
				throw new ContentError("Component " + id + " must set 'type' or 'extends' attributes");
			}
		} else {
			if (type == null && name != null) {
				if (parentType != null) {
					type = parentType.getChildType(name);
				}
				if (type == null) {
					type = name;
				}
			}
			if (type == null) {
				throw new ContentError("No type for " + this + " extends=" + eXtends);
			}
			
			r_type = lems.getComponentTypeByName(type);
			
			if (eXtends != null) {
				if (lems.hasComponent(eXtends)) {
					pinst = lems.getComponent(eXtends);
					pinst.checkResolve(lems, parentType);
					r_type = pinst.getComponentType();
					if (r_type.getName().equals(type)) {
						// as it should be
					} else {
						throw new ContentError("extension of " + id + " from " + pinst + " tries to change type");
					}
					
				} else {
					throw new ContentError("no such component " + eXtends + " (needed for proto of " + id);
				}
			}
		}

		
		
		if (r_type == null) {
			throw new ContentError("no type found for " + id);
		} else {
			r_type.addCpt(this);
		}

		for (FinalParam dp : r_type.getFinalParams()) {
	 		
			ParamValue pv = new ParamValue(dp);
			paramValues.add(pv);
			String pvn = pv.getName();
			String atval = null;

			boolean gotFromProto = false;
			if (pinst != null) {
				ParamValue protoPV = pinst.getParamValue(pv.getName());
				if (protoPV != null) {
					pv.copyFrom(protoPV);
					gotFromProto = true;
				} else {
					E.info("proto params " + pinst.stringParams());
					E.error("proto " + eXtends + " provides no pv for " + pv.getName());
				}
			}

			if (attributes.hasName(pvn)) {
				Attribute att = attributes.getByName(pvn);
				atval = att.getValue();
				att.setFlag();

			} else if (gotFromProto) {
				// OK - the prototype supplied a value

			} else if (dp.hasSValue()) {
				atval = dp.getSValue();

			} else if (dp instanceof DerivedFinalParam) {
				// will populate it later

			} else {
				String msg = "no value supplied for parameter: " + pvn + " in " + this + "\n";
				msg += "Defined attributes: " + makeAttributeText();
				throw new ContentError(msg);
			}
					
			if (atval != null) {
				pv.setValue(atval, lems.getUnits());
			}
		}

		for (ComponentReference cr : r_type.getComponentRefs()) {
 			String crn = cr.getName();
			if (attributes.hasName(crn)) {
				Attribute att = attributes.getByName(crn);
				String attval = att.getValue();
				att.setFlag();
				Component cpt = lems.getComponent(attval);

				if (cpt != null) {
					refHM.put(crn, cpt);

				} else {
					String err = "No such component: " + crn + " " + attval + ", existing:";
					for (Component comp : lems.components) {
						err = err + "\n   " + comp;
					}
					E.error(err);
				}
			} else {
				// can be OK to resolve with dangling refs as long as we will resolve them again
				// before trying to run it
				if (bwarn) {
					E.warning("component reference " + crn + " missing in " + this + " type " + r_type);
				} 
			}
		}

		for (Link lin : r_type.getLinks()) {
			String crn = lin.getName();
			if (attributes.hasName(crn)) {
				Attribute att = attributes.getByName(crn);
				String attval = att.getValue();
				att.setFlag();
				Component cpt = null;

				/*
				 * PathEvaluator pe = new PathEvaluator(); cpt =
				 * pe.getComponent(r_parent, attval);
				 */

				cpt = r_parent.getLocalByID(attval);

				if (cpt != null) {
					refHM.put(crn, cpt);
				} else {
					E.error("The path " + attval + " for attribute '" + crn + "'" +
							"does not match any component relative to my (" + this + ") parent:\n" + 
							this.getParent().details(""));
				}
			} else {
				if (bwarn) {
					throw new ContentError("Component " + this + " must supply a value for link '" + crn + "'");
				}
			}
		}

		for (Component cpt : components) {		
			cpt.checkResolve(lems, r_type);
			String scb = cpt.getName();
			boolean done = false;
			if (scb != null) {
 				if (r_type.hasChild(scb)) {
					childHM.put(scb, cpt);
					done = true;
				}
			}

			if (!done) {
				if (freeChildren == null) {
					freeChildren = new ArrayList<Component>();
				}
				freeChildren.add(cpt);
				
				
				Children children = r_type.getChildren(cpt.getComponentType());
				if (children != null) {
					String st = children.getName();

					if (st == null) {
						throw new ContentError("anon children array " + children);
					}

					if (childrenHM.containsKey(st)) {
						childrenHM.get(st).add(cpt);

					} else {
						ArrayList<Component> acpt = new ArrayList<Component>();
						acpt.add(cpt);
						childrenNames.add(st);
						childrenHM.put(st, acpt);
					}
					done = true;
				}
			}
			if (!done) {
				throw new ContentError("no such child allowed: " + scb);
			}
		}

		for (Path p : r_type.getPaths()) {
			flagAttribute(p.getName());
		}

		for (ComponentTypeReference ctr : r_type.getComponentTypeRefs()) {
			flagAttribute(ctr.getName());
		}

		for (PathParameter pp : r_type.getPathParameters()) {
			flagAttribute(pp.getName());
		}

		for (Text t : r_type.getTexts()) {
			String tnm = t.getName();
			flagAttribute(t.getName());
			if (attributes.hasName(tnm)) {
				textParamHM.put(tnm, new TextParam(tnm, attributes.getByName(tnm).getValue()));
			}
		}

		for (Attribute att : attributes) {
			if (att.flagged()) {
				// fine - we've used it
			} else {
				E.shortWarning("Unused attribute in " + this + ": " + att);
			}
		}

		resolved = true;

		// E.info("--------    "+this.getID()+ ": childrenHM: "+childrenHM+
		// ": paramValues: "+paramValues+ ": textParamHM: "+textParamHM+
		// ": attributes: "+attributes);
	}

	
	
	public String getListName(Component cpt) throws ContentError {
		Children children = r_type.getChildren(cpt.getComponentType());
		String ret = null;
		if (children != null) {
			ret = children.getName();
		}
		if (ret == null) {
			throw new ContentError("No containing lis for " + cpt);
		}
		return ret;
	}
	
	
	
	
	public void addToChildren(String childrenName, Component c) throws ContentError {

		if (childrenHM == null)
        {
			childrenNames = new ArrayList<String>();
			childrenHM = new HashMap<String, ArrayList<Component>>();
        }

		if (childrenHM.containsKey(childrenName)) {
			childrenHM.get(childrenName).add(c);

		} else {
			ArrayList<Component> acpt = new ArrayList<Component>();
			acpt.add(c);
			childrenNames.add(childrenName);
			childrenHM.put(childrenName, acpt);
		}
		// E.info(this.getID()+ ": childrenHM: "+childrenHM);
	}

	public void evaluateStatic(Lems lems) throws ContentError, ParseError {
		if (evaluatedStatic) {
			return;
		}
		HashMap<String, Double> valHM = new HashMap<String, Double>();

		for (ParamValue pv : paramValues) {
			valHM.put(pv.getName(), pv.getDoubleValue());
		}

		for (FinalParam fp : r_type.getFinalParams()) {
			if (fp instanceof DerivedFinalParam) {
				DerivedFinalParam dfp = (DerivedFinalParam) fp;

				double qv = 0.;
				if (dfp.isSelect()) {
					String sel = dfp.getSelect();
					qv = PathEvaluator.getValue(lems, this, sel);

				} else if (dfp.isValue()) {
					// TODO this can be done in the class, not here
					ParseTree pt = lems.getParser().parseExpression(dfp.getValueString());

					qv = pt.makeFloatEvaluator().evalD(valHM);
				}

				if (paramValues.hasName(dfp.getName())) {
					paramValues.getByName(dfp.getName()).setDoubleValue(qv);
				} else {
					paramValues.add(new ParamValue(dfp, qv));
				}
				valHM.put(dfp.getName(), qv);

			}
		}
		evaluatedStatic = true;
		for (Component cpt : components) {
			cpt.evaluateStatic(lems);
		}
	}

	// TODO evaluate phase for components
	/*
	 * for (ExternalQuantity equan : externalQuantitys) { String qn =
	 * equan.getName(); double qv = PathEvaluator.getValue(cpt,
	 * equan.getPath()); ret.addFixed(qn, qv); fixedHM.put(qn, qv); // MUSTDO we
	 * don't need both of these: // either Ext quans shouldn't say they are
	 * fixed and we should use ret.addFixed, or it should // and use
	 * fixedHM.put. }
	 */

	private void flagAttribute(String pn) throws ContentError {
		if (attributes.hasName(pn)) {
			Attribute att = attributes.getByName(pn);
			att.setFlag();
		}
	}

	public LemsCollection<ParamValue> getParamValues() {
		return paramValues;
	}

	private Component getLocalByID(String sid) {
		Component ret = null;
		for (Component c : components) {
			String cid = c.getID();
			if (cid != null && cid.equals(sid)) {
				ret = c;
				break;
			}
		}
		return ret;
	}

	private String stringParams() {
		return paramValues.toString();
	}
	
	public boolean hasParam(String p) throws ContentError {
		boolean ret = false;
		if (p != null && paramValues.hasName(p)) {
			ret = true;
		}
		return ret;
	}
	

	public ParamValue getParamValue(String pvn) throws ContentError {
		// paramValues.getByName(pvn);

		ParamValue ret = null;
		if (pvn.indexOf("/") >= 0) {
			ret = getPathParamValue(pvn.split("/"));
		} else {
			ret = paramValues.getByName(pvn);
		}
		if (ret == null) {
			String warn = "No such param: " + pvn + " on " + getID() + ", existing params:";
			for (ParamValue pv : paramValues) {
				warn = warn + "\n" + pv;
			}
			E.warning(warn + "\n");
		}
		return ret;
	}

	public ComponentType getComponentType() {
		return r_type;
	}

	public void setDeclaredName(String snm) {
		name = snm;
	}
	
	
	public void setTypeName(String scl) {
		type = scl;
	}

	public boolean hasTextParam(String pnm) {
		boolean ret = false;
		if (textParamHM.containsKey(pnm)) {
			ret = true;
		}
		return ret;
	}

	public String getTextParam(String pnm) {
		String ret = null;
		if (textParamHM.containsKey(pnm)) {
			ret = textParamHM.get(pnm).getText();
		} else {
			ret = null;
		}
		return ret;
	}
	
	public String getInheritableTextParam(String pnm) {
		String ret = getTextParam(pnm);
		if (ret == null && r_parent != null) {
			ret = r_parent.getInheritableTextParam(pnm);
		}
		if (ret == null) {
			E.info("Inheritable - no " + pnm + " in " + this);
			E.info("Parent = " + r_parent);
		}
		return ret;
	}
	
	
	public Component getInheritableLinkTarget(String pnm) {
		Component ret = null;
		if (refHM.containsKey(pnm)) {
			ret = refHM.get(pnm);
		} else if (r_parent != null) {
			ret = r_parent.getInheritableLinkTarget(pnm);
		}
		if (ret == null) {
			E.info("Inheritable - no ref " + pnm + " in " + this);
			E.info("Parent = " + r_parent);
		}  
		return ret;
	}
	
	

	public StateType makeStateType() throws ContentError, ParseError {
	
	 
		
		if (madeCB) {
			throw new ContentError("remaking a component behavior that is already made " + id + " " + r_type);
		}

		StateType ret = r_type.makeStateType(this);
		stateType = ret;
		madeCB = true;
		return ret;
	}
	
	
		
	
	
	public StateType makeConsolidatedCoponentBehavior(String knownas) throws ContentError, ParseError {
		StateType cb = getStateType();
	    StateType ret = cb.makeConsolidatedStateType(knownas);
 	    return ret;
	}
	
	
	public RuntimeType getRuntimeType() throws ContentError, ParseError {
		RuntimeType ret = null;
		if (runtimeType != null) {
			ret = runtimeType;
		} else {
			ret = getStateType();
		}
		return ret;
	}
	

	public StateType getStateType() throws ContentError, ParseError {
		StateType ret = null;
	
		if (r_replacement != null) {
			
			if (stateType != null) {
				E.warning("Component " + getID() + " has been replaced after being built. The original " +
						"type may still be in use");
			}
			
			ret = r_replacement.getStateType();
 			
			
		} else {
			if (stateType == null) {
				//	E.info("Building stae type for " + getID());
 				makeStateType();
			}
			ret = stateType;
		}
		return ret;
	}

	
	public boolean hasChildrenAL(String s) {
		boolean ret = false;
		if (childrenHM.containsKey(s)) {
			ret = true;
		}
		return ret;
	}

	public ArrayList<Component> getChildrenAL(String s) {

		ArrayList<Component> ret = childrenHM.get(s);
		if (ret == null) {
			//E.info("No children of class: " + s + " in " + this.getID() + ", valid values: " + childrenHM.keySet());
			ret = new ArrayList<Component>();
		}
		return ret;

	}
	
	
	public HashMap<String, Component> getChildHM() {
		return childHM;
	}
	
	public HashMap<String, Component> getRefHM() {
 		return refHM;
	}

	
	
	public ArrayList<Component> getStrictChildren() {
		ArrayList<Component> comps = new ArrayList<Component>();
		if (childrenHM != null) {
			for (ArrayList<Component> compList : childrenHM.values()) {
				comps.addAll(compList);
			}
		}
		return comps;
	}
	
	public HashMap<String, Component> getRefComponents() {
		return refHM;
	}

	

	public ArrayList<Component> getAllChildren() {
		ArrayList<Component> comps = new ArrayList<Component>();
		comps.addAll(childHM.values());
		for (ArrayList<Component> compList : childrenHM.values()) {
			comps.addAll(compList);
		}
		comps.addAll(refHM.values());
		return comps;
	}

	public boolean hasAttribute(String s) throws ContentError {
		boolean ret = false;
		if (attributes.hasName(s)) {
			ret = true;
		}
		return ret;
	}

	public String getStringValue(String sn) throws ContentError {
		String ret = null;

        //E.info("--- Get string value ("+sn+") on component ref "+this);

        if (sn.equals(THIS_COMPONENT)) {
            return THIS_COMPONENT;
        }

        if (sn.equals(PARENT_COMPONENT)) {
            return PARENT_COMPONENT;
        }
 
		if (refHM.containsKey(sn)) {
			ret = refHM.get(sn).getID();

		} else if (childHM.containsKey(sn)) {
			ret = childHM.get(sn).getID();

		} else if (attributes.getByName(sn) != null) {
			ret = attributes.getByName(sn).getValue();

		} else {
			(new Exception()).printStackTrace();
			throw new ContentError("No such field '"
                    + sn + "' in " + this + "\n" + details(""));
		}

		// which should be the same as attval(sn);
		Attribute att = attributes.getByName(sn);
		if (att != null && att.getValue().equals(ret)) {
			// all well
		} else {
			throw new ContentError("Get string value ("+sn+") mismatch on component ref "+this);
		}
		return ret;
	}

	public Component getParent() {
		return r_parent;
	}

	public Component quietGetChild(String rp) {
		Component ret = null;
		if (childHM.containsKey(rp)) {
			ret = childHM.get(rp);
		} else if (refHM.containsKey(rp)) {
			ret = refHM.get(rp);
		}
		return ret;
	}

	public Component getChild(String rp) throws ContentError {
		Component ret = quietGetChild(rp);
		if (ret == null) {
			String info = "no such child " + rp + " in " + this;
			info = info + "\n - Child: " + childHM;
			info = info + "\n - Children: " + childrenHM;
			info = info + "\n - Refs: " + refHM;
			throw new ContentError(info);
		}
		return ret;
	}

 

	public ParamValue getPathParamValue(String[] bits) throws ContentError {
		ParamValue ret = null;
		if (bits.length == 1) {
			ret = getParamValue(bits[0]);
		} else {
			String[] rbits = new String[bits.length - 1];
			for (int i = 0; i < bits.length - 1; i++) {
				rbits[i] = bits[i + 1];
			}

			Component cpt = getRelativeComponent(bits[0]);
			ret = cpt.getPathParamValue(rbits);
		}
		return ret;
	}

	public Component getRelativeComponent(String nm) throws ContentError {
		Component ret = null;
		if (nm.startsWith("../")) {
            return getParent().getRelativeComponent(nm.substring(3));
        } else if (childHM.containsKey(nm)) {
			ret = childHM.get(nm);
		} else if (refHM.containsKey(nm)) {
			ret = refHM.get(nm);
		} else {
			throw new ContentError("No such relative component: " + nm + " rel to " + this);
		}
		return ret;
	}

	// TODO more general
	public Component getScopeComponent(String at) throws ContentError {
		Component p = this;
		Component ret = null;
		for (int nu = 0; nu < 3; nu++) {
			if (p != null && p.quietGetChild(at) != null) {
				ret = p.quietGetChild(at);
				break;
			} else if (p != null) {
				p = p.getParent();
			} else {
				break;
			}
		}
		if (ret == null) {
			throw new ContentError("Can't locate '" + at + "' relative to " + this);
		}
		return ret;
	}

	public void setParameter(String sa, String sv) {
		attributes.add(new XMLAttribute(sa, sv));
	}

	public String getAbout() {
		String ret = "";
		for (About about : abouts) {
			ret += about.getText();
		}
		return ret;
	}

	public void clear() {
		attributes.clear();
	}

	public HashMap<String, String> getTextParamMap() {
		HashMap<String, String> ret = new HashMap<String, String>();
		for (String s : textParamHM.keySet()) {
			ret.put(s, textParamHM.get(s).getText());
		}
		return ret;
    }

	public String getTypeName() {
		String ret = null;
		if (r_type != null) {
			ret = r_type.getName();
		} else if (type != null) {
			ret = type;
		}  
		return ret;
	}
	
	public String getExtendsName() {
		String ret = null;
		if (eXtends != null) {
			ret = eXtends;
		}
		return ret;
	}

	public LemsCollection<Attribute> getAttributes() {
		return attributes;
	}

	public void setPosition(double x, double y) {
		 xPosition = x;
		 yPosition = y;
	}

	public XMLElement getContextMeta(String ctxt) {
		XMLElement ret = null;
		for (Meta m : metas) {
			if (m.context != null && m.context.equals(ctxt)) {
				ret = m.getXMLValue();
			}
		}
		return ret;
	}
	public void removeChild(Component c) {
		E.missing("Need to remove " + c + " from parent component");
		
	}

	public LemsCollection<Component> getComponents() {
		return components;
	}





	 
}
