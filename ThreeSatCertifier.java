import java.util.*;

public class ThreeSatCertifier {
	private static int NUMBER_OF_LITERALS_IN_CLAUSE = 3;	//3-sat so 3 literals

	private String cnfInput = "";		//Conjunctive Normal Form equation
	private String[] clauses;
	private ArrayList<ArrayList<Boolean>> negateLiteral = new ArrayList<ArrayList<Boolean>>();	//rows for each clause, True means a "NOT" preceded the literal in the column so negate it later
	private ArrayList<ArrayList<Integer>> X_i_subscripts = new ArrayList<ArrayList<Integer>>();	//rows for each clause, columns store the subscript of that literal (e.g. X1 OR X2 OR X4 creates the row(1, 2, 4))
	private HashMap<Integer, Boolean> X = new HashMap<Integer, Boolean>();	//X_i values for the literals. Updated from the certificate
	
	public ThreeSatCertifier(String cnfInput){
		this.cnfInput=cnfInput.toUpperCase();
		parseCNF();
	}

	public void parseCNF(){
		clauses=cnfInput.split("AND");	//break up string into clauses

		//Loop over clauses to populate negateLiteral & X
		for(int i=0; i<clauses.length; i++){
			clauses[i]=clauses[i].replace("(", "");		//remove parentheses
			clauses[i]=clauses[i].replace(")", "");
			clauses[i]=clauses[i].trim();			//trim whitespace

			negateLiteral.add(new ArrayList<Boolean>());		//add new row for each clause
			X_i_subscripts.add(new ArrayList<Integer>());

			String[] words = clauses[i].split(" +");	//split on multiple spaces

			boolean previousWordWasNOT = false;		//hasn't read any words yet, so the last thing read cannot be a "NOT"
			for(int j=0; j<words.length; j++){
				if(words[j].equals("NOT")){		//update negateLiteral to true so the value with be flipped later when evaluating
					negateLiteral.get(i).add(true);
					previousWordWasNOT = true;	//found "NOT" so update the status
				}
				//store "false" in negateLiteral if the word is an Xi. Also don't want duplicate entries if the previous word in the row was already "NOT". This adds the correct number of columns in negateLiter to match the number of literals
				else if(!words[j].equals("OR") && !previousWordWasNOT){		
					negateLiteral.get(i).add(false);
				}

				if(!words[j].equals("NOT")){	//if it finds "OR" or an X literal, these aren't "NOT" so update status
					previousWordWasNOT = false;
				}

				//Add the correct X_i subscript
				if(Character.toString(words[j].charAt(0)).equals("X")){		//If the 1st character is "X", we found a literal
					int subscript = Integer.parseInt(words[j].replace("X", ""));	//Get just the subscript number by removing the "X" at the beginning leaving a number
					X_i_subscripts.get(i).add(subscript);
				}
			}
		}
	}

	public boolean isValidTruthAssignment(String certificate){
		certificate=certificate.toUpperCase();
		certificate=certificate.replace("(", "");	//remove parentheses
		certificate=certificate.replace(")", "");

		//Parse certificate
		certificate+=",";		//add a trailing comma so each assignment can be split on the commas
		String[] literalAssignmentPairs=certificate.split(",");
		for(int i=0; i<literalAssignmentPairs.length; i++){
			literalAssignmentPairs[i]=literalAssignmentPairs[i].trim();		//remove leading whitespace & any potential trailing

			String[] literalAndItsAssignment = literalAssignmentPairs[i].split("=");	//Break each Xi=k into [Xi, k]
			int subscript = Integer.parseInt( literalAndItsAssignment[0].replace("X", "") );	//get the subscript i by removing the "X"

			boolean valueOfLiteral = true;		//assume it's true
			if(literalAndItsAssignment[1].equals("0")){		//if Xi=0, then set its value to false
				valueOfLiteral = false;
			}
			X.put(subscript, valueOfLiteral);	//update the Map of X_i with a subscript & the literal's
		}


		//Now Check the certificate
		boolean isValidTruthAssignment= true;	//start out assuming it's a valid assignment, break early if 1 clause evaluates to false

		for(int clause=0; clause<clauses.length; clause++){
			boolean isClauseValid = false;		//since clauses are composed of ORs, start out assuming clause is invalid & break early if we find a true literal
			for(int j=0; j<NUMBER_OF_LITERALS_IN_CLAUSE; j++){
				if(negateLiteral.get(clause).get(j)){	//this means NOT is present before a literal, so reverse its value with a !
					//compound ORs work by storing the value from the previous loop (literal to the left) & comparing with the current literal
					isClauseValid = isClauseValid  ||  ! X.get(X_i_subscripts.get(clause).get(j));
				}
				else{	//otherwise just use its actual value
					isClauseValid = isClauseValid  ||  X.get(X_i_subscripts.get(clause).get(j));
				}

				if(isClauseValid){	//Inside each clause is built of ORs, so stop checking literals if we find a true one
					break;
				}
			}

			isValidTruthAssignment = isValidTruthAssignment && isClauseValid;	//compound ANDs work by storing the value from the previous loop & comparing with the current clause
			if(!isValidTruthAssignment){	//if we found a clause that evaluates to false, don't bother checking any others
				break;
			}
		}

		return isValidTruthAssignment;
	}

	public static void main(String[] args) {
		String cnfFormula = "(NOT x1 OR X2 OR x3) AND (x1 OR NOT x2 OR x3) AND (x1 OR x2 OR x4) AND (NOT x1 OR NOT x3 OR NOT x4)";
		ThreeSatCertifier certifier = new ThreeSatCertifier(cnfFormula);
		String[] certificates={"(x1=1, x2=1, x3=0, x4=1)",  "(x1=1, x2=1, x3=1, x4=1)",  "(x1=0, x2=0, x3=0, x4=1)",  "(x1=0, x2=1, x3=0, x4=1)"};
		System.out.println("Given the CNF  "+cnfFormula+"\n");
		for(int i=0; i<certificates.length; i++){
			System.out.println("Is certificate/assignment  "+certificates[i]+" valid?  " +certifier.isValidTruthAssignment(certificates[i]));
		}
	}

}