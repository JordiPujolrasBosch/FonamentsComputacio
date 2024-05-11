package Grammars;

import Elements.Grammars.CfgVariable;

public class GramexChar implements GramexNonEmpty {
    private final char c;

    public GramexChar(char c){
        this.c = c;
    }

    public char getC() {
        return c;
    }

    public TypesGramex type() {
        return TypesGramex.CHAR;
    }

    public int length() {
        return 1;
    }

    public Gramex toGramex() {
        return this;
    }

    public GramexEmpty toGramexEmpty() {
        return null;
    }

    public GramexNonEmpty toGramexNonEmpty() {
        return this;
    }

    public GramexChar toGramexChar() {
        return this;
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
        return new GramexVar(x);
    }

    @Override
    public String toString() {
        return Character.toString(c);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GramexChar rightChar = (GramexChar) o;
        return c == rightChar.c;
    }
}
