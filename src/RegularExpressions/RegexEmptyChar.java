package RegularExpressions;

import Factory.Algorithms;
import Automatons.Nfa;
import Factory.TokenFactory;

public class RegexEmptyChar implements RegularExpression {
    public RegexEmptyChar(){}

    public Nfa getNfa(){
        return Algorithms.regexEmptyCharToNfa();
    }

    public TypesRegex type() {
        return TypesRegex.EMPTY;
    }

    public RegularExpression simplify() {
        return this;
    }

    @Override
    public String toString() {
        return TokenFactory.regexEmptyCharString();
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && getClass() == obj.getClass();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
