package RegularExpressions;

import Factory.Algorithms;
import Automatons.Nfa;
import Factory.TokenFactory;

import java.util.Objects;

public class RegexUnion implements RegularExpression {
    private RegularExpression a;
    private RegularExpression b;

    public RegexUnion(RegularExpression a, RegularExpression b){
        this.a = a;
        this.b = b;
    }

    public Nfa getNfa(){
        return Algorithms.union(a.getNfa(),b.getNfa());
    }

    public TypesRegex type() {
        return TypesRegex.UNION;
    }

    public RegularExpression simplify() {
        a = a.simplify();
        b = b.simplify();

        if(a.type() == TypesRegex.VOID) return b;
        if(b.type() == TypesRegex.VOID) return a;
        if(a.type() == b.type() && a.type() == TypesRegex.EMPTY) return a;

        return this;
    }

    @Override
    public String toString() {
        return a + TokenFactory.regexUnionString() + b;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegexUnion b = (RegexUnion) o;
        return getNfa().toDfa().minimize().equal(b.getNfa().toDfa().minimize());
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }
}
