package Automatons;

import AutomatonElements.Alphabet;
import AutomatonElements.Ntf;
import AutomatonElements.State;
import Factory.NfaConstructor;

import java.util.HashSet;
import java.util.Set;

public class Nfa {
    private final Set<State> states;
    private final Alphabet alphabet;
    private final State start;
    private final Set<State> finalStates;
    private final Ntf transition;

    public Nfa(Set<State> states, Alphabet alphabet, State start, Set<State> finalStates, Ntf transition){
        this.states = states;
        this.alphabet = alphabet;
        this.start = start;
        this.finalStates = finalStates;
        this.transition = transition;
    }

    public NfaConstructor getConstructor() {
        return new NfaConstructor(states, alphabet, start, finalStates, transition);
    }

    //Step

    public Set<State> stepWithEmptyChar(State o, Character c){
        Set<State> res = new HashSet<>();
        for(State s : transition.step(o,c)) res.addAll(transition.stateExtended(s));
        return res;
    }

    public Set<State> stateExtended(State x) {
        return transition.stateExtended(x);
    }

}
