package RegularExpressions;

import Factory.Algorithms;
import Automatons.Nfa;
import Factory.TokenFactory;
import Grammars.Cfg;
import Utils.IntegerInf;

import java.util.List;

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

    public List<String> generateWords(int n){
        return Algorithms.generateWords(this, n);
    }

    //TO STRING AND EQUALS

    @Override
    public String toString() {
        return TokenFactory.getRVoid();
    }

    /**
     * Compares if two objects are equal.
     * @param o The object to compare.
     * @return True if this and o are equal. False otherwise.
     */
    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    /**
     * @return A hash code for this object.
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
