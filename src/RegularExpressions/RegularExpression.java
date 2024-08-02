package RegularExpressions;

import Automatons.Nfa;
import Grammars.Cfg;
import Utils.IntegerInf;

import java.util.List;

public interface RegularExpression {
    Nfa toNfa();
    TypesRegex type();
    RegularExpression simplify();
    Cfg toCfg();
    IntegerInf wordsCount();
    List<String> generateWords(int n);
}
