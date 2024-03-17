package RegularExpresions;

import Automatons.NondeterministicFiniteAutomaton;

public class RegexConcat implements RegularExpresion {
    private final RegularExpresion a;
    private final RegularExpresion b;

    public RegexConcat(RegularExpresion a, RegularExpresion b){
        this.a = a;
        this.b = b;
    }

    public NondeterministicFiniteAutomaton getNfa() {
        return NondeterministicFiniteAutomaton.concatenation(a.getNfa(), b.getNfa());
    }
}
