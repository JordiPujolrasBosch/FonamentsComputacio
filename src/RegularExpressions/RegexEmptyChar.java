package RegularExpressions;

import Factory.Algorithms;
import Automatons.Nfa;
import Factory.TokenFactory;

public class RegexEmptyChar implements RegularExpression {
    public RegexEmptyChar(){}

    public Nfa getNfa(){
        return Algorithms.regexEmptyCharToNfa();
    }

    public RegularExpression simplify() {
        return this;
    }

    @Override
    public String toString() {
        return TokenFactory.regexEmptyCharString();
    }

    public TypesRegex type() {
        return TypesRegex.EMPTY;
    }
}
