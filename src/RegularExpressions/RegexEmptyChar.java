package RegularExpressions;

import Factory.AutomatonFactory;
import Automatons.Nfa;

public class RegexEmptyChar implements RegularExpression {
    public RegexEmptyChar(){}

    public Nfa getNfa(){
        return AutomatonFactory.regexEmptyCharToNfa();
    }
}
