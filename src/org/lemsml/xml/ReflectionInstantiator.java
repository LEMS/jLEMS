package org.lemsml.xml;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.lemsml.io.FormatException;
import org.lemsml.io.IOFace;
import org.lemsml.io.IOFaceMixed;
import org.lemsml.io.ImportNameMapper;
import org.lemsml.type.AddableTo;
import org.lemsml.type.Attribute;
import org.lemsml.type.Attributed;
import org.lemsml.type.Component;
import org.lemsml.type.ComponentContainer;
import org.lemsml.type.ElementAdder;
import org.lemsml.type.LemsCollection;
import org.lemsml.type.MetaContainer;
import org.lemsml.type.MetaItem;
import org.lemsml.type.Namable;
import org.lemsml.type.Parameterized;
import org.lemsml.type.Parented;
import org.lemsml.util.ContentError;
import org.lemsml.util.E;

public class ReflectionInstantiator implements Constructor {

    int npkg;
    String[] pkgs;
    String wkpkg;
    
    ArrayList<String> prefixes = new ArrayList<String>();
    HashMap<String, String> packagePrefixes = new HashMap<String, String>();
    
    
    ImportNameMapper impmap = null;
    
    

    public ReflectionInstantiator() {
        pkgs = new String[100];
        npkg = 0;   
    }

    
    public ReflectionInstantiator(String path) {
        this();
        addSearchPackage(path);
    }

    
    public void setImportNameMapper(ImportNameMapper m) {
    	impmap = m;
    }
    
    public final void addSearchPackage(Package pkg) {
    	addSearchPackage(pkg, null);
    }
    
    public final void addSearchPackages(ArrayList<Package> pkga) {
    	for (Package p : pkga) {
    		addSearchPackage(p, null);
    	}
    }
    
    public final void addSearchPackage(Package pkg, String pfx) {
        // REFAC keep package
        String s = pkg.getName();
        addSearchPackage(s);
        if (pfx != null) {
        	prefixes.add(pfx);
        	packagePrefixes.put(pfx, pkg.getName());
        }
    }

    public final void addSearchPackage(String s) {
        wkpkg = s;
        pkgs[npkg++] = s;
    }

    

    public void checkAddPackage(Object oret) {
        String scl = oret.getClass().getName();
        if (scl.startsWith("java")) {
            return;
        }

        int ild = scl.lastIndexOf(".");
        String pkg = scl.substring(0, ild);
        if (pkg.equals(wkpkg)) {
            // just same as before;
        } else {
            boolean got = false;
            for (int i = 0; i < npkg; i++) {
                if (pkgs[i].equals(pkg)) {
                    got = true;
                    break;
                }
            }
            if (!got) {
                pkgs[npkg++] = pkg;

                // System.out.println("Reflection instantiator added search package
                // " + pkg);
            }
        }
    }

