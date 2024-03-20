package RegularExpressions;

import Factory.AutomatonFactory;
import Automatons.Nfa;

public class RegexUnion implements RegularExpression {
    private final RegularExpression a;
    private final RegularExpression b;

    public RegexUnion(RegularExpression a, RegularExpression b){
        this.a = a;
        this.b = b;
    }

    public Nfa getNfa(){
        return AutomatonFactory.union(a.getNfa(),b.getNfa());
    }
}
