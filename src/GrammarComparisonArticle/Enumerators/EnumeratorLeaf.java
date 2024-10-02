package GrammarComparisonArticle.Enumerators;

import Grammars.GramexChar;

import java.util.Objects;

public class EnumeratorLeaf implements Enumerator {
    private final GramexChar r;

    public EnumeratorLeaf(GramexChar r){
        this.r = r;
    }

    public String getWord(){
        return Character.toString(r.getC());
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
        EnumeratorLeaf that = (EnumeratorLeaf) o;
        return r.equals(that.r);
    }

    /**
     * @return A hash code for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(r);
    }
}
