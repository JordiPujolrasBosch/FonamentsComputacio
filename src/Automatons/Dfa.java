package Automatons;

import Elements.Alphabet;
import Elements.Transitions.Dtf;
import Elements.State;
import Factory.Algorithms;
import Factory.Constructors.DfaConstructor;
import Factory.Printer;

import java.util.Set;

public class Dfa {
    private final Set<State> states;
    private final Set<State> finalStates;
    private final Alphabet alphabet;
    private final State start;
    private final Dtf transition;

    public Dfa(Set<State> states, Alphabet alphabet, State start, Set<State> finalStates, Dtf transition){
        this.states = states;
        this.alphabet = alphabet;
        this.start = start;
        this.finalStates = finalStates;
        this.transition = transition;
    }

    public DfaConstructor getConstructor() {
        return new DfaConstructor(states, alphabet, start, finalStates, transition);
    }

    public boolean checkWord(String word){
        return Algorithms.checkWordDfa(this, word);
    }

    public boolean compare(Dfa b){
        return Algorithms.equalsDfas(this, b);
    }

    public Nfa toNfa(){
        return Algorithms.dfaToNfa(this);
    }

    public Dfa minimize(){
        return Algorithms.minimize(this);
    }

    public Dfa complement(){
        return Algorithms.complement(this);
    }

    public Nfa reverse(){
        return Algorithms.reverse(this);
    }

    @Override
    public String toString(){
        return Printer.stringOfDfa(this);
    }
}
