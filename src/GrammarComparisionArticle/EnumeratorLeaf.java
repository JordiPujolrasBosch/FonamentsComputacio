package GrammarComparisionArticle;

import Grammars.RightChar;

public class EnumeratorLeaf implements Enumerator{
    private final RightChar r;
    public EnumeratorLeaf(RightChar r){
        this.r = r;
    }
}
