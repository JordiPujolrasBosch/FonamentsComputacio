package Elements.Grammars;

import Factory.TokenFactory;
import Grammars.GramexNonEmpty;

import java.util.Objects;

public class GruleNonEmpty {
    private final Gvar left;
    private final GramexNonEmpty right;

    public GruleNonEmpty(Gvar l, GramexNonEmpty r){
        left = l;
        right = r;
    }

    //Getters

    public Gvar getLeft() {
        return left;
    }

    public GramexNonEmpty getRight() {
        return right;
    }

    //String and equals

    @Override
    public String toString() {
        return left.toString() + " " + TokenFactory.getGArrow() + " " + right.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GruleNonEmpty that = (GruleNonEmpty) o;
        return left.equals(that.left) && right.equals(that.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }
}
