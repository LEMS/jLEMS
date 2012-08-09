
package org.lemsml.xml;


import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;


public class XMLWriter {

   boolean conciseTags;
   boolean quoteStrings;


   public XMLWriter() {
      conciseTags = false;
      quoteStrings = true;
   }


   public void setConciseTags(boolean b) {
      conciseTags = b;
   }


   public void setQuoteStrings(boolean b) {
      quoteStrings = b;
   }


   public static void err(String s) {
      System.out.println(s);
   }


   public static XMLWriter newInstance() {
      return new XMLWriter();
   }


   public static String serialize(Object ob) {
      return getSerialization(ob);
   }


   public static String getSerialization(Object ob) {
      return newInstance().writeObject(ob);
   }



   public String writeObject(Object obj) {
      StringBuffer sb = new StringBuffer();
      appendObject(sb, "", null, obj);
      return sb.toString();
   }

 
public void appendObject(StringBuffer sbv, String psk, String knownAs, Object ob) {
      boolean write = true;

      if (ob instanceof String) {
         sbv.append(psk);
         sbv.append("<String>");
         sbv.append(ob);
         sbv.append("</String>\n");
         return;
      }


      if (!write) {
         // skip this object - it contains no information;
         return;
      }



       String tag = "error";
      if (knownAs != null) {
         tag = knownAs;
      } else {
         tag = ob.getClass().getName();

         if (conciseTags) {
            int ilast = tag.lastIndexOf(".");
            if (ilast >= 0) {
               tag = tag.substring(ilast + 1, tag.length());
            }
         }
      }

      sbv.append(psk);
      sbv.append("<" + tag + ">\n");

      /*
       * if (writeClass) { sbv.append("<"+tag + " class=\"" +
       * ob.getClass().getName() + "\">\n"); } else { sbv.append("<"+tag +
       * ">\n"); }
       */


      String sk = psk + "   ";

      Field[] flds = ob.getClass().getFields();

      for (int i = 0; i < flds.length; i++) {
         String fieldName = flds[i].getName();
         Object ret = null;
         try {
            ret = flds[i].get(ob);
         } catch (Exception e) {
            err("WARNING - failed to get field " + fieldName + " in  " + ob);
         }
         if (Modifier.isFinal(flds[i].getModifiers()))
            ret = null;

         if (ret instanceof Double) {
            appendNV(sbv, sk, fieldName, ((Double)ret).doubleValue());

         } else if (ret instanceof Integer) {
            appendNV(sbv, sk, fieldName, ((Integer)ret).intValue());

         } else if (ret instanceof Boolean) {
            appendNV(sbv, sk, fieldName, ((Boolean)ret).booleanValue());

         } else if (ret instanceof String) {
            appendNV(sbv, sk, fieldName, (String)ret);

         } else if (ret instanceof double[]) {
            appendNV(sbv, sk, fieldName, (double[])ret);

         } else if (ret instanceof int[]) {
            appendNV(sbv, sk, fieldName, (int[])ret);

         } else if (ret instanceof boolean[]) {
            appendNV(sbv, sk, fieldName, (boolean[])ret);

         } else if (ret instanceof String[]) {
            appendNV(sbv, sk, fieldName, (String[])ret);

         } else if (ret instanceof double[][]) {
            appendNV(sbv, sk, fieldName, (double[][])ret);

         } else if (ret instanceof int[][]) {
            appendNV(sbv, sk, fieldName, (int[][])ret);

         } else if (ret != null) {
            appendObject(sbv, sk, fieldName, ret);

         }
      }


      if (ob instanceof List) {
         for (Object listobj : (List<?>)ob) {
            appendObject(sbv, psk + "   ", null, listobj);
         }
      }


      sbv.append(psk);
      sbv.append("</" + tag + ">\n");
   }



   private void appendNV(StringBuffer sbv, String sk, String name, String value) {
      sbv.append(sk + "<" + name + ">");
      appendString(sbv, value);
      sbv.append("</" + name + ">\n");
   }


