package org.lemsml.xml;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.HashMap;
import java.util.HashSet;

import org.lemsml.util.ContentError;
import org.lemsml.util.E;
 

public class XMLTokenizer {

   static int idq;
   static int isq;
   
   static int ieq;
   static int iabo;
   static int iabc;
   static int iqm;
   static int iexc;
   static int ims;
   static int ieof = -1;
   
   StreamTokenizer streamTokenizer;

   XMLToken[] lastTokens = new XMLToken[20];
   int ilastToken = 0;

   int count;


   static {
      String sord = "\"=<>?!-\'";
      idq = sord.charAt(0);
      ieq = sord.charAt(1);
      iabo = sord.charAt(2);
      iabc = sord.charAt(3);
      iqm = sord.charAt(4);
      iexc = sord.charAt(5);
      ims = sord.charAt(6);
      isq = sord.charAt(7);
   }



   HashMap<String, String> cdataHM;

   String srcString;

   boolean initialized = false;
   
   
   public XMLTokenizer(String s) throws ContentError {
      // EFF remove this - just for debugging;
      srcString = extractCDATAs(s);
   }

   public void init() {
      streamTokenizer = new StreamTokenizer(new StringReader(srcString));
      initializeStreamTokenizer(streamTokenizer);
      initialized = true;
   }


   private String extractCDATAs(String src) throws ContentError {
      StringBuffer sret = new StringBuffer();
      int icur = 0;
      int iscd = src.indexOf("<![CDATA[");

      while (iscd >= icur) {
         sret.append(src.substring(icur, iscd));
         int iecd = src.indexOf("]]>", iscd + 9);
         if (iecd >= 0) {
            String cdata = src.substring(iscd + 9, iecd);
            if (cdataHM == null) {
               cdataHM = new HashMap<String, String>();
            }
            String rpl = "xyz" + cdataHM.size();
            cdataHM.put(rpl, cdata);
            sret.append(rpl);

         } else {
            iecd = iscd + 6;
            throw new ContentError("no closure of cdata beginning character " + iscd + "? ");
         }
         icur = iecd + 3;
         iscd = src.indexOf("<![CDATA[", icur);
      }
      if (icur < src.length()) {
         sret.append(src.substring(icur, src.length()));
      }
      return sret.toString();
   }


   private void setStringValue(XMLToken xmlt, String svin) {
	   String sv = svin;
      if (sv.startsWith("xyz")) {
         if (cdataHM != null && cdataHM.containsKey(sv)) {
            sv = cdataHM.get(sv);
         } else {
            E.warning("looks like a CDATA key, but not present? " + sv);
         }
      }

      xmlt.setStringValue(sv);
   }


   public int lineno() {
	  if (!initialized) {
		  init();
	  }
	   
      return streamTokenizer.lineno();
   }


   public void initializeStreamTokenizer(StreamTokenizer st) {
      st.resetSyntax();
      st.eolIsSignificant(false);
      st.slashStarComments(false);
      st.slashSlashComments(false);
      st.lowerCaseMode(false);
      String slim = "AZaz09";
      st.wordChars(slim.charAt(0), slim.charAt(1));
      st.wordChars(slim.charAt(2), slim.charAt(3));
      st.wordChars(slim.charAt(4), slim.charAt(5));
      // st.wordChars(0x00A0, 0x00FF);


      String wsc = " \t\n";
      for (int i = 0; i < wsc.length(); i++) {
         int ic = wsc.charAt(i);
         st.whitespaceChars(ic, ic);
      }

      st.quoteChar(isq); // single quote
      // st.quoteChar(idq); // double quote

      String swc = "_/.:&;,()+-.[]{}$";
      for (int i = 0; i < swc.length(); i++) {
         int ic = swc.charAt(i);
         st.wordChars(ic, ic);
      }
      
      
      st.quoteChar(isq); // single quote
      st.quoteChar(idq); // double quote

   }


