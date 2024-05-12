package Elements.Grammars;

import Factory.TokenFactory;
import Grammars.Gramex;

import java.util.Objects;

public class Grule {
    private final Gvar left;
    private final Gramex right;

    public Grule(Gvar l, Gramex r){
        left = l;
        right = r;
    }

    //Getters

    public Gvar getLeft(){
        return left;
    }

    public Gramex getRight() {
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
        Grule grule = (Grule) o;
        return Objects.equals(left, grule.left) && Objects.equals(right, grule.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }
}
