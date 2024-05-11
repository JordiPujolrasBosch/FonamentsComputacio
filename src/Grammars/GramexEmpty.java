package Grammars;

import Elements.Grammars.CfgVariable;
import Factory.TokenFactory;

public class GramexEmpty implements Gramex {
    public GramexEmpty(){}

    public TypesGramex type() {
        return TypesGramex.EMPTY;
    }

    public int length() {
        return 0;
    }

    public Gramex toGramex() {
        return this;
    }

    public GramexEmpty toGramexEmpty() {
        return this;
    }

    public GramexNonEmpty toGramexNonEmpty() {
        return null;
    }

    public GramexChar toGramexChar() {
        return null;
    }

    public GramexVar toGramexVar() {
        return null;
    }

    public GramexConcat toGramexConcat() {
        return null;
    }



    public boolean containsPair(GramexConcat pair) {
        return false;
    }

    public Gramex getChanged(char c, CfgVariable x) {
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && getClass() == obj.getClass();
    }

    @Override
    public String toString() {
        return TokenFactory.getGrammarEmpty();
    }

}
