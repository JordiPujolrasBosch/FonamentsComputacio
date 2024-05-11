package Grammars;

import Elements.Grammars.CfgVariable;

public interface Gramex {
    TypesGramex type();
    int length();
    Gramex toGramex();
    GramexEmpty toGramexEmpty();
    GramexNonEmpty toGramexNonEmpty();
    GramexChar toGramexChar();
    GramexVar toGramexVar();
    GramexConcat toGramexConcat();

    boolean containsPair(GramexConcat pair);
    Gramex getChanged(char c, CfgVariable x);
}
