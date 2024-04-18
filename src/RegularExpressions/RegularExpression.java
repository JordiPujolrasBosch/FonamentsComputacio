package RegularExpressions;

import Automatons.Nfa;

public interface RegularExpression {
    Nfa getNfa();
    RegularExpression simplify();
    TypesRegex type();
}
