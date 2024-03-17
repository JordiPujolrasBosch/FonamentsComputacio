package RegularExpresions;

import Automatons.NondeterministicFiniteAutomaton;

public class RegexStar implements RegularExpresion {
    private final RegularExpresion r;

    public RegexStar(RegularExpresion r){
        this.r = r;
    }

    public NondeterministicFiniteAutomaton getNfa() {
        return NondeterministicFiniteAutomaton.star(r.getNfa());
    }
}