    public Object newInstance(String rscl, boolean allowComponents) {
    	String scl = rscl;
    	if (impmap != null && impmap.renames(scl)) {
    		scl = impmap.getInternalElementName(scl);
    	}
    	
    	
         Object oret = null;
        Class<?> c = null;
        if (scl.indexOf(".") > 0) {
            try {
                c = Class.forName(scl);
            } catch (Exception e) {
            }
        }
        
        if (c == null) {
        	for (String pfx : prefixes) {
        		String spkg = packagePrefixes.get(pfx);
        		String scls = spkg + "." + pfx + scl;
        		try {
        			c = Class.forName(scls);
         		} catch (ClassNotFoundException e) {
          		}
        	}
        }
        

        if (c == null) {
            for (int i = 0; i < npkg; i++) {
                try {
                    c = Class.forName(pkgs[i] + "." + scl);
                    if (c != null) {
                        break;
                    }
                } catch (ClassNotFoundException e) {
                    // normal - ignore these
                } catch (Exception e) {
                    E.warning("possible problem with class instantiation? " + e);
                }
            }
        }

        if (c == null) {
            if (allowComponents) { 
            	// TODO get this dependency out!
            	c = Component.class;
            	E.info("Adding a component, type=" + rscl);
            } else {
            	// not allowing Components - will return null
            }
        }


       if (c != null) {
            int imod = c.getModifiers();
            if (Modifier.isAbstract(imod)) {
                E.error("cant instantiatie " + c + ":  it is an abstract class");
            } else {

                try {
                    oret = c.newInstance();

                    if (oret instanceof Component) {
                        ((Component) oret).setDeclaredName(scl);
                    }

                } catch (Exception e) {
                    E.error(" " + e + " instantiating " + c + ". Make sure there is a default constructor for that class");
                }
            }
       }

        if (oret != null) {
            checkAddPackage(oret);
        }

        return oret;
    }

 
    public Object getField(Object ob, String fnm) throws ContentError {
        Object ret = null;

        boolean hasField = false;

        // EFF improve
        Field[] flds = ob.getClass().getFields();
        for (int i = 0; i < flds.length; i++) {
            if (flds[i].getName().equals(fnm)) {
                hasField = true;
                break;
            }
        }

        if (hasField) {
            try {
                Field f = ob.getClass().getField(fnm);

                Class<?> fcl = f.getType();

                if (fcl.equals(String[].class)) {
                    ret = new String[0];

                } else if (fcl.isArray()) {
                    ret = new ArrayList<Object>(); // ADHOC - wrap ArrayList?
                } else {
                    ret = f.get(ob);
                }

                if (ret == null) {
                    Class<?> cl = f.getType();
                    ret = cl.newInstance();
                }
            } catch (Exception e) {
                throw new ContentError("cant get field " + fnm + " on " + ob + " " + "excception= " + e);
            }
        }
 
        return ret;
    }

    public Object getChildObject(Object parent, String name, Attribute[] attain) throws BuildException, ContentError {
        Object child = null;
     
        ///E.info("checking for child object called " + name + " in " + parent);

        Attribute[] atta = attain;

        if (parent != null) {
            checkAddPackage(parent); // EFF inefficient
        }

        // Three possibilities:
        // 1 there is an attribute called class;
        // 2 the parent has a field called name;
        // 3 the name is a class name;


        if (atta == null) {
            atta = new Attribute[0];
        }


        // process special attributes and instantiate child if class is known
        // (case 1);
        for (int i = 0; i < atta.length; i++) {
            Attribute att = atta[i];
            String attName = att.getName();
            String attValue = att.getValue();

            if (attName.equals("package")) {
                StringTokenizer stok = new StringTokenizer(attValue, ", ");
                while (stok.hasMoreTokens()) {
                    addSearchPackage(stok.nextToken());
                }

            } 
        }
  
        
        if (name.startsWith("meta:")) {
            String fnm = name.substring(5, name.length());
            // funny XML stuff, should have a
            child = new MetaItem(fnm);
        }


        // dont know the class - the parent may know it; (CASE 2)
        if (child == null && parent != null) { // / && hasField(parent, name)) {
            child = getField(parent, name);

            if (child == null) {
                // try it with a lower case name
                String sname = name.substring(0, 1).toLowerCase() + name.substring(1, name.length());
                if (sname.equals(name)) {
                    // nothing to try
                } else {
                    child = getField(parent, sname);
                }
            }
        }
    
        // or perhaps the open tag is a class name? (CASE 3)
        if (child == null) {
        	String cnm = name;
        	
        	if (parent instanceof Component) {
        		// TODO these shouldn't be listed here.
        		// they are the hard-coded element types that are allowed inside components
        		if (cnm.equals("Insertion") || cnm.equals("Meta") || cnm.equals("About")) {
        			child = newInstance(cnm, false);
        			
        		} else {
        			Component c = new Component();
        			c.setDeclaredName(cnm);
        			child = c;
        		}
        		
        	} else if (parent instanceof ComponentContainer) {
        		child = newInstance(cnm, true);
         
        	} else {
        		child = newInstance(cnm, false);        		
        	}	
        }


        if (child == null) {
            E.warning("ReflectionInstantiator failed to get field " + name + " on " + parent + " "
                + (parent != null ? parent.getClass().toString() : ""));
        }


        /* POSERR did this do anything useful?
        if (child instanceof IDd && ((IDd)child).getID() == null) {
        //         setAttributeField(child, "id", name);
        // System.out.println("autoset id to " + name);
        }
         */
        return child;
    }

