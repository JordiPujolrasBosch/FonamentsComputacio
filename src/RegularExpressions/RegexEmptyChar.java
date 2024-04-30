package RegularExpressions;

import Factory.AutomatonFactory;
import Automatons.Nfa;
import Factory.TokenFactory;

public class RegexEmptyChar implements RegularExpression {
    public RegexEmptyChar(){}

    public Nfa getNfa(){
        return AutomatonFactory.regexEmptyCharToNfa();
    }

    public RegularExpression simplify() {
        return this;
    }

    public String toString() {
        return TokenFactory.regexEmptyCharString();
    }

    public TypesRegex type() {
        return TypesRegex.EMPTY;
    }
}
