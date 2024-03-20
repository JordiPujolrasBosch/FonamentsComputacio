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
}
