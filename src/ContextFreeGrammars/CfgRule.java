package ContextFreeGrammars;

import java.util.ArrayList;
import java.util.List;

public class CfgRule {
    private final CfgVariable left;
    private final RuleRight right;

    public CfgRule(CfgVariable l, String[] r){
        left = l;
        right = new RuleRight(true);
    }

    private class RuleRight {
        private final boolean empty;
        private final List<RuleRightElement> list;

        public RuleRight(boolean e){
            empty = e;
            if(e) list = null;
            else list = new ArrayList<>();
        }

        private interface RuleRightElement {}
        private class RuleRightElementChar implements RuleRightElement {
            public char c;
            public RuleRightElementChar(char c){this.c=c;}
        }
        public class RuleRightElementVar implements RuleRightElement {
            public CfgVariable v;
            public RuleRightElementVar(CfgVariable v){this.v=v;}
        }
    }
}
