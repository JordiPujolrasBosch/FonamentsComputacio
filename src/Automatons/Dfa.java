package Automatons;

import AutomatonElements.Alphabet;
import AutomatonElements.Dtf;
import AutomatonElements.State;
import Factory.DfaConstructor;

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
        char c;

        while(i<word.length() && in){
            c = word.charAt(i);
            in = alphabet.contains(c);
            if(in) act = transition.step(act,c);
            i++;
        }

        return in && finalStates.contains(act);
    }

}
