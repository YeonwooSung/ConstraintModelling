package main;

import binary.BinaryCSP;
import binary.BinaryCSPReader;
import binary.algorithms.ForwardChecking;
import binary.algorithms.MaintainingArcConsistency;
import binary.algorithms.Solution;

import java.io.IOException;
import java.util.LinkedHashSet;

/**
 * Provides a main method to run the basic implementation.
 *
 * @author 160021429
 * @version 2.0
 */
public class Basic extends ArgumentParser {

    public static void main(String[] args) {
        Basic basic = new Basic(args);

        // run satisfaction algorithm to get solutions
        LinkedHashSet<Solution> solutions = basic.runSatisfactionAlg();

        // print out all solutions
        solutions.forEach(System.out::println);

        // print out 1) solution count, 2) total execution time, 3) node counts, and 4) arc revisions
        System.out.println("Solution count: " + solutions.size());
        long executionTime = basic.getExecutionTime();
        System.out.println("Found in: " + executionTime + " milliseconds");
        System.out.println("Node count: " + basic.getSearchTreeNodes());
        System.out.println("Arc revisions: " + basic.getArcRevisions());
    }

    /**
     * Constructs a {@link Basic} instance with the given arguments.
     *
     * @param args the command line arguments
     */
    public Basic(String[] args) {
        super(args);
    }

    @Override
    protected SolvingAlgorithm getAlgorithm(String cspfilename, String algname) {
        BinaryCSP csp;
        try {
            csp = BinaryCSPReader.readBinaryCSP(cspfilename);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        // check the algorithm name (FC | MAC | MAC3), and return corresponding instance
        if (algname.equalsIgnoreCase("FC")) {
            return new ForwardChecking(csp);
        } else if (algname.equalsIgnoreCase("MAC") || algname.equalsIgnoreCase("MAC3")) {
            return new MaintainingArcConsistency(csp);
        } else {
            System.out.println("Algorithm not recognized!");
            System.out.println("Use one of \"FC\" or \"MAC3\"");
            return null;
        }
    }

}
