package Elements.Grammars;

import java.util.Objects;

public class CfgVariable {
    private final char c;
    private final int n;

    public CfgVariable(char c, int n){
        this.c = c;
        this.n = n;
    }

    //Getters

    public char getC(){
        return c;
    }

    public int getN(){
        return n;
    }

    //String and equals

    @Override
    public String toString() {
        return Character.toString(c) + n;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CfgVariable that = (CfgVariable) o;
        return c == that.c && n == that.n;
    }

    @Override
    public int hashCode() {
        return Objects.hash(c, n);
    }
}
