package RegularExpressions;

import Factory.Algorithms;
import Automatons.Nfa;
import Factory.TokenFactory;
import Grammars.Cfg;
import Utils.IntegerInf;

import java.util.Objects;

public class RegexUnion implements RegularExpression {
    private RegularExpression a;
    private RegularExpression b;

    public RegexUnion(RegularExpression a, RegularExpression b){
        this.a = a;
        this.b = b;
    }

    public RegularExpression getA() {
        return a;
    }

    public RegularExpression getB() {
        return b;
    }

    //REGEX METHODS

    public Nfa toNfa(){
        return Algorithms.union(a.toNfa(),b.toNfa());
    }

    public TypesRegex type() {
        return TypesRegex.UNION;
    }

    public RegularExpression simplify() {
        a = a.simplify();
        b = b.simplify();

        if(a.type() == TypesRegex.VOID) return b;
        if(b.type() == TypesRegex.VOID) return a;
        if(a.type() == b.type() && a.type() == TypesRegex.EMPTY) return a;

        return this;
    }

    public Cfg toCfg() {
        return Algorithms.regexToCfg(this);
    }

    public IntegerInf wordsCount() {
        return a.wordsCount().add(b.wordsCount());
    }

    //TO STRING AND EQUALS

    @Override
    public String toString() {
        return a + TokenFactory.getRUnion() + b;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegexUnion that = (RegexUnion) o;
        return (Objects.equals(a, that.a) && Objects.equals(b, that.b)) || (Objects.equals(a, that.b) && Objects.equals(b, that.a));
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }
}
