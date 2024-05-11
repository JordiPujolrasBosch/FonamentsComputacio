package RegularExpressions;

import Factory.Algorithms;
import Automatons.Nfa;
import Factory.TokenFactory;

public class RegexVoid implements RegularExpression {
    public RegexVoid(){}

    public Nfa getNfa() {
        return Algorithms.regexVoidToNfa();
    }

    public TypesRegex type() {
        return TypesRegex.VOID;
    }

    public RegularExpression simplify() {
        return this;
    }

    @Override
    public String toString() {
        return TokenFactory.regexVoidString();
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
