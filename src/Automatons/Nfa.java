package Automatons;

import Elements.Alphabet;
import Elements.Transitions.Ntf;
import Elements.State;
import Factory.Algorithms;
import Factory.Constructors.NfaConstructor;
import Factory.Printer;

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

    public Dfa toDfa(){
        return Algorithms.nfaToDfa(this);
    }

    public Gnfa toGnfa(){
        return Algorithms.nfaToGnfa(this);
    }

    @Override
    public String toString(){
        return Printer.stringOfNfa(this);
    }

}
