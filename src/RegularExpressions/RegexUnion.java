package RegularExpressions;

import Factory.Algorithms;
import Automatons.Nfa;
import Factory.TokenFactory;

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

    public TypesRegex type() {
        return TypesRegex.UNION;
    }
}
