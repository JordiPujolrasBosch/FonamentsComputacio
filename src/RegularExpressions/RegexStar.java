package RegularExpressions;

import Factory.AutomatonFactory;
import Automatons.Nfa;

public class RegexStar implements RegularExpression {
    private final RegularExpression x;

    public RegexStar(RegularExpression x){
        this.x = x;
    }

    public Nfa getNfa(){
        return AutomatonFactory.star(x.getNfa());
    }
}
