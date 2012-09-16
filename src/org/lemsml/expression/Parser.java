package org.lemsml.expression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.lemsml.util.E;

public class Parser {
	
	static String[] sf = {"sin", "cos", "tan", "exp", "sqrt", "sum", "product", "ln", "log", "random"};
	static HashSet<String> stdFuncs = new HashSet<String>();
	
	static HashMap<String, OperatorNode> opHM = new HashMap<String, OperatorNode>();	
	
	String snum = ".0123456789";
	
	static Pattern numpat = Pattern.compile("([\\d\\.]*[Ee]([\\+-]?)[\\d]+)");
 
	
	{
		for (String s : sf) {
			stdFuncs.add(s);
		}
		
		opHM.put("+", new PlusNode());
		opHM.put("-", new MinusNode());
		opHM.put("*", new TimesNode());
		opHM.put("/", new DivideNode());
		opHM.put("^", new PowerNode());

		opHM.put(AndNode.SYMBOL, new AndNode());
		opHM.put(OrNode.SYMBOL, new OrNode());
		
		opHM.put(".lt.", new LessThanNode());
		opHM.put(".gt.", new GreaterThanNode());
		opHM.put(".geq.", new GreaterThanOrEqualsNode());
		opHM.put(".leq.", new LessThanOrEqualsNode());
		opHM.put(".eq.", new EqualsNode());
		opHM.put(".neq.", new NotEqualsNode());
		
	}

	
	static void addOperator(OperatorNode op) {
		opHM.put(op.getSymbol(), op);
	}
	
	
	HashSet<String> funcHS = new HashSet<String>();

	boolean verbose = false;
	
	
	public Parser() {
		this(null);
	}
	
	
	public Parser(HashSet<String> fhs) {
		funcHS.addAll(stdFuncs);
		if (fhs != null) {
			funcHS.addAll(fhs);
		}
	}
	
  
	public void setVerbose() {
		verbose = true;
	}
	

	public BooleanEvaluable parseCondition(String e) throws ParseError {
		Evaluable ev = parse(e);
		BooleanEvaluable ret = null;
		if (ev instanceof BooleanEvaluable) {
			ret = (BooleanEvaluable)ev;
		} else {
			E.error("not a condition: " + e);
		}
		return ret;
	}
	
