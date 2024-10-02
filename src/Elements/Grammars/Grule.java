package Elements.Grammars;

import Factory.TokenFactory;
import Grammars.Gramex;

import java.util.Objects;

/**
 * Substitution rule of a context free grammar. A gvar can be substituted with an expression.
 */

public class Grule {
    private final Gvar left;
    private final Gramex right;

    public Grule(Gvar l, Gramex r){
        left = l;
        right = r;
    }

    //Getters

    /**
     * @return The gvar to substitute.
     */
    public Gvar getLeft(){
        return left;
    }

    /**
     * @return The expression that substitutes the gvar.
     */
    public Gramex getRight() {
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
        Grule that = (Grule) o;
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