   public XMLToken nextToken() throws ParseException, XMLException {
	   if (!initialized) {
			  init();
		}
	   
	   XMLToken xmlt = new XMLToken();
      int itok = ntok(streamTokenizer);


      if (streamTokenizer.ttype == StreamTokenizer.TT_EOF) {
         xmlt.setType(XMLToken.NONE);


      } else if (itok == idq || itok == isq) {
         xmlt.setType(XMLToken.STRING);
         // quoted string;
         String sss = streamTokenizer.sval;
         setStringValue(xmlt, StringEncoder.xmlUnescape(sss));


      } else if (streamTokenizer.ttype == StreamTokenizer.TT_WORD) {
         xmlt.setType(XMLToken.STRING);
         setStringValue(xmlt, StringEncoder.xmlUnescape(streamTokenizer.sval));

      } else if (streamTokenizer.ttype == StreamTokenizer.TT_NUMBER) {
         xmlt.setType(XMLToken.NUMBER);
         // boolean, int or double, all as doubles;
         double d = streamTokenizer.nval;
         ntok(streamTokenizer);
         if (streamTokenizer.ttype == StreamTokenizer.TT_WORD
               && ((streamTokenizer.sval).startsWith("E-")
                     || (streamTokenizer.sval).startsWith("E+") || (streamTokenizer.sval).startsWith("E"))) { 
        	 // POSERR - could catch wrong things?
                                                                                                           

            String s = streamTokenizer.sval.substring(1, streamTokenizer.sval.length());
            int ppp = Integer.parseInt(s);
            // err ("st.sval " + st.sval);
            // err ("read exponent: " + ppp);
            d *= Math.pow(10., ppp);
         } else {
            streamTokenizer.pushBack();
         }
         xmlt.setDValue(d);


      } else if (itok == iabo) {
         itok = ntok(streamTokenizer);
         String sv = streamTokenizer.sval;

         if (itok == iqm) {
            // should be the first line of a file - read on until
            // the next question mark, just keeping the text in sinfo
            // for now;
            xmlt.setType(XMLToken.INTRO);
            String svalue = "";
            itok = -1;
            while (itok != iqm) {
               itok = ntok(streamTokenizer);
               if (streamTokenizer.sval != null)
                  svalue += streamTokenizer.sval + " ";
            }
            setStringValue(xmlt, svalue);

         } else if (itok == iexc) {
            itok = ntok(streamTokenizer);
            String sval = streamTokenizer.sval;

            String svalue = "";
            if (sval != null && sval.startsWith("[CDATA[")) {
               E.error("shouldn't get CDATA in xml tokenizer");

            } else if (sval.startsWith("--")) {
               xmlt.setType(XMLToken.COMMENT);
               svalue = streamTokenizer.sval.substring(2, streamTokenizer.sval.length()) + " ";
               
               
               while (true) {
            	   if (itok == iabc && svalue.endsWith("--")) {
            		   break;
            	   }
            	   if (itok == -1) {
            		   E.error("-1 token inside comment?");
            		   break;
            	   }
            	   itok = ntok(streamTokenizer);

               
              // while (itok != iabc || !(svalue.endsWith("--"))) {
              //    itok = ntok(streamTokenizer);
                  if (streamTokenizer.ttype == StreamTokenizer.TT_WORD) {
                     svalue += " " + streamTokenizer.sval;
                   //  pstok = streamTokenizer.sval;
                  } else if (streamTokenizer.ttype == StreamTokenizer.TT_NUMBER) {
                     svalue += " " + streamTokenizer.nval;
                   //  pstok = "";
                  }
               }
               xmlt.setStringValue(svalue.substring(0, svalue.length() -2));
               streamTokenizer.pushBack();

            } else if (sval.startsWith("DOCTYPE")) {
            	 xmlt.setType(XMLToken.COMMENT); // MAYDO doctype...
            	 while (itok != iabc) {
                     itok = ntok(streamTokenizer);
                     if (streamTokenizer.ttype == StreamTokenizer.TT_WORD) {
                        svalue += " " + streamTokenizer.sval;
                     } else if (streamTokenizer.ttype == StreamTokenizer.TT_NUMBER) {
                        svalue += " " + streamTokenizer.nval;
                     }
                  }
                  xmlt.setStringValue(svalue.substring(0, svalue.length() -2));
                  streamTokenizer.pushBack();


            } else if (itok == ims) {
               itok = ntok(streamTokenizer);
               if (itok == ims) {
                  E.info("reading comment start as separate minus signs");
                  int[] ipr = new int[3];
                  while (ipr[0] != ims || ipr[1] != ims || ipr[2] != iabc) {
                     itok = ntok(streamTokenizer);

                     if (streamTokenizer.ttype == StreamTokenizer.TT_WORD) {
                        svalue += streamTokenizer.sval + " ";
                     } else if (streamTokenizer.ttype == StreamTokenizer.TT_NUMBER) {
                        svalue += " " + streamTokenizer.nval;
                     }
                     if (streamTokenizer.sval != null && streamTokenizer.sval.endsWith("--")) {
                        ipr[1] = ims;
                        ipr[2] = ims;
                     } else {
                        ipr[0] = ipr[1];
                        ipr[1] = ipr[2];
                        ipr[2] = itok;
                     }
                  }
                  streamTokenizer.pushBack();
               } else {
                  E.error("found <!- but not followed by -  at " + streamTokenizer.lineno() +
                		  " (followed by " + streamTokenizer.sval + ")");
               }
            } else {
               E.error("found <! but not followed by -  at " + streamTokenizer.lineno() +
            		   " (followed by " + streamTokenizer.sval + ")");
            }
            setStringValue(xmlt, svalue);


         } else if (sv.startsWith("/")) {
            xmlt.setType(XMLToken.CLOSE);
            setStringValue(xmlt, sv.substring(1, sv.length()));

         } else {
            if (sv.endsWith("/")) {
               xmlt.setType(XMLToken.OPENCLOSE);
               setStringValue(xmlt, sv.substring(0, sv.length() - 1));
            } else {
               xmlt.setType(XMLToken.OPEN);
               setStringValue(xmlt, sv);
            }
         }

         itok = ntok(streamTokenizer);
         if (itok == iabc) {
            // fine - end of tag;

         } else if (streamTokenizer.ttype == StreamTokenizer.TT_WORD) {
            String[] attNV = new String[1600]; // EFF check eff
            int natt = 0;
            HashSet<String> attHS = null;  
            
            while (itok != iabc) {

               if (streamTokenizer.ttype == StreamTokenizer.TT_WORD) {
                  if (streamTokenizer.sval.equals("/")) {
                     xmlt.setType(XMLToken.OPENCLOSE);

                  } else {
                	  if (attHS == null) {
                		  attHS = new HashSet<String>();
                	  }
                	 String attname = streamTokenizer.sval;
                	 if (attHS.contains(attname)) {
                		 E.shortError("Duplicate attribute " + attname + 
                				 " each attribute can only appear once");
                	 } else {
                		 attHS.add(attname);
                	 }

                	 //    System.out.println("Last tokens: "+getLastTokens());
                	   //  System.out.println("attname: "+attname);
                     attNV[2 * natt] = attname;
                     itok = ntok(streamTokenizer);
                     if (itok == ieq) {
                        itok = ntok(streamTokenizer);

                        if (itok == idq || itok == isq) {
                           attNV[2 * natt + 1] = streamTokenizer.sval;
                           natt++;
                        } else {
                           E.shortError("expecting quoted string " + " while reading attributes "
                                 + "but got token  no " +itok + ": " +  stok(itok) + " sval=" + streamTokenizer.sval
                                 + " nval=" + streamTokenizer.nval + " at line " + streamTokenizer.lineno());
                          //  E.info("original string was " + srcString);
                        }
                     } else {
                        E.shortError("at " + streamTokenizer.lineno()
                              + " expecting = while reading attributes " + "but got " + stok(itok)
                              + " sval=" + streamTokenizer.sval + " nval=" + streamTokenizer.nval);
                       //  E.info("original string was " + srcString);
                     }
                  }
               } else {
                  E.shortError("at line " + streamTokenizer.lineno()
                        + " found non-word while reading attributes " + stok(itok)
                        + "  item so far = " + this);
                  E.info("original string was " + srcString);
               }
               itok = ntok(streamTokenizer);
            }
            String[] sat = new String[2 * natt];
            for (int i = 0; i < 2 * natt; i++) {
               sat[i] = attNV[i];
            }
            xmlt.setAttributes(sat);

         } else {
        	 StringBuffer smsg = new StringBuffer();
        	 smsg.append("expecting a word but got " + stok(itok) + " at line " + lineno());
        	 smsg.append(getLastTokens());
            throw new ParseException(smsg.toString());

         }

      } else {
         // just return the token as a string;
         xmlt.setType(XMLToken.STRING);
         setStringValue(xmlt, stok(itok));

      }
      lastTokens[ilastToken++ % 20] = xmlt;
      return xmlt;
   }


