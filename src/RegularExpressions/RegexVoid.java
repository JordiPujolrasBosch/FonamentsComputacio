package RegularExpressions;

import Factory.Algorithms;
import Automatons.Nfa;
import Factory.TokenFactory;

public class RegexVoid implements RegularExpression {
    public RegexVoid(){}

    public Nfa getNfa() {
        return Algorithms.regexVoidToNfa();
    }

    public RegularExpression simplify() {
        return this;
    }

    @Override
    public String toString() {
        return TokenFactory.regexVoidString();
    }

    public TypesRegex type() {
        return TypesRegex.VOID;
    }
}
