package RegularExpresions;

import Automatons.AutomatonFactory;
import Automatons.Nfa;

public class RegexConcat implements RegularExpresion {
    private final RegularExpresion a;
    private final RegularExpresion b;

    public RegexConcat(RegularExpresion a, RegularExpresion b){
        this.a = a;
        this.b = b;
    }

    public Nfa getNfa() {
        return AutomatonFactory.concatenation(a.getNfa(), b.getNfa());
    }
}
