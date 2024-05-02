package Automatons;

import Elements.Alphabet;
import Elements.Transitions.Dtf;
import Elements.State;
import Factory.Algorithms;
import Factory.AutomatonFactory;
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
        boolean in = true;
        State act = start;
        int i = 0;

        while(i < word.length() && in){
            char c = word.charAt(i);
            in = alphabet.contains(c);
            if(in) act = transition.step(act,c);
            i++;
        }

        return in && finalStates.contains(act);
    }

    public boolean equal(Dfa b){
        return Algorithms.equalsDfas(this, b);
    }

    public Nfa toNfa(){
        return AutomatonFactory.dfaToNfa(this);
    }

    public Dfa minimize(){
        return AutomatonFactory.minimize(this);
    }

    public String toString(){
        return Printer.stringOfDfa(this);
    }
}