   private String getLastTokens() {
	   StringBuffer sb = new StringBuffer();
	   for (int i = 0; i < 20; i++) {
		   XMLToken tok = lastTokens[(ilastToken + i + 1) % 20];
		   if (tok != null) {
			   sb.append(tok.toString());
			   sb.append("\n");
		   }
	   }
	   return sb.toString();
   }


   private int ntok(StreamTokenizer st) throws XMLException {
      int itok = -1;
      try {
         itok = st.nextToken();
      } catch (IOException e) {
         itok = -999;
      }
 
      if (itok == -999) {
    	  throw new XMLException("can't read next token from " + st);
      }
      
      return itok;
   }


   private String stok(int itok) {
      return "" + (char)itok;
   }

  
   
	public String readUntil(String closeTagString) throws XMLException {
		StringBuffer sb = new StringBuffer();

		String closer = "/" + closeTagString;

		while (true) {
			int itok = ntok(streamTokenizer);
			if (itok == ieof) {
				break;
			} else {

				if (itok == iabo) {
					// could be the closer we're looking for...
		 			@SuppressWarnings("unused")
					int jtok = ntok(streamTokenizer);
					String ss = streamTokenizer.sval;

					if (ss != null && ss.equals(closer)) {
						int ktok = ntok(streamTokenizer);
						if (ktok == iabc) {
							// OK
						} else {
							E.error("matched closer, but no > after?");
						}

						// we're done
						break;
					} else {
						sb.append("<" + ss);
					}

				} else {
					if (streamTokenizer.ttype == StreamTokenizer.TT_WORD) {
						sb.append(" " + streamTokenizer.sval);

					} else if (streamTokenizer.ttype == StreamTokenizer.TT_NUMBER) {
						sb.append(" " + streamTokenizer.nval);

					} else {
						String schar = (new Character((char) itok)).toString();

						if (itok == idq) {
							sb.append("\"");
							if (streamTokenizer.sval != null) {
								sb.append(streamTokenizer.sval);
							}
							sb.append("\"");

						} else {
							sb.append(schar);
						}
					}

				}
			}

		}
		String ret = sb.toString();
		return ret;
	}
  
   
}
