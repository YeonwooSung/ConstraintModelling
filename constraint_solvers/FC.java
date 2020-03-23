import java.util.ArrayList;
import java.util.HashMap;


public class FC extends Solver {

	public FC(String instance) {
		super(instance);

		System.out.println("\nPerforming Forward Checking on instance: " + instance);
		start = System.currentTimeMillis();
		forwardChecking(unassigned);
	}


	//////////////////////////////////////////////
	//			    FC MAIN PROCEDURE 			//
	//////////////////////////////////////////////		
	private void forwardChecking(ArrayList<Integer> varList) {
		itreation++;

		if(assigned.size() == binaryCSP.getNoVariables() && !solutionFound) {	//Print the first solution found.
			printSolution();
			solutionFound = true;
			System.out.println("\n Solution found after " + itreation + " nodes");
			long finish = System.currentTimeMillis();
			long timeElapsed = finish - start;
			System.out.println(" Solution found after " + timeElapsed + " ms");
		}
		else if(!solutionFound) {	//If a solution was not found, proceed.
			int var = selectVar(varList);	//Select a variable.
			int val = selectVal(var);	//Select a value from the variable's domain.	
			
			branchFCLeft(varList, var, val);	//Branch left.
			branchFCRight(varList, var, val);	//Branch right.
		}
	}
	
	
	/**
	 * Branching left.
	 * @param varList
	 * @param var
	 * @param val
	 */
	private void branchFCLeft(ArrayList<Integer> varList, int var, int val) {
		HashMap<Integer, ArrayList<Integer>> prevDomains = new HashMap<Integer, ArrayList<Integer>>(varDomains); //Saving domains to undo the pruning.
		assign(var, val); 	//Assign the variable.

        // Check if consistent.
        if (reviseFutureArcs(varList, var)) {
            //Re-create varList.
			varList = getVarList();
			forwardChecking(varList);
		}

		varDomains = prevDomains; // Undo pruning.
		unassign(var, val); // Unassigning variable.
	}

	
	/**
	 * Branching right.
	 * @param varList
	 * @param var
	 * @param val
	 */
	private void branchFCRight(ArrayList<Integer> varList, int var, int val) {
		
		deleteValue(var, val);
		
		if(!varDomains.get(var).isEmpty()) {
			HashMap<Integer, ArrayList<Integer>> prevDomains = new HashMap<Integer, ArrayList<Integer>>(varDomains);	//Saving domains to undo the pruning.
			if(reviseFutureArcs(varList, var)) {
				forwardChecking(varList);
            }

			varDomains = prevDomains;	//Undoing pruning.
		}

		restoreValue(var, val);
	}

	/**
	 * Revises if future arcs are consistent.
	 * @param varList
	 * @param var
	 * @return true if consistent, otherwise return false.
	 */
	private boolean reviseFutureArcs(ArrayList<Integer> varList, int var) {
		boolean consistent = true;

		for(int futureVar : varList) {
			if(futureVar != var) {
				consistent = revise(var, futureVar);
				if(!consistent)
					return false;
			}
		}
		
		return true;
	}

}
