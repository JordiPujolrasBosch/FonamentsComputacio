package RegularExpressions;

import Automatons.Nfa;
import Grammars.Cfg;
import Utils.IntegerInf;

public interface RegularExpression {
    Nfa toNfa();
    TypesRegex type();
    RegularExpression simplify();
    Cfg toCfg();
    IntegerInf wordsCount();
}
