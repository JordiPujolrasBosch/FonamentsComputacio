package RegularExpressions;

import Factory.AutomatonFactory;
import Automatons.Nfa;

public class RegexUnion implements RegularExpression {
    private RegularExpression a;
    private RegularExpression b;

    public RegexUnion(RegularExpression a, RegularExpression b){
        this.a = a;
        this.b = b;
    }

    public Nfa getNfa(){
        return AutomatonFactory.union(a.getNfa(),b.getNfa());
    }

    public RegularExpression simplify() {
        a = a.simplify();
        b = b.simplify();

        if(a.type() == TypesRegex.VOID) return b;
        if(b.type() == TypesRegex.VOID) return a;
        if(a.type() == b.type() && a.type() == TypesRegex.EMPTY) return a;

        return this;
    }

    public String toString() {
        return a + "|" + b;
    }

    public TypesRegex type() {
        return TypesRegex.UNION;
    }
}
