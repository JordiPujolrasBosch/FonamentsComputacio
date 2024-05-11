package RegularExpressions;

import Factory.Algorithms;
import Automatons.Nfa;

import java.util.Objects;

public class RegexConcat implements RegularExpression {
    private RegularExpression a;
    private RegularExpression b;

    public RegexConcat(RegularExpression a, RegularExpression b){
        this.a = a;
        this.b = b;
    }

    public Nfa getNfa() {
        return Algorithms.concatenation(a.getNfa(), b.getNfa());
    }

    public TypesRegex type() {
        return TypesRegex.CONCAT;
    }

    public RegularExpression simplify() {
        a = a.simplify();
        b = b.simplify();

        if(a.type() == TypesRegex.EMPTY) return b;
        if(b.type() == TypesRegex.EMPTY) return a;
        if(a.type() == TypesRegex.VOID) return a;
        if(b.type() == TypesRegex.VOID) return b;

        return this;
    }

    @Override
    public String toString() {
        if(a.type() == b.type() && a.type() == TypesRegex.UNION) return "(" + a + ")(" + b + ")";
        if(a.type() == TypesRegex.UNION) return "(" + a + ")" + b;
        if(b.type() == TypesRegex.UNION) return a + "(" + b + ")";
        return a + b.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegexConcat b = (RegexConcat) o;
        return getNfa().toDfa().minimize().equal(b.getNfa().toDfa().minimize());
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }
}
