package org.lemsml.jlems.core.selection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import org.lemsml.jlems.core.expression.AbstractOperatorNode;
import org.lemsml.jlems.core.expression.ConstantNode;
import org.lemsml.jlems.core.expression.EqualsNode;
import org.lemsml.jlems.core.expression.FunctionNode;
import org.lemsml.jlems.core.expression.GreaterThanNode;
import org.lemsml.jlems.core.expression.GreaterThanOrEqualsNode;
import org.lemsml.jlems.core.expression.GroupNode;
import org.lemsml.jlems.core.expression.IntegerConstantNode;
import org.lemsml.jlems.core.expression.LessThanNode;
import org.lemsml.jlems.core.expression.LessThanOrEqualsNode;
import org.lemsml.jlems.core.expression.MinusNode;
import org.lemsml.jlems.core.expression.ModuloNode;
import org.lemsml.jlems.core.expression.Node;
import org.lemsml.jlems.core.expression.OpenNode;
import org.lemsml.jlems.core.expression.ParseError;
import org.lemsml.jlems.core.expression.Parser;
import org.lemsml.jlems.core.expression.PlusNode;
import org.lemsml.jlems.core.expression.PrecedenceComparator;
import org.lemsml.jlems.core.expression.TimesNode;
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.util.StringUtil;

public class SelectionParser {

	
	static HashMap<String, AbstractOperatorNode> mathOPHM = new HashMap<String, AbstractOperatorNode>();
  
	static HashMap<String, AbstractSelectionOperatorNode> selOPHM = new HashMap<String, AbstractSelectionOperatorNode>();	

	static HashSet<String> funcHS = new HashSet<String>();
	
	String sdig = "123456789";
	String snum = "0." + sdig;
	
 
	static HashSet<String> numberHS = new HashSet<String>();
	 
//	static Pattern numpat = Pattern.compile("([\\d\\.]*[Ee]([\\+-]?)[\\d]+)");
  
	
	Parser expressionParser = new Parser();
	
