package org.lemsml.jlems.core.type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.lemsml.jlems.core.annotation.ModelElement;
import org.lemsml.jlems.core.expression.Dimensional;
import org.lemsml.jlems.core.expression.ParseError;
import org.lemsml.jlems.core.expression.Parser;
import org.lemsml.jlems.core.expression.Valued;
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.run.ConnectionError;
import org.lemsml.jlems.core.run.Constants;
import org.lemsml.jlems.core.run.EventManager;
import org.lemsml.jlems.core.run.RuntimeError;
import org.lemsml.jlems.core.run.StateInstance;
import org.lemsml.jlems.core.run.StateType;
import org.lemsml.jlems.core.sim.ContentError;

@ModelElement(info="Root element for any lems content")
public class Lems {
	
		
	final static int STRICT = 0;
	final static int LOOSE = 1;
	
	private int resolveMode = STRICT;
	   
	 
    public LemsCollection<Dimension> dimensions = new LemsCollection<Dimension>();
 
 
	public LemsCollection<Constant> constants = new LemsCollection<Constant>();

	 
	public LemsCollection<Unit> units = new LemsCollection<Unit>();

 
	public LemsCollection<Assertion> assertions = new LemsCollection<Assertion>();
    
 
    public LemsCollection<ComponentType> componentTypes = new LemsCollection<ComponentType>();

 
	public LemsCollection<Component> components = new LemsCollection<Component>();

 
	public LemsCollection<Target> targets = new LemsCollection<Target>();
 
    Parser parser;

    public String description;

    
    
    private final LemsCollection<Valued> globals = new LemsCollection<Valued>();
    private LemsCollection<Valued> constantValued = null;
    
    private static Random randomGenerator = new Random();

    private boolean resolved = false;
    
     
    
    public Lems() {
        globals.add(new IndVar("t"));
        parser = new Parser();
    }
    

    public void deduplicate() throws ContentError {
        dimensions.deduplicate();
        units.deduplicate();
    }

    
    public void setResolveModeLoose() {
    	resolveMode = LOOSE;
    }
    
    public boolean looseResolving() {
    	boolean ret = false;
    	if (resolveMode == LOOSE) {
    		ret = true;
    	}
    	return ret;
    }
    
    
    public static Random getRandomGenerator() {
        return randomGenerator;
    }

    
    public void resolve(ComponentType ct) throws ParseError, ContentError {
    	if (!resolved) {
    		resolve();
    	}
    	ct.checkResolve(this, parser);
    }
    
    
    public void resolve(Component c) throws ParseError, ContentError {
    	if (!resolved) {
    		resolve();
    	}
    	c.checkResolve(this, null);
    }
    
    
    
    
    
    public void resolve() throws ContentError, ParseError {
          
        if (!dimensions.hasName(Dimension.NO_DIMENSION)) {
        	dimensions.add(new Dimension(Dimension.NO_DIMENSION));
        }
        
        if (!units.hasPseudoName(Unit.NO_UNIT)) {
         	units.add(new Unit(Unit.NO_UNIT, Unit.NO_UNIT, dimensions.getByName(Dimension.NO_DIMENSION)));
        }

        for (Assertion da : assertions) {
        	da.resolve(dimensions);
        }
        
        for (Assertion da : assertions) {
        	da.check(dimensions, parser);
        }
        
        HashMap<String, Double> cvalHM = new HashMap<String, Double>();
        for (Constant c : constants) {
        	c.resolve(dimensions, null, parser, cvalHM);
        	cvalHM.put(c.getSymbol(), c.getValue());
        }
        Constants.setConstantsHM(cvalHM);
        
        
        
        for (Unit unit : units) {
            unit.resolve(dimensions);
        }

        for (ComponentType t : componentTypes) {
            // some types cause others to be resolved first, so they may already have been resolved when we
            // get to them here. Call checkResolve instead of plain resolve
            t.checkResolve(this, parser);
        }

        for (Component inst : components) {
            // as above for components
            inst.checkResolve(this, null);
        }

        for (Target dr : targets) {
            dr.resolve(this);
        }

        HashMap<String, Dimensional> cdimHM = getConstantDimHM();
        for (ComponentType t : componentTypes) {
            try {
                t.checkEquations(cdimHM);
            } catch (ContentError ce) {
                throw new ParseError("Error checking equations of ComponentType: " + t
                        + "\n(" + ce.getMessage() + ")", ce);

            }
        }
        resolved = true;
    }

    public void addComponent(Component c) {
    	E.info("Added cpt " + c.getID());
         components.add(c);
    }

    
    public void addComponentType(ComponentType ct) {
        E.info("Adding component type: " + ct);
        componentTypes.add(ct);
    }

    public void evaluateStatic() throws ContentError, ParseError {
        for (Component cpt : components) {
            cpt.evaluateStatic(this);
        }
    }

    public LemsCollection<Dimension> getDimensions() {
        return dimensions;
    }

    public LemsCollection<Unit> getUnits() {
        return units;
    }

    public LemsCollection<Valued> getGlobals() {
        return globals;
    }
    
    public LemsCollection<Valued> getConstantValueds() {
    	if (constantValued == null) {
    		constantValued = new LemsCollection<Valued>();
    		for (Constant c : constants) {
    			ConstantValued cvd = new ConstantValued(c.getSymbol(), c.getDimension(), c.getValue()); 
    			constantValued.add(cvd);
    		}
    	}
    	return constantValued;
    }
    
