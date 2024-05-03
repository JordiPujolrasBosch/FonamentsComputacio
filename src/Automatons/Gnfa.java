package Automatons;

import Elements.Alphabet;
import Elements.Transitions.Gntf;
import Elements.State;

import Factory.Algorithms;
import Factory.Constructors.GnfaConstructor;
import RegularExpressions.RegularExpression;

import java.util.Set;

public class Gnfa {
    private final Set<State> states;
    private final Alphabet alphabet;
    private final State start;
    private final State accept;
    private final Gntf transition;

    public Gnfa(Set<State> states, Alphabet alphabet, State start, State accept, Gntf transition){
        this.states = states;
        this.start = start;
        this.alphabet = alphabet;
        this.accept = accept;
        this.transition = transition;
    }

    public GnfaConstructor getConstructor() {
        return new GnfaConstructor(states, alphabet, start, accept, transition);
    }

    public RegularExpression toRegex(){
        return Algorithms.gnfaToRegex(this);
    }

}
