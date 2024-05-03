package RegularExpressions;

import Factory.Algorithms;
import Automatons.Nfa;
import Factory.TokenFactory;

public class RegexStar implements RegularExpression {
    private RegularExpression x;

    public RegexStar(RegularExpression x){
        this.x = x;
    }

    public Nfa getNfa(){
        return Algorithms.star(x.getNfa());
    }

    public RegularExpression simplify() {
        x = x.simplify();
        if(x.type() == TypesRegex.EMPTY || x.type() == TypesRegex.STAR) return x;
        if(x.type() == TypesRegex.VOID) return new RegexEmptyChar();
        return this;
    }

    @Override
    public String toString() {
        if(x.type() == TypesRegex.CONCAT || x.type() == TypesRegex.STAR || x.type() == TypesRegex.UNION){
            return "(" + x + ")" + TokenFactory.regexStarString();
        }
        return x + TokenFactory.regexStarString();
    }

    public TypesRegex type() {
        return TypesRegex.STAR;
    }
}
