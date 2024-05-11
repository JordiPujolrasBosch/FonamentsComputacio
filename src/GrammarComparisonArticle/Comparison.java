package GrammarComparisonArticle;

import Grammars.Gramex;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Comparison {
    private final Set<Gramex> left;
    private final Set<Gramex> right;
    private final boolean opEq; //true = equivalence, false = inclusion

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

    //Equals

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comparison that = (Comparison) o;
        return opEq == that.opEq && Objects.equals(left, that.left) && Objects.equals(right, that.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right, opEq);
    }
}
