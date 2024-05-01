package Factory.Constructors;

import Elements.Alphabet;
import Elements.Transitions.Ntf;
import Elements.State;

import Automatons.Nfa;

import java.util.Set;
import java.util.HashSet;

public class NfaConstructor {
    public Set<State> states;
    public Alphabet alphabet;
    public State start;
    public Set<State> finalStates;
    public Ntf transition;

    public NfaConstructor() {
        states = new HashSet<>();
        alphabet = new Alphabet();
        start = null;
        finalStates = new HashSet<>();
        transition = new Ntf();
    }

    public NfaConstructor(Set<State> states, Alphabet alphabet, State start, Set<State> finalStates, Ntf transition) {
        this.states = new HashSet<>();
        this.states.addAll(states);

        this.alphabet = new Alphabet();
        this.alphabet.addAll(alphabet);

        this.start = start;

        this.finalStates = new HashSet<>();
        this.finalStates.addAll(finalStates);

        this.transition = new Ntf();
        this.transition.addRules(transition.getRules());
    }

    public Nfa getNfa(){
        return new Nfa(states, alphabet, start, finalStates, transition);
    }
}
