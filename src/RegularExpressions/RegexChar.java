package RegularExpressions;

import Factory.Algorithms;
import Automatons.Nfa;
import Factory.TokenFactory;

import java.util.Objects;

public class RegexChar implements RegularExpression {
    private final char c;

    public RegexChar(char c){
        this.c = c;
    }

    public Nfa getNfa() {
        return Algorithms.regexCharToNfa(c);
    }

    public TypesRegex type() {
        return TypesRegex.CHAR;
    }

    public RegularExpression simplify() {
        return this;
    }

    @Override
    public String toString(){
        if(TokenFactory.isRString(c)) return TokenFactory.getRString(c);
        return String.valueOf(c);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegexChar regexChar = (RegexChar) o;
        return c == regexChar.c;
    }

    @Override
    public int hashCode() {
        return Objects.hash(c);
    }
}
