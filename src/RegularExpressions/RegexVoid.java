package RegularExpressions;

import Factory.Algorithms;
import Automatons.Nfa;
import Factory.TokenFactory;

import java.util.Objects;

public class RegexVoid implements RegularExpression {
    private RegexVoid(){}

    private static final RegexVoid instance = new RegexVoid();

    public static RegexVoid getInstance(){
        return instance;
    }

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
        return TokenFactory.getRVoid();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