	static {
		for (int i = 0; i <= 9; i++) {
			numberHS.add("" + i);
		}
		
		selOPHM.put("/", new SlashNode());
		selOPHM.put(":", new ColonNode());
		selOPHM.put(".minus.", new ComplementNode(".minus."));
		selOPHM.put(".or.", new UnionNode(".or."));
		selOPHM.put(".and.", new IntersectionNode(".and."));
	
	
		mathOPHM.put(".gt.", new GreaterThanNode());
		mathOPHM.put(".lt.", new LessThanNode());
		mathOPHM.put(".eq.", new EqualsNode());
		mathOPHM.put(".le.", new LessThanOrEqualsNode());
		mathOPHM.put(".ge.", new GreaterThanOrEqualsNode());
		mathOPHM.put("+", new PlusNode());
		mathOPHM.put("-", new MinusNode());
		mathOPHM.put("*", new TimesNode());
		mathOPHM.put("%", new ModuloNode());
	
		funcHS.add("abs");
	}

	
	
	 
	boolean verbose = false;
	
 
 
  
	public void setVerbose() {
		verbose = true;
	}
	
	
	public SelectionExpression parse(String e) throws ParseError {
		if (verbose) {
			E.info("parsing " + e);
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
		ArrayList<AbstractOperatorNode> ops = new ArrayList<AbstractOperatorNode>();
		
		ArrayList<AbstractSelectionOperatorNode> selops = new ArrayList<AbstractSelectionOperatorNode>();
	
		ArrayList<PredicateNode> pnodes = new ArrayList<PredicateNode>();
		ArrayList<GroupNode> gnodes = new ArrayList<GroupNode>();
		ArrayList<FunctionNode> fnodes = new ArrayList<FunctionNode>();
		
		
		for (Node n : nodes) {
			if (n instanceof AbstractSelectionOperatorNode) {
				AbstractSelectionOperatorNode sn = (AbstractSelectionOperatorNode)n;
				sn.setSequencePosition(selops.size());
				selops.add(sn);
			}
			
			if (n instanceof AbstractOperatorNode) {
				ops.add((AbstractOperatorNode)n);
			}
			if (n instanceof PredicateNode) {
				PredicateNode pn = (PredicateNode)n;
				pnodes.add(pn);
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
		// we come to them we don't need recursion - we process a group back to its opening node and then
		// replace the opening node, so an enclosing group just treats it as any other node 
		 
		for (GroupNode gn : gnodes) {
			gn.gatherPreceeding();
		}
		
		for (PredicateNode pn : pnodes) {
			pn.gatherPreceeding();
		}
		
		if (verbose) {
			E.info(groot.toString());
		}
		
		// mathematical operators need sorting by precedence
		Collections.sort(ops, new PrecedenceComparator());
		
		// precedence for selection operators - "/" is transitive, so just take in expression order.
		// but has to come before the others.
		
	 
		Collections.sort(selops, new SelectionPrecedenceComparator());
	 	 
		
		for (FunctionNode fn : fnodes) {
			fn.claim();
		}
		
		for (AbstractSelectionOperatorNode selop : selops) {
			selop.claim();
		}
		
		for (AbstractOperatorNode op : ops) {
			op.claim();
		}
		
		for (GroupNode gn : gnodes) {
			gn.supplantByChild();
		}
		
		AbstractSelectionNode root = null;
		if (groot.size() == 1) {
			Node fc = groot.first();
			if (fc instanceof AbstractSelectionNode) {
				root = (AbstractSelectionNode)fc;
			} else {
				throw new ParseError("root node is not evaluable " + fc);
			}
		} else {
			StringBuilder sb = new StringBuilder();
			sb.append(" too many children left in container: " + groot.getChildren().size());
			sb.append("\n");
			for (Node n : groot.getChildren()) {
				sb.append("Node: " + n + "\n");
			}
			
			throw new ParseError(sb.toString());
		}
           
		
		SelectionExpression expr = new SelectionExpression(e, root);
				
	
		return expr;
	}
	
	
	
	
	public ArrayList<Node> tokenize(String e) {
		ArrayList<Node> ret = new ArrayList<Node>();
		
		String ewk = expressionParser.disambiguate(e);
		for (String op : mathOPHM.keySet()) {
			ewk = replaceAll(ewk, op, " " + op + " ");
		}
		for (String op : selOPHM.keySet()) {
			ewk = replaceAll(ewk, op, " " + op + " ");
		}
		
		ewk = replaceAll(ewk, "(", " ( ");
		ewk = replaceAll(ewk, ")", " ) ");
		
		ewk = replaceAll(ewk, "[", " [ ");
		ewk = replaceAll(ewk, "]", " ] ");
		
		ewk = reambiguate(ewk);
		if (verbose) {
			E.info("after spaces " + ewk);
		}
		
	//	StringTokenizer st = new StringTokenizer(ewk, " ");
		Node pretok = null;
		
		String[] bits = StringUtil.splitWords(ewk);
		for (String stok : bits) {
			stok = stok.trim();
			
//		while (st.hasMoreTokens()) {
//			String stok = st.nextToken();
	
		 
			
			Node n = null;
			if (stok.equals(")")) {
				n = new GroupNode();
				
			} else if (stok.equals("(")) {
				n = new OpenNode();
			
			} else if (stok.equals("]")) {
				n = new PredicateNode();
				
			} else if (stok.equals("[")) {
				Node ap = new ApplyPredicateNode(" P: ");
				if (pretok != null) {
					pretok.linkNext(ap);
				}
				ret.add(ap);
				pretok = ap;
				n = new OpenPredicateNode();

				
			} else if (selOPHM.containsKey(stok)) {
				n = selOPHM.get(stok).copy();  
				
			} else if (mathOPHM.containsKey(stok)) {
				n = mathOPHM.get(stok).copy();
				
			} else if (funcHS.contains(stok)) {
				n = new FunctionNode(stok);
				
			} else if (isInteger(stok)) {
				n = new IntegerConstantNode(stok);
				
			} else if (isFloat(stok)) {
				n = new ConstantNode(stok);
				
			} else {
				// this could be a constant value, a variable, or a path segment - we don't know yet.
				n = new SelectorNode(stok);
			}
		 
			if (pretok != null) {
				if (n == null) {
					E.error("null node?? " + stok);
				}
				
				pretok.linkNext(n);
			}
			pretok = n;
			ret.add(n);
		}
		return ret;
	}
	
	
	
	private boolean isInteger(String s) {
		boolean ret = false;
		if (sdig.indexOf(s.substring(0, 1)) >= 0) {
			try {
			 	Integer.parseInt(s);
				ret = true;
			} catch (Exception ex) {
				ret = false;
			}
		}
		return ret;
	}
	
	private boolean isFloat(String s) {
		boolean ret = false;
		if (snum.indexOf(s.substring(0, 1)) >= 0) {
			try {
				Double.parseDouble(s);
				ret = true;
			} catch (Exception ex) {
				ret = false;
			}
		}
		return ret;
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
	

	
	
	public static void main(String[] argv) {
		String[] expressions = {"a .or. b",  
								"a[index .gt. 3][index .lt. 5]",
								"a/b/c[2]/d[l .eq. 4]",
								"a/b[c/d + 5 .gt. e]",
								"a[p  .lt. 4]/b[v .gt. 1]",
								"a[3] .or. b[4]",
								"a .minus. a[3]",	
								"a[b[1]/p .gt. b[2]/p]",
								"line[index % 5 .eq. 0]"};
		
		SelectionParser sp = new SelectionParser();
		for (String s : expressions) {
			try {
				SelectionExpression expr = sp.parse(s);
				String sep = expr.getEvaluationProcessDescription();
				E.info(sep);
		
			} catch (ParseError pe) {
				E.report("can't parse " + s + " ", pe);
			}
		}
		
	}
	
	
}