    public void applyAttributes(Object target, Attribute[] atta, Parameterized ptzd) throws ContentError, FormatException {

        for (int i = 0; i < atta.length; i++) {
            Attribute att = atta[i];
            setAttributeField(target, att.getName(), att.getValue(), ptzd);
        }
    }

    public boolean setAttributeField(Object target, String name, String arg, Parameterized ptzd) throws ContentError, FormatException {
        boolean bret = false;
        if (name.equals("package") || name.equals("provides") || name.equals("archive-hash")) {
           // TODO used to iunclude "class" here too
        	// already done; ADHOC
        } else {
            bret = setField(target, name, arg, ptzd);
        }

        return bret;
    }

       public boolean setField(Object ob, String sfin, Object argin, Parameterized ptzd) throws ContentError, FormatException {
        String sf = sfin;
        Object arg = argin;

        if (arg instanceof Parented) {
            ((Parented) arg).setParent(ob);
        }

        if (impmap != null) {
        	sf = impmap.getInternalAttributeName(ob.getClass(), sf);
        }
   
        //  E.info("setting field " + sf + " in " + ob + " to " + arg);
        if (ob == null) {
            throw new ContentError("null parent for " + sf + " (" + arg + ")");
          }

        if (arg == null) {
            throw new ContentError("reflection instantiator has null arg setting " + sf + " in " + ob);
         }

        if (arg.equals(ob)) {
            throw new ContentError("ReflectionInstantiator setField: " + "the child is the same as the parent " + ob);
         }

        boolean ok = false;
        if (arg instanceof MetaItem && ob instanceof MetaContainer) {
            ((MetaContainer) ob).addMetaItem((MetaItem) arg);
            ok = true;
        } else {
        	ok = setClassField(ob, sf, arg, ptzd);
        }
        return ok;
       }

        
        public boolean setClassField(Object ob, String sfin, Object argin, Parameterized ptzd) throws FormatException, ContentError {
        	  String sf = sfin;
              Object arg = argin;

        
        int icolon = sf.indexOf(":");
        if (icolon >= 0) {
            //	  E.info("got colon field settiung " + sf + " on " + ob + " to " + arg);
            sf = sf.substring(0, icolon) + "_" + sf.substring(icolon + 1, sf.length());
        }


        boolean ok = false;

        Class<?> c = ob.getClass();
        Field f = null;
        try {
            f = c.getField(sf);
        } catch (NoSuchFieldException e) {
        }

        if (f == null) {
            String ssf = sf.substring(0, 1).toLowerCase() + sf.substring(1, sf.length());
            if (ssf.equals(sf)) {
                // nothign to try
            } else {
                try {
                    f = c.getField(ssf);
                } catch (NoSuchFieldException e) {
                }
            }
        }


        if (f == null) {
            if (arg instanceof Namable) {
                ((Namable) arg).setName(sf);
            }
            if (arg instanceof IOFace && ! (ob instanceof IOFace)) {
             	Object intarg = ((IOFace)arg).getInternal();
            
            	if (arg instanceof IOFaceMixed) {
            		ArrayList<Object> poa = ((IOFaceMixed)arg).getPending();
            		if (poa.size() > 0) {
            			for (Object pend : poa) {
            				if (addToList(intarg, pend)) {
            					// OK
            				} else {
            					
            					E.missing("Haven't added " + pend + " to " + intarg + 
            							" need to do something with pending children");            					
            				}
            			}
            		}
            	}
            
            	arg = intarg;
            
            }
            

            if (ob instanceof ArrayList<?>) {
                if (sf.equals("xmlns")) {
                    // skip it;
                } else {
                    @SuppressWarnings("unchecked")
					ArrayList<Object> alo = (ArrayList<Object>)ob;
                	alo.add(arg);
                }
                ok = true;

                /*
                } else if (arg instanceof String && ob instanceof AttributeAddableTo) {
                ((AttributeAddableTo)ob).addAttribute(sf, (String)arg);
                ok = true;
                 */

            } else if (ob instanceof ElementAdder && nonPrimitive(arg)) {
                ((ElementAdder) ob).addElement(arg);
                ok = true;

            } else if (ob instanceof AddableTo && nonPrimitive(arg)) {
                ((AddableTo) ob).add(arg);
                ok = true;

            } else if (nonPrimitive(arg) && addToList(ob, arg)) {
                ok = true;

            } else if (arg instanceof String && ob instanceof Attributed) {
                ((Attributed) ob).addAttribute(new XMLAttribute(sf, (String) arg));


            } else if (sf.equals("xmlns") || sf.equals("xmlns_xsi") || sf.equals("xsi_schemaLocation")) {
                // Ignoring xmlns attributes on main Lems object
                ok = true;
                
            } else if (ob instanceof IOFaceMixed) {
            	((IOFaceMixed)ob).addPending(arg);
                
            } else {
                E.linkToWarning("No such field " + sf + " on " + ob + " while setting " + arg, ob);
                E.reportCached();
                ok = false;
            }

        } else {
            // POSERR avoids warning but bit silly
            if (arg instanceof ArrayList && ((ArrayList<?>) arg).size() == 1) {
                for (Object sub : (ArrayList<?>) arg) {
                    arg = sub;
                }
            }

            try {
                Class<?> ftyp = f.getType();
                if (ftyp == String.class && arg instanceof String) {
                    f.set(ob, arg);

                } else if (ftyp == Double.TYPE && arg instanceof String) {
                    Double d = new Double((String) arg);
                    f.set(ob, d);

                } else if (ftyp == Integer.TYPE && arg instanceof String) {
                    Integer ig = Integer.valueOf((String) arg);
                    f.set(ob, ig);

                } else if (ftyp == Long.TYPE && arg instanceof String) {
                    Long ig = Long.valueOf((String) arg);
                    f.set(ob, ig);


                } else if (ftyp == Double.TYPE && arg instanceof Double) {
                    f.set(ob, arg);

                } else if (ftyp == Boolean.TYPE && arg instanceof Boolean) {
                    f.set(ob, arg);

                } else if (ftyp == Boolean.TYPE && arg instanceof String) {
                    String s = ((String) arg).toLowerCase();
                    if (s.equals("yes") || s.equals("1") || s.equals("true")) {
                        f.set(ob, true);
                    } else {
                        f.set(ob, false);
                    }


                } else if (ftyp == Integer.TYPE && arg instanceof Integer) {
                    f.set(ob, arg);

                } else if (ftyp == Long.TYPE && arg instanceof Integer) {
                    f.set(ob, arg);


                } else if (f.getType().isArray() && arg instanceof ArrayList) {
                    setArrayField(ob, f, (ArrayList<?>) arg);

                    /*
                    } else if (f.getComponentClass().equals(QuantityArray.class)) {
                    QuantityArray qa = new QuantityArray();
                    QuantityReader.populateArray(qa, (String)arg);
                    f.set(ob, qa);
                     */

                } else {


                    boolean bdone = false;
                    if (arg instanceof String) {
                        String sarg = (String) arg;
                        for (Class<?> ci : ftyp.getInterfaces()) {
                            if (checkSet(f, ftyp, ci, ob, sarg, ptzd)) {
                                bdone = true;
                                break;
                            }
                        }

                        if (!bdone) {
                            for (Class<?> ci : ftyp.getSuperclass().getInterfaces()) {
                                if (checkSet(f, ftyp, ci, ob, sarg, ptzd)) {
                                    bdone = true;
                                    break;
                                }

                                /*
                                if (ci.equals(DimensionalExpression.class)) {
                                DimensionalExpression dq = (DimensionalExpression) (ftyp.newInstance());
                                Units dfltUnits = null;
                                for (Annotation ant : f.getAnnotations()) {
                                if (ant instanceof Expression) {
                                dfltUnits = ((Expression) ant).units();
                                }
                                }
                                ExpressionReader.populate(dq, (String) arg, dfltUnits);
                                f.set(ob, dq);
                                bdone = true;
                                break;
                                }
                                 */

                            }
                        }
                    }


                    if (!bdone) {
                        Object onarg = Narrower.narrow(ftyp.getName(), arg);

                        if (onarg != null) {
                            f.set(ob, onarg);
                        } else {
                            // E.info("setting field " + f + " on " + ob + " from " + arg);

                            f.set(ob, arg);
                        }
                    }
                }
                ok = true;
            } catch (Exception e) {
                ok = false;
                e.printStackTrace();
                E.fatalError("Can't set field " + sf + " in " + ob + " from value " + arg + " " + e);
            }
        }


        return ok;
    }

