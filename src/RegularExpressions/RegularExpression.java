package RegularExpressions;

import Automatons.Nfa;

public interface RegularExpression {
    Nfa getNfa();
    TypesRegex type();
    RegularExpression simplify();
}
