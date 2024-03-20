package RegularExpressions;

import Factory.AutomatonFactory;
import Automatons.Nfa;

public class RegexConcat implements RegularExpression {
    private final RegularExpression a;
    private final RegularExpression b;

    public RegexConcat(RegularExpression a, RegularExpression b){
        this.a = a;
        this.b = b;
    }

    public Nfa getNfa() {
        return AutomatonFactory.concatenation(a.getNfa(), b.getNfa());
    }
}
