package RegularExpressions;

import Factory.AutomatonFactory;
import Automatons.Nfa;

public class RegexVoid implements RegularExpression {
    public RegexVoid(){}

    public Nfa getNfa() {
        return AutomatonFactory.regexVoidToNfa();
    }

    public RegularExpression simplify() {
        return this;
    }

    public String toString() {
        return "#";
    }

    public TypesRegex type() {
        return TypesRegex.VOID;
    }
}
