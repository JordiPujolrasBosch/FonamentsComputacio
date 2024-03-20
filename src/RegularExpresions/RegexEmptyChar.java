package RegularExpresions;

import Automatons.AutomatonFactory;
import Automatons.Nfa;

public class RegexEmptyChar implements RegularExpresion {
    public RegexEmptyChar(){}

    public Nfa getNfa(){
        return AutomatonFactory.regexEmptyCharToNfa();
    }
}
