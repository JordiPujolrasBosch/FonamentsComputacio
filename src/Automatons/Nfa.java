package Automatons;

import AutomatonElements.Alphabet;
import AutomatonElements.Ntf;
import AutomatonElements.State;

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

    public Dfa toDfa(){
        return AutomatonFactory.nfaToDfa(this);
    }

    //STEP

    public Set<State> stepWithEmptyChar(State o, Character c){
        return transition.setStateExtended(transition.step(o,c));
    }

    public Set<State> stateWithEmptyChar(State x) {
        return transition.stateExtended(x);
    }

    public Set<State> setStatesWithEmptyChar(Set<State> s){
        return transition.setStateExtended(s);
    }

    //GETTERS

    public Set<State> getStates() {
        return states;
    }

    public Alphabet getAlphabet() {
        return alphabet;
    }

    public State getStart(){
        return start;
    }

    public Set<State> getFinalStates() {
        return finalStates;
    }

    public Ntf getTransition(){
        return transition;
    }
}