   private void appendNV(StringBuffer sbv, String sk, String name, boolean value) {
      sbv.append(sk + "<" + name + ">");
      sbv.append(value ? "1" : "0");
      sbv.append("</" + name + ">\n");
   }



   private void appendNV(StringBuffer sbv, String sk, String name, int value) {
      sbv.append(sk + "<" + name + ">");
      sbv.append("" + value);
      sbv.append("</" + name + ">\n");
   }


   private void appendNV(StringBuffer sbv, String sk, String name, double value) {
      sbv.append(sk + "<" + name + ">");
      sbv.append("" + value);
      sbv.append("</" + name + ">\n");
   }



   private void appendNV(StringBuffer sbv, String sk, String name, String[] value) {
      sbv.append(sk + "<" + name + ">\n");

      for (int i = 0; i < value.length; i++) {
         sbv.append(sk);
         sbv.append("   ");
         appendString(sbv, (value[i] != null ? value[i] : ""));
         sbv.append("\n");
      }
      sbv.append(sk + "</" + name + ">\n");
   }


   private void appendNV(StringBuffer sbv, String sk, String name, int[] value) {
      sbv.append(sk + "<" + name + ">");
      for (int i = 0; i < value.length; i++) {
         if (i % 16 == 0)
            sbv.append("\n" + sk + "   ");
         sbv.append(" " + value[i] + " ");
      }
      sbv.append("\n");
      sbv.append(sk + "</" + name + ">\n");
   }


   private void appendNV(StringBuffer sbv, String sk, String name, boolean[] value) {
      sbv.append(sk + "<" + name + ">\n" + sk + "  ");
      for (int i = 0; i < value.length; i++) {
         sbv.append(" " + (value[i] ? 1 : 0) + " ");
      }
      sbv.append("\n");
      sbv.append(sk + "</" + name + ">\n");
   }


   private void appendNV(StringBuffer sbv, String sk, String name, double[] value) {
      sbv.append(sk + "<" + name + ">");
      for (int i = 0; i < value.length; i++) {
         if (i % 4 == 0)
            sbv.append("\n" + sk + "   ");
         sbv.append(" " + value[i] + " ");
      }
      sbv.append("\n");
      sbv.append(sk + "</" + name + ">\n");
   }



   private void appendNV(StringBuffer sbv, String sk, String name, int[][] value) {
      sbv.append(sk + "<" + name + ">\n");
      for (int i = 0; i < value.length; i++) {
         sbv.append(sk);
         sbv.append("   <row>");
         int[] ii = value[i];
         for (int k = 0; k < ii.length; k++) {
            if (k % 16 == 0)
               sbv.append("\n " + sk + "      ");
            sbv.append(" " + ii[k] + " ");
         }
         sbv.append("\n");
         sbv.append(sk);
         sbv.append("   ");
         sbv.append("</row>\n");
      }
      sbv.append(sk + "</" + name + ">\n");
   }



   private void appendNV(StringBuffer sbv, String sk, String name, double[][] value) {
      sbv.append(sk + "<" + name + ">\n");
      for (int i = 0; i < value.length; i++) {
         sbv.append(sk);
         sbv.append("   <row>");
         double[] ii = value[i];
         for (int k = 0; k < ii.length; k++) {
            if (k % 4 == 0)
               sbv.append("\n " + sk + "      ");
            sbv.append(" " + ii[k] + " ");
         }
         sbv.append("\n");
         sbv.append(sk);
         sbv.append("   ");
         sbv.append("</row>\n");
      }
      sbv.append(sk + "</" + name + ">\n");

   }



   private void appendString(StringBuffer sbv, String sssin) {
	   String sss = sssin;
      if (sss == null) {
         if (quoteStrings) {
            sbv.append("\"null\"");
         } else {
            sbv.append("null");
         }

      } else {
         sss = StringEncoder.xmlEscape(sss);

         if (!quoteStrings) {
            sbv.append(sss);
         } else {
            sbv.append("\"");
            sbv.append(sss);
            sbv.append("\"");
         }
      }
   }


}
