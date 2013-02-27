package org.lemsml.jlems.core.logging;

import org.lemsml.jlems.core.sim.ContentError;




public final class E {

   private static boolean debug = true;

   public static long time0 = 0;

   static String lastShortSource;
   static String cachedAction;

   static String lastErr;
   static int nrep;

   static String lastWarning;
   static int nwrep;

   static MessageForkHandler handler = new MessageForkHandler();

   
   private E() {
	   
   }

   public static void setDebug(boolean b) {
	   debug = b;
   }
  
   
   private static long getTime() {
      return System.currentTimeMillis();
   }


   public static void zeroTime() {
      time0 = getTime();
   }

   public static String getStringTime() {
      if (time0 == 0) {
	 zeroTime();
      }
      long dt = (getTime() - time0);
      return "" + dt;
   }

   public static void info(String s) {
	   if (debug) {
		   handler.msg(MessageType.INFO,  s + getShortSource());
	   } else {
		   handler.msg(MessageType.INFO, s);
	   }
   }
   
   
   public static void srcinfo(String s) {
	      handler.msg(MessageType.INFO,  s + getShortSource());
   }

   public static void coreInfo(String s) {
	      handler.msg(MessageType.COREINFO, s); //  + getShortSource());
   }

   public static void coreError(String s) {
	      handler.msg(MessageType.COREERROR,  s + getShortSource());
   }

   public static void log(String s) {
	   handler.msg(MessageType.LOG, s);
   }


   public static void infoTime(String s) {
      handler.msg(MessageType.INFO, s + " at " + getStringTime());
   }


   public static void longInfo(String s) {
      handler.msg(MessageType.INFO, s);
      showSource(16);
   }


   public static void message(String s) {
      handler.msg(MessageType.MESSAGE,  s);
   }

   public static void oneLineWarning(String s) {
      handler.msg(MessageType.WARNING, s + getShortSource());
   }

   public static void oneOffOneLineWarning(String s) {
	      nonRepeatShortWarning(s);
   }
   
   public static void oneLineError(String s) {
	      handler.msg(MessageType.ERROR, s + getShortSource());
   }

  public static void shortWarning(String s) {
      handler.msg(MessageType.WARNING, s + getShortSource());
   }

  public static void shortError(String s) {
     handler.msg(MessageType.ERROR, s + getShortSource());
  }

  public static void medWarning(String s) {
     handler.msg(MessageType.WARNING,  s);
     showSource(4);
  }

  public static void repeatableWarning(String s) {
	  handler.msg("WARNING - " + s);
  }
  
   public static void warning(String s) {
	      if (lastWarning != null && s.startsWith(lastWarning)) {
	        nwrep += 1;
	        if (nwrep == 3 || nwrep == 10 || nwrep ==30 || nwrep == 100) {
	           handler.msg(" .......  last warning repeated " + nwrep + " times");
	        }
	      } else {
	         if (nwrep > 0) {
	            handler.msg("total repeats of last warning " + nwrep);
	         }
	         nwrep = 0;
	         lastWarning = s;
	         if (lastWarning.length() > 20) {
	        	 lastWarning = lastWarning.substring(0, 20);
	         }
	         handler.msg(MessageType.WARNING, s + getShortSource());
	      }
	   }
   
   

   public static void nonRepeatShortWarning(String s) {
	      if (lastWarning != null && s.startsWith(lastWarning)) {
	        nwrep += 1;
	        if (nwrep == 3 || nwrep == 10 || nwrep ==30 || nwrep == 100) {
	           handler.msg(" .......  last warning repeated " + nwrep + " times");
	        }
	      } else {
	         if (nwrep > 0) {
	            handler.msg("total repeats of last warning " + nwrep);
	         }
	         nwrep = 0;
	      lastWarning = s;
	      if (lastWarning.length() > 20) {
	    	  lastWarning = lastWarning.substring(0, 20);
	      }
	      handler.msg("WARNING - " + s + " " + getShortSource());
	      }
	   }






   public static void linkToWarning(String s, Object obj) {
      handler.msg(MessageType.WARNING, s);
      String fcn = obj.getClass().getName();
      String scn = fcn.substring(fcn.lastIndexOf(".") + 1, fcn.length());
      handler.msg("  at " + fcn + ".nomethod(" + scn + ".java:1)");
   }

   public static void linkToError(String s, Object obj) {
	      handler.msg(MessageType.ERROR, s);
	      String fcn = obj.getClass().getName();
	      String scn = fcn.substring(fcn.lastIndexOf(".") + 1, fcn.length());
	      handler.msg("  at " + fcn + ".nomethod(" + scn + ".java:1)");
	   }


   public static void simpleError(String s) {
	   multiError(s, false);
   }

   public static void error(String s) {
	   multiError(s, true);
   }

