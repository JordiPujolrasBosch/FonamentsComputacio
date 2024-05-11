package Elements.Grammars;

import Grammars.Gramex;

import java.util.Objects;

public class CfgRule {
    private final CfgVariable left;
    private final Gramex right;

    public CfgRule(CfgVariable l, Gramex r){
        left = l;
        right = r;
    }

    //Getters

    public CfgVariable getLeft(){
        return left;
    }

    public Gramex getRight() {
        return right;
    }

    //String and equals

    @Override
    public String toString() {
        return left.toString() + " -> " + right.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CfgRule cfgRule = (CfgRule) o;
        return Objects.equals(left, cfgRule.left) && Objects.equals(right, cfgRule.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }
}
