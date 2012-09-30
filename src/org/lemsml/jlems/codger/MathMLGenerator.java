package org.lemsml.jlems.codger;

import org.lemsml.jlems.expression.DoubleEvaluable;
import org.lemsml.jlems.expression.ParseError;
import org.lemsml.jlems.expression.Parser;
import org.lemsml.jlems.util.E;

public class MathMLGenerator {
	
	
	 
	    public void testMathML() throws ParseError {

	        Parser p = new Parser();
	        String src = "3 + (1.3e-4 + 5) + (3 *4)+ 4/(4.*45) + 34.2E-2 + sin(a + b) / cos(b + c)";
	        DoubleEvaluable root = p.parseExpression(src);

	       E.info("parsing " + src);
	       E.info("Parsed to: " + root.toString()+", ("+root.getClass()+")");

	       // TODO need a mechanism to generate MathML that doesn't involve embedding it deeply in executable components
	       // E.info("\n" + root.getMathML("    ", "  "));
	    }

	    /*
		public String getMathML(String indent, String innerIndent) {

			return indent + "<apply>\n" + indent + innerIndent + "<" + fname + "/>\n"
			        + ((Evaluable) right).getMathML(indent + innerIndent, innerIndent) + "\n" + 
			        indent + innerIndent
			        + "</apply>";
		}
		
		
		
		
		
		  public String getMathML(String indent, String innerIndent) {

        return indent + "<apply>\n"
                + indent + innerIndent + "<" + getMathMLElementName() + "/>\n"
                + ((Evaluable) left).getMathML(indent + innerIndent, innerIndent) + "\n"
                + ((Evaluable) right).getMathML(indent + innerIndent, innerIndent) + "\n"
                + indent + "</apply>";
    }
    
    
    
    
        public String getMathML(String indent, String innerIndent) {
                return indent+"<cn> "+(float)dval+" </cn>";
        }

    
    
    
         @Override
        protected String getMathMLElementName() {
                return "divide";
        }

         @Override
        protected String getMathMLElementName() {
                return "eq";
        }
        
        	@Override
	protected String getMathMLElementName() {
		return "gt";
	}
	
	   @Override
    protected String getMathMLElementName() {
            return "geq";
    }
	
	  @Override
    protected String getMathMLElementName() {
            return "lt";
    }
	    @Override
        protected String getMathMLElementName() {
                return "leq";
        }
	
             @Override
        protected String getMathMLElementName() {
                return "minus";
        }


        @Override
        protected String getMathMLElementName() {
                return "mod";
        }

  @Override
    protected String getMathMLElementName() {
            return "neq";
    }
    
        @Override
        protected String getMathMLElementName() {
                return "or"; // TODO: check..
        }

    
        @Override
        protected String getMathMLElementName() {
                return "plus";
        }
    @Override
        protected String getMathMLElementName() {
                return "power";
        }
        
           @Override
        protected String getMathMLElementName() {
                return "times";
        }

        
        

		*/
}

