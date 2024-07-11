package GrammarComparisonArticle.Enumerators;

import Grammars.GramexChar;

import java.util.Objects;

public class EnumeratorLeaf implements Enumerator {
    private final GramexChar r;

    public EnumeratorLeaf(GramexChar r){
        this.r = r;
    }

    public String getWord(){
        return Character.toString(r.getC());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EnumeratorLeaf that = (EnumeratorLeaf) o;
        return Objects.equals(r, that.r);
    }

    @Override
    public int hashCode() {
        return Objects.hash(r);
    }
}
