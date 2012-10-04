package org.lemsml.jlemsio.xmlio;

import java.util.ArrayList;
 

import org.lemsml.jlems.util.E;
import org.lemsml.jlems.util.StringUtil;
 


public final class Narrower {


   static String destCache;


   public static Object narrow(String fcln, Object arg) throws NumberFormatException {
      Object ret = null;

      destCache = fcln;

       // POSERR - should use field types here!
      if (fcln.endsWith(".int")) {
         ret = Integer.valueOf(makeInt(arg));

      } else if (fcln.endsWith("boolean")) {
         ret = Boolean.valueOf(makeBoolean(arg));

      } else if (fcln.endsWith("double")) {
         ret = new Double(makeDouble(arg));

      } else if (fcln.startsWith("java.lang.String")) {
         ret = arg;

      } else if (fcln.startsWith("[D")) {
         ret = makeDoubleArray(arg);

      } else if (fcln.startsWith("[I")) {
         ret = makeIntArray(arg);

      } else if (fcln.startsWith("[Ljava.lang.String")) {
         ret = makeStringArray(arg);

      } else if (fcln.startsWith("[Z")) {
         ret = makeBooleanArray(arg);

      } else if (fcln.startsWith("[[D")) {
         ret = makeDDArray(arg);

      } else if (fcln.startsWith("[[I")) {
         ret = makeIIArray(arg);

      } else if (fcln.endsWith("ArrayList")) {
         ret = makeArrayList(arg);

         /*
          * } else if (fcln.endsWith("SColor")) { ret = new SColor((String)arg);
          */

      }

      return ret;

   }



   public static void err(String s) {
      E.error(s);
   }



   public static double parseDouble(String s) {
      double dret = 0.;
      int ii = s.indexOf("e");
      if (ii < 0)
         ii = s.indexOf("E");
      if (ii < 0) {
         dret = (new Double(s)).doubleValue();

      } else {
         String sa = s.substring(0, ii - 1);
         String sp = s.substring(ii + 1, s.length());
         int ppp = Integer.parseInt(sp);
         dret = (new Double(sa)).doubleValue();
         dret *= Math.pow(10., ppp);

      }
      return dret;
   }

  
   public static String[] makeStringArray(Object ob) {
      String[] sret = null;
      if (ob instanceof String[]) {
         sret = (String[])ob;

      } else if (ob instanceof String) {
         sret = new String[1];
         sret[0] = (String)ob;

      } else if (ob instanceof ArrayList<?>) {
         @SuppressWarnings("unchecked")
		ArrayList<? extends Object> v = (ArrayList<? extends Object>)ob;
         int n = v.size();
         sret = new String[n];
         int iout = 0;
         for (Object sub : v) {
            sret[iout++] = (String)sub;
         }

      } else {
         err("ERROR - cant make string array from " + ob);
      }
      return sret;
   }


   public static double[] makeDoubleArray(Object ob) {
      double[] dret = null;
      if (ob instanceof double[]) {
         dret = (double[])ob;

      } else if (ob instanceof ArrayList<?>) {
         ArrayList<?> v = (ArrayList<?>)ob;
         int n = v.size();
         dret = new double[n];
         int iout = 0;
         for (Object sub : v) {
            dret[iout++] = makeDouble(sub);
         }

      } else if (ob instanceof String) {
         dret = readDoubleArray((String)ob);

      } else if (ob != null) {
         dret = new double[1];
         dret[0] = makeDouble(ob);
      }
      return dret;
   }


   public static int[] makeIntArray(Object ob) {
      int[] iret = null;
      if (ob instanceof int[]) {
         iret = (int[])ob;

      } else if (ob instanceof ArrayList<?>) {
         ArrayList<?> v = (ArrayList<?>)ob;
         int n = v.size();
         iret = new int[n];
         int iout = 0;
         for (Object sub : v) {
            iret[iout++] = makeInt(sub);
         }

         // MISSING following needs repeating for other array types
      } else if (ob instanceof String) {
         String sob = (String)ob;
          
         String[] bits = StringUtil.splitCommaWords(sob);
         int ntok = bits.length;
         
         iret = new int[ntok];
         for (int i = 0; i < bits.length; i++) {
            iret[i] = Integer.parseInt(bits[i]);
         }

      } else if (ob != null) {
         iret = new int[1];
         iret[0] = makeInt(ob);
      }
      return iret;
   }



