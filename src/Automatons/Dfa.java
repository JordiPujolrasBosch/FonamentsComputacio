package Automatons;

import AutomatonElements.Alphabet;
import AutomatonElements.Dtf;
import AutomatonElements.Ntf;
import AutomatonElements.State;

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

    public Dtf getTransition(){
        return transition;
    }
}
