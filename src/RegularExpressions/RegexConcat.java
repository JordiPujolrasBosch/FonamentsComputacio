package RegularExpressions;

import Factory.AutomatonFactory;
import Automatons.Nfa;

public class RegexConcat implements RegularExpression {
    private RegularExpression a;
    private RegularExpression b;

    public RegexConcat(RegularExpression a, RegularExpression b){
        this.a = a;
        this.b = b;
    }

    public Nfa getNfa() {
        return AutomatonFactory.concatenation(a.getNfa(), b.getNfa());
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

    public String toString() {
        if(a.type() == b.type() && a.type() == TypesRegex.UNION) return "(" + a + ")(" + b + ")";
        if(a.type() == TypesRegex.UNION) return "(" + a + ")" + b;
        if(b.type() == TypesRegex.UNION) return a + "(" + b + ")";
        return a + b.toString();
    }

    public TypesRegex type() {
        return TypesRegex.CONCAT;
    }
}
