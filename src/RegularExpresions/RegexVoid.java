package RegularExpresions;

import Automatons.AutomatonFactory;
import Automatons.Nfa;

public class RegexVoid implements RegularExpresion {
    public RegexVoid(){}

    public Nfa getNfa() {
        return AutomatonFactory.regexVoidToNfa();
    }
}
