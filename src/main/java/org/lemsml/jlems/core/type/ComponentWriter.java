package org.lemsml.jlems.core.type;


public final class ComponentWriter {
	
	
	private ComponentWriter() {
		
	}
	
	
	
	public static String summarize(Component cpt) {
		StringBuilder sb = new StringBuilder();
		ComponentType ctype = cpt.getComponentType();
		if (ctype == null) {
			// return "ERROR in component: "+toString();
			sb.append(cpt.getID() + " (" + cpt.type + ": ");
		} else {
			sb.append(cpt.getID() + " (" + ctype.getName() + ": ");
		}
		
		LemsCollection<ParamValue> paramValues = cpt.getParamValues();
		if (paramValues != null) {
			for (ParamValue pv : paramValues) {
				String si = "SI ";
				if (pv.getDimensionName() == null) {
					si = "???";
				} else if (pv.getDimensionName().equals(Dimension.NO_DIMENSION)) {
					si = "";
				}
				sb.append(" " + pv.getName() + "=" + pv.stringValue() + " (" + si + pv.getDimensionName() + ")");
			}
		}
		sb.append(")");

		return sb.toString();
	}
	
	
	
	
	
	
	public static String writeDetails(Component cpt, String indent) {	
		StringBuilder sb = new StringBuilder();
		ComponentType ctype = cpt.getComponentType();
		sb.append(indent+"-- Component, id = "+ cpt.getID() + ", type: " + ctype.getName() + " ----------\n");
		sb.append(indent+"    Parameters:\n");
		
		LemsCollection<ParamValue> paramValues = cpt.getParamValues();
		if (paramValues != null) {
			for (ParamValue pv : paramValues) {
				String si = "SI ";
				if (pv.getDimensionName() == null) {
					si = "???";
				} else if (pv.getDimensionName().equals(Dimension.NO_DIMENSION)) {
					si = "";
				}
				sb.append(indent+"        " + pv.getName() + " = " + pv.stringValue() + " (" + si + pv.getDimensionName() + ")\n");
			}
		}

		LemsCollection<Component> components = cpt.getComponents();
        if (components!=null && !components.isEmpty()) {
            sb.append(indent+"    Components:\n");
            for (Component comp: components){
                sb.append(comp.details(indent+"        ")+"\n");
            }
        }
    
         
        if (cpt.textParamHM!=null && !cpt.textParamHM.isEmpty()) {
            sb.append(indent+"    Text Parameters:\n");
            sb.append(indent+"        "+cpt.textParamHM+"\n");
        }

        if (cpt.childHM!=null && !cpt.childHM.isEmpty()) {
            sb.append(indent+"    Child Components:\n");
            for (String name: cpt.childHM.keySet()){
                sb.append(indent+"        --"+name+"--\n");
                sb.append(cpt.childHM.get(name).details(indent+"        ")+"\n");
            }
        }

        if (cpt.refHM!=null && !cpt.refHM.isEmpty()) {
            sb.append(indent+"    Refs:\n");
            for (String name: cpt.refHM.keySet()){
                sb.append(indent+"        --"+name+"--\n");

                //sb.append(refHM.get(name).details(indent+"        ")+"\n");
                sb.append(indent+"            --> "+cpt.refHM.get(name)+"\n");

            }
        }

        if (cpt.childrenHM!=null && !cpt.childrenHM.isEmpty()) {
            sb.append(indent+"    Children Components:\n");
            for (String name: cpt.childrenHM.keySet()){
                sb.append(indent+"        --"+name+"--\n");
                for (Component comp: cpt.childrenHM.get(name)){
                    sb.append(comp.details(indent+"        ")+"\n");
                }

            }
        }


		sb.append(indent+"---------------------------------------------");

		return sb.toString();
	}
	
	
	
	
	
	
	
	
	
}
