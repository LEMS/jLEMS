package org.lemsml.jlems.core.type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.lemsml.jlems.core.annotation.ModelElement;
import org.lemsml.jlems.core.annotation.ModelProperty;
import org.lemsml.jlems.core.expression.Dimensional;
import org.lemsml.jlems.core.expression.ParseError;
import org.lemsml.jlems.core.expression.Parser;
import org.lemsml.jlems.core.expression.Valued;
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.run.Constants;
import org.lemsml.jlems.core.run.StateType;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.type.dynamics.Dynamics;
import org.lemsml.jlems.core.type.dynamics.Equilibrium;
import org.lemsml.jlems.core.type.geometry.Geometry;
import org.lemsml.jlems.core.type.procedure.Procedure;
import org.lemsml.jlems.core.type.simulation.Simulation;
import org.lemsml.jlems.core.type.structure.Structure;
import org.lemsml.jlems.core.xml.XMLElement;

@ModelElement(info="Root element for defining component types.")
	 
@SuppressWarnings("StringConcatenationInsideStringBufferAppend")
public class ComponentType extends Base implements Named, Summaried, Inheritor {

	@ModelProperty(info="The name of the component type. This can be uses as an XML element name in the shorthand form when" +
			"defining components. ")
	public String name;

	@ModelProperty(info="The component type that this type inherits field definitions for, if any")
	public String eXtends;

	public ComponentType r_extends;

	public LemsCollection<Parameter> parameters = new LemsCollection<Parameter>();

	public LemsCollection<IndexParameter> indexParameters = new LemsCollection<IndexParameter>();

	public LemsCollection<DerivedParameter> derivedParameters = new LemsCollection<DerivedParameter>();

	public LemsCollection<PathParameter> pathParameters = new LemsCollection<PathParameter>();

	public LemsCollection<Requirement> requirements = new LemsCollection<Requirement>();

	public LemsCollection<ComponentRequirement> componentRequirements = new LemsCollection<ComponentRequirement>();

	public LemsCollection<InstanceRequirement> instanceRequirements = new LemsCollection<InstanceRequirement>();

	
	public LemsCollection<Exposure> exposures = new LemsCollection<Exposure>();

	public LemsCollection<Child> childs = new LemsCollection<Child>();

	public LemsCollection<Children> childrens = new LemsCollection<Children>();

	public LemsCollection<Link> links = new LemsCollection<Link>();

	public LemsCollection<ComponentReference> componentReferences = new LemsCollection<ComponentReference>();

	public LemsCollection<ComponentTypeReference> componentTypeReferences = new LemsCollection<ComponentTypeReference>();

	
	public LemsCollection<Location> locations = new LemsCollection<Location>();
	
	
	public LemsCollection<Property> propertys = new LemsCollection<Property>();
 	
	public LemsCollection<Dynamics> dynamicses = new LemsCollection<Dynamics>();
 	
	public LemsCollection<Structure> structures = new LemsCollection<Structure>();
	
	public LemsCollection<Simulation> simulations = new LemsCollection<Simulation>();
 	
	public LemsCollection<Equilibrium> equilibriums  = new LemsCollection<Equilibrium>();
	
	public LemsCollection<Procedure> procedures = new LemsCollection<Procedure>();
	
	public LemsCollection<Geometry> geometrys = new LemsCollection<Geometry>();
 	
	
	public LemsCollection<Fixed> fixeds = new LemsCollection<Fixed>();

	public LemsCollection<Constant> constants = new LemsCollection<Constant>();

	public LemsCollection<Attachments> attachmentses = new LemsCollection<Attachments>();

	public LemsCollection<EventPort> eventPorts = new LemsCollection<EventPort>();

	public LemsCollection<Path> paths = new LemsCollection<Path>();

	public LemsCollection<Text> texts = new LemsCollection<Text>();

	public LemsCollection<Collection> collections = new LemsCollection<Collection>();

	public LemsCollection<PairCollection> pairCollections = new LemsCollection<PairCollection>();

	 	
	
	
	 	
	private final LemsCollection<Component> cpts = new LemsCollection<Component>();

	private final LemsCollection<FinalParam> finalParams = new LemsCollection<FinalParam>();
	
	private final LemsCollection<InstanceProperty> instancePropertys = new LemsCollection<InstanceProperty>();

	private final LemsCollection<FinalExposed> finalExposeds = new LemsCollection<FinalExposed>();
	
	
	
	
	
