package GrammarComparisonArticle;

import Grammars.Gramex;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Comparison {
    private final Set<Gramex> left;
    private final Set<Gramex> right;
    private final boolean opEq; //true = equivalence (l==r), false = inclusion (l in r)

    //Constructors

    public Comparison(Set<Gramex> sl, Set<Gramex> sr, boolean eq){
        left = new HashSet<>(sl);
        right = new HashSet<>(sr);
        opEq = eq;
    }

    public Comparison(Gramex l, Set<Gramex> sr, boolean eq){
        left = new HashSet<>();
        left.add(l);
        right = new HashSet<>(sr);
        opEq = eq;
    }

    public Comparison(Set<Gramex> sl, Gramex r, boolean eq){
        left = new HashSet<>(sl);
        right = new HashSet<>();
        right.add(r);
        opEq = eq;
    }

    public Comparison(Gramex l, Gramex r, boolean eq){
        left = new HashSet<>();
        left.add(l);
        right = new HashSet<>();
        right.add(r);
        opEq = eq;
    }

    //Getters

    public Set<Gramex> getLeft() {
        return left;
    }

    public Set<Gramex> getRight() {
        return right;
    }

    public boolean isEquivalence(){
        return opEq;
    }

    public boolean isInclusion(){
        return !opEq;
    }

    //Methods

    public int size(){
        int n = 0;
        for(Gramex g : left) n += g.length();
        for(Gramex g : right) n += g.length();
        return n;
    }

    //Equals

    /**
     * Compares if two objects are equal.
     * @param o The object to compare.
     * @return True if this and o are equal. False otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comparison that = (Comparison) o;
        return opEq == that.opEq && left.equals(that.left) && right.equals(that.right);
    }

    /**
     * @return A hash code for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(left, right, opEq);
    }

    @Override
    public String toString() {
        return "Comparison{" +
                "left=" + left +
                ", right=" + right +
                ", opEq=" + opEq +
                '}';
    }
}
