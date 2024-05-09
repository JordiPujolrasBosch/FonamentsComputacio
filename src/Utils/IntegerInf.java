package Utils;

public class IntegerInf {
    private final int n;
    private final boolean inf;

    public IntegerInf(int n){
        this.n = n;
        this.inf = false;
    }

    public IntegerInf(boolean inf){
        this.n = 0;
        this.inf = inf;
    }

    public boolean isInfinity(){
        return inf;
    }

    public int getValue(){
        return n;
    }

    public IntegerInf add(IntegerInf x){
        if(inf || x.inf) return new IntegerInf(true);
        return new IntegerInf(n + x.n);
    }

    public IntegerInf multiply(IntegerInf x){
        if(inf || x.inf) return new IntegerInf(true);
        return new IntegerInf(n * x.n);
    }


}
