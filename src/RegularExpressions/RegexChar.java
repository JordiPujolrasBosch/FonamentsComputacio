package RegularExpressions;

import Factory.Algorithms;
import Automatons.Nfa;
import Factory.TokenFactory;
import Grammars.Cfg;
import Utils.IntegerInf;

import java.util.List;
import java.util.Objects;

public class RegexChar implements RegularExpression {
    private final char c;

    public RegexChar(char c){
        this.c = c;
    }

    public char getC(){
        return c;
    }

    //REGEX METHODS

    public Nfa toNfa() {
        return Algorithms.regexCharToNfa(c);
    }

    public TypesRegex type() {
        return TypesRegex.CHAR;
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
    public String toString(){
        if(TokenFactory.isRString(c)) return TokenFactory.getRString(c);
        return String.valueOf(c);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegexChar that = (RegexChar) o;
        return c == that.c;
    }

    @Override
    public int hashCode() {
        return Objects.hash(c);
    }
}