      public boolean addToList(Object ob, Object arg) {
        String fnm = collectionName(arg.getClass());
        String supfnm = collectionName(arg.getClass().getSuperclass());

        boolean added = false;
        try {
            Field f = null;
            try {
                f = ob.getClass().getField(fnm);
            } catch (Exception ex) {
                // ok - maybe in super class;
            }
            if (f == null) {
                try {
                    f = ob.getClass().getField(supfnm);
                } catch (Exception ex) {
                }
            }
            if (f != null) {
                Object fv = f.get(ob);
                if (fv instanceof LemsCollection<?>) {
                	
                	@SuppressWarnings("unchecked")
					LemsCollection<Object> lc = (LemsCollection<Object>)fv;
                    if (lc.add(arg)) {
                        added = true;
                    }
                }
            }
        } catch (Exception ex) {
            E.info("Can't add to list in : " + ob + " arg=" + arg + " fnm=" + fnm);
            ex.printStackTrace();
        }
        if (added) {
            // E.info("added " + arg + " to list");
        }
        return added;
    }

    public String collectionName(Class<? extends Object> c) {
        String fnm = c.getName();

        fnm = fnm.substring(fnm.lastIndexOf(".") + 1, fnm.length());
        fnm = fnm.substring(0, 1).toLowerCase() + fnm.substring(1, fnm.length());
	    
        if (fnm.endsWith("s")) {
        	fnm = fnm + "es";
        } else {
        	fnm = fnm + "s";
        }
        return fnm;
    }

