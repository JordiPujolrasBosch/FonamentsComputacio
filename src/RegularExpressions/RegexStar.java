package RegularExpressions;

import Factory.AutomatonFactory;
import Automatons.Nfa;

public class RegexStar implements RegularExpression {
    private RegularExpression x;

    public RegexStar(RegularExpression x){
        this.x = x;
    }

    public Nfa getNfa(){
        return AutomatonFactory.star(x.getNfa());
    }

    public RegularExpression simplify() {
        x = x.simplify();
        if(x.type() == TypesRegex.EMPTY || x.type() == TypesRegex.STAR) return x;
        if(x.type() == TypesRegex.VOID) return new RegexEmptyChar();
        return this;
    }

    public String toString() {
        if(x.type() == TypesRegex.CONCAT || x.type() == TypesRegex.STAR || x.type() == TypesRegex.UNION) return "(" + x + ")*";
        return x + "*";
    }

    public TypesRegex type() {
        return TypesRegex.STAR;
    }
}