   public static boolean[] makeBooleanArray(Object ob) {
      boolean[] bret = null;
      if (ob instanceof ArrayList<?>) {
         ArrayList<?> v = (ArrayList<?>)ob;
         int n = v.size();
         bret = new boolean[n];
         int iout = 0;
         for (Object sub : v) {
            bret[iout++] = makeBoolean(sub);
         }
      } else if (ob != null) {
         bret = new boolean[1];
         bret[0] = makeBoolean(ob);
      }
      return bret;
   }


   public static int makeInt(Object arg) throws NumberFormatException {
      int iret = 0;
      if (arg instanceof Integer) {
         iret = ((Integer)arg).intValue();

      } else if (arg instanceof Double) {
         iret = (int)(((Double)arg).doubleValue());

      } else if (arg instanceof String) {
         String s = (String)arg;
         if (s.equals("false")) {
            iret = 0;
         } else if (s.equals("true")) {
            iret = 1;
         } else {
            iret = parseInt((String)arg);
         }
      } else {
         err("cant make an int from " + arg + " " + arg.getClass());
      }
      return iret;
   }



   public static int parseInt(String sin) {
	   String s = sin;
      int iret = 0;
      if (s.startsWith("0x")) {
         s = s.substring(2, s.length());
         iret = Integer.parseInt(s, 16);
      } else {
         iret = Integer.parseInt(s, 10);
      }
      return iret;
   }



   public static double makeDouble(Object arg) {
      double dret = 0;
      if (arg instanceof Double) {
         dret = ((Double)arg).doubleValue();

      } else if (arg instanceof String) {
         dret = parseDouble((String)arg);

      } else {
         err(" cant make a double from " + arg + " " + arg.getClass());
         (new Exception()).printStackTrace();
      }
      return dret;
   }


   public static boolean makeBoolean(Object arg) {
      boolean bret = false;
      if (arg instanceof Double) {
         bret = ((((Double)arg).doubleValue()) > 0.5);

      } else if (arg instanceof String) {
         String sob = ((String)arg).trim();
         bret = (sob.equals("1") || sob.equals("true"));
      } else {
         err(" instantiator cant make a boolean from " + arg);
      }
      return bret;
   }



   public static double[][] makeDDArray(Object ob) {
      double[][] dret = null;
      if (ob == null) {
         dret = new double[0][0];

      } else if (ob instanceof Double || ob instanceof String) {
         dret = new double[1][1];
         dret[0][0] = makeDouble(ob);

      } else if (ob instanceof ArrayList<?>) {
         ArrayList<?> v = (ArrayList<?>)ob;
         dret = new double[v.size()][];
         int iout = 0;
         for (Object sub : v) {
            dret[iout++] = makeDoubleArray(sub);
         }
      } else {
         err("cant make DD array from " + ob);
      }
      return dret;
   }


   public static int[][] makeIIArray(Object ob) {
      int[][] iret = null;
      if (ob == null) {
         iret = new int[0][0];

      } else if (ob instanceof Double || ob instanceof String) {
         iret = new int[1][1];
         iret[0][0] = makeInt(ob);

      } else if (ob instanceof ArrayList<?>) {
         ArrayList<?> v = (ArrayList<?>)ob;
         iret = new int[v.size()][];
         int iout = 0;
         for (Object sub : v) {
            iret[iout++] = makeIntArray(sub);
         }
      } else {
         err("cant make II array from " + ob);
      }
      return iret;
   }


   public static ArrayList<Object> makeArrayList(Object arg) {
      ArrayList<Object> vret = new ArrayList<Object>();

      if (arg instanceof ArrayList<?>) {
         vret.addAll((ArrayList<?>)arg);

      } else {
         vret.add(arg);
      }
      return vret;
   }



   public static double[] readDoubleArray(String sin) {
	   String s = sin;
      if (s.startsWith("{")) {
         s = s.substring(1, s.indexOf("}"));
      }
      s = s.trim();

      String[] sa = s.split("[ ,\t\n\r]+");

      /*
       * E.info("after splitting " + s); for (int i = 0; i < sa.length; i++) {
       * E.info("item " + i + " " + sa[i]); }
       */

      int nt = sa.length;
      double[] value = new double[nt];

      try {
         for (int i = 0; i < nt; i++) {
            value[i] = (new Double(sa[i])).doubleValue();
         }
      } catch (Exception ex) {
         E.error("float reading cant extract " + nt + " doubles from " + s);
         for (int i = 0; i < nt; i++) {
            E.info("string " + i + "=xxx" + sa[i] + "xxx");
         }
      }

      return value;
   }

}
