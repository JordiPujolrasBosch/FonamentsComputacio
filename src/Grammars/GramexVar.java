package Grammars;

import Elements.Grammars.Gvar;

import java.util.Objects;

public class GramexVar implements GramexNonEmpty {
    private final Gvar v;

    public GramexVar(Gvar v){
        this.v = v;
    }

    public Gvar getV(){
        return v;
    }

    public TypesGramex type() {
        return TypesGramex.VAR;
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
        return null;
    }

    public GramexVar toGramexVar() {
        return this;
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GramexVar that = (GramexVar) o;
        return v.equals(that.v);
    }

    /**
     * @return A hash code for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(v);
    }

    @Override
    public String toString() {
        return v.toString();
    }

}
