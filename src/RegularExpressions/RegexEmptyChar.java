package RegularExpressions;

import Factory.Algorithms;
import Automatons.Nfa;
import Factory.TokenFactory;

public class RegexEmptyChar implements RegularExpression {
    private RegexEmptyChar(){}

    private static final RegexEmptyChar instance = new RegexEmptyChar();

    public static RegexEmptyChar getInstance(){
        return instance;
    }

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
        return TokenFactory.getREmptyChar();
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
