import java.util.*;

public final class BinaryConstraint {
    private int firstVar, secondVar ;
    private ArrayList<BinaryTuple> tuples ;

    public BinaryConstraint(int fv, int sv, ArrayList<BinaryTuple> t) {
        firstVar = fv ;
        secondVar = sv ;
        tuples = t ;
    }

    public String toString() {
        StringBuffer result = new StringBuffer() ;
        result.append("c("+firstVar+", "+secondVar+")\n") ;
        for (BinaryTuple bt : tuples)
            result.append(bt+"\n") ;
        return result.toString() ;
    }

    public ArrayList<BinaryTuple> getTuples(){
	    return tuples;
    }

	public int getFirstVar() {
		return firstVar;
	}
	
	public int getSecondVar() {
		return secondVar;
	}
    
    
}
