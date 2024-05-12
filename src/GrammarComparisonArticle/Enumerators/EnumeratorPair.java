package GrammarComparisonArticle.Enumerators;

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
}
