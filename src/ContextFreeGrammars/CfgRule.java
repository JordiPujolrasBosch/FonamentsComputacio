package ContextFreeGrammars;

import java.util.ArrayList;
import java.util.List;

public class CfgRule {
    private final CfgVariable left;
    private final Right right;

    public CfgRule(CfgVariable l, Right r){
        left = l;
        right = r;
    }

}
