package Automatons;

import AutomatonElements.*;

import java.util.Set;

public class DeterministicFiniteAutomaton {
    private final Set<State> states;
    private final Alphabet alphabet;
    private final State start;
    private final Set<State> finalStates;
    private final DeterministicTransitionFunction transtition;

    public DeterministicFiniteAutomaton(
            Set<State> states,
            Alphabet alphabet,
            State start,
            Set<State> finalStates,
            DeterministicTransitionFunction transition){
        this.states = states;
        this.alphabet = alphabet;
        this.transtition = transition;
        this.start = start;
        this.finalStates = finalStates;
    }
}