	public DoubleEvaluable parseExpression(String e) throws ParseError {

		DoubleEvaluable ret = null;
        try {
            Evaluable ev = parse(e);
            if (ev instanceof DoubleEvaluable) {
                ret = (DoubleEvaluable)ev;
            } else {
                E.error("not a condition: " + e);
            }
        } catch (Exception ex){
            E.error("Exception while parsing ("+e+"): "+ex.getMessage());
        }
		return ret;
	}
	
	
	
	
	public Evaluable parse(String ea) throws ParseError {

		String e = ea;
		if (verbose) {
			E.info("Parsing: " + e);
		}
        e = e.trim();

        if (e.lastIndexOf("(") == 0 && e.indexOf(")") == e.length()-1) {
            e = e.substring(1, e.length()-1);
			E.info("Replaced with: " + e);
        }
		
		ArrayList<Node> nodes = tokenize(e);
		// now we've got a list of tokens, and each is linked to is neighbor on either side
	 
		if (verbose) {
			E.info("tokens: " + nodes);
		}
		
		// a group node to hold the whole lot, the same as is used for the content of bracketed chunks
		GroupNode groot = new GroupNode(null);
		groot.addAll(nodes);
		
		
		// some nodes will get replaced, but the operators remain throughout and will be needed later 
		// to claim their operands. Use a list here so we can sort by precedence.
		ArrayList<OperatorNode> ops = new ArrayList<OperatorNode>();
		ArrayList<GroupNode> gnodes = new ArrayList<GroupNode>();
		ArrayList<FunctionNode> fnodes = new ArrayList<FunctionNode>();
		
		for (Node n : nodes) {
			if (n instanceof OperatorNode) {
				ops.add((OperatorNode)n);
			}
			if (n instanceof GroupNode) {
				gnodes.add((GroupNode)n);
			}
			if (n instanceof FunctionNode) {
				fnodes.add((FunctionNode)n);
			}
		}
		
		
		// Right parentheses have been mapped to group nodes. Left parentheses 
		// are still present. Make each group node claim the content back to the corresponding
		// opening parenthesis. By starting at the left and processing groups as 
		// we come to them we don't need recursion
		 
		for (GroupNode gn : gnodes) {
			gn.gatherPreceeding();
		}
		
		if (verbose) {
			E.info(groot.toString());
		}
		
		OperatorNode[] aops = ops.toArray(new OperatorNode[ops.size()]);
		Arrays.sort(aops, new PrecedenceComparator());
		
		
		for (FunctionNode fn : fnodes) {
			fn.claim();
		}
		
		for (OperatorNode op : aops) {
			op.claim();
		}
		
		for (GroupNode gn : gnodes) {
			gn.supplantByChild();
		}
		
		Evaluable root = null;
		if (groot.children().size() == 1) {
			Node fc = groot.children().get(0);
			if (fc instanceof Evaluable) {
				root = (Evaluable)fc;
			} else {
				throw new ParseError("root node is not evaluable " + fc);
			}
		} else {
			StringBuilder sb = new StringBuilder();
			sb.append(" too many children left in container: " + groot.children().size());
			sb.append("\n");
			for (Node n : groot.children()) {
				sb.append("Node: " + n + "\n");
			}
			
			throw new ParseError(sb.toString());
		}
	 
		root.evaluablize();
		return root;
	}
	
	
	
	
	public ArrayList<Node> tokenize(String e) {
		ArrayList<Node> ret = new ArrayList<Node>();
		
		String ewk = disambiguate(e);
		for (String op : opHM.keySet()) {
			ewk = replaceAll(ewk, op, " " + op + " ");
		}
		ewk = replaceAll(ewk, "(", " ( ");
		ewk = replaceAll(ewk, ")", " ) ");
		ewk = reambiguate(ewk);
		if (verbose) {
			E.info("after spaces " + ewk);
		}
		
		StringTokenizer st = new StringTokenizer(ewk, " ");
		Node pretok = null;
		while (st.hasMoreTokens()) {
			String stok = st.nextToken();
			
			Node n = null;
			if (stok.equals(")")) {
				n = new GroupNode();
				
			} else if (stok.equals("(")) {
				n = new OpenNode();
				
			} else if (funcHS.contains(stok)) {
				n = new FunctionNode(stok);
			
			} else if (opHM.containsKey(stok)) {
				n = opHM.get(stok).copy();  
				
			} else if (snum.indexOf(stok.substring(0, 1)) >= 0) {
				n = new ConstantNode(stok);
			
			} else {
				n = new VariableNode(stok);
			}
		 
			
			
			if (pretok != null) {
				pretok.linkNext(n);
			}
			pretok = n;
			ret.add(n);
		}
		return ret;
	}
	
	
	
	
	
	private String disambiguate(String e) {
		// This is a hack to because the "+" and "-" operators are also used inside numbers for 
		// scientific notation (eg 1.2e-3). We replace them with "~" and "#" temporarily to
		// give an expression in which the operators only occur as themselves.
		// (of course, this only works if ~ and # aren't allowed anywhere in the expression) 
		String emod = e;
		Matcher matcher = numpat.matcher(e);
		int ipos = 0;
		while (matcher.find(ipos)) {
			String spm = matcher.group(2);
			if (spm.length() > 0) {
				String spmnew = (spm.equals("+") ? "~" : "#");
				int ipm = matcher.start(2);
				emod = emod.substring(0, ipm) + spmnew + emod.substring(ipm+1, emod.length());
			}
			ipos = matcher.end();
		}
		return emod;
	}
	
	
	
	
	public String reambiguate(String e) {
		String ret = e;
		ret = ret.replaceAll("~", "+");
		ret = ret.replaceAll("#", "-");
		return ret;
	}
	
	
	
	private String replaceAll(String src, String so, String sn) {
		// not using String.replaceAll as it takes a regex for the replacee so we'd have to 
		// provide the regexs for the operators ("\\*"  for "*" etc) which seems a bit silly
		String ret = "";
		int lastmatch = 0;
		int newmatch = src.indexOf(so);
		while (newmatch >= 0) {
			ret += src.substring(lastmatch, newmatch);
			ret += sn;
			lastmatch = newmatch + so.length();
			newmatch = src.indexOf(so, lastmatch);
		}
		ret += src.substring(lastmatch, src.length());
		return ret;
	}
	
	   //Moved to org.lemsml.test.expression.ParserTest
	public static void main(String[] argv) {
		try {
            
            Parser p = new Parser();
            p.setVerbose();
            String s1 = "V * -59";
            DoubleEvaluable de = p.parseExpression(s1);
            E.info("Parsed " + s1 + " to: " + de);
		
            String s2 = "1.2 * 5 * 5.0e-3 * 6e34";
            DoubleEvaluable de2 = p.parseExpression(s2);
            E.info("Parsed " + s2 + " to: " + de2);
		 
			
		} catch (Exception ex) {
			ex.printStackTrace();
		 
		}
		
	}
 
}
