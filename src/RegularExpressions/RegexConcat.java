package RegularExpressions;

import Factory.Algorithms;
import Automatons.Nfa;
import Grammars.Cfg;
import Utils.IntegerInf;

import java.util.List;
import java.util.Objects;

public class RegexConcat implements RegularExpression {
    private RegularExpression a;
    private RegularExpression b;

    public RegexConcat(RegularExpression a, RegularExpression b){
        this.a = a;
        this.b = b;
    }

    public RegularExpression getA(){
        return a;
    }

    public RegularExpression getB(){
        return b;
    }

    //REGEX METHODS

    public Nfa toNfa() {
        return Algorithms.concatenation(a.toNfa(), b.toNfa());
    }

    public TypesRegex type() {
        return TypesRegex.CONCAT;
    }

    public RegularExpression simplify() {
        a = a.simplify();
        b = b.simplify();

        if(a.type() == TypesRegex.EMPTY) return b;
        if(b.type() == TypesRegex.EMPTY) return a;
        if(a.type() == TypesRegex.VOID) return a;
        if(b.type() == TypesRegex.VOID) return b;

        return this;
    }

    public Cfg toCfg() {
        return Algorithms.regexToCfg(this);
    }

    public IntegerInf wordsCount() {
        return a.wordsCount().multiply(b.wordsCount());
    }

    public List<String> generateWords(int n){
        return Algorithms.generateWords(this, n);
    }

    //TO STRING AND EQUALS

    @Override
    public String toString() {
        if(a.type() == b.type() && a.type() == TypesRegex.UNION) return "(" + a + ")(" + b + ")";
        if(a.type() == TypesRegex.UNION) return "(" + a + ")" + b;
        if(b.type() == TypesRegex.UNION) return a + "(" + b + ")";
        return a + b.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegexConcat that = (RegexConcat) o;
        return a.equals(that.a) && b.equals(that.b);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }
}
