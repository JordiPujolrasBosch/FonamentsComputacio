package GrammarComparisionArticle;

import Grammars.GramexChar;

public class EnumeratorLeaf implements Enumerator{
    private final GramexChar r;
    public EnumeratorLeaf(GramexChar r){
        this.r = r;
    }
}
