package RegularExpressions;

import Factory.Algorithms;
import Automatons.Nfa;
import Factory.TokenFactory;
import Grammars.Cfg;
import Utils.IntegerInf;

import java.util.List;
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
        if(a.equals(b)) return a;

        return this;
    }

    public Cfg toCfg() {
        return Algorithms.regexToCfg(this);
    }

    public IntegerInf wordsCount() {
        return a.wordsCount().add(b.wordsCount());
    }

    public List<String> generateWords(int n){
        return Algorithms.generateWords(this, n);
    }

    //TO STRING AND EQUALS

    @Override
    public String toString() {
        return a + TokenFactory.getRUnion() + b;
    }

    /**
     * Compares if two objects are equal.
     * @param o The object to compare.
     * @return True if this and o are equal. False otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegexUnion that = (RegexUnion) o;
        return (a.equals(that.a) && b.equals(that.b)) || (a.equals(that.b) && b.equals(that.a));
    }

    /**
     * @return A hash code for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }
}
