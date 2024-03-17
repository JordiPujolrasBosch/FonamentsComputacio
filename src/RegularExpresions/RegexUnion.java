package RegularExpresions;

import Automatons.NondeterministicFiniteAutomaton;

public class RegexUnion implements RegularExpresion {
    private final RegularExpresion a;
    private final RegularExpresion b;

    public RegexUnion(RegularExpresion a, RegularExpresion b){
        this.a = a;
        this.b = b;
    }

    public NondeterministicFiniteAutomaton getNfa() {
        return NondeterministicFiniteAutomaton.union(a.getNfa(), b.getNfa());
    }
}
