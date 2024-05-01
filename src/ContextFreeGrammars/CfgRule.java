package ContextFreeGrammars;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CfgRule {
    private final CfgVariable left;
    private final Right right;

    public CfgRule(CfgVariable l, Right r){
        left = l;
        right = r;
    }

    public CfgVariable left(){
        return left;
    }

    public Right right() {
        return right;
    }

    public boolean isEmptyRule() {
        return right.equals(new RightEmpty());
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CfgRule cfgRule = (CfgRule) o;
        return Objects.equals(left, cfgRule.left) && Objects.equals(right, cfgRule.right);
    }
}
