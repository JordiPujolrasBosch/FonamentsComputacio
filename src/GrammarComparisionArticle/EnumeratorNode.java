package GrammarComparisionArticle;

import Grammars.GramexVar;

public class EnumeratorNode implements Enumerator{
    private final GramexVar r;
    private final Enumerator e;
    public EnumeratorNode(GramexVar r, Enumerator e){
        this.r = r;
        this.e = e;
    }
}
