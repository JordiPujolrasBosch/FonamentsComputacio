package RegularExpressions;

import Factory.AutomatonFactory;
import Automatons.Nfa;

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
        boolean eight = (c=='(' || c==')' || c=='|' || c=='*' || c=='+' || c=='#' || c=='\\' || c=='$');
        if(eight) return "$" + c;
        if(c==' ') return "$s";
        return String.valueOf(c);
    }

    public TypesRegex type() {
        return TypesRegex.CHAR;
    }
}
