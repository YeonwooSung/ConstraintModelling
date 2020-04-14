import java.util.ArrayList;
import java.util.HashMap;

public class MAC extends Solver {

	public MAC(String instance) {
		super(instance);

		System.out.println("\nPerforming MAC3 on instance: " + instance);
		start = System.currentTimeMillis();
		MAC3(unassigned);
	}


	//////////////////////////////////////////////
	//			 MAC3 MAIN PROCEDURE			//
	//////////////////////////////////////////////				
	private void MAC3(ArrayList<Integer> varList) {
		itreation++;

		int var = selectVar(varList); //Select a variable.
		int val = selectVal(var);	  //Select a value from the variable's domain.		

        // Saving domains to undo the pruning.
        HashMap<Integer, ArrayList<Integer>> prevDomains = new HashMap<Integer, ArrayList<Integer>>(varDomains);

		assign(var, val);	

		if(assigned.size() == binaryCSP.getNoVariables() && !solutionFound) { //Print the first solution found.
			printSolution();
			solutionFound = true;
			System.out.println("\n Solution found after " + itreation + " nodes");
			long finish = System.currentTimeMillis();
			long timeElapsed = finish - start;
			System.out.println(" Solution found after " + timeElapsed + " ms");
		} else if(AC3(var) && !solutionFound) { //If a solution was not found, proceed.
			varList = getVarList();
			MAC3(varList);
		}

		varDomains = prevDomains; //Undoing pruning.
		unassign(var, val); 
		
        deleteValue(var, val);

        // Saving domains to undo the pruning.
        prevDomains = new HashMap<Integer, ArrayList<Integer>>(varDomains);

		if (!varDomains.get(var).isEmpty()) {
			if(AC3(var)) {
                // call itself recursively
                MAC3(varList);
            } else {
                // Undo pruning.
                varDomains = prevDomains;
            }
		}

		restoreValue(var, val);		
	}

	/**
	 * AC3 MAIN PROCEDURE
	 * @param var
	 * @return true if consistent, otherwise return false.
	 */
	private boolean AC3(int var) {
		ArrayList<int[]> queue = new ArrayList<>(); //Initialises Queue.

        // Inputs arcs into the queue by using the for loop
		for (BinaryConstraint c : constraints) {
			if(c.getFirstVar() == var && !assigned.contains(c.getSecondVar())){
				int[] arc = new int[2];
				arc[0] = var;
				arc[1] = c.getSecondVar();
				queue.add(arc);
			}
		}

		while (!queue.isEmpty()) { //While queue is not empty.
			int[] arc = queue.get(0); //Select first arc of the queue.
			queue.remove(0); //Removes arc from the queue.
			try {
				if(revise(arc[0], arc[1])) { 
						for(BinaryConstraint c : constraints) {    //Inputs new arcs into the queue.
							if(c.getFirstVar() == arc[1]) {
								int[] newArc = {arc[1], c.getSecondVar()};
								queue.add(newArc);
							}
						}
				}
			}
			catch(Exception e) { //If pruned domain is empty.
				return false;
			}
		}

		return true;
	}
}
	










































