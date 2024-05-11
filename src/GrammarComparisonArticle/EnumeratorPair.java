package GrammarComparisonArticle;

public class EnumeratorPair implements Enumerator{
    private final Enumerator a;
    private final Enumerator b;
    public EnumeratorPair(Enumerator a, Enumerator b){
        this.a = a;
        this.b = b;
    }
}
