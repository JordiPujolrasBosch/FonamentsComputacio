package Grammars;

import Factory.TokenFactory;

public class GramexEmpty implements Gramex {
    private GramexEmpty(){}

    private static final GramexEmpty instance = new GramexEmpty();

    public static GramexEmpty getInstance(){
        return instance;
    }

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

    @Override
    public String toString() {
        return TokenFactory.getGEmptyChar();
    }

}
