package GrammarComparisonArticle.Enumerators;

import Grammars.GramexChar;

public class EnumeratorLeaf implements Enumerator {
    private final GramexChar r;

    public EnumeratorLeaf(GramexChar r){
        this.r = r;
    }

    public String getWord(){
        return Character.toString(r.getC());
    }
}
