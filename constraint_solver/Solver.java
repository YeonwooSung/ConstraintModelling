import java.util.ArrayList;
import java.util.HashMap;

public class Solver {
    protected BinaryCSPReader reader = new BinaryCSPReader();
    protected BinaryCSP binaryCSP;
    protected ArrayList<BinaryConstraint> constraints;

    protected ArrayList<Integer> unassigned = new ArrayList<>(); // List of unassigned variables (varList).
    protected ArrayList<Integer> assigned = new ArrayList<>(); // List of assigned variables.
    protected HashMap<Integer, ArrayList<Integer>> varDomains = new HashMap<>(); // Variables domain.

    protected int itreation = 0; // Count the number of FC procedures.
    protected boolean solutionFound = false; // Checks if a solution was found (Used to stop the algorithm).

    protected long start; // Used to measure the time taken to find a solution.

    public Solver(String instance) {
        binaryCSP = reader.readBinaryCSP(instance);
        constraints = binaryCSP.getConstraints();
        setVars();
    }

    /**
     * Method to populate the unassigned list with all the variables and varDomains
     * Hash Map with the corresponding domains of each variable.
     */
    private void setVars() {
        int numOfVars = binaryCSP.getNoVariables();

        for (int i = 0; i < numOfVars; i++) {
            int variable = i;
            ArrayList<Integer> domain = new ArrayList<>();

            for (int j = binaryCSP.getLB(i); j < binaryCSP.getUB(i) + 1; j++) {
                domain.add(j);
            }

            unassigned.add(i);
            varDomains.put(variable, domain);
        }
    }


    /**
     * Prunes domain of arc(var, futureVar)
     * 
     * @param var
     * @param futureVar
     * @return false if there where no changes, otherwise return true.
     * @throws Exception when the pruned domain is empty.
     */
    boolean revise(int var, int futureVar) {
        // Values from supported tuples for arc(var, futureVar).
        ArrayList<Integer> acceptableValues = getAcceptableValues(var, futureVar);
        // Get domain.
        ArrayList<Integer> newDomain = new ArrayList<Integer>(varDomains.get(futureVar));

        for (int i = 0; i < varDomains.get(futureVar).size(); i++) {
            if (!acceptableValues.contains(varDomains.get(futureVar).get(i))) { // If domain contains an unsupported
                                                                                // value.
                newDomain.remove(newDomain.indexOf(varDomains.get(futureVar).get(i))); // Prune unsupported value.
            }
        }

        varDomains.put(futureVar, newDomain); // Replace the domain with the new pruned domain.

        if (newDomain.isEmpty()) // If the new pruned domain is empty.
            return false;
        else
            return true;
    }

    /**
     * Method to get the values that where supported by the tuples of c(var,
     * futureVar).
     * 
     * @param var
     * @param futureVar
     * @return an ArrayList with the supported values.
     */
    ArrayList<Integer> getAcceptableValues(int var, int futureVar) {

        ArrayList<Integer> acceptableValues = new ArrayList<>();

        for (BinaryConstraint c : constraints) {
            if (c.getFirstVar() == var && c.getSecondVar() == futureVar) {
                for (BinaryTuple bt : c.getTuples()) {
                    for (int val : varDomains.get(var)) {
                        if (bt.getVal1() == val) {
                            acceptableValues.add(bt.getVal2());
                        }
                    }
                }
            }
        }
        return acceptableValues;
    }

    /**
     * Method to get a varList with the variables that have not been assigned yet.
     * 
     * @return populated ArrayList varList
     */
    ArrayList<Integer> getVarList() {
        ArrayList<Integer> varList = new ArrayList<>();
        for (int i = 0; i < binaryCSP.getNoVariables(); i++) {
            if (!assigned.contains(i)) { // If variable i has not been assigned yet.
                varList.add(i); // Add the variable to the list.
            }
        }
        return varList;
    }

    /**
     * Unassigns a variable by removing the variable of the assigned list.
     * 
     * @param var
     * @param val
     */
    void unassign(int var, int val) {
        assigned.remove(assigned.indexOf(var));
    }

    /**
     * Assigns a variable by adding it to the assigned list, and prunes all of the
     * values from the domain apart from the assigned value (Domain will only
     * contain val).
     * 
     * @param var
     * @param val
     */
    void assign(int var, int val) {
        assigned.add(var);
        ArrayList<Integer> domain = new ArrayList<>();
        domain.add(val);
        varDomains.put(var, domain);
    }

    /**
     * Re-introduces the value in the domain of the specified variable.
     * 
     * @param var
     * @param val
     */
    void restoreValue(int var, int val) {
        ArrayList<Integer> domain = varDomains.get(var);
        domain.add(val);
        varDomains.put(var, domain);
    }

    /**
     * Deletes a value from the domain of the specified variable.
     * 
     * @param var
     * @param val
     */
    void deleteValue(int var, int val) {
        ArrayList<Integer> domain = varDomains.get(var);
        domain.remove(domain.indexOf(val));
        varDomains.put(var, domain);
    }

    /**
     * Selects the first value from the domain of the specified variable, after the
     * domain is ordered in ascending order.
     * 
     * @param var
     * @return val
     */
    int selectVal(int var) {
        ArrayList<Integer> domain = varDomains.get(var);
        domain.sort(null); // Sorts domain in ascending order.
        return domain.get(0);
    }

    /**
     * Selects a variable from the varList. At the beginning, return the first
     * variable. Else, return the variable with the smallest domain.
     * 
     * @param varList
     * @return variable
     */
    int selectVar(ArrayList<Integer> varList) {
        int variable = varList.get(0);
        int smallestDomain = varDomains.get(variable).size();
        if (varList.size() == binaryCSP.getNoVariables()) // If at the beginning
            return variable;
        else {
            for (int i = 0; i < varList.size(); i++) {
                if (varDomains.get(varList.get(i)).size() < smallestDomain) {
                    variable = varList.get(i); // Keeps track of the variable with the smallest domain.
                    smallestDomain = varDomains.get(varList.get(i)).size(); // Keeps track of the size of the smallest
                                                                            // domain encountered.
                }
            }
        }
        return variable;
    }

    /**
     * Prints the solution.
     */
    void printSolution() {
        System.out.println("\n Solution: ");
        for (int i = 0; i < varDomains.size(); i++) {
            System.out.println(" Variable " + (i + 1) + ":    " + varDomains.get(i).toString());
        }
    }
}
