package Grammars;

import Elements.Grammars.CfgVariable;

public interface RightNonEmpty extends Right {
    RightNonEmpty getPrefix(int n);

    boolean hasPrefixTerminalOfSize(int n);

    CfgVariable getLeftMostVar();

    RightNonEmpty getSubstitutionLeft(RightNonEmpty subs);
}
