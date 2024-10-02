package Utils;

import java.util.Objects;

public class IntegerInf {
    private final int n;
    private final boolean inf;

    //Constructors

    public IntegerInf(int n){
        this.n = n;
        this.inf = false;
    }

    public IntegerInf(boolean inf){
        this.n = 0;
        this.inf = inf;
    }

    //Getters

    public boolean isInfinity(){
        return inf;
    }

    public int getValue(){
        return n;
    }

    //Operations

    public IntegerInf add(IntegerInf x){
        if(inf || x.inf) return new IntegerInf(true);
        return new IntegerInf(n + x.n);
    }

    public IntegerInf multiply(IntegerInf x){
        if(inf || x.inf) return new IntegerInf(true);
        return new IntegerInf(n * x.n);
    }

    //Comparisons

    public boolean isZero(){
        return !inf && n==0;
    }

    public boolean isGreaterThan(IntegerInf x) {
        if(x.inf) return false;
        if(inf) return true;
        return n > x.n;
    }

    public boolean isLesserThan(IntegerInf x) {
        if(inf) return false;
        if(x.inf) return true;
        return n < x.n;
    }

    public boolean isEqual(IntegerInf x){
        if(inf && x.inf) return true;
        if(inf != x.inf) return false;
        return n == x.n;
    }

    public boolean isLesserEqualThan(IntegerInf x) {
        return isLesserThan(x) || isEqual(x);
    }

    public boolean isGreaterEqualThan(IntegerInf x){
        return isGreaterThan(x) || isEqual(x);
    }

    //Equals

    /**
     * Compares if two objects are equal.
     * @param o The object to compare.
     * @return True if this and o are equal. False otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntegerInf that = (IntegerInf) o;
        return n == that.n && inf == that.inf;
    }

    /**
     * @return A hash code for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(n, inf);
    }
}