	@ModelProperty(info="Metadata about a component type can be included anywhere by wrapping it in an About element, though this " +
			"is not necessary: LEMS does not use the body text of XML elements itself, so this is free for the " +
			"modeler to include descriptive text or other markup of thier own.")
	public LemsCollection<About> abouts = new LemsCollection<About>();

	@ModelProperty(info="Structured metadata can be put in Meta elements. The content is read into a generic xml data structure. " +
			"Other tools can then do their own thing with it. Each Meta element should set the context attribute, so " +
			"tools can use the getMeta(context) method to retrieve elements that match a particular context.")
	public LemsCollection<Meta> metas = new LemsCollection<Meta>();
	
	public boolean standalone;
	
	boolean resolved = false;

	 
	public ComponentType() {
		super();
		
	}
	 
	public ComponentType(String s) {
		name = s;
		
	}
	
	
	protected void setName(String s) {
		name = s;
	}

    @Override
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "ComponentType, name=" + summary();
	}

    @Override
	public String summary() {
		StringBuilder sb = new StringBuilder();
		sb.append(name);
		if (description != null) {
			sb.append(" (" + description + ")\n");
		}
		if (r_extends != null) {
			sb.append(" extends " + r_extends.getName() + "\n");
		}
		for (FinalParam fp : finalParams) {
			String sv = fp.getSValue();
			sb.append(fp.getName() + " (" + fp.getDimension().getName() + ") "
					+ (sv != null ? " = " + sv : "") + "\n");
		}

		return sb.toString();
	}

	
	
	
	public String getAbout() {
		String ret = "";
		for (About about : abouts) {
			ret += about.getText();
		}
		return ret;
	}
	
	
	public boolean isExtension() {
		boolean ret = false;
		if (this.r_extends != null) {
			ret = true;
		}
		return ret;
	}
 

	public boolean isOrExtends(String typeName) {
		boolean ret = false;
		if (getName().equals(typeName)) {
			ret = true;
		} else {
			ComponentType ext = this.r_extends;
			while (ext != null) {
				if (ext.getName().equals(typeName)) {
					ret = true;
					break;
				}
				ext = ext.r_extends;
			}
		}
		return ret;
	}
	
	
	
	

	

	public boolean isOrExtendsType(ComponentType ct) {
		boolean ret = false;
		if (this == ct) {
			ret = true;
		} else {
			ComponentType ext = this.r_extends;
			while (ext != null) {
				if (ext.equals(ct)) {
					ret = true;
				}
				ext = ext.r_extends;
			}
		}
		return ret;
	}

	
	
	
	public boolean extendsType(ComponentType ct) {
		boolean ret = false;
		ComponentType ext = r_extends;
		while (ext != null) {
			if (ext.equals(ct)) {
				ret = true;
				break;
			}
			ext = ext.r_extends;
		}
		return ret;
	}

	public void addCpt(Component cpt) {
		cpts.add(cpt);
	}

	public LemsCollection<Component> getComponents() {
		return cpts;
	}

	public void resolve(Lems lems, Parser p) throws ContentError, ParseError {

		for (Parameter dp : parameters) {
			dp.resolve(lems.getDimensions());
		}

		for (DerivedParameter dp : derivedParameters) {
			dp.resolve(lems.getDimensions());
		}

		HashMap<String, Double> valHM = new HashMap<String, Double>();
		for (Constant c : constants) {
			c.resolve(lems.getDimensions(), lems.getUnits(), null, valHM);
		}

		for (Property pr : propertys) {
			pr.resolve(lems.getDimensions());
		}

		for (Requirement req : requirements) {
			req.resolve(lems.getDimensions());
		}

		for (Exposure exp : exposures) {
			exp.resolve(lems.getDimensions());
		}

		for (Child ch : childs) {
			ch.resolve(lems, p);
		}

		for (ComponentReference cr : componentReferences) {
			if (cr.resolving()) {
				// OK - we must be a ref to self
			} else {
				cr.resolve(lems, p);
			}
		}

		for (ComponentTypeReference tr : componentTypeReferences) {
			tr.resolve(lems, p);
		}

		for (Link lin : links) {
			lin.resolve(lems, p);
		}

		for (Children chn : childrens) {
			chn.resolve(lems, p);
		}

		for (Attachments atn : attachmentses) {
			atn.resolve(lems, p);
		}

		if (eXtends != null) {
			r_extends = lems.getComponentTypeByName(eXtends);

			if (r_extends == null) {
				throw new ContentError("missing extends " + eXtends);
			} else {
				r_extends.checkResolve(lems, p);
			}

		}
		
		for (Property prop : propertys) {
			instancePropertys.add(new InstanceProperty(prop.getName(), prop.getDimension()));
		}

		for (Parameter dp : parameters) {
			finalParams.add(new FinalParam(dp.getName(), dp.getDimension()));
		}

		
		for (DerivedParameter dp : derivedParameters) {
			finalParams.add(new DerivedFinalParam(dp.getName(), 
					dp.getDimension(), dp.getSelect(), dp.getValue()));
		}

		
		// using addIfNew so we an locally override an inherited element
		// happens with rereading lems models where the resolved type gets written out
		if (r_extends != null) {
			for (FinalParam fp : r_extends.getFinalParams()) {
                //System.out.println("FinalParam: "+fp);
				finalParams.addIfNew(fp.makeCopy());
			}
			for (EventPort ep : r_extends.getEventPorts()) {
				eventPorts.addIfNew(ep.makeCopy());
			}

			for (ComponentReference cr : r_extends.getComponentRefs()) {
				componentReferences.addIfNew(cr.makeCopy());
			}

			for (Link lin : r_extends.getLinks()) {
				links.addIfNew(lin.makeLinkCopy());
			}
			for (Requirement req : r_extends.getRequirements()) {
				requirements.addIfNew(req.makeCopy());
			}
			for (Exposure exp : r_extends.getExposures()) {
				exposures.addIfNew(exp.makeCopy());
			}
			
			for (Children chn : r_extends.getChildrens()) {
				childrens.addIfNew(chn.makeCopy());
			}
			
			for (Child ch : r_extends.getChilds()) {
				childs.addIfNew(ch.makeCopy());
			}
			
			
			for (PairCollection pc : r_extends.getPairCollections()) {
				pairCollections.addIfNew(pc.makeCopy());
			}
			
			for (Collection c : r_extends.getCollections()) {
				collections.addIfNew(c.makeCopy());
			}
			
		}

		for (Fixed sp : fixeds) {
		 
				if (finalParams.hasName(sp.getPseudoName())) {
					finalParams.getByName(sp.getPseudoName()).setSValue(
							sp.getValue());
				}
			 
		}

		for (Constant c : constants) {
			FinalParam fp = new FinalParam(c.getName(), c.getDimension());
			fp.setSValue(c.getStringValue());
			finalParams.add(fp);
		}

		for (EventPort ep : eventPorts) {
			ep.resolve(lems.getDimensions());
		}

		for (Dynamics b : dynamicses) {
			b.setComponentType(this);
			
			if (b.inheritDynamics()) {
				if (r_extends != null) {
					
					b.makePeerCopy();
					
					b.getPeer().inheritFrom(r_extends.getDynamics());
					
				} else {
					throw new ContentError("Dynamics in " + this + 
							" can't inherit super type dynamics : no supertype");
				}
			}
			
			b.resolve(lems, p);
		}

		
		for (Structure s : structures) {
			s.resolve(lems, this);
		}
	 
		for (Simulation sim : simulations) {
			sim.resolve(lems, this);
		}
		
		for (Exposure exp : exposures) {

			finalExposeds.add(new FinalExposed(exp.getName(), exp.getDimension()));
		}

		resolved = true;
	}

	
	
	
	
	
	
	
	public LemsCollection<EventPort> getEventPorts() {
		return eventPorts;
	}

	public LemsCollection<FinalParam> getFinalParams() {
		return finalParams;
	}

	public LemsCollection<FinalExposed> getFinalExposures() {
		return finalExposeds;
	}

	public LemsCollection<InstanceProperty> getInstancePropertys() {
		return instancePropertys;
	}
	
	public void checkResolve(Lems lems, Parser p) throws ContentError,
			ParseError {
		if (!resolved) {
			resolve(lems, p);
		}
	}

	public LemsCollection<Parameter> getDimParams() {
		return parameters;
	}

	public LemsCollection<Constant> getConstants() {
		return constants;
	}

	public LemsCollection<DerivedParameter> getDerivedParameters() {
		return derivedParameters;
	}

	public boolean setsParam(Parameter dp) throws ContentError {
		return fixeds.hasPseudoName(dp.getName());
	}

	public String getSetParamValue(Parameter dp) throws ContentError {
		Fixed sp = fixeds.getByPseudoName(dp.getName());
		return sp.getValue();
	}

	public EventPort getInEventPort(String port) throws ContentError {
		EventPort ret = null;
		if (eventPorts.hasName(port)) {
			EventPort ep = eventPorts.getByName(port);
			if (ep.isDirectionIn()) {
				ret = ep;
			} else {
				E.error("input port needed for " + port + " but got output port");
			}
		}
		if (ret == null) {
			E.error("No such port: " + port+" on "+this);
		}
		return ret;
	}

	public EventPort getOutEventPort(String port) throws ContentError {
		EventPort ret = null;
		if (eventPorts.hasName(port)) {
			EventPort ep = eventPorts.getByName(port);
			if (ep.isDirectionOut()) {
				ret = ep;
			} else {
				E.error("output port needed for " + port + " but got input port");
			}
		}
		if (ret == null) {
			E.error("No such port: " + port + " on " + this);
		}
		return ret;
	}



	public Dynamics getDynamics() throws ContentError {
		Dynamics ret = null;
		
		if (ret == null) {
			if (dynamicses.isEmpty() && r_extends != null) {
				ret = r_extends.getDynamics();
			} else {
				if (dynamicses.size() == 1) {
					ret = dynamicses.getOnly();
				} else {
					//E.info("No dynamics is specified for " + name);
				}
			}
		}
		if (ret != null) {
			ret = ret.getPeer();
		}
		return ret;
	}
	
	
	
	
	public Simulation getSimulation() throws ContentError {
		Simulation ret = null;
		

		if (ret == null) {
			if (simulations.isEmpty() && r_extends != null) {
				ret = r_extends.getSimulation();
			} else {
				if (simulations.size() == 1) {
					ret = simulations.getOnly();
				} else {
					//E.info("No dynamics is specified for " + name);
 				}
			}
		}
		return ret;
	}
	
	
	
	
	

	public Valued getSimpleExposed(String nm) throws ContentError {
		Valued ret = null;
		if (finalParams.hasName(nm)) {
			ret = finalParams.getByName(nm);
		}
		if (ret == null) {
			if (finalExposeds.hasName(nm)) {
				ret = finalExposeds.getByName(nm);
			}
		}

		if (ret == null) {
			throw new ContentError("No quantity '" + nm + "' available in "
					+ this + "\nfinalParams: " + finalParams
					+ "\nfinalExposeds: " + finalExposeds);
		}

		return ret;
	}

	 
	
	public boolean hasChild(String scb) throws ContentError {
		return childs.hasName(scb);
	}


	public Children getChildren(ComponentType ftype) throws ContentError {
		Children ret = null;
		
		for (Children chn : childrens) {
			ComponentType t = chn.getComponentType();
			if (ftype.equals(t) || ftype.eXtends(t)) {
				ret = chn;
			}
		}
		
		if (ret == null) {
			String msg = "No such children list for type: (" + ftype.getName()+") in "+ this.getName() + "\n";
			msg += "Existing children lists: [" + childrens.listAsText() + "]\n";
			msg += "existing childs: ["+childs.listAsText()+"]\n";
			
			throw new ContentError(msg);
		}
		return ret;
	}

	public ArrayList<ComponentType> getChildTypes() throws ContentError {
		ArrayList<ComponentType> childCTs = new ArrayList<ComponentType>();

		for (Children chn : childrens) {
			ComponentType t = chn.getComponentType();
			childCTs.add(t);
		}
		for (Child child : childs) {
			ComponentType t = child.getComponentType();
			childCTs.add(t);
		}
		return childCTs;
	}

	private boolean eXtends(ComponentType t) {
		boolean ret = false;
		if (r_extends != null) {
			if (t.equals(r_extends) || r_extends.eXtends(t)) {
				ret = true;
			}
		}
		return ret;
	}

	public ComponentType getExtends() {
		return r_extends;
	}

	
	public boolean hasDynamics() {
		boolean ret = false;
		if (dynamicses.size() > 0) {
			ret = true;
		} else if (r_extends != null && r_extends.hasDynamics()) {
			ret = true;
		}
		return ret;
	}
	
	
	public boolean hasSimulation() {
		boolean ret = false;
		if (simulations.size() > 0) {
			ret = true;
		} else if (r_extends != null && r_extends.hasSimulation()) {
			ret = true;
		}
		return ret;
	}
	

	public LemsCollection<ComponentReference> getComponentRefs() {
		return componentReferences;
	}

	public LemsCollection<Link> getLinks() {
		return links;
	}


	public void checkEquations(HashMap<String, Dimensional> cdimHM) throws ContentError {
		for (Dynamics b : dynamicses) {
			b.checkEquations(cdimHM);
		}

	}

	public LemsCollection<Attachments> getAttachmentss() {
		return attachmentses;
	}

	public Children getChildrenByName(String sn) throws ContentError {
		return childrens.getByName(sn);
	}

	public Valued getFinalParam(String sn) throws ContentError {
		return finalParams.getByName(sn);
	}

	public ComponentReference getComponentRef(String sn) throws ContentError {
		return componentReferences.getByName(sn);
	}

 

	public LemsCollection<Path> getPaths() {
		return paths;
	}

	public LemsCollection<Text> getTexts() {
		return texts;
	}

	public LemsCollection<Requirement> getRequirements() {
		return requirements;
	}

	public LemsCollection<Exposure> getExposures() {
		return exposures;
	}

	public LemsCollection<Children> getChildrens() {
		return childrens;
	}
	
	public LemsCollection<Child> getChilds() {
		return childs;
	}
	
	public LemsCollection<ComponentReference> getComponentReferences() {
		return componentReferences;
	}
	
	public Exposure getExposure(String snm) throws ContentError {
		if (!exposures.hasName(snm)) {
			throw new ContentError("No such exposure " + snm + " in " + this);
		}
		Exposure ret = exposures.getByName(snm);
		if (ret == null) {
			throw new ContentError("Problem getting value of exposure " + snm + " in " + this);
		}
		return ret;
	}

	public LemsCollection<Property> getPropertys() {
		return propertys;
	}

	public Collection getCollection(String cnm) throws ContentError {
		return collections.getByName(cnm);
	}

	public PairCollection getPairCollection(String cnm) throws ContentError {
		return pairCollections.getByName(cnm);
	}

	public LemsCollection<Collection> getCollections() {
		return collections;
	}
	
	public LemsCollection<PairCollection> getPairCollections() {
		return pairCollections;
	}

	public LemsCollection<ComponentTypeReference> getComponentTypeRefs() {
		return componentTypeReferences;
	}

	public LemsCollection<PathParameter> getPathParameters() {
		return pathParameters;
	}

	public LemsCollection<Parameter> getParameters() {
		return parameters;
	}

	public double getHierarchyWeight(Lems lems) {
		double ret = 0.;
		ret += childrens.size();
		ret += 0.5 * childs.size();
		for (ComponentType ct : lems.getComponentTypes()) {
			if (this.isOrExtendsType(ct)) {
				
			} else if (ct.isOrExtendsType(this)) {
				
			} else {
				ret -= ct.referencesTo(this);
			}
		}
		return ret;
	}
	
	
    @Override
	public boolean inherited(Object obj) throws ContentError {
		boolean ret = false;
		if (r_extends != null && r_extends.hasInheritable(obj)) {
			ret = true;
		}
		return ret;
	}
	
	

	private double referencesTo(ComponentType dest) {
		double ret = 0;
		for (ComponentReference cr : componentReferences) {
			if (dest.isOrExtendsType(cr.getComponentType())) {
				ret += 1;
			}
		}
		return ret;
	}
	
	
	private boolean hasInheritable(Object obj) throws ContentError {
		 boolean ret = false;
		 if (obj instanceof Exposure && exposures.hasName(((Exposure)obj).getName())) {
			 ret = true;
		 } else if (obj instanceof EventPort && eventPorts.hasName(((EventPort)obj).getName())) {
			 ret = true;
		 } else if (obj instanceof Requirement && requirements.hasName(((Requirement)obj).getName())) {
			 ret = true;
		 }
		 
		 return ret;
	}
	
	
	
	public void addParameter(Parameter p) {
		 parameters.add(p);
		
	}
	
	public void addDerivedParameter(DerivedParameter dp) {
		 derivedParameters.add(dp);
		
	}
	
	public ArrayList<ComponentType> getChildCompTypes() throws ContentError {
		ArrayList<ComponentType> childCTs = new ArrayList<ComponentType>();

		for (Children chn : childrens) {
			ComponentType t = chn.getComponentType();
			childCTs.add(t);
		}
		for (Child child : childs) {
			ComponentType t = child.getComponentType();
			childCTs.add(t);
		}
		return childCTs;
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

	public void addEventPort(EventPort ep) {
		eventPorts.add(ep);
		
	}


	public void addDynamics(Dynamics d) {
		dynamicses.add(d);
	}

	public void addExposure(Exposure expo) {
		exposures.add(expo);
	}

	public String getChildType(String name) {
		String ret = null;
		for (Child ch : childs) {
			if (ch.name != null && ch.name.equals(name)) {
				ret = ch.type;
				break;
			}
		}
		return ret;
	}

	public LemsCollection<Procedure> getProcedures() {
		return procedures;
	}


	public StateType makeStateType(Component cpt, boolean fixParams) throws ContentError, ParseError {

		HashMap<String, Double> fixedHM = new HashMap<String, Double>();

		HashMap<String, Double> chm = Constants.getConstantsHM();
		
		if (chm != null) {
			fixedHM.putAll(chm);
		}
		
		
		// TODO this can contain the parm values that aren't changed by instances,
		// but not those that are.
		// for now, only use fixParams=true when generating LemsLite
		if (fixParams) {
			E.info("AM fixing params in " + this);
		for (ParamValue pv : cpt.getParamValues()) {
			fixedHM.put(pv.getName(), pv.getDoubleValue());
				E.info("Fixed param " + pv.getName());
		}
		} else {
			//E.info("NOT fixing params in " + this);
		}

		StateType ret = null;
		
		if (hasDynamics()) {
			Dynamics bv = getDynamics();
			ret = bv.makeStateType(cpt, fixedHM);

		} else {
 			ret = new StateType(cpt.getID(), getName());
			for (ParamValue pv : cpt.getParamValues()) {
				 String qn = pv.getName();
				 double qv = pv.getDoubleValue();
				 ret.addFixed(qn, qv);
			}

		}
		
		if (structures.size() > 0) {
			for (Structure b : structures) {
				ret.addBuilder(b.makeBuilder(cpt));
			}
		}
	 
	
		for (Property p : getPropertys()) {
			String pnm = p.getName();
			ret.addExposureMapping(pnm, pnm);
		}

		for (Text text : getTexts()) {
			String tnm = text.getName();
			if (cpt.hasAttribute(tnm)) {
				ret.addTextParam(tnm, cpt.getAttributeValue(tnm));
			}
		}

		for (String s :cpt.refHM.keySet()) {
			Component ch = cpt.refHM.get(s);
			StateType chb = ch.getStateType();
			ret.addRefStateType(s, chb);
		}

		for (String s : cpt.childHM.keySet()) {
			Component ch = cpt.childHM.get(s);
			StateType chb = ch.getStateType();
			ret.addChildStateType(s, chb);
		}

		
		if (cpt.freeChildren != null) {
			for (Component fc : cpt.freeChildren) {
				StateType chb = fc.getStateType();
				ret.addListStateType(cpt.getListName(fc), chb);
			}
		}
			
		 
		
		for (Attachments ats : getAttachmentss()) {
 			ret.addAttachmentSet(ats.getName(), ats.getComponentType().getName());
		}

		for (Collection c : getCollections()) {
			ret.addInstanceSet(c.getName());
		}

		for (PairCollection c : getPairCollections()) {
			ret.addInstancePairSet(c.getName());
		}
		
		
		for (Simulation sim : simulations) {
			sim.appendToBehavior(cpt, ret);
		}
	 
		return ret;
	
	}

	protected void addConstant(String name, Dimension dim, String value) {
		 constants.add(new Constant(name, dim, value));
	}

	protected void addText(String txt) {
		 texts.add(new Text(txt));
	}

	protected void addRequirement(Requirement req) {
		requirements.add(req);
	}
	
	public void removeRequirement(String rnm) throws ContentError {
		requirements.remove(rnm);
	}

	
	protected void setDynamics(Dynamics td) {
		dynamicses.add(td);
		
	}
    
	public Map<String, Dimensional> getExposedDimensions() {
		HashMap<String, Dimensional> ret = new HashMap<String, Dimensional>();
		for (Exposure e : exposures) {
			ret.put(e.getName(), e.getDimension());
		}
		for (Requirement r : getRequirements()) {
			ret.put(r.getName(),  r.getDimension());
		}
		return ret;
	}

	
	public ComponentType getScopeType(String over) throws ContentError {
		ComponentType ret = null;
		if (childrens.hasName(over)) {
			ret = childrens.getByName(over).getComponentType();
			
		} else if (childs.hasName(over)) {
			ret = childs.getByName(over).getComponentType();

		} else if (attachmentses.hasName(over)) {
			ret = attachmentses.getByName(over).getComponentType();
			
		} else {
			throw new ContentError("Not such element '" + over + "' in " + this);
		}
		return ret;
	}
 
	public IndexParameter getIndexParameter(String nm) throws ContentError {
		return indexParameters.getByName(nm);
	}

 
	
 
	
}
