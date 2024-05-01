package ContextFreeGrammars;

public class CfgVariable {
    private final char c;
    private final int n;

    public CfgVariable(char c, int n){
        this.c = c;
        this.n = n;
    }

    public char c(){
        return c;
    }

    public int n(){
        return n;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CfgVariable that = (CfgVariable) o;
        return c == that.c && n == that.n;
    }
}
