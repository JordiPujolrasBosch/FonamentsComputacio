package RegularExpressions;

import Factory.Algorithms;
import Automatons.Nfa;
import Factory.TokenFactory;
import Grammars.Cfg;
import Utils.IntegerInf;

import java.util.Objects;

public class RegexVoid implements RegularExpression {
    private RegexVoid(){}

    private static final RegexVoid instance = new RegexVoid();

    public static RegexVoid getInstance(){
        return instance;
    }

    //REGEX METHODS

    public Nfa toNfa() {
        return Algorithms.regexVoidToNfa();
    }

    public TypesRegex type() {
        return TypesRegex.VOID;
    }

    public RegularExpression simplify() {
        return this;
    }

    public Cfg toCfg() {
        return Algorithms.regexToCfg(this);
    }

    public IntegerInf wordsCount() {
        return new IntegerInf(0);
    }

    //TO STRING AND EQUALS

    @Override
    public String toString() {
        return TokenFactory.getRVoid();
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
