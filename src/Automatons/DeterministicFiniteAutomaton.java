package Automatons;

import AutomatonElements.*;

import java.util.Set;

public class DeterministicFiniteAutomaton {
    private final Set<State> states;
    private final Alphabet alphabet;
    private final State start;
    private final Set<State> finalStates;
    private final DeterministicTransitionFunction transition;

    public DeterministicFiniteAutomaton(
            Set<State> states,
            Alphabet alphabet,
            State start,
            Set<State> finalStates,
            DeterministicTransitionFunction transition){
        this.states = states;
        this.alphabet = alphabet;
        this.transition = transition;
        this.start = start;
        this.finalStates = finalStates;
    }

    public boolean checkWord(String word){
        char c;
        int i = 0;
        State act = start;
        boolean stop = false;

        while (i<word.length() && !stop){
            c = word.charAt(i);
            act = transition.step(act,c);
            stop = (act == null);
            i++;
        }
        if (stop) return false;
        return finalStates.contains(act);
    }

}
