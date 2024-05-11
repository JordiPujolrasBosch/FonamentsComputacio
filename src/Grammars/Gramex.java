package Grammars;

public interface Gramex {
    TypesGramex type();
    int length();
    Gramex toGramex();
    GramexEmpty toGramexEmpty();
    GramexNonEmpty toGramexNonEmpty();
    GramexChar toGramexChar();
    GramexVar toGramexVar();
    GramexConcat toGramexConcat();
}
