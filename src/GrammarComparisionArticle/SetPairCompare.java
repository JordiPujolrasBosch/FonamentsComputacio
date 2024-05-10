package GrammarComparisionArticle;

import Grammars.Right;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class SetPairCompare {
    private final Set<Right> left;
    private final Set<Right> right;
    private final boolean opEq; //true = equivalence, false = inclusion

    public SetPairCompare(Set<Right> sl, Set<Right> sr, boolean eq){
        left = new HashSet<>(sl);
        right = new HashSet<>(sr);
        opEq = eq;
    }

    public SetPairCompare(Right l, Set<Right> sr, boolean eq){
        left = new HashSet<>();
        left.add(l);
        right = new HashSet<>(sr);
        opEq = eq;
    }

    public SetPairCompare(Set<Right> sl, Right r, boolean eq){
        left = new HashSet<>(sl);
        right = new HashSet<>();
        right.add(r);
        opEq = eq;
    }

    public SetPairCompare(Right l, Right r, boolean eq){
        left = new HashSet<>();
        left.add(l);
        right = new HashSet<>();
        right.add(r);
        opEq = eq;
    }

    public Set<Right> getLeft() {
        return left;
    }

    public Set<Right> getRight() {
        return right;
    }

    public boolean isEquivalence(){
        return opEq;
    }

    public boolean isInclusion(){
        return !opEq;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SetPairCompare that = (SetPairCompare) o;
        return opEq == that.opEq && Objects.equals(left, that.left) && Objects.equals(right, that.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right, opEq);
    }
}
