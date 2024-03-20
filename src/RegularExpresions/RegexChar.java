package RegularExpresions;

import Automatons.AutomatonFactory;
import Automatons.Nfa;

public class RegexChar implements RegularExpresion {
    private final char c;

    public RegexChar(char c){
        this.c = c;
    }

    public Nfa getNfa() {
        return AutomatonFactory.regexCharToNfa(c);
    }
}
