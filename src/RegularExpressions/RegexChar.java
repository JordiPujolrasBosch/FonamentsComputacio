package RegularExpressions;

import Factory.AutomatonFactory;
import Automatons.Nfa;
import Factory.TokenFactory;

public class RegexChar implements RegularExpression {
    private final char c;

    public RegexChar(char c){
        this.c = c;
    }

    public Nfa getNfa() {
        return AutomatonFactory.regexCharToNfa(c);
    }

    public RegularExpression simplify() {
        return this;
    }

    public String toString(){
        if(TokenFactory.rtokensReverseContains(c)) return TokenFactory.rtokensReverseGet(c);
        return String.valueOf(c);
    }

    public TypesRegex type() {
        return TypesRegex.CHAR;
    }
}
