package Grammars;

import Factory.TokenFactory;

import java.util.Objects;

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

    @Override
    public String toString() {
        if(TokenFactory.isGString(c)) return TokenFactory.getGString(c);
        return Character.toString(c);
    }

    /**
     * @return A hash code for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(c);
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
        GramexChar that = (GramexChar) o;
        return c == that.c;
    }
}
