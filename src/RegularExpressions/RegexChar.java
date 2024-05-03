package RegularExpressions;

import Factory.Algorithms;
import Automatons.Nfa;
import Factory.TokenFactory;

public class RegexChar implements RegularExpression {
    private final char c;

    public RegexChar(char c){
        this.c = c;
    }

    public Nfa getNfa() {
        return Algorithms.regexCharToNfa(c);
    }

    public RegularExpression simplify() {
        return this;
    }

    @Override
    public String toString(){
        if(TokenFactory.rtokensReverseContains(c)) return TokenFactory.rtokensReverseGet(c);
        return String.valueOf(c);
    }

    public TypesRegex type() {
        return TypesRegex.CHAR;
    }
}