    public HashMap<String, Valued> getConstantsHM() {
    	HashMap<String, Valued> ret = new HashMap<String, Valued>();
    	for (Valued v : getConstantValueds()) {
    		ret.put(v.getName(), v);
    	}
    	return ret;
    }
    
    public HashMap<String, Double> getConstantsValueHM() {
    	HashMap<String, Double> ret = new HashMap<String, Double>();
    	for (Valued v : getConstantValueds()) {
    		ret.put(v.getName(), v.getValue());
    	}
    	return ret;
    }
    
    
    public HashMap<String, Dimensional> getConstantDimHM() throws ContentError {
    	HashMap<String, Dimensional> ret = new HashMap<String, Dimensional>();
    	for (Constant c : constants) {
    		Dimension d = c.getDimension();
    		if (d == null) {
    			throw new ContentError("No dimension for constant " + c);
    		}
    		ret.put(c.getSymbol(), c.getDimension());
    	}
    	return ret;
    }
    

    public LemsCollection<ComponentType> getComponentTypesExtending(String typeName) {
        LemsCollection<ComponentType> ext = new LemsCollection<ComponentType>();

        for (ComponentType ct : componentTypes) {
            if (ct.getExtends() != null && ct.getExtends().getName().equals(typeName)) {
                ext.add(ct);
            }
        }
        return ext;
    }

    public ComponentType getComponentTypeByName(String nm) throws ContentError {
        ComponentType t = componentTypes.getByName(nm);

        if (t == null) {
            StringBuilder error = new StringBuilder("No such type " + nm + ", "
                    + componentTypes.size() + " existing types: ");
            for (ComponentType class_ : componentTypes) {
                error.append("\n" + class_.getName());
            }
            throw new ContentError(error.toString());
        }
        return t;
    }

    public boolean hasComponent(String sid) throws ContentError {
        return components.hasID(sid);
    }

    public Component getComponent(String sid) throws ContentError {
        Component comp = components.getByID(sid);
        if (comp == null) {
            String info = "No component found: " + sid + ". Existing components: \n";
            for (Component c : components) {
                info += " - " + c + "\n";
            }
            throw new ContentError(info);
        }
        return comp;
    }

    public String textSummary() {
        return textSummary(true);
    }

    public String textSummary(boolean showTypes) {
        return textSummary(showTypes, true);
    }

    public String textSummary(boolean showTypes, boolean showDimsUnits) {
        StringBuilder sb = new StringBuilder();
        if (showDimsUnits) {
            sb.append("\n");
            sb.append(dimensions.listAsText());
            sb.append("\n");
            sb.append(units.listAsText());
            sb.append("\n");
        }
        if (showTypes) {
            sb.append(componentTypes.listAsText("\n"));
            sb.append("\n");
        }
        sb.append(components.listAsText());

        return sb.toString();
    }

    @Override
    public String toString() {
        return "Lems (" + dimensions.size() + " dimensions, " + units.size() + " units, " + 
        componentTypes.size() + " types, " + components.size() + " components)";
    }

    public Target getTarget() throws ContentError {
    	Target ret = null;
    	if (targets.size() > 0) {
    		ret = targets.first();
    	} else {
    		for (Component cpt : components) {
    			ComponentType ct = cpt.getComponentType();
    			if (ct.hasSimulation() && ct.getSimulation().definesRun()) {
    				ret = new Target();
    				ret.r_component = cpt;
    				ret.component = cpt.id;
    			}
    		}
    		
    	}
    	if (ret == null) {
    		throw new ContentError("No runnable components have been specified");
    	}
    	return ret;
    }
    
     

    public StateInstance build(StateType cptb, EventManager em) throws ContentError, ConnectionError, ParseError, RuntimeError {
      	Constants.setConstantsHM(getConstantsValueHM());
      	StateInstance ret = cptb.newInstance();
      //  ret.setEventManager(em);
        
        ret.checkBuilt();

        return ret;
    }

    public LemsCollection<ComponentType> getComponentTypes() {
        return componentTypes;
    }
    
    public LemsCollection<Component> getComponents() {
        return components;
    }
    
    
    public ArrayList<Component> getAllByType(ComponentType tgtct) throws ContentError {
    	ArrayList<Component> ret = new ArrayList<Component>();
    	for (ComponentType ct : componentTypes) {
    		if (ct.isOrExtendsType(tgtct)) {
    			ret.addAll(ct.getComponents().getContents());    	
		
    		}
    	}
        return ret;
    }


    public ArrayList<Component> getAllByType(String typeName) throws ContentError {
        ArrayList<Component> ret = new ArrayList<Component>();
        ComponentType ct = componentTypes.getByName(typeName);
        ret.addAll(ct.getComponents().getContents());
        return ret;
    }

    public Parser getParser() {
        return parser;
    }

    public String[] getSubtypeNames(String typeName) throws ContentError {
        ArrayList<String> tnms = new ArrayList<String>();
        ComponentType rt = getComponentTypeByName(typeName);

        for (ComponentType ct : componentTypes) {
            if (ct.extendsType(rt)) {
                tnms.add(ct.getName());
            }
        }

        return tnms.toArray(new String[tnms.size()]);
    }


	public LemsCollection<Target> getTargets() {
		return targets;
	}





	public Unit getUnit(String symbol) throws ContentError {
		Unit ret = units.getByPseudoName(symbol);
		return ret;
	}


	public void addDimension(Dimension d) {
		dimensions.add(d);
	}


	public void setTargetComponent(Component cp) throws ContentError {
		targets = new LemsCollection<Target>();
		Target t = new Target();
		t.setComponentID(cp.getID());
		t.resolve(this);
		targets.add(t);
		
	}
 


}
