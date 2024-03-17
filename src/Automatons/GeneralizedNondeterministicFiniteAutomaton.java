package Automatons;

import AutomatonElements.Alphabet;
import AutomatonElements.GeneralizedNondeterministicTransitionFunction;
import AutomatonElements.State;

import java.util.Set;

public class GeneralizedNondeterministicFiniteAutomaton {
    private final Set<State> states;
    private final Alphabet alphabet;
    private final State start;
    private final State accept;
    private final GeneralizedNondeterministicTransitionFunction transition;

    public GeneralizedNondeterministicFiniteAutomaton(
            Set<State> states,
            Alphabet alphabet,
            State start,
            State accept,
            GeneralizedNondeterministicTransitionFunction tf){
        this.states = states;
        this.alphabet = alphabet;
        this.start = start;
        this.accept = accept;
        this.transition = tf;
    }
}
