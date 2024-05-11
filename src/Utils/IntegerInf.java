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

    //Equals

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntegerInf that = (IntegerInf) o;
        return n == that.n && inf == that.inf;
    }

    @Override
    public int hashCode() {
        return Objects.hash(n, inf);
    }
}