    private boolean checkSet(Field f, Class<? extends Object> ftyp,
        Class<? extends Object> ci, Object ob, String arg, Parameterized ptzd)
        throws IllegalAccessException, InstantiationException {
        boolean ret = false;
        return ret;
    }

    public void setArrayField(Object obj, Field fld, ArrayList<? extends Object> vals) {

        E.missing("setting array field of " + vals.size() + " in " + obj + " fnm=" + fld.getName());
 
    }

    private boolean nonPrimitive(Object arg) {
        boolean ret = true;
        if (arg instanceof String || arg instanceof Integer || arg instanceof Double) {
            ret = false;
        }
        return ret;
    }

    public void setIntFromStatic(Object ret, String id, String sv) throws ContentError, FormatException {
        String svu = sv.toUpperCase();
        Object obj = getField(ret, svu);
        if (obj instanceof Integer) {
            setField(ret, id, obj, null);
        } else {
            throw new ContentError("need an Integer, not  " + obj);
        }
    }

    public void applyAttributes(Object obj, Attribute[] atta) throws ContentError, FormatException {
        for (Attribute att : atta) {
            if (att.getName().equals("package")) {
                // MAYDO use this?
            } else {
                setField(obj, att.getName(), att.getValue(), null);
            }
        }

    }


	@Override
	public void appendContent(Object obj, String content) throws ContentError {
		throw new ContentError("Bare content found while instantiating, content=\"" + content + "\" target=" + obj);
	}
}
