package GrammarComparisonArticle.Enumerators;

import java.util.Objects;

public class EnumeratorPair implements Enumerator {
    private final Enumerator a;
    private final Enumerator b;

    public EnumeratorPair(Enumerator a, Enumerator b){
        this.a = a;
        this.b = b;
    }

    public String getWord(){
        return a.getWord() + b.getWord();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EnumeratorPair that = (EnumeratorPair) o;
        return a.equals(that.a) && b.equals(that.b);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }
}
