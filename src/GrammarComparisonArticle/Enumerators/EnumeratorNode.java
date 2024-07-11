package GrammarComparisonArticle.Enumerators;

import Grammars.GramexVar;

import java.util.Objects;

public class EnumeratorNode implements Enumerator {
    private final GramexVar r;
    private final Enumerator e;

    public EnumeratorNode(GramexVar r, Enumerator e){
        this.r = r;
        this.e = e;
    }

    public String getWord(){
        return e.getWord();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EnumeratorNode that = (EnumeratorNode) o;
        return Objects.equals(r, that.r) && Objects.equals(e, that.e);
    }

    @Override
    public int hashCode() {
        return Objects.hash(r, e);
    }
}
