package Grammars;

import Elements.Grammars.CfgVariable;

public interface Right {
    boolean containsVar(CfgVariable v);

    int length();

    boolean containsTerminal();

    boolean containsTerminal(char c);

    boolean containsPair(RightConcat pair);

    Right getChanged(char c, CfgVariable x);

    TypesRight type();
    Right toRight();
    RightEmpty toRightEmpty();
    RightNonEmpty toRightNonEmpty();
    RightChar toRightChar();
    RightVar toRightVar();
    RightConcat toRightConcat();
}
