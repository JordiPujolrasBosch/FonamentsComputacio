package GrammarComparisionArticle;

import Grammars.RightVar;

public class EnumeratorNode implements Enumerator{
    private final RightVar r;
    private final Enumerator e;
    public EnumeratorNode(RightVar r, Enumerator e){
        this.r = r;
        this.e = e;
    }
}
