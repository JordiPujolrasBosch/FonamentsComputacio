package RegularExpressions;

import Factory.Algorithms;
import Automatons.Nfa;
import Factory.TokenFactory;
import Grammars.Cfg;
import Utils.IntegerInf;

import java.util.List;

public class RegexEmptyChar implements RegularExpression {
    private RegexEmptyChar(){}

    private static final RegexEmptyChar instance = new RegexEmptyChar();

    public static RegexEmptyChar getInstance(){
        return instance;
    }

    //REGEX METHODS

    public Nfa toNfa(){
        return Algorithms.regexEmptyCharToNfa();
    }

    public TypesRegex type() {
        return TypesRegex.EMPTY;
    }

    public RegularExpression simplify() {
        return this;
    }

    public Cfg toCfg() {
        return Algorithms.regexToCfg(this);
    }

    public IntegerInf wordsCount() {
        return new IntegerInf(1);
    }

    public List<String> generateWords(int n){
        return Algorithms.generateWords(this, n);
    }

    //TO STRING AND EQUALS

    @Override
    public String toString() {
        return TokenFactory.getREmptyChar();
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
