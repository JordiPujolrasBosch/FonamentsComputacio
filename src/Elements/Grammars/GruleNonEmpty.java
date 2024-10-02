package Elements.Grammars;

import Factory.TokenFactory;
import Grammars.GramexNonEmpty;

import java.util.Objects;

/**
 * Substitution rule of a context free grammar (without empty). A gvar can be substituted with a non-empty expression.
 */

public class GruleNonEmpty {
    private final Gvar left;
    private final GramexNonEmpty right;

    public GruleNonEmpty(Gvar l, GramexNonEmpty r){
        left = l;
        right = r;
    }

    //Getters

    /**
     * @return The gvar to substitute.
     */
    public Gvar getLeft() {
        return left;
    }

    /**
     * @return The non-empty expression that substitutes the gvar.
     */
    public GramexNonEmpty getRight() {
        return right;
    }

    //String and equals

    /**
     * @return A string that represents this grule in the defined format.
     */
    @Override
    public String toString() {
        return left.toString() + " " + TokenFactory.getGArrow() + " " + right.toString();
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
        GruleNonEmpty that = (GruleNonEmpty) o;
        return left.equals(that.left) && right.equals(that.right);
    }

    /**
     * @return A hash code for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }
}