   public static void multiError(String s, boolean b) {
      if (lastErr != null && lastErr.equals(s)) {
        nrep += 1;
        if (nrep == 3 || nrep == 10 || nrep ==30 || nrep == 100) {
           handler.msg(" .......  last error repeated " + nrep + " times");
        }
      } else {
         if (nrep > 0) {
            handler.msg("total repeats of last error " + nrep);
         }
         nrep = 0;
       lastErr = s;
       handler.msg(MessageType.ERROR, s);

       if (b) {
    	   showSource();
       }
      }
   }

   public static void debugError(String s) {
      handler.msg(MessageType.ERROR, s);
      handler.msg("stack trace follows: ");
      stackTrace();
   }


   public static void fatalError(String s) {
      handler.msg(MessageType.FATAL, s);
      stackTrace();
   }


   public static void override(String s) {
      handler.msg(MessageType.OVERRIDE, "method should be overridden: " + s);
      showSource();
   }

   public static void override() {
      handler.msg(MessageType.OVERRIDE, "method should be overridden: ");
      showSource();
   }


   public static void deprecate(String s) {
      handler.msg(MessageType.DEPRECATED, "using deprecated class: " + s + " " +
               getShortSource());
      showShortSource();
   }

  public static void deprecate() {
      handler.msg(MessageType.DEPRECATED, "using deprecated method " + getShortSource());
      showShortSource();
  }


  public static void missing(String s) {
	   handler.msg(MessageType.MISSING, s + getShortSource());
   }


   public static void missing() {
	   handler.msg(MessageType.MISSING, "" + getShortSource());
   }


   public static void stackTrace() {
      (new Exception()).printStackTrace();
   }


   public static void stackTrace(String msg) {
	      (new Exception(msg)).printStackTrace();
   }

   public static void showSource() {
      showSource(10);
   }

   public static void showShortSource() {
      showSource(2);
   }

   public static void showSource(int n) {
      StackTraceElement[] stea = new Exception().getStackTrace();
      for (int i = 2; i < 2 + n && i < stea.length; i++) {
       handler.msg("  at " + stea[i].toString());
 
      }
   }


   public static String getShortSource() {
	   StackTraceElement[] stea = new Exception().getStackTrace();
	   int iel = 0;
	   String ss = stea[iel].toString();
	   while (ss.indexOf(".E.") > 0 && iel < stea.length - 1) {
		   iel += 1;
		   ss = stea[iel].toString();
	   }
      if (ss.equals(lastShortSource)) {
          	 ss = "";
      } else {
	         lastShortSource = ss;
      }
      return " at: " + ss;
   }

  


   public static void newLine() {
      handler.msg("...");
   }


   public static void cacheAction(String s) {
      cachedAction = s;
   }

   public static void reportCached() {
      handler.msg("may relate to: " + cachedAction);
   }


   public static void dump(String[] labs) {
      if (labs != null) {
         for (int i = 0; i < labs.length; i++) {
            handler.msg("element " + i + ": " + labs[i]);
         }
      }
   }





   public static void dump(String s, String[] labs) {
      handler.msg(s + " " + labs + " " + getShortSource());
      E.dump(labs);
   }

   public static void dump(String s, int[] ia) {
      handler.msg("int[] array: " + s + " " + ia.length + " " + getShortSource());
      for (int i = 0; i < ia.length; i++) {
         handler.msg("   elt " + i + " = " + ia[i]);
      }
   }

   public static void dump(String s, double[] ia) {
      handler.msg("double[] array: " + s + " " + ia.length + " " + getShortSource());
      for (int i = 0; i < ia.length; i++) {
         handler.msg("   elt " + i + " = " + ia[i]);
      }
   }


public static void typeError(Object obj) throws ContentError {
	 throw new ContentError("wrong type " + obj);

}


	public static void dump(double[][] fstate) {
		handler.msg("double[][] array " + fstate.length + " " + getShortSource());
		bareDump(fstate);
	}

	public static void dump(String s, double[][] fstate) {
		handler.msg("double[][] array " + s + " " + fstate.length + " " + getShortSource());
		bareDump(fstate);
	}

	private static void bareDump(double[][] fstate) {
		for (int i = 0; i < fstate.length; i++) {
			double[] da = fstate[i];
			handler.msg(" " + i + "(" + da.length + ") " + printArray(da));
		}
	}

	private static String printArray(double[] da) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < da.length; i++) {
			// sb.append(String.format("%8.3g, ", da[i]));

			// String.format not available in GWT
			sb.append("" + (float)da[i]);
		}
		return sb.toString();
	}



	public static void addMessageHandler(MessageHandler mh) {
		 handler.addHandler(mh);
	}


	public static void setMessageHandler(MessageHandler mh) {
 		handler.setHandler(mh);
	}

	public static void report(String msg, Exception ex) {
		E.error(msg);
		ex.printStackTrace();
		
	}

	public static void trace() {
		stackTrace();
		
	}

	
}
